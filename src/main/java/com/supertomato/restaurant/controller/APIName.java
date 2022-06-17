package com.supertomato.restaurant.controller;

/**
 * @author DiGiEx
 */
public interface APIName {

    String CHARSET = "application/json;charset=utf-8";

    // Base URL
    String BASE_API_URL = "/api";
    String ID = "/{id}";
    String LIST = "/list";
    String ADD = "/add";
    String PAGE = "/page";
    String UPDATES = "/updates";
    String UPDATE_ID = "/update/{id}";
    String DELETE_ID = "/delete/{id}";
    String DETAIL_ID = "/detail/{id}";
    String RESEND_ID = "/resend-invitation/{id}";
    String ASSUME = "/assume";


    // Authenticate APIs
    String AUTHENTICATE_API = BASE_API_URL + "/auth";
    String AUTHENTICATE_INFO = "/info";
    String AUTHENTICATE_LOGIN = "/login";
    String AUTHENTICATE_LOGOUT = "/logout";
    String AUTHENTICATE_FORGOT_PASSWORD = "/forgot-password";
    // Dashboard APIs
    String DASHBOARD = BASE_API_URL + "/dashboard";
    String TOP_INGREDIENTS = "/top-ingredients";
    String TOTAL_SERVED = "/total-served";
    String AVERAGE_NUMBER = "/average-number";
    String DEVIATION_HISTORY = "deviation/history/list";
    String DEVIATION = "deviations";
    String MODULE = BASE_API_URL + "/module";
    String PRODUCT_LINE = BASE_API_URL + "/product-line";

    String HISTORY_DEVIATION = "/history-of-deviation";

    String KITCHEN = BASE_API_URL + "/kitchen";

    String LIST_MODULE = "/modules";
    // User APIs

    String USER_API = BASE_API_URL + "/user";

    String SIGN_UP = "/sign-up";
    String COMPANY = BASE_API_URL + "/company";
    String OUTLET = BASE_API_URL + "/outlet";

    String ADMIN_CHANGE_PASSWORD = "/change-password/{active_code}";

    String DELETE_MODULE = "/delete/{module}";
    String UPDATE_MODULE = "/update/{module}";
    //Kitchen APIs

    String KITCHEN_LOGIN = "/login";

    String KITCHEN_LOGOUT = "/logout";

    String ADD_PRODUCT = "/product";

    String ADD_INGREDIENT = "/ingredient";
    String UPDATE_USAGE_MODULE = "/update-usage-module";

}
