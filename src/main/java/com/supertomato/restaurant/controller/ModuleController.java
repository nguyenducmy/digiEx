package com.supertomato.restaurant.controller;

import com.supertomato.restaurant.auth.AuthSession;
import com.supertomato.restaurant.auth.AuthUser;
import com.supertomato.restaurant.auth.AuthorizeValidator;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.ProductType;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.common.util.UniqueID;
import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.controller.helper.ModuleHelper;
import com.supertomato.restaurant.controller.model.request.IngredientRequest;
import com.supertomato.restaurant.controller.model.request.ModuleRequest;
import com.supertomato.restaurant.controller.model.request.UpdateIngredient;
import com.supertomato.restaurant.controller.model.request.UpdateModule;
import com.supertomato.restaurant.controller.model.response.ModuleResponse;
import com.supertomato.restaurant.controller.model.response.ProductLineResponse;
import com.supertomato.restaurant.controller.model.response.ProductResponse;
import com.supertomato.restaurant.controller.util.APIStatus;
import com.supertomato.restaurant.controller.util.RestAPIResponse;
import com.supertomato.restaurant.entity.Ingredient;
import com.supertomato.restaurant.entity.Outlet;
import com.supertomato.restaurant.entity.ProductLine;
import com.supertomato.restaurant.service.IngredientService;
import com.supertomato.restaurant.service.OutletService;
import com.supertomato.restaurant.service.ProductLineService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(APIName.MODULE)
public class ModuleController extends AbstractBaseController {
    final IngredientService ingredientService;
    final OutletService outletService;
    final ProductLineService productLineService;
    final ModuleHelper moduleHelper;

    public ModuleController(IngredientService ingredientService,
                            OutletService outletService,
                            ProductLineService productLineService,
                            ModuleHelper moduleHelper) {

        this.ingredientService = ingredientService;
        this.outletService = outletService;
        this.productLineService = productLineService;
        this.moduleHelper = moduleHelper;
    }

    /**
     * Add module with ingredient
     */
    @AuthorizeValidator(UserRole.SUPER_ADMIN)
    @RequestMapping(path = APIName.ADD, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> saveAll(
            @RequestBody @Valid ModuleRequest moduleRequest
    ) {

        Outlet outlet = outletService.getOutletByIdIn(moduleRequest.getOutletId());
        Validator.notNull(outlet, APIStatus.NOT_FOUND, "Outlet not found");

        if (moduleRequest.getIngredients().size() != 4) {
            throw new ApplicationException(APIStatus.CAN_ONLY_CREATE_4_INGREDIENT_IN_MODULE);
        }
        List<Integer> ordinalNumber = moduleRequest.getIngredients().stream().map(IngredientRequest::getOrdinalNumber).distinct().collect(Collectors.toList());

        if (ordinalNumber.size() != moduleRequest.getIngredients().size()) {
            throw new ApplicationException(APIStatus.BAD_REQUEST, "The OrdinalNumber of ingredient is duplicate");
        }
        List<Ingredient> ingredients = ingredientService.getByOutletId(outlet.getId());
        // validate size module
        int module = 0;
        if (!ingredients.isEmpty()) {
            module = ingredients.get(0).getModule();
            if (module == 8) {
                throw new ApplicationException(APIStatus.CAN_ONLY_CREATE_UP_8_MODULE);
            }
        }

        // add Ingredient list
        List<Ingredient> ingredientList = moduleHelper.addIngredients(moduleRequest.getIngredients(), module, outlet);
        ingredientService.saveIngredient(ingredientList);
        return responseUtil.successResponse(ingredientList);
    }


    @RequestMapping(path = APIName.LIST, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getModules(
            @RequestParam(name = "outlet_id") String outletId,
            @RequestParam(value = "search_key", required = false, defaultValue = "") String searchKey

    ) {
        Outlet outlet = outletService.getById(outletId);
        Validator.notNull(outlet, APIStatus.NOT_FOUND, "Outlet not found");
        List<ProductLine> productLineList = productLineService.getAllByOutletId(outlet.getId());
        return responseUtil.successResponse(moduleHelper.getProductResponse(productLineList, ingredientService, outlet, searchKey));
    }

    @AuthorizeValidator(UserRole.SUPER_ADMIN)
    @RequestMapping(path = APIName.DELETE_MODULE, method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteModule(
            @PathVariable(value = "module") int module

    ) {
        List<Ingredient> ingredients = ingredientService.getByModule(module);
        Validator.notNullAndNotEmpty(ingredients, APIStatus.NOT_FOUND, "Module not found");
        // delete module
        moduleHelper.deleteModule(ingredients,ingredientService);

        return responseUtil.successResponse("DELETE OK");
    }

    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.UPDATES, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateModule(
            @RequestBody @Valid UpdateModule updateModule,
            @AuthSession AuthUser authUser

    ) {
        List<Ingredient> ingredients = ingredientService.findByModule(updateModule.getModule());
        Validator.notNullAndNotEmpty(ingredients, APIStatus.NOT_FOUND, "Module not found");
        // update Module
        List<Ingredient> ingredientList =moduleHelper.updateModule(authUser,updateModule,ingredients,ingredientService);
        ingredientService.saveIngredient(ingredientList);

        return responseUtil.successResponse(ingredientList);
    }
}
