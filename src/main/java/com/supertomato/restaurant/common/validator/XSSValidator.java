package com.supertomato.restaurant.common.validator;

import com.supertomato.restaurant.common.util.Constant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: DiGiEx
 */
public class XSSValidator implements ConstraintValidator<NoXSS, String> {
    public static final Pattern VALID_XSS = Pattern.compile(Constant.VALID_XSS, Pattern.CASE_INSENSITIVE);

    @Override
    public void initialize(NoXSS noXSS) {
        //TODO nothing to do in here
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        boolean valid;
        Matcher matcher = VALID_XSS.matcher(value);
        valid = matcher.matches();
        return valid;
    }
}
