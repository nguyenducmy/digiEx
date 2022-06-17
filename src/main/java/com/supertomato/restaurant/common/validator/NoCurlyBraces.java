package com.supertomato.restaurant.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author DiGiEx
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = CurlyBracesValidator.class)
@Documented
public @interface NoCurlyBraces {

    String message() default "Contain string is not allowed.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
