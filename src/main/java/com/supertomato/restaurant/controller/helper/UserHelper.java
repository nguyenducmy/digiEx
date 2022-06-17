package com.supertomato.restaurant.controller.helper;

import com.supertomato.restaurant.auth.AuthUser;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.common.util.AppUtil;
import com.supertomato.restaurant.common.util.DateUtil;
import com.supertomato.restaurant.common.util.UniqueID;
import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.controller.model.request.AddUser;
import com.supertomato.restaurant.controller.model.request.ChangePassword;
import com.supertomato.restaurant.controller.model.request.UpdateUser;
import com.supertomato.restaurant.controller.model.response.CompanyResponse;
import com.supertomato.restaurant.controller.model.response.UserResponse;
import com.supertomato.restaurant.controller.util.APIStatus;
import com.supertomato.restaurant.entity.Company;
import com.supertomato.restaurant.entity.Outlet;
import com.supertomato.restaurant.entity.User;
import com.supertomato.restaurant.entity.UserOutlet;
import com.supertomato.restaurant.service.CompanyService;
import com.supertomato.restaurant.service.OutletService;
import com.supertomato.restaurant.service.UserOutletService;
import com.supertomato.restaurant.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserHelper {

    public User createUser(AddUser addUser) {

        User user = new User();
        user.setId(UniqueID.getUUID());
        user.setFirstName(addUser.getFirstName());
        user.setLastName(addUser.getLastName());
        user.setEmail(addUser.getEmail());
        user.setActiveCode(UniqueID.getUUID());
        user.setUserRole(addUser.getUserRole());
        user.setPasswordHash(UniqueID.getUUID());
        user.setPasswordSalt(UniqueID.getUUID());
        user.setActiveCode(UniqueID.getUUID());
        user.setUserRoot(false);
        Date date = DateUtil.addHoursToJavaUtilDate(new Date(), 48);
        user.setExpiryDate(DateUtil.convertToUTC(date));
        user.setStatus(AppStatus.PENDING);
        return user;
    }

    public User updateUser(User user, AuthUser authUser, UpdateUser updateUser,List<Outlet> outlets, UserService userService,
                           UserOutletService userOutletService,OutletHelper outletHelper, OutletService outletService) {

        if (updateUser.getEmail() != null && !updateUser.getEmail().trim().isEmpty()) {
            Validator.validateEmail(updateUser.getEmail().trim());
            User userExited = userService.getByEmail(updateUser.getEmail().trim());
            if (userExited != null && !userExited.getId().equals(user.getId())) {
                throw new ApplicationException(APIStatus.EXISTED);
            }
            user.setEmail(updateUser.getEmail());
        }

        if (updateUser.getFirstName() != null && !updateUser.getFirstName().trim().isEmpty()) {
            user.setFirstName(updateUser.getFirstName().trim());
        }
        if (updateUser.getLastName() != null && !updateUser.getLastName().trim().isEmpty()) {
            user.setLastName(updateUser.getLastName().trim());
        }
        if (updateUser.getUserRole() != null && !updateUser.getUserRole().equals(user.getUserRole())) {
            if (UserRole.COMPANY_ADMIN.equals(authUser.getRole()) && UserRole.SUPER_ADMIN.equals(updateUser.getUserRole())) {
                throw new ApplicationException(APIStatus.USER_COMPANY_CANNOT_ADD_USER_SYSTEM);
            }
            if (UserRole.SUPER_ADMIN.equals(updateUser.getUserRole()) && !user.getUserRole().equals(UserRole.SUPER_ADMIN)) {
                List<UserOutlet> userOutlets = userOutletService.getAllByUserId(user.getId());
                outlets = outletService.getAllByOutlet();
                if (!userOutlets.isEmpty()) {
                    userOutletService.deleteAll(userOutlets);
                }
                outletHelper.addUserOutlet(outlets, user, userOutletService);
            }
            user.setUserRole(updateUser.getUserRole());
        }


        return user;
    }

    public List<UserResponse> getUserResponse(List<User> users, UserOutletService userOutletService, CompanyService companyService, OutletService outletService) {

        List<UserResponse> userList = new ArrayList<>();
        if (!users.isEmpty()) {
            List<String> userIds = users.stream().map(User::getId).collect(Collectors.toList());
            List<UserOutlet> userOutletList = userOutletService.getAllByUserIdIn(userIds);
            List<Company> companyList = new ArrayList<>();
            if (!userOutletList.isEmpty()) {
                List<String> companyIds = userOutletList.stream().map(UserOutlet::getCompanyId).distinct().collect(Collectors.toList());
                companyList = companyService.getAllByIdIn(companyIds);
            }

            for (User user : users) {
                List<CompanyResponse> companyResponses = new ArrayList<>();
                List<UserOutlet> userOutlets = userOutletList.stream().filter(userOutlet -> user.getId().equals(userOutlet.getUserId())).collect(Collectors.toList());
                if (!userOutlets.isEmpty()) {
                    List<String> _companyIds = userOutlets.stream().map(UserOutlet::getCompanyId).distinct().collect(Collectors.toList());
                    List<String> outletIds = userOutlets.stream().map(UserOutlet::getOutletId).distinct().collect(Collectors.toList());
                    List<Company> companies = companyList.stream().filter(company -> _companyIds.contains(company.getId())).collect(Collectors.toList());
                    List<Outlet> outletList = outletService.getAllByIdIn(outletIds);
                    for (Company company : companies) {
                        List<Outlet> outlets = outletList.stream().filter(outlet -> outlet.getCompanyId().equals(company.getId())).collect(Collectors.toList());
                        companyResponses.add(new CompanyResponse(company, outlets));
                    }
                }
                userList.add(new UserResponse(companyResponses, user));
            }

        }
        return userList;
    }


    public UserResponse getUserResponse(User user, UserOutletService userOutletService,
                                        CompanyService companyService, OutletService outletService) {

        List<UserOutlet> userOutlets = userOutletService.getAllByUserId(user.getId());
        List<CompanyResponse> companyList = new ArrayList<>();
        if (!userOutlets.isEmpty()) {
            List<String> companyIds = userOutlets.stream().map(UserOutlet::getCompanyId).distinct().collect(Collectors.toList());
            List<String> outletIds = userOutlets.stream().map(UserOutlet::getOutletId).distinct().collect(Collectors.toList());
            List<Company> companies = companyService.getAllByIdIn(companyIds);
            List<Outlet> outletList = outletService.getAllByIdIn(outletIds);
            for (Company company : companies) {
                List<Outlet> outlets = outletList.stream().filter(outlet -> company.getId().equals(outlet.getCompanyId())).collect(Collectors.toList());
                companyList.add(new CompanyResponse(company, outlets));
            }
        }

        return new UserResponse(companyList, user);
    }


    public User changePassword(User user, PasswordEncoder passwordEncoder, ChangePassword changePassword) {

        Date nowDate = new Date();
        if (nowDate.getTime() > user.getExpiryDate().getTime()) {
            throw new ApplicationException(APIStatus.EXPIRED, "Time change password is expired");
        }
        String passwordSalt = AppUtil.generateSalt();
        String passwordHash = passwordEncoder.encode(changePassword.getPasswordHash().concat(passwordSalt));
        user.setPasswordSalt(passwordSalt);
        user.setPasswordHash(passwordHash);
        if (AppStatus.PENDING.equals(user.getStatus())) {
            user.setStatus(AppStatus.ACTIVE);
        }
        return user;
    }

}
