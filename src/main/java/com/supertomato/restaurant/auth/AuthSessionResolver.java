package com.supertomato.restaurant.auth;

import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.controller.util.APIStatus;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author DiGiEx
 */
@Component
public class AuthSessionResolver implements HandlerMethodArgumentResolver {

    private final AuthHelper authHelper;

    public AuthSessionResolver(AuthHelper authHelper) {
        this.authHelper = authHelper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.getParameterAnnotation(AuthSession.class) != null);
    }

    @Override
    public AuthUser resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory){

        AuthUser authUser = null;
        AuthSession authSession = parameter.getParameterAnnotation(AuthSession.class);

        if (authSession != null) {
            try {
                authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Validator.notNull(authUser, APIStatus.UNAUTHORIZED);
            }catch (Exception e){
                throw new ApplicationException(APIStatus.UNAUTHORIZED, e.getMessage());
            }
        }
        return authUser;
    }

}
