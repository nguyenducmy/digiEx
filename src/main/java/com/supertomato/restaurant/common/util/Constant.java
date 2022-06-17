package com.supertomato.restaurant.common.util;

/**
 * @author DiGiEx
 */
public interface Constant {

    String HEADER_TOKEN = "Auth-Token";

    String MAIL_ADDRESS_PATTERN = "^[a-zA-Z0-9.!#$%&’*+\\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    String PATTERN_PASSWORD = "(?=\\S+$).{8,32}";
    String PATTERN_PASSWORD_STRICT = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\\w\\s]).{8,32}$";


    // API format date
    String API_FORMAT_DATE_TIME = "MM/dd/yyyy HH:mm:ss";

    String API_FORMAT_TIME = "MM/dd/yyyy HH:mm";

    String API_FORMAT_DATE = "MM/dd/yyyy";

    String FORMAT_TIME = "HH:mm";

    String VALID_XSS = "^((?!<|>)[\\s\\S])*?$";
    String VALID_CURLY_BRACES = "^((?!\\{|\\})[\\s\\S])*?$";

    int SALT_LENGTH = 6;

    long ONE_DAY_MILLISECOND = 24 * 60 * 60 * 100;

    String SORT_BY_NAME = "name";

    String SORT_BY_OUTLET_PRE = "outletPrefix";

    String SORT_BY_OUTLET_NAME = "outletName";
    String SORT_BY_TIMEZONE = "timezone";
    String SORT_BY_EMAIL = "email";
    String SORT_BY_KITCHEN_LOGIN_EMAIL = "kitchenLoginEmail";
    String SORT_BY_PORTION = "portion";

    String SORT_BY_STATUS = "status";

    String SORT_BY_TOTAL_PORTION = "totalPortion";
    String SORT_BY_TOTAL_VOLUME = "totalVolume";
    String SORT_BY_ACCEPTABLE_DEVIATION = "acceptableDeviation";
    String SORT_BY_TOTAL = "total";
    String SORT_BY_BURRITO = "burrito";
    String SORT_BY_QUESADILLA = "quesadilla";
    String SORT_BY_KEBAB = "kebab";
    String SORT_BY_BOWL = "bowl";

}


