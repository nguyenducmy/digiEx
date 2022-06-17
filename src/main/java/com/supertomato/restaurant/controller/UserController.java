package com.supertomato.restaurant.controller;

import com.supertomato.restaurant.auth.AuthSession;
import com.supertomato.restaurant.auth.AuthUser;
import com.supertomato.restaurant.auth.AuthorizeValidator;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.common.util.AppUtil;
import com.supertomato.restaurant.common.util.DateUtil;
import com.supertomato.restaurant.common.util.UniqueID;
import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.controller.helper.EmailSenderHelper;
import com.supertomato.restaurant.controller.helper.OutletHelper;
import com.supertomato.restaurant.controller.helper.SessionHelper;
import com.supertomato.restaurant.controller.helper.UserHelper;
import com.supertomato.restaurant.controller.model.request.AddUser;
import com.supertomato.restaurant.controller.model.request.Assume;
import com.supertomato.restaurant.controller.model.request.ChangePassword;
import com.supertomato.restaurant.controller.model.request.UpdateUser;
import com.supertomato.restaurant.controller.model.response.CompanyResponse;
import com.supertomato.restaurant.controller.model.response.LoginResponse;
import com.supertomato.restaurant.controller.model.response.PagingResponse;
import com.supertomato.restaurant.controller.model.response.UserResponse;
import com.supertomato.restaurant.controller.util.APIStatus;
import com.supertomato.restaurant.controller.util.RestAPIResponse;
import com.supertomato.restaurant.entity.*;
import com.supertomato.restaurant.service.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(APIName.USER_API)
public class UserController extends AbstractBaseController {

    final PasswordEncoder passwordEncoder;
    final UserService userService;
    final CompanyService companyService;
    final OutletService outletService;
    final UserOutletService userOutletService;
    final UserHelper userHelper;
    final OutletHelper outletHelper;
    final EmailSenderHelper emailSenderHelper;
    final SessionHelper sessionHelper;
    final SessionService sessionService;


    public UserController(
            PasswordEncoder passwordEncoder,
            UserService userService,
            CompanyService companyService,
            OutletService outletService,
            UserOutletService userOutletService,
            UserHelper userHelper,
            OutletHelper outletHelper,
            EmailSenderHelper emailSenderHelper,
            SessionHelper sessionHelper,
            SessionService sessionService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.companyService = companyService;
        this.outletService = outletService;
        this.userOutletService = userOutletService;
        this.userHelper = userHelper;
        this.outletHelper = outletHelper;
        this.emailSenderHelper = emailSenderHelper;
        this.sessionHelper = sessionHelper;
        this.sessionService = sessionService;
    }

    /**
     * get User detail API
     *
     * @param id
     * @return
     */

    //get User detail API
    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.ID, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getUserDetail(
            @PathVariable("id") String id,
            @AuthSession AuthUser authUser
    ) {

        User user = userService.getByUserId(id);
        Validator.notNull(user, APIStatus.NOT_FOUND, "User not found");

        if (UserRole.SUPER_ADMIN.equals(user.getUserRole()) && UserRole.COMPANY_ADMIN.equals(authUser.getRole())) {
            throw new ApplicationException(APIStatus.FORBIDDEN);
        }
        return responseUtil.successResponse(userHelper.getUserResponse(user, userOutletService, companyService, outletService));
    }

    /**
     * Add new user API
     * Get all user with SuperAdmin, CompanyAdmin
     */

    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.PAGE, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getPageUser(
            @AuthSession AuthUser authUser,
            @RequestParam(name = "sort", required = false, defaultValue = "false") boolean ascSort,
            @RequestParam(name = "sort_field", required = false, defaultValue = "") String sortField,
            @RequestParam(name = "page", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "5") int pageSize
    ) {

        validatePageSize(pageNumber, pageSize);
        List<String> userIds = null;
        if (authUser.getRole().equals(UserRole.COMPANY_ADMIN)) {
            List<UserOutlet> userOutlets = userOutletService.getAllByUserId(authUser.getId());
            List<String> companyIds = userOutlets.stream().map(UserOutlet::getCompanyId).distinct().collect(Collectors.toList());
            List<UserOutlet> userOutletList = userOutletService.getAllByCompanyIdInAndUserIdNot(companyIds, authUser.getId());
            userIds = userOutletList.stream().map(UserOutlet::getUserId).distinct().collect(Collectors.toList());
        }
        //get User Page
        Page<User> userPage = userService.getPageUser(authUser.getRole(), userIds, sortField, ascSort, pageNumber, pageSize);
        // get User Response
        List<UserResponse> users = userHelper.getUserResponse(userPage.getContent(), userOutletService, companyService, outletService);

        return responseUtil.successResponse(new PagingResponse(users, userPage));
    }


    /**
     * Add new user
     */
    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.ADD, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> createUser(
            @RequestBody @Valid AddUser addUser,
            @AuthSession AuthUser authUser
    ) {
        // validate email format using regex to validate
        Validator.validateEmail(addUser.getEmail().trim());

        if (UserRole.COMPANY_ADMIN.equals(authUser.getRole()) && UserRole.SUPER_ADMIN.equals(addUser.getUserRole())) {
            throw new ApplicationException(APIStatus.USER_COMPANY_CANNOT_ADD_USER_SYSTEM);
        }
        User emailExited = userService.getByEmail(addUser.getEmail());
        Validator.mustNull(emailExited, APIStatus.EXISTED, "");
        //create User
        User user = userHelper.createUser(addUser);
        List<Outlet> outletList;
        // create UserOutlet
        if (UserRole.SUPER_ADMIN.equals(user.getUserRole())) {
            outletList = outletService.getAllByOutlet();
            outletHelper.addUserOutlet(outletList, user, userOutletService);
        } else {
            outletList = outletHelper.addUserOutlets(addUser, user, authUser, outletService, userOutletService);
        }
        user = userService.save(user);
        emailSenderHelper.confirmEmail(user);
        return responseUtil.successResponse(new UserResponse(user, outletList));
    }

    /**
     * Delete user API
     */
    //Delete user API
    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.DELETE_ID, method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteUser(
            @PathVariable("id") String id,
            @AuthSession AuthUser authUser
    ) {
        User user = userService.getByUserId(id);
        Validator.notNull(user, APIStatus.NOT_FOUND, "User not found");

        if (UserRole.COMPANY_ADMIN.equals(authUser.getRole()) && UserRole.SUPER_ADMIN.equals(user.getUserRole())) {
            throw new ApplicationException(APIStatus.ERR_PERMISSION);
        }
        if (user.isUserRoot()) {
            throw new ApplicationException(APIStatus.CANNOT_DELETE_USER_WITH_ROLE_SYSTEM_ADMIN);
        }
        List<UserOutlet> userOutlets = userOutletService.getAllByUserId(user.getId());
        if (!userOutlets.isEmpty()) {
            userOutletService.deleteAll(userOutlets);
        }
        user.setStatus(AppStatus.INACTIVE);
        userService.save(user);
        return responseUtil.successResponse("DELETE OK");
    }
    //Change Password

    @RequestMapping(path = APIName.ADMIN_CHANGE_PASSWORD, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> changePassword(
            @PathVariable("active_code") String activeCode,
            @RequestBody @Valid ChangePassword changePassword
    ) {
        if (!changePassword.getPasswordHash().trim().equals(changePassword.getConfirmPassword().trim())) {
            throw new ApplicationException(APIStatus.BAD_REQUEST, "New Password and Confirm Password not match ");
        }
        User user = userService.getByActiveCode(activeCode);
        Validator.notNull(user, APIStatus.NOT_FOUND, "User not found");

        user = userHelper.changePassword(user, passwordEncoder, changePassword);
        userService.save(user);
        return responseUtil.successResponse("Change Success");
    }


    /**
     * Update user
     */
    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.UPDATE_ID, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateUser(
            @AuthSession AuthUser authUser,
            @PathVariable("id") String id,
            @RequestBody @Valid UpdateUser updateUser
    ) {
        User user = userService.getByUserId(id);
        Validator.notNull(user, APIStatus.NOT_FOUND, "User not found");
        if (UserRole.SUPER_ADMIN.equals(user.getUserRole()) && UserRole.COMPANY_ADMIN.equals(authUser.getRole())) {
            throw new ApplicationException(APIStatus.ERR_PERMISSION);
        }
        UserRole userRole = user.getUserRole();

        List<Outlet> outlets = new ArrayList<>();
        // validate email format using regex to validate
        user = userHelper.updateUser(user, authUser, updateUser, outlets, userService, userOutletService, outletHelper, outletService);
        if (!UserRole.SUPER_ADMIN.equals(userRole)) {
            outlets = outletHelper.updateUserOutlets(updateUser, user, outlets, authUser, outletService, userOutletService);
        }
        userService.save(user);
        return responseUtil.successResponse(new UserResponse(user, outlets));
    }


    /**
     * Update user
     */
    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.RESEND_ID, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> resendInvitation(
            @PathVariable("id") String id
    ) {
        User user = userService.getByUserId(id);
        Validator.notNull(user, APIStatus.NOT_FOUND, "User not found");

        user.setActiveCode(UniqueID.getUUID());
        Date date = DateUtil.addHoursToJavaUtilDate(new Date(), 48);
        user.setExpiryDate(DateUtil.convertToUTC(date));
        userService.save(user);
        emailSenderHelper.confirmEmail(user);
        return responseUtil.successResponse("Resend Invitation Success");
    }


    /**
     * Update user
     */
    @AuthorizeValidator(UserRole.SUPER_ADMIN)
    @RequestMapping(path = APIName.ASSUME, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> assume(
            @RequestBody @Valid Assume assume
    ) {
        User user = userService.getByUserId(assume.getUserId());
        Validator.notNull(user, APIStatus.NOT_FOUND, "User not found");

        if (UserRole.SUPER_ADMIN.equals(user.getUserRole())) {
            throw new ApplicationException(APIStatus.ERR_PERMISSION);
        }
        Session session = sessionHelper.createSession(user, false);
        sessionService.save(session);
        return responseUtil.successResponse(new LoginResponse(session.getId(), session.getExpiryDate().getTime(), user.getUserRole()));
    }


}
