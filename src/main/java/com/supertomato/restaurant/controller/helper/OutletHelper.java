package com.supertomato.restaurant.controller.helper;

import com.supertomato.restaurant.auth.AuthUser;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.common.util.UniqueID;
import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.controller.model.request.*;
import com.supertomato.restaurant.controller.model.response.OutletResponse;
import com.supertomato.restaurant.controller.util.APIStatus;
import com.supertomato.restaurant.entity.*;
import com.supertomato.restaurant.service.CompanyService;
import com.supertomato.restaurant.service.OutletService;
import com.supertomato.restaurant.service.ProductLineService;
import com.supertomato.restaurant.service.UserOutletService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OutletHelper {

    public List<Outlet> addUserOutlets(AddUser addUser, User user, AuthUser authUser, OutletService outletService, UserOutletService userOutletService) {

        List<Outlet> outlets = new ArrayList<>();
        if (addUser.getOutletIds() != null && !addUser.getOutletIds().isEmpty()) {
            outlets = validateOutlet(addUser.getOutletIds(), outlets, outletService);
            if (!UserRole.SUPER_ADMIN.equals(authUser.getRole())) {
                List<UserOutlet> userOutlets = userOutletService.getAllByUserId(authUser.getId());
                List<String> companyIds = userOutlets.stream().map(UserOutlet::getCompanyId).distinct().collect(Collectors.toList());
                List<String> outletCompanyIds = outlets.stream().map(Outlet::getCompanyId).distinct().collect(Collectors.toList());
                List<String> companyIdNotExited = outletCompanyIds.stream().filter(oc -> !companyIds.contains(oc)).collect(Collectors.toList());
                if (!companyIdNotExited.isEmpty()) {
                    throw new ApplicationException(APIStatus.OUTLET_INVALID);
                }
            }
            addUserOutlet(outlets, user, userOutletService);
        }
        return outlets;
    }

    public List<Outlet> addOutlets(Company company, AddCompany addCompany) {
        List<Outlet> outletList = new ArrayList<>();
        if (addCompany.getOutlets() != null && !addCompany.getOutlets().isEmpty()) {
            for (OutletRequest outletRequest : addCompany.getOutlets()) {
                Validator.validatePassword(outletRequest.getKitchenPassword().trim());
                Outlet outlet = new Outlet();
                outlet.setId(UniqueID.getUUID());
                outlet.setCompanyId(company.getId());
                outlet.setOutletPrefix(outletRequest.getOutletPrefix().trim());
                outlet.setKitchenPassword(outletRequest.getKitchenPassword().trim());
                outlet.setKitchenAccount(addCompany.getCompanyPrefix().trim() + "-" + outletRequest.getOutletPrefix().trim());
                outlet.setName(outletRequest.getName().trim());
                outlet.setStatus(AppStatus.ACTIVE);
                outlet.setStartLineStatus(AppStatus.INCOMPLETE);
                outlet.setEndLineStatus(AppStatus.INCOMPLETE);
                outlet.setMasterIngredientsStatus(AppStatus.INCOMPLETE);
                outletList.add(outlet);
            }
        }
        return outletList;
    }


    public Outlet updateOutlet(UpdateOutlet updateOutlet, Outlet outlet, OutletService outletService, CompanyService companyService) {

        if (updateOutlet.getOutletPrefix() != null && !updateOutlet.getOutletPrefix().trim().isEmpty() &&
                !outlet.getOutletPrefix().equals(updateOutlet.getOutletPrefix().trim())) {

            Outlet outletPrefix = outletService.getByCompanyIdAndOutletPrefix(outlet.getCompanyId(), updateOutlet.getOutletPrefix().trim());
            if (outletPrefix != null && !outletPrefix.getId().equals(outlet.getId())) {
                throw new ApplicationException(APIStatus.OUTLET_PREFIX_EXISTED);
            }
            Company company = companyService.getById(outlet.getCompanyId());
            Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");
            outlet.setOutletPrefix(updateOutlet.getOutletPrefix().trim());
            outlet.setKitchenAccount(company.getCompanyPrefix().trim() + "-" + outlet.getOutletPrefix().trim());
        }

        if (updateOutlet.getName() != null && !updateOutlet.getName().trim().isEmpty()) {
            outlet.setName(updateOutlet.getName().trim());
        }

        if (updateOutlet.getKitchenPassword() != null && !updateOutlet.getKitchenPassword().trim().isEmpty() &&
                !outlet.getKitchenPassword().equals(updateOutlet.getKitchenPassword().trim())) {
            Validator.validatePassword(updateOutlet.getKitchenPassword().trim());
            outlet.setKitchenPassword(updateOutlet.getKitchenPassword().trim());
        }

        if (updateOutlet.getEndLineStatus() != null) {
            outlet.setEndLineStatus(updateOutlet.getEndLineStatus());
        }

        if (updateOutlet.getStartLineStatus() != null) {
            outlet.setStartLineStatus(updateOutlet.getStartLineStatus());
        }

        if (updateOutlet.getMasterIngredientsStatus() != null) {
            outlet.setMasterIngredientsStatus(updateOutlet.getMasterIngredientsStatus());
        }

        return outlet;
    }

    public Outlet addOutlet(AddOutlet addOutlet, Company company) {


        Validator.validatePassword(addOutlet.getKitchenPassword().trim());
        Outlet outlet = new Outlet();
        outlet.setId(UniqueID.getUUID());
        outlet.setCompanyId(company.getId());
        outlet.setOutletPrefix(addOutlet.getOutletPrefix().trim());
        outlet.setKitchenPassword(addOutlet.getKitchenPassword().trim());
        outlet.setKitchenAccount(company.getCompanyPrefix().trim() + "-" + outlet.getOutletPrefix().trim());
        outlet.setName(addOutlet.getName().trim());
        outlet.setStatus(AppStatus.ACTIVE);
        outlet.setStartLineStatus(AppStatus.INCOMPLETE);
        outlet.setEndLineStatus(AppStatus.INCOMPLETE);
        outlet.setMasterIngredientsStatus(AppStatus.INCOMPLETE);
        return outlet;
    }

    public List<Outlet> updateUserOutlets(UpdateUser updateUser, User user, List<Outlet> outlets, AuthUser authUser, OutletService outletService, UserOutletService userOutletService) {

        if (updateUser.getOutletIds() != null) {
            List<UserOutlet> userOutlets = userOutletService.getAllByUserId(user.getId());
            if (!updateUser.getOutletIds().isEmpty()) {
                outlets = validateOutlet(updateUser.getOutletIds(), outlets, outletService);
                if (!UserRole.SUPER_ADMIN.equals(authUser.getRole())) {
                    List<String> userIds = Arrays.asList(authUser.getId(), user.getId());
                    List<UserOutlet> userOutletList = userOutletService.getAllByUserIdIn(userIds);
                    List<String> outletCompanyIds = outlets.stream().map(Outlet::getCompanyId).distinct().collect(Collectors.toList());
                    List<String> companyIds = userOutletList.stream().map(UserOutlet::getCompanyId).collect(Collectors.toList());
                    List<String> companyIdNotExited = outletCompanyIds.stream().filter(oc -> !companyIds.contains(oc)).collect(Collectors.toList());
                    if (!companyIdNotExited.isEmpty()) {
                        throw new ApplicationException(APIStatus.OUTLET_INVALID);
                    }
                }
                addUserOutlet(outlets, user, userOutletService);
            }
            if (!userOutlets.isEmpty()) {
                userOutletService.deleteAll(userOutlets);
            }
        }

        return outlets;
    }

    public void addUserOutlet(List<Outlet> outletList, User user, UserOutletService userOutletService) {

        List<UserOutlet> userOutlets = new ArrayList<>();
        for (Outlet outlet : outletList) {
            UserOutlet userOutlet = new UserOutlet();
            userOutlet.setId(UniqueID.getUUID());
            userOutlet.setUserId(user.getId());
            userOutlet.setOutletId(outlet.getId());
            userOutlet.setCompanyId(outlet.getCompanyId());
            userOutlets.add(userOutlet);
        }
        userOutletService.saveAll(userOutlets);
    }

    public List<Outlet> validateOutlet(List<String> outletIds, List<Outlet> outlets, OutletService outletService) {

        List<String> _outletIds = outletIds.stream().distinct().collect(Collectors.toList()); //get outletIds form FE
        if (outletIds.size() != _outletIds.size()) {
            throw new ApplicationException(APIStatus.BAD_REQUEST, " OutletId is duplicate");
        }
        outlets = outletService.getAllByIdIn(_outletIds); //get outletIds form database
        if (outlets.size() != _outletIds.size()) {
            throw new ApplicationException(APIStatus.NOT_FOUND, " Outlet Not Found");
        }
        return outlets;
    }

    public List<Outlet> addOutletCompany(AddCompany addCompany, Company company, ProductLineHelper productLineHelper, ProductLineService productLineService, OutletService outletService) {

        List<Outlet> outletList = new ArrayList<>();
        if (addCompany.getOutlets() != null && !addCompany.getOutlets().isEmpty()) {

            List<String> outletPrefixes = addCompany.getOutlets().stream().map(OutletRequest::getOutletPrefix).distinct().collect(Collectors.toList());

            List<String> outletPrefixList = new ArrayList<>();
            for (String outletPrefix : outletPrefixes) {
                outletPrefixList.add(outletPrefix.trim());
            }

            if (outletPrefixList.stream().distinct().count() != addCompany.getOutlets().size()) {
                throw new ApplicationException(APIStatus.OUTLET_PREFIX_EXISTED);
            }

            outletList = addOutlets(company, addCompany);
            if (!outletList.isEmpty()) {
                List<ProductLine> productLines = new ArrayList<>();
                for (Outlet outlet : outletList) {
                    productLines = productLineHelper.addProductLine(productLines, outlet.getId());
                }
                productLineService.saveAll(productLines);
                outletService.saveAll(outletList);
            }
        }
        return outletList;
    }


    public List<OutletResponse> getOutletResponse(String companyId, boolean isAsc, String sortField,
                                                  CompanyService companyService, OutletService outletService,
                                                  UserOutletService userOutletService, AuthUser authUser) {
        List<OutletResponse> outletList = new ArrayList<>();

        if (companyId != null && !companyId.trim().isEmpty()) {
            Company company = companyService.getById(companyId.trim());
            Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");
            outletList = outletService.getAllByCompanyId(company.getId(), null, isAsc, sortField);
        } else {
            List<UserOutlet> userOutlets = userOutletService.getAllByUserId(authUser.getId());
            if (!userOutlets.isEmpty()) {
                List<String> outletIds = userOutlets.stream().map(UserOutlet::getOutletId).distinct().collect(Collectors.toList());
                outletList = outletService.getAllByCompanyId(null, outletIds, isAsc, sortField);
                if (!outletList.isEmpty() && sortField != null && "companyName".equals(sortField.trim())) {
                    if (isAsc) {
                        outletList = outletList.stream().sorted(Comparator.comparing(OutletResponse::getCompanyName)).collect(Collectors.toList());
                    } else {
                        outletList = outletList.stream().sorted(Comparator.comparing(OutletResponse::getCompanyName).reversed()).collect(Collectors.toList());
                    }
                }
            }
        }
        return outletList;
    }


    public void addUserOutlet(List<Outlet> outlets, Outlet outlet, UserOutletService userOutletService) {

        List<UserOutlet> userOutletList = userOutletService.getAllByCompanyId(outlet.getCompanyId());
        if (!userOutletList.isEmpty() && !outlets.isEmpty()) {
            List<String> userIds = userOutletList.stream().map(UserOutlet::getUserId).distinct().collect(Collectors.toList());
            List<UserOutlet> userOutlets = new ArrayList<>();
            for (String userId : userIds) {
                long userOutletSize = userOutletList.stream().filter(userOutlet -> userOutlet.getUserId().equals(userId)).count();
                if (userOutletSize == outlets.size()) {
                    UserOutlet userOutlet = new UserOutlet();
                    userOutlet.setId(UniqueID.getUUID());
                    userOutlet.setUserId(userId);
                    userOutlet.setCompanyId(outlet.getCompanyId());
                    userOutlet.setOutletId(outlet.getId());
                    userOutlets.add(userOutlet);
                }
            }
            if (!userOutlets.isEmpty()) {
                userOutletService.saveAll(userOutlets);
            }
        }
    }

}
