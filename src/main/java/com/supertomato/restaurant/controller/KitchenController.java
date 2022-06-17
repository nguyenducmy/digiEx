package com.supertomato.restaurant.controller;

import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.common.util.DateUtil;
import com.supertomato.restaurant.common.util.UniqueID;
import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.controller.helper.ModuleHelper;
import com.supertomato.restaurant.controller.helper.SessionHelper;
import com.supertomato.restaurant.controller.model.request.*;
import com.supertomato.restaurant.controller.model.response.OutletResponse;
import com.supertomato.restaurant.controller.model.response.SessionKitchenResponse;
import com.supertomato.restaurant.controller.util.APIStatus;
import com.supertomato.restaurant.controller.util.RestAPIResponse;
import com.supertomato.restaurant.entity.*;
import com.supertomato.restaurant.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(APIName.KITCHEN)
public class KitchenController extends AbstractBaseController {


    final OutletService outletService;
    final CompanyService companyService;
    final IngredientService ingredientService;
    final IngredientHistoryService ingredientHistoryService;
    final ModuleHelper moduleHelper;
    final SessionHelper sessionHelper;
    final SessionService sessionService;
    final SessionKitchenService sessionKitchenService;
    final ProductHistoryService productHistoryService;

    final ProductLineService productLineService;

    public KitchenController(OutletService outletService,
                             CompanyService companyService,
                             IngredientService ingredientService,
                             IngredientHistoryService ingredientHistoryService,
                             ModuleHelper moduleHelper, SessionHelper sessionHelper,
                             SessionService sessionService,
                             SessionKitchenService sessionKitchenService,
                             ProductHistoryService productHistoryService, ProductLineService productLineService) {
        this.outletService = outletService;
        this.companyService = companyService;
        this.ingredientService = ingredientService;
        this.ingredientHistoryService = ingredientHistoryService;
        this.moduleHelper = moduleHelper;
        this.sessionHelper = sessionHelper;
        this.sessionService = sessionService;
        this.sessionKitchenService = sessionKitchenService;
        this.productHistoryService = productHistoryService;
        this.productLineService = productLineService;
    }

    @RequestMapping(path = APIName.LIST_MODULE, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> getModules(
            @RequestBody @Valid AuthToken authToken
    ) {

        SessionKitchen sessionKitchen = sessionKitchenService.findById(authToken.getAuthToken());
        Validator.notNull(sessionKitchen, APIStatus.UNAUTHORIZED);

        Outlet outlet = outletService.getById(sessionKitchen.getOutletId());
        Validator.notNull(outlet, APIStatus.NOT_FOUND, "Outlet not found");

        List<Ingredient> ingredientList = ingredientService.getByOutletId(outlet.getId());

        Company company = companyService.getById(outlet.getCompanyId());
        Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");

        return responseUtil.successResponse(new OutletResponse(company, outlet, ingredientList));
    }

    @RequestMapping(path = APIName.KITCHEN_LOGIN, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> kitchenLogin(
            @RequestBody @Valid LoginKitchen loginKitchen
    ) {
        Outlet outlet = outletService.getByKitchenAccount(loginKitchen.getKitchenAccount().trim());
        Validator.notNull(outlet, APIStatus.INVALID_AUTHENTICATE_CREDENTIAL);

        if (!outlet.getKitchenPassword().equals(loginKitchen.getKitchenPassword().trim())) {
            throw new ApplicationException(APIStatus.INVALID_AUTHENTICATE_CREDENTIAL);
        }
        Company company = companyService.getById(outlet.getCompanyId());
        Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");
        List<SessionKitchen> sessionKitchens = sessionKitchenService.getAllByOutletId(outlet.getId());
        List<ProductLine> productLines = productLineService.getAllByOutletId(outlet.getId());
        SessionKitchen sessionKitchen = new SessionKitchen();
        sessionKitchen.setId(UniqueID.getUUID());
        sessionKitchen.setOutletId(outlet.getId());
        sessionKitchen.setCreatedDate(DateUtil.convertToUTC(new Date()));
        if (!sessionKitchens.isEmpty()) {
            sessionKitchenService.deleteAll(sessionKitchens);
        }
        sessionKitchenService.save(sessionKitchen);
        return responseUtil.successResponse(new SessionKitchenResponse(company, sessionKitchen.getId(), outlet, productLines));
    }

    @RequestMapping(path = APIName.KITCHEN_LOGOUT, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> kitchenLogin(
            @RequestBody @Valid AuthToken authToken
    ) {
        SessionKitchen sessionKitchen = sessionKitchenService.findById(authToken.getAuthToken());
        if (sessionKitchen != null) {
            sessionKitchenService.delete(sessionKitchen);
        }
        return responseUtil.successResponse("Logout Successfully");
    }

    @RequestMapping(path = APIName.AUTHENTICATE_INFO, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> getInfo(
            @RequestBody @Valid AuthToken authToken
    ) {
        SessionKitchen sessionKitchen = sessionKitchenService.findById(authToken.getAuthToken());
        Validator.notNull(sessionKitchen, APIStatus.INVALID_AUTHENTICATE_CREDENTIAL);
        Outlet outlet = outletService.getByKitchenAccount(sessionKitchen.getOutletId());
        Validator.notNull(outlet, APIStatus.INVALID_AUTHENTICATE_CREDENTIAL);
        Company company = companyService.getById(outlet.getCompanyId());
        Validator.notNull(company, APIStatus.INVALID_AUTHENTICATE_CREDENTIAL);
        return responseUtil.successResponse("OK");
    }

    @RequestMapping(path = APIName.ADD_PRODUCT, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addProduct(
            @RequestHeader(value = Constant.HEADER_TOKEN) String authToken,
            @RequestBody @Valid List<AddProduct> addProducts
    ) {
        SessionKitchen sessionKitchen = sessionKitchenService.findById(authToken);
        Validator.notNull(sessionKitchen, APIStatus.INVALID_AUTHENTICATE_CREDENTIAL);

        Outlet outlet = outletService.getById(sessionKitchen.getOutletId());
        Validator.notNull(outlet, APIStatus.INVALID_AUTHENTICATE_CREDENTIAL);


        List<ProductHistory> productHistoryList = new ArrayList<>();
        if (addProducts != null && !addProducts.isEmpty()) {
            for (AddProduct addProduct : addProducts) {
                ProductHistory productHistory = new ProductHistory();
                productHistory.setId(UniqueID.getUUID());
                productHistory.setProductType(addProduct.getProductType());
                productHistory.setStartTime(addProduct.getStartTime());
                productHistory.setEndTime(addProduct.getEndTime());
                productHistory.setWaitingTime(addProduct.getWaitingTime());
                productHistory.setOutletId(addProduct.getOutletId());
                productHistoryList.add(productHistory);
            }
            productHistoryService.saveAll(productHistoryList);
        }
        return responseUtil.successResponse("OK");
    }


    @RequestMapping(path = APIName.ADD_INGREDIENT, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addIngredientHistory(
            @RequestHeader(value = Constant.HEADER_TOKEN) String authToken,
            @RequestBody @Valid List<AddIngredientHistory> addIngredientHistories
    ) {
        SessionKitchen sessionKitchen = sessionKitchenService.findById(authToken);

        Validator.notNull(sessionKitchen, APIStatus.INVALID_AUTHENTICATE_CREDENTIAL);
        Outlet outlet = outletService.getByKitchenAccount(sessionKitchen.getOutletId());
        Validator.notNull(outlet, APIStatus.INVALID_AUTHENTICATE_CREDENTIAL);
        List<IngredientHistory> ingredientHistories = new ArrayList<>();
        if (addIngredientHistories != null && !addIngredientHistories.isEmpty()) {
            for (AddIngredientHistory addIngredientHistory : addIngredientHistories) {
                IngredientHistory ingredientHistory = new IngredientHistory();
                ingredientHistory.setId(UniqueID.getUUID());
                ingredientHistory.setIngredientId(addIngredientHistory.getIngredientId());
                ingredientHistory.setDispensedTime(addIngredientHistory.getDispensedTime());
                ingredientHistory.setDeviation(addIngredientHistory.isDeviation());
                ingredientHistory.setPortion(addIngredientHistory.getPortion());
                ingredientHistories.add(ingredientHistory);
            }
            ingredientHistoryService.saveAll(ingredientHistories);
        }
        return responseUtil.successResponse("OK");
    }


    /**
     * update list module
     */
    @RequestMapping(path = APIName.UPDATES, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> updateAllModule(
            @RequestBody UpdateModules updateModules
    ) {
        Outlet outlet = outletService.getOutletByIdIn(updateModules.getOutletId());
        Validator.notNull(outlet, APIStatus.NOT_FOUND, "Outlet not found");

        List<String> ingredientIds = updateModules.getIngredients().stream().map(UpdateIngredientRe::getId).collect(Collectors.toList());
        List<Ingredient> ingredientList = ingredientService.getAllByIdInAndOutletId(ingredientIds, outlet.getId());

        if (ingredientList.size() != ingredientIds.size()) {
            throw new ApplicationException(APIStatus.BAD_REQUEST, "Ingredient is duplicate");
        }

        ingredientList.forEach(ingredient -> updateModules.getIngredients().forEach(updateIngredient -> {
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

            }
        }));

        ingredientService.saveIngredient(ingredientList);

        return responseUtil.successResponse(ingredientList);
    }


}

