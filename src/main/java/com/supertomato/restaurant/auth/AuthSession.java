package com.supertomato.restaurant.auth;

import java.lang.annotation.*;

/**
 * This annotation is handled by {@link AuthSessionResolver}. It will return a
 * {@link AuthUser} model by X-Access-Token header
 * in the request. By default it will
 * throw ApplicationException with status is ERR_UNAUTHORIZED if X-Access-Token
 * header is null or status is ERR_INVALID_TOKEN if can't find session by access
 * token. Otherwise, throw ApplicationException with status is ERR_TOKEN_EXPIRED
 * if token is expired
 *
 * @author NIT
 * @version 1.0
 * @see AuthSessionResolver
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface AuthSession {

    /**
     * If true then throw ApplicationException when access token header not
     * found
     *
     * @return
     */
    boolean required() default true;

}
