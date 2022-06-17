package com.supertomato.restaurant.controller;

import com.supertomato.restaurant.auth.AuthSession;
import com.supertomato.restaurant.auth.AuthUser;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.common.util.*;
import com.supertomato.restaurant.controller.helper.EmailSenderHelper;
import com.supertomato.restaurant.controller.helper.SessionHelper;
import com.supertomato.restaurant.controller.model.request.ForgotPassword;
import com.supertomato.restaurant.controller.model.request.LoginRequest;
import com.supertomato.restaurant.controller.model.response.LoginResponse;
import com.supertomato.restaurant.controller.util.APIStatus;
import com.supertomato.restaurant.controller.util.RestAPIResponse;
import com.supertomato.restaurant.entity.Session;
import com.supertomato.restaurant.entity.User;
import com.supertomato.restaurant.service.SessionService;
import com.supertomato.restaurant.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * @author Quy Duong
 */
@RestController
@RequestMapping(APIName.AUTHENTICATE_API)
public class AuthenticateController extends AbstractBaseController {

    final SessionService sessionService;
    final SessionHelper sessionHelper;
    final PasswordEncoder passwordEncoder;
    final UserService userService;
    final EmailSenderHelper emailSenderHelper;

    final ApplicationValueConfigure applicationValueConfigure;

    public AuthenticateController(SessionService sessionService, PasswordEncoder passwordEncoder,
                                  SessionHelper sessionHelper, UserService userService,
                                  EmailSenderHelper emailSenderHelper, ApplicationValueConfigure applicationValueConfigure) {
        this.sessionService = sessionService;
        this.passwordEncoder = passwordEncoder;
        this.sessionHelper = sessionHelper;
        this.userService = userService;
        this.emailSenderHelper = emailSenderHelper;
        this.applicationValueConfigure = applicationValueConfigure;
    }

    /**
     * User Login API
     *
     * @param loginRequest
     * @return
     */
    @RequestMapping(path = APIName.AUTHENTICATE_LOGIN, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> login(
            @RequestBody @Valid LoginRequest loginRequest
    ) {
        // validate user existed
        User user = userService.getByEmail(loginRequest.getEmail().trim());
        Validator.notNull(user, APIStatus.INVALID_AUTHENTICATE_CREDENTIAL, "User does not existed");

        // validate password
        if (!passwordEncoder.matches(loginRequest.getPasswordHash().trim().concat(user.getPasswordSalt()), user.getPasswordHash())) {
            throw new ApplicationException(APIStatus.INVALID_AUTHENTICATE_CREDENTIAL, "Wrong password");
        }

        Session session = sessionHelper.createSession(user, loginRequest.isKeepLogin());
        sessionService.save(session);
        return responseUtil.successResponse(new LoginResponse(session.getId(), session.getExpiryDate().getTime(), user.getUserRole()));
    }

    /**
     * Get User Authenticate Information
     *
     * @param authUser
     * @return
     */
    @RequestMapping(path = APIName.AUTHENTICATE_INFO, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAuthInfo(
            @AuthSession AuthUser authUser
    ) {
        // return auth user info
        return responseUtil.successResponse(authUser);
    }

    /**
     * Logout API
     *
     * @param request
     * @return
     */
    @RequestMapping(path = APIName.AUTHENTICATE_LOGOUT, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> logout(HttpServletRequest request) {
        String authToken = request.getHeader(Constant.HEADER_TOKEN);
        Session session = sessionService.findById(authToken);
        if (session != null) {
            sessionService.delete(session);
        }
        return responseUtil.successResponse("Logout Successfully");
    }

    /**
     * Get User Authenticate Information
     *
     * @return
     */
    @RequestMapping(path = APIName.AUTHENTICATE_FORGOT_PASSWORD, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> forgotPassword(
            @RequestBody @Valid ForgotPassword forgotPassword
    ) {
        // validate user existed
        User user = userService.getByEmail(forgotPassword.getEmail().trim());
        Validator.notNull(user, APIStatus.NOT_FOUND, "User does not existed");
        user.setActiveCode(UniqueID.getUUID());
        Date date = DateUtil.addHoursToJavaUtilDate(new Date(), 48);
        user.setExpiryDate(DateUtil.convertToUTC(date));
        userService.save(user);
        emailSenderHelper.changePassword(user);
        return responseUtil.successResponse("OK");
    }

}
