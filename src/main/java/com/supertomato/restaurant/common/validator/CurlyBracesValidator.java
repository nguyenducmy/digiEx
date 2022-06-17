package com.supertomato.restaurant.common.validator;

import com.supertomato.restaurant.common.util.Constant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author DiGiEx
 */
public class CurlyBracesValidator implements ConstraintValidator<NoCurlyBraces, String> {
    public static final Pattern VALID_CURLY_BRACES = Pattern.compile(Constant.VALID_CURLY_BRACES, Pattern.CASE_INSENSITIVE);

    @Override
    public void initialize(NoCurlyBraces noCurlyBraces) {
        //TODO nothing to do in here
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        boolean valid;
        Matcher matcher = VALID_CURLY_BRACES.matcher(value);
        valid = matcher.matches();
        return valid;
    }
}
