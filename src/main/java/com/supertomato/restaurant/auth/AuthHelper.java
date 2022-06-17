package com.supertomato.restaurant.auth;

import com.supertomato.restaurant.common.util.DateUtil;
import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.controller.util.APIStatus;
import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.entity.Session;
import com.supertomato.restaurant.entity.User;
import com.supertomato.restaurant.service.SessionService;
import com.supertomato.restaurant.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author DiGiEx
 */
@Component
public class AuthHelper {
    final SessionService sessionService;
    final UserService userService;

    public AuthHelper(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    public AuthUser loadAuthUserFromAuthToken(String authToken) {
        Session session = sessionService.findById(authToken);
        Validator.notNull(session, APIStatus.UNAUTHORIZED, "");

        // Check expired date
        if (DateUtil.convertToUTC(new Date()).getTime() >= session.getExpiryDate().getTime()) {
            throw new ApplicationException(APIStatus.UNAUTHORIZED);
        }
        User user = userService.getById(session.getUserId());
        Validator.notNull(user, APIStatus.UNAUTHORIZED);
        return new AuthUser(user);
    }

}
