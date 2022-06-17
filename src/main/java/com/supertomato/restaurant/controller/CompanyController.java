package com.supertomato.restaurant.controller;

import com.supertomato.restaurant.auth.AuthorizeValidator;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.common.util.DateUtil;
import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.controller.helper.CompanyHelper;
import com.supertomato.restaurant.controller.helper.OutletHelper;
import com.supertomato.restaurant.controller.helper.ProductLineHelper;
import com.supertomato.restaurant.controller.model.request.AddCompany;
import com.supertomato.restaurant.controller.model.request.OutletRequest;
import com.supertomato.restaurant.controller.model.request.UpdateCompany;
import com.supertomato.restaurant.controller.model.response.CompanyResponse;
import com.supertomato.restaurant.controller.model.response.PagingResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(APIName.COMPANY)
public class CompanyController extends AbstractBaseController {


    final CompanyService companyService;
    final OutletService outletService;
    final CompanyHelper companyHelper;
    final OutletHelper outletHelper;
    final UserOutletService userOutletService;
    final ProductLineHelper productLineHelper;
    final ProductLineService productLineService;


    public CompanyController(CompanyService companyService,
                             OutletService outletService,
                             CompanyHelper companyHelper,
                             OutletHelper outletHelper,
                             UserOutletService userOutletService,
                             ProductLineHelper productLineHelper,
                             ProductLineService productLineService) {
        this.companyService = companyService;
        this.outletService = outletService;
        this.companyHelper = companyHelper;
        this.outletHelper = outletHelper;
        this.userOutletService = userOutletService;
        this.productLineHelper = productLineHelper;
        this.productLineService = productLineService;
    }

    /**
     * add Company
     *
     * @param
     * @return
     */
    @AuthorizeValidator(UserRole.SUPER_ADMIN)
    @RequestMapping(path = APIName.ADD, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addCompany(
            @RequestBody @Valid AddCompany addCompany

    ) {
        ZoneId zoneId = DateUtil.getTimezone(addCompany.getTimezone().trim());

        Company companyPrefix = companyService.getByCompanyPrefix(addCompany.getCompanyPrefix().trim());
        Validator.mustNull(companyPrefix, APIStatus.COMPANY_PREFIX_EXISTED, "");
        // addCompany
        Company company = companyHelper.createCompany(addCompany, zoneId);
        //add outlet list
        List<Outlet> outletList = outletHelper.addOutletCompany(addCompany, company, productLineHelper, productLineService, outletService);
        company = companyService.save(company);
        return responseUtil.successResponse(new CompanyResponse(company, outletList));
    }


    /**
     * update Company API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator(UserRole.SUPER_ADMIN)
    @RequestMapping(path = APIName.UPDATE_ID, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateCompany(
            @PathVariable("id") String id,
            @RequestBody @Valid UpdateCompany updateCompany
    ) {
        Company company = companyService.getById(id);
        Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");

        company = companyHelper.updateCompany(updateCompany, company);
        companyService.save(company);

        return responseUtil.successResponse(company);
    }

    /**
     * get Company detail API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator(UserRole.SUPER_ADMIN)
    @RequestMapping(path = APIName.DETAIL_ID, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getCompanyDetail(
            @PathVariable("id") String id
    ) {
        Company company = companyService.getById(id);
        Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");

        return responseUtil.successResponse(company);
    }

    /**
     * get Company detail API
     *
     * @param id
     * @return
     */
    @AuthorizeValidator(UserRole.SUPER_ADMIN)
    @RequestMapping(path = APIName.DELETE_ID, method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteCompany(
            @PathVariable("id") String id
    ) {
        Company company = companyService.getById(id);
        Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");
        company = companyHelper.deleteCompany(company, outletService, userOutletService);
        companyService.save(company);
        return responseUtil.successResponse("Delete Ok");
    }


    /**
     * get CompanyPage API
     *
     * @param isAsc,sortField,pageNumber,pageSize
     * @return
     */
    @AuthorizeValidator(UserRole.SUPER_ADMIN)
    @RequestMapping(path = APIName.PAGE, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getCompanyPage(
            @RequestParam(name = "sort", required = false, defaultValue = "false") boolean isAsc,
            @RequestParam(name = "sort_field", required = false, defaultValue = "") String sortField,
            @RequestParam(value = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(value = "page_size", required = false, defaultValue = "10") int pageSize) {

        validatePageSize(pageNumber, pageSize);
        Page<Company> companyPage = companyService.getPageCompany(isAsc, sortField, pageNumber, pageSize);
        List<CompanyResponse> companyResponses = companyHelper.getCompanyPage(companyPage.getContent(), outletService);
        return responseUtil.successResponse(new PagingResponse(companyResponses, companyPage));
    }


    /**
     * get CompanyPage API
     *
     * @param
     * @return
     */
    @RequestMapping(path = APIName.LIST, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getCompanies(
    ) {
        return responseUtil.successResponse(companyHelper.getCompanyList(outletService, companyService));
    }


}
