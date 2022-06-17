package com.supertomato.restaurant.common.util;

import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.controller.util.APIStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author DiGiEx
 */
public class Validator {



    /*
     * Validate an {@code obj} must null. If {@code obj} is NOT null then throw
     * exception with {@code apiStatus}
     *
     * @param obj
     * @param apiStatus
     * @param message
     * @throws ApplicationException if {@code obj} is NOT null
     */
    public static void mustNull(Object obj, APIStatus apiStatus, String message) {

        if (obj != null) {
            throw new ApplicationException(apiStatus, message);
        }
    }

    /**
     * Use when validate {@code obj} must not be null. Throw
     * ApplicationException with {@code apiStatus} if {@code obj} is null
     *
     * @param obj
     * @param apiStatus APIStatus will throw when failed
     * @param message
     * @throws ApplicationException if {@code obj} is null
     */
    public static void notNull(Object obj, APIStatus apiStatus, String message) {

        if (obj == null) {
            throw new ApplicationException(apiStatus, message);
        }
    }

    public static void notNull(Object obj, APIStatus apiStatus) {

        if (obj == null) {
            throw new ApplicationException(apiStatus, "");
        }
    }

    /**
     * Validate list object not null & not empty
     *
     * @param obj
     * @param apiStatus
     * @param message
     */
    public static void notNullAndNotEmpty(List<?> obj, APIStatus apiStatus, String message) {

        if (obj == null || obj.isEmpty()) {
            throw new ApplicationException(apiStatus, message);
        }
    }

    /**
     * Validate object not null & not empty
     *
     * @param obj
     * @param apiStatus
     * @param message
     */
    public static void notNullAndNotEmpty(Object obj, APIStatus apiStatus, String message) {

        if (obj == null || "".equals(obj)) {
            throw new ApplicationException(apiStatus, message);
        }
    }

    /**
     * Validate list array not null & not empty
     *
     * @param arrays
     * @param apiStatus
     * @param message
     */
    public static void notNullAndNotEmpty(String[] arrays, APIStatus apiStatus, String message) {

        for (String str : arrays) {
            if (str == null || "".equals(str)) {
                throw new ApplicationException(apiStatus, message);
            }
        }
    }

    /**
     * Validate list object must null & must empty
     *
     * @param obj
     * @param apiStatus
     * @param message
     */
    public static void mustNullAndMustEmpty(List<?> obj, APIStatus apiStatus, String message) {
        if (obj != null && !obj.isEmpty()) {
            throw new ApplicationException(apiStatus, message);
        }
    }

    public static void mustEquals(String str1, String str2, APIStatus apiStatus, String message) {
        if (!str1.equals(str2)) {
            throw new ApplicationException(apiStatus, message);
        }
    }


    public static void mustNotEquals(String str1, String str2, APIStatus apiStatus, String message) {
        if (str1.equals(str2)) {
            throw new ApplicationException(apiStatus, message);
        }
    }


    public static void mustContainString(String value, List<String> containList) {
        if (!containList.contains(value)) {
            throw new ApplicationException(APIStatus.BAD_REQUEST, "Value " + value + " was not in " + containList.toString());
        }
    }

    public static void checkFileExtensionType(MultipartFile file, String[] extensions) {
        // get extension
        String extensionType = AppUtil.getFileExtension(file);
        if (!Arrays.asList(extensions).contains(extensionType)) {
            throw new ApplicationException(APIStatus.INVALID_FILE);
        }
    }

    public static void validPhoneNumber(String phone) {
        phone = phone.replaceAll(" ", "").replace("-","").replace("+","");
        String regex = "\\d{8,15}";
        boolean isPhone = phone.matches(regex);
        if (!isPhone) {
            throw new ApplicationException(APIStatus.INVALID_PHONE);
        }
    }

    public static double round(double performance) {
        return (double) Math.round(performance* 10) / 10;
    }


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    public static boolean isEmailFormat(String valueToValidate) {
        Pattern regexPattern = Pattern.compile(Constant.MAIL_ADDRESS_PATTERN);

        if (valueToValidate != null) {
            if (valueToValidate.indexOf("@") <= 0) {
                return false;
            }
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(valueToValidate);
            return matcher.matches();
        } else { // The case of empty Regex expression must be accepted
            Matcher matcher = regexPattern.matcher("");
            return matcher.matches();
        }
    }

    /**
     * validate email format
     *
     * @param
     */
    public static void validateEmail(String emailAddress) {
        boolean isEmailFormat = isEmailFormat(emailAddress);
        if (!isEmailFormat) {
            throw new ApplicationException(APIStatus.ERR_INVALID_EMAIL_FORMAT);
        }

    }

    /**
     * validate password format
     *
     * @param password
     */
    public static void validatePassword(String password) {
        boolean isPassword = password.trim().matches(Constant.PATTERN_PASSWORD_STRICT);
        // check invalid password
        if (!isPassword) {
            throw new ApplicationException(APIStatus.ERR_PASSWORD_INVALID_FORMAT, "Min is 8 characters and Max is 32 characters, at least 1 numeric ,1 lowercase ,1 uppercase ,1 special character");
        }

    }



}
