package com.supertomato.restaurant.controller;

import com.supertomato.restaurant.auth.AuthSession;
import com.supertomato.restaurant.auth.AuthUser;
import com.supertomato.restaurant.auth.AuthorizeValidator;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.util.UniqueID;
import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.controller.helper.OutletHelper;
import com.supertomato.restaurant.controller.helper.ProductLineHelper;
import com.supertomato.restaurant.controller.model.request.AddOutlet;
import com.supertomato.restaurant.controller.model.request.UpdateOutlet;
import com.supertomato.restaurant.controller.model.response.OutletResponse;
import com.supertomato.restaurant.controller.util.APIStatus;
import com.supertomato.restaurant.controller.util.RestAPIResponse;
import com.supertomato.restaurant.entity.Company;
import com.supertomato.restaurant.entity.Outlet;
import com.supertomato.restaurant.entity.ProductLine;
import com.supertomato.restaurant.entity.UserOutlet;
import com.supertomato.restaurant.service.CompanyService;
import com.supertomato.restaurant.service.OutletService;
import com.supertomato.restaurant.service.ProductLineService;
import com.supertomato.restaurant.service.UserOutletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(APIName.OUTLET)
public class OutletController extends AbstractBaseController {

    final CompanyService companyService;
    final OutletService outletService;
    final UserOutletService userOutletService;
    final OutletHelper outletHelper;
    final ProductLineHelper productLineHelper;
    final ProductLineService productLineService;


    public OutletController(CompanyService companyService,
                            OutletService outletService,
                            UserOutletService userOutletService,
                            OutletHelper outletHelper,
                            ProductLineHelper productLineHelper,
                            ProductLineService productLineService) {
        this.companyService = companyService;
        this.outletService = outletService;
        this.userOutletService = userOutletService;
        this.outletHelper = outletHelper;
        this.productLineHelper = productLineHelper;
        this.productLineService = productLineService;
    }


    /**
     * get getOutlets API
     *
     * @param isAsc,sortField
     * @return
     */
    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.LIST, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getOutlets(
            @AuthSession AuthUser authUser,
            @RequestParam(name = "sort", required = false, defaultValue = "false") boolean isAsc,
            @RequestParam(name = "sort_field", required = false, defaultValue = "") String sortField,
            @RequestParam(value = "company_id", required = false) String companyId) {

        return responseUtil.successResponse(outletHelper.getOutletResponse(companyId, isAsc, sortField, companyService, outletService, userOutletService, authUser));
    }

    /**
     * get Outlet Detail API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator(UserRole.SUPER_ADMIN)
    @RequestMapping(path = APIName.DETAIL_ID, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getOutletDetail(
            @PathVariable("id") String id
    ) {
        Outlet outlet = outletService.getById(id);
        Validator.notNull(outlet, APIStatus.NOT_FOUND, "Outlet not found");

        return responseUtil.successResponse(outlet);
    }

    /**
     * delete Outlet API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator(UserRole.SUPER_ADMIN)
    @RequestMapping(path = APIName.DELETE_ID, method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteOutlet(
            @PathVariable("id") String id
    ) {
        Outlet outlet = outletService.getById(id);
        Validator.notNull(outlet, APIStatus.NOT_FOUND, "Outlet not found");

        List<UserOutlet> userOutlets = userOutletService.getAllByOutletId(outlet.getId());
        if (!userOutlets.isEmpty()) {
            userOutletService.deleteAll(userOutlets);
        }
        outlet.setStatus(AppStatus.INACTIVE);
        outletService.save(outlet);

        return responseUtil.successResponse("Delete Ok");
    }


    /**
     * update Outlet API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator({UserRole.SUPER_ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(path = APIName.UPDATE_ID, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateOutlet(
            @PathVariable("id") String id,
            @RequestBody @Valid UpdateOutlet updateOutlet,
            @AuthSession AuthUser authUser
    ) {
        Outlet outlet = outletService.getById(id);
        Validator.notNull(outlet, APIStatus.NOT_FOUND, "Outlet not found");

        if (UserRole.COMPANY_ADMIN.equals(authUser.getRole())) {
            UserOutlet userOutlet = userOutletService.getByOutletIdAndUserId(outlet.getId(), authUser.getId());
            Validator.notNull(userOutlet, APIStatus.FORBIDDEN);
        }
        // update Outlet
        outlet = outletHelper.updateOutlet(updateOutlet, outlet, outletService, companyService);

        outletService.save(outlet);
        return responseUtil.successResponse(outlet);
    }

    /**
     * add Outlet API
     *
     * @param addOutlet
     * @return
     */
    @AuthorizeValidator(UserRole.SUPER_ADMIN)
    @RequestMapping(path = APIName.ADD, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addOutlet(
            @RequestBody @Valid AddOutlet addOutlet
    ) {
        Company company = companyService.getById(addOutlet.getCompanyId().trim());
        Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");

        Outlet outletPrefix = outletService.getByCompanyIdAndOutletPrefix(company.getId(), addOutlet.getOutletPrefix().trim());
        Validator.mustNull(outletPrefix, APIStatus.OUTLET_PREFIX_EXISTED, "");
        List<Outlet> outlets = outletService.getAllByCompanyId(company.getId());

        Outlet outlet = outletHelper.addOutlet(addOutlet, company);
        List<ProductLine> productLines = new ArrayList<>();
        productLineHelper.addProductLine(productLines, outlet.getId());
        productLineService.saveAll(productLines);
        outletService.save(outlet);

        outletHelper.addUserOutlet(outlets, outlet, userOutletService);
        return responseUtil.successResponse(outlet);
    }

}
