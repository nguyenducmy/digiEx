package com.supertomato.restaurant.controller;

import com.supertomato.restaurant.auth.AuthorizeValidator;

import com.supertomato.restaurant.common.enums.ProductType;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.common.util.DateUtil;
import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.controller.helper.DashboardHelper;
import com.supertomato.restaurant.controller.model.response.*;
import com.supertomato.restaurant.controller.util.APIStatus;
import com.supertomato.restaurant.controller.util.RestAPIResponse;
import com.supertomato.restaurant.entity.*;
import com.supertomato.restaurant.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(APIName.DASHBOARD)
public class DashboardController extends AbstractBaseController {

    final CompanyService companyService;
    final OutletService outletService;
    final IngredientService ingredientService;
    final IngredientHistoryService ingredientHistoryService;

    final ProductHistoryService productHistoryService;

    final DashboardHelper dashboardHelper;


    public DashboardController(CompanyService companyService,
                               OutletService outletService,
                               IngredientService ingredientService,
                               IngredientHistoryService ingredientHistoryService,
                               ProductHistoryService productHistoryService, DashboardHelper dashboardHelper) {
        this.companyService = companyService;
        this.outletService = outletService;
        this.ingredientService = ingredientService;
        this.ingredientHistoryService = ingredientHistoryService;
        this.productHistoryService = productHistoryService;
        this.dashboardHelper = dashboardHelper;
    }

    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.TOP_INGREDIENTS, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getTopIngredients(
            @RequestParam(value = "company_id") String companyId,
            @RequestParam(value = "outlet_id") String outletId,
            @RequestParam(value = "sort_field", required = false, defaultValue = "portion") String sortField,
            @RequestParam(value = "from_date") String fromDate,
            @RequestParam(value = "to_date") String toDate) {

        Company company = companyService.getById(companyId);
        Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");

        ZoneId zoneId = DateUtil.getTimezone(company.getTimezone());

        Date from = DateUtil.convertLocalToUTC(DateUtil.stringToDate(fromDate, apiDateTimeFormat), zoneId);
        Date to = DateUtil.convertLocalToUTC(DateUtil.stringToDate(toDate, apiDateTimeFormat), zoneId);

        Outlet outlet = outletService.getByIdAndCompanyId(outletId, company.getId());
        Validator.notNull(outlet, APIStatus.NOT_FOUND, "Outlet not found");


        List<TopIngredient> topIngredients = new ArrayList<>();
        topIngredients.add(new TopIngredient("b6358ac3432748c5b524c02120bf4c18", "Ingredient 1", 40, 2000));
        topIngredients.add(new TopIngredient("se358ac3432748c5b524c02120bf4c33", "Ingredient 2", 30, 3000));
        if (Constant.SORT_BY_PORTION.equals(sortField)) {
            topIngredients = topIngredients.stream().sorted(Comparator.comparingDouble(TopIngredient::getPortion).reversed()).collect(Collectors.toList());
        } else {
            topIngredients = topIngredients.stream().sorted(Comparator.comparingDouble(TopIngredient::getVolume).reversed()).collect(Collectors.toList());
        }
        return responseUtil.successResponse(topIngredients);
    }


    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.TOTAL_SERVED, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getTotalServed(
            @RequestParam(value = "company_id") String companyId,
            @RequestParam(value = "outlet_id") String outletId,
            @RequestParam(value = "sort_field", required = false, defaultValue = "") String sortField,
            @RequestParam(name = "sort", required = false, defaultValue = "false") boolean isAsc,
            @RequestParam(value = "from_date") String fromDate,
            @RequestParam(value = "to_date") String toDate) {


        Company company = companyService.getById(companyId);
        Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");

        ZoneId zoneId = DateUtil.getTimezone(company.getTimezone());

        Date from = DateUtil.convertLocalToUTC(DateUtil.stringToDate(fromDate, apiDateTimeFormat), zoneId);
        Date to = DateUtil.convertLocalToUTC(DateUtil.stringToDate(toDate, apiDateTimeFormat), zoneId);

        Outlet outlet = outletService.getByIdAndCompanyId(outletId, company.getId());
        Validator.notNull(outlet, APIStatus.NOT_FOUND, "Outlet not found");


        return responseUtil.successResponse(dashboardHelper.getServers(from, to, productHistoryService, outlet, sortField, isAsc));
    }


    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.AVERAGE_NUMBER, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAverageNumber(
            @RequestParam(value = "company_id") String companyId,
            @RequestParam(value = "outlet_id") String outletId,
            @RequestParam(value = "from_date") String fromDate,
            @RequestParam(value = "to_date") String toDate) {


        Company company = companyService.getById(companyId);
        Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");

        ZoneId zoneId = DateUtil.getTimezone(company.getTimezone());
        Date from = DateUtil.convertLocalToUTC(DateUtil.stringToDate(fromDate, apiDateTimeFormat), zoneId);
        Date to = DateUtil.convertLocalToUTC(DateUtil.stringToDate(toDate, apiDateTimeFormat), zoneId);

        Outlet outlet = outletService.getByIdAndCompanyId(outletId, company.getId());
        Validator.notNull(outlet, APIStatus.NOT_FOUND, "Outlet not found");

        return responseUtil.successResponse(dashboardHelper.getAverageNumber(outlet,from,to,productHistoryService));
    }


    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.DEVIATION_HISTORY, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getDeviationHistoryDetail(
            @RequestParam(value = "company_id") String companyId,
            @RequestParam(value = "outlet_id") String outletId,
            @RequestParam(value = "ingredient_id") String ingredientId,
            @RequestParam(value = "sort_field", required = false, defaultValue = "") String sortField,
            @RequestParam(name = "sort", required = false, defaultValue = "false") boolean isAsc,
            @RequestParam(value = "from_date") String fromDate,
            @RequestParam(value = "to_date") String toDate) {


        Company company = companyService.getById(companyId);
        Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");

        ZoneId zoneId = DateUtil.getTimezone(company.getTimezone());
        Date from = DateUtil.convertLocalToUTC(DateUtil.stringToDate(fromDate, apiDateTimeFormat), zoneId);
        Date to = DateUtil.convertLocalToUTC(DateUtil.stringToDate(toDate, apiDateTimeFormat), zoneId);

        Outlet outlet = outletService.getByIdAndCompanyId(outletId, company.getId());
        Validator.notNull(outlet, APIStatus.NOT_FOUND, "Outlet not found");

        Ingredient ingredient = ingredientService.getByIdAndOutletId(ingredientId, outlet.getId());
        Validator.notNull(ingredient, APIStatus.NOT_FOUND, "Ingredient not found");

        List<IngredientHistory> ingredientHistories = ingredientHistoryService.getAllByIngredientIdAndDispensedTimeBetween(ingredient.getId(), from, to, sortField, isAsc);

        return responseUtil.successResponse(new IngredientHistoryRes(ingredient, ingredientHistories));
    }


    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.HISTORY_DEVIATION, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getDeviations(
            @RequestParam(value = "company_id") String companyId,
            @RequestParam(value = "outlet_id") String outletId,
            @RequestParam(value = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(value = "sort_field", required = false, defaultValue = "") String sortField,
            @RequestParam(name = "sort", required = false, defaultValue = "false") boolean isAsc,
            @RequestParam(value = "from_date") String fromDate,
            @RequestParam(value = "to_date") String toDate) {

        Company company = companyService.getById(companyId);
        Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");

        ZoneId zoneId = DateUtil.getTimezone(company.getTimezone());
        Date from = DateUtil.convertLocalToUTC(DateUtil.stringToDate(fromDate, apiDateTimeFormat), zoneId);
        Date to = DateUtil.convertLocalToUTC(DateUtil.stringToDate(toDate, apiDateTimeFormat), zoneId);

        Outlet outlet = outletService.getByIdAndCompanyId(outletId, company.getId());
        Validator.notNull(outlet, APIStatus.NOT_FOUND, "Outlet not found");

        List<Ingredient> ingredientList = ingredientService.getIngredientAllByOutletId(outlet.getId(), searchKey);

        List<String> ingredientIds = ingredientList.stream().map(Ingredient::getId).collect(Collectors.toList());

        List<IngredientHistory> ingredientHistoryList = ingredientHistoryService.getAllByIngredientIdInAndDispensedTimeBetween(ingredientIds, from, to);

        List<HistoryOfDeviation> historyOfDeviationList = dashboardHelper.getHistoryOfDeviation(sortField, isAsc, ingredientList, ingredientHistoryList);

        return responseUtil.successResponse(historyOfDeviationList);
    }


}
