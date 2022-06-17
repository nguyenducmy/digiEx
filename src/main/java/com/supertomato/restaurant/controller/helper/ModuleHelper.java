package com.supertomato.restaurant.controller.helper;

import com.supertomato.restaurant.auth.AuthUser;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.ProductType;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.common.util.UniqueID;
import com.supertomato.restaurant.controller.model.request.IngredientRequest;
import com.supertomato.restaurant.controller.model.request.UpdateIngredient;
import com.supertomato.restaurant.controller.model.request.UpdateIngredientRe;
import com.supertomato.restaurant.controller.model.request.UpdateModule;
import com.supertomato.restaurant.controller.model.response.ModuleResponse;
import com.supertomato.restaurant.controller.model.response.ProductLineResponse;
import com.supertomato.restaurant.controller.model.response.ProductResponse;
import com.supertomato.restaurant.controller.util.APIStatus;
import com.supertomato.restaurant.entity.Ingredient;
import com.supertomato.restaurant.entity.Outlet;
import com.supertomato.restaurant.entity.ProductLine;
import com.supertomato.restaurant.service.IngredientService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ModuleHelper {

    public void validateAddIngredient(List<UpdateIngredient> addIngredients) {
        Optional<UpdateIngredient> name = addIngredients.stream()
                .filter(updateIngredient -> updateIngredient.getName() == null || updateIngredient.getName().trim().isEmpty()).findAny();
        if (name.isPresent()) {
            throw new ApplicationException(APIStatus.BAD_REQUEST, "name is null");
        }

        Optional<UpdateIngredient> portion = addIngredients.stream()
                .filter(updateIngredient -> updateIngredient.getPortion() == null).findAny();
        if (portion.isPresent()) {
            throw new ApplicationException(APIStatus.BAD_REQUEST, "portion is null");
        }

        Optional<UpdateIngredient> acceptableDeviation = addIngredients.stream()
                .filter(updateIngredient -> updateIngredient.getAcceptableDeviation() == null).findAny();
        if (acceptableDeviation.isPresent()) {
            throw new ApplicationException(APIStatus.BAD_REQUEST, "acceptableDeviation is null");
        }

        Optional<UpdateIngredient> ordinalNumber = addIngredients.stream()
                .filter(updateIngredient -> updateIngredient.getOrdinalNumber() == null).findAny();
        if (ordinalNumber.isPresent()) {
            throw new ApplicationException(APIStatus.BAD_REQUEST, "ordinalNumber is null");
        }
    }

    public List<Ingredient> addIngredients(List<IngredientRequest> ingredientRequests, int module, Outlet outlet) {
        List<Ingredient> ingredientList = new ArrayList<>();

        for (IngredientRequest ingredientRequest : ingredientRequests) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(UniqueID.getUUID());
            ingredient.setModule(module + 1);
            ingredient.setName(ingredientRequest.getName().trim());
            ingredient.setPortion(ingredientRequest.getPortion());
            ingredient.setAcceptableDeviation(ingredientRequest.getAcceptableDeviation());
            ingredient.setOrdinalNumber(ingredientRequest.getOrdinalNumber());
            ingredient.setOutletId(outlet.getId());
            ingredient.setProductType(ProductType.NOT_APPLICABLE);
            ingredient.setStatus(AppStatus.ACTIVE);
            ingredientList.add(ingredient);
        }
        return ingredientList;
    }

    public ProductResponse getProductResponse(List<ProductLine> productLineList,
                                              IngredientService ingredientService,
                                              Outlet outlet, String searchKey) {

        List<ProductLineResponse> productLines = new ArrayList<>();
        List<ModuleResponse> moduleResponses = new ArrayList<>();

        for (ProductLine productLine : productLineList) {
            productLines.add(new ProductLineResponse(productLine));
        }
        List<Ingredient> ingredientList = ingredientService.getAllByOutletId(outlet.getId(), searchKey);

        List<Integer> modules = ingredientList.stream().map(Ingredient::getModule).sorted().distinct().collect(Collectors.toList());
        for (Integer module : modules) {
            List<Ingredient> ingredients = ingredientList.stream().filter(ingredient -> ingredient.getModule() == module).collect(Collectors.toList());
            moduleResponses.add(new ModuleResponse(module, ingredients));
        }

        return new ProductResponse(productLines, moduleResponses);
    }


    public void deleteModule(List<Ingredient> ingredients, IngredientService ingredientService) {
        String outletId = ingredients.get(0).getOutletId();

        ingredients.forEach(ingredient -> ingredient.setStatus(AppStatus.INACTIVE));
        ingredientService.saveIngredient(ingredients);

        List<Ingredient> ingredientList = ingredientService.getByOutletId(outletId);

        if (!ingredientList.isEmpty()) {
            List<Integer> modules = ingredientList.stream().map(Ingredient::getModule).distinct().sorted().collect(Collectors.toList());
            List<Ingredient> mewIngredientList = new ArrayList<>();
            for (int i = 0; i < modules.size(); i++) {
                int module = modules.get(i);
                List<Ingredient> _ingredients = ingredientList.stream().filter(ingredient -> ingredient.getModule() == module).collect(Collectors.toList());
                for (Ingredient ingredient : _ingredients) {
                    ingredient.setModule(i + 1);
                }
                mewIngredientList.addAll(_ingredients);
            }
            if (!mewIngredientList.isEmpty()) {
                ingredientService.saveIngredient(mewIngredientList);
            }
        }
    }

    public List<Ingredient> updateModule(AuthUser authUser, UpdateModule updateModule,
                                         List<Ingredient> ingredients,
                                         IngredientService ingredientService) {

        List<Ingredient> ingredientList = new ArrayList<>();

        if (UserRole.COMPANY_ADMIN.equals(authUser.getRole())) {

            List<UpdateIngredient> deleteIngredients = updateModule.getIngredients().stream()
                    .filter(updateIngredient -> updateIngredient.getId() != null && !updateIngredient.getId().isEmpty() &&
                            AppStatus.ARCHIVED.equals(updateIngredient.getStatus())).collect(Collectors.toList());

            List<UpdateIngredient> addIngredients = updateModule.getIngredients().stream()
                    .filter(updateIngredient -> updateIngredient.getId() == null || updateIngredient.getId().isEmpty()).collect(Collectors.toList());

            if (deleteIngredients.size() != addIngredients.size()) {
                throw new ApplicationException(APIStatus.CAN_ONLY_CREATE_4_INGREDIENT_IN_MODULE);
            }

            if (!addIngredients.isEmpty()) {
                String outletId = ingredients.get(0).getOutletId();


                //validate request
                validateAddIngredient(addIngredients);

                for (UpdateIngredient updateIngredient : addIngredients) {
                    ProductType productType = ProductType.NOT_APPLICABLE;
                    if (updateIngredient.getProductType() != null) {
                        productType = updateIngredient.getProductType();
                    }
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(UniqueID.getUUID());
                    ingredient.setName(updateIngredient.getName().trim());
                    ingredient.setPortion(updateIngredient.getPortion());
                    ingredient.setAcceptableDeviation(updateIngredient.getAcceptableDeviation());
                    ingredient.setOrdinalNumber(updateIngredient.getOrdinalNumber());
                    ingredient.setStatus(AppStatus.ACTIVE);
                    ingredient.setProductType(productType);
                    ingredient.setCategory(updateIngredient.getCategory());
                    ingredient.setModule(updateModule.getModule());
                    ingredient.setOutletId(outletId);
                    ingredientList.add(ingredient);
                }
            }

            if (!deleteIngredients.isEmpty()) {
                List<String> ingredientIds = deleteIngredients.stream().map(UpdateIngredient::getId).distinct().collect(Collectors.toList());
                List<Ingredient> _ingredientList = ingredientService.getAllByIdInAndModule(ingredientIds, updateModule.getModule());
                if (ingredientIds.size() != _ingredientList.size()) {
                    throw new ApplicationException(APIStatus.NOT_FOUND, "Ingredient not found");
                }
                _ingredientList.forEach(ingredient -> ingredient.setStatus(AppStatus.ARCHIVED));
                ingredientList.addAll(_ingredientList);
            }
        }

        List<UpdateIngredient> updateIngredients = updateModule.getIngredients().stream()
                .filter(updateIngredient -> updateIngredient.getId() != null && !updateIngredient.getId().isEmpty() &&
                        (updateIngredient.getStatus() == null || AppStatus.ACTIVE.equals(updateIngredient.getStatus()))).collect(Collectors.toList());


        if (!updateIngredients.isEmpty()) {
            List<String> ingredientIds = updateIngredients.stream().map(UpdateIngredient::getId).distinct().collect(Collectors.toList());
            List<Ingredient> updateIngredientList = ingredientService.getAllByIdInAndModule(ingredientIds, updateModule.getModule());
            if (ingredientIds.size() != updateIngredientList.size()) {
                throw new ApplicationException(APIStatus.NOT_FOUND, "Ingredient not found");
            }

            updateIngredientList.forEach(ingredient -> updateIngredients.forEach(updateIngredient -> {
                if (ingredient.getId().equals(updateIngredient.getId())) {

                    if (updateIngredient.getName() != null && !updateIngredient.getName().trim().isEmpty()) {
                        ingredient.setName(updateIngredient.getName().trim());
                    }
                    if (updateIngredient.getPortion() != null) {
                        ingredient.setPortion(updateIngredient.getPortion());
                    }
                    if (updateIngredient.getAcceptableDeviation() != null) {
                        ingredient.setAcceptableDeviation(updateIngredient.getAcceptableDeviation());
                    }
                    if (updateIngredient.getOrdinalNumber() != null) {
                        ingredient.setOrdinalNumber(updateIngredient.getOrdinalNumber());
                    }
                    if (UserRole.COMPANY_ADMIN.equals(authUser.getRole())) {
                        if (updateIngredient.getCategory() != null) {
                            ingredient.setCategory(updateIngredient.getCategory());
                        }
                        if (updateIngredient.getProductType() != null) {
                            ingredient.setProductType(updateIngredient.getProductType());
                        }
                    }
                }
            }));

            ingredientList.addAll(updateIngredientList);
        }

        return ingredientList;
    }

}
