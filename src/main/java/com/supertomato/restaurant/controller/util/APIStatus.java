package com.supertomato.restaurant.controller.util;

/**
 * @author DiGiEx
 */
public enum APIStatus {
    //////////////////
    //   DEFAULT    //
    //////////////////
    OK(200, "OK"),
    NO_RESULT(201, "No more result."),
    FAIL(202, "Fail"),

    //////////////////
    // CLIENT SIDE  //
    //////////////////
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "Unauthorized or Access Token is expired"),
    INVALID_AUTHENTICATE_CREDENTIAL(402, "Invalid authenticated credential"),
    FORBIDDEN(403, "Forbidden! Access denied"),
    NOT_FOUND(404, "Not Found"),
    EXISTED(405, "Email already existed"),
    BAD_PARAMS(406, "There is some invalid data"),
    EXPIRED(407, "Expired"),
    INVALID_FILE(408, "Invalid file"),
    INVALID_PHONE(409, "Invalid PhoneNumber"),
    ALREADY_VERIFIED(410, "Already verified"),
    INVALID(411, "Invalid file"),
    ERR_INVALID_EMAIL_FORMAT(412, "Invalid email format"),
    ERR_PASSWORD_INVALID_FORMAT(413, "Password invalid format"),
    CANNOT_CHANGE_PASS (414,"Password cannot be change"),
    ERROR_CHANGE_PASSWORD(415,"Error change password"),
    CAN_ONLY_CREATE_4_INGREDIENT_IN_MODULE(416,"Can only create 4 Ingredients in module"),
    COMPANY_PREFIX_EXISTED(417, "Company prefix already existed"),
    OUTLET_PREFIX_EXISTED(418, "Outlet prefix already existed"),
    CAN_ONLY_CREATE_UP_8_MODULE(419, "Can only create up to 8 modules"),

    CANNOT_ASSUME_FOR_USER_SYSTEM_ADMIN(420, "Cannot Assume for User with role is System Admin"),
    CANNOT_DELETE_USER_WITH_ROLE_SYSTEM_ADMIN(421, "Cannot delete User with role is Root System Admin"),
    USER_COMPANY_CANNOT_ADD_USER_SYSTEM(422, "Admin Company cannot add User System Admin"),
    OUTLET_INVALID(423, "Outlets Invalid"),
    ERR_PERMISSION(424, "You do not have permission to do this action"),

    ERR_TIME_ZONE_INVALID(425, "Timezone invalid"),





    INTERNAL_SERVER_ERROR(500, "Internal server error");

    private final int code;
    private final String description;

    private APIStatus(int s, String v) {
        code = s;
        description = v;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static APIStatus getEnum(int code) {
        for (APIStatus v : values())
            if (v.getCode() == code) return v;
        throw new IllegalArgumentException();
    }
}
