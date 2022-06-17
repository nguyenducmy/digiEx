package com.supertomato.restaurant.auth;

import com.supertomato.restaurant.common.enums.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Quy Duong
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeValidator {
    UserRole[] value() default UserRole.SUPER_ADMIN;
}
