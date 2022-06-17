package com.supertomato.restaurant.controller.helper;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.util.DateUtil;
import com.supertomato.restaurant.common.util.UniqueID;
import com.supertomato.restaurant.controller.model.request.AddCompany;
import com.supertomato.restaurant.controller.model.request.UpdateCompany;
import com.supertomato.restaurant.controller.model.response.CompanyResponse;
import com.supertomato.restaurant.controller.model.response.PagingResponse;
import com.supertomato.restaurant.entity.Company;
import com.supertomato.restaurant.entity.Outlet;
import com.supertomato.restaurant.entity.UserOutlet;
import com.supertomato.restaurant.service.CompanyService;
import com.supertomato.restaurant.service.OutletService;
import com.supertomato.restaurant.service.UserOutletService;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyHelper {
    public Company createCompany(AddCompany addCompany, ZoneId zoneId) {
        // addCompany
        Company company = new Company();
        company.setId(UniqueID.getUUID());
        company.setName(addCompany.getName().trim());
        company.setCompanyPrefix(addCompany.getCompanyPrefix().trim());
        company.setTimezone(zoneId.getId());
        company.setStatus(AppStatus.ACTIVE);
        return company;
    }


    public Company updateCompany(UpdateCompany updateCompany, Company company) {

        if (updateCompany.getTimezone() != null && !updateCompany.getTimezone().trim().isEmpty() &&
                !updateCompany.getTimezone().trim().equals(company.getTimezone())) {

            ZoneId zoneId = DateUtil.getTimezone(updateCompany.getTimezone().trim());
            company.setTimezone(zoneId.getId());
        }

        if (updateCompany.getName() != null && !updateCompany.getName().trim().isEmpty()) {
            company.setName(updateCompany.getName().trim());
        }
        return company;
    }

    public Company deleteCompany(Company company,OutletService outletService,
                                 UserOutletService userOutletService) {

        List<Outlet> outletList = outletService.getAllByCompanyId(company.getId());
        if (!outletList.isEmpty()) {
            outletList.forEach(outlet -> outlet.setStatus(AppStatus.INACTIVE));
            List<String> outletIds = outletList.stream().map(Outlet::getId).collect(Collectors.toList());
            List<UserOutlet> userOutlets = userOutletService.getAllByOutletIdIn(outletIds);
            if (!userOutlets.isEmpty()) {
                userOutletService.deleteAll(userOutlets);
            }
            outletService.saveAll(outletList);
        }
        company.setStatus(AppStatus.INACTIVE);
        return company;
    }

    public List<CompanyResponse> getCompanyPage(List<Company> companyList,OutletService outletService) {

        List<CompanyResponse> companyResponses = new ArrayList<>();
        if (!companyList.isEmpty()) {
            List<String> ids = companyList.stream().map(Company::getId).collect(Collectors.toList());
            List<Outlet> outletList = outletService.getAllByCompanyIdIn(ids);
            for (Company company : companyList) {
                List<Outlet> outlets = outletList.stream().filter(outlet -> company.getId().equals(outlet.getCompanyId())).collect(Collectors.toList());
                companyResponses.add(new CompanyResponse(company, outlets));
            }
        }
        return companyResponses;
    }


    public List<CompanyResponse> getCompanyList(OutletService outletService, CompanyService companyService) {

        List<CompanyResponse> companyResponses = new ArrayList<>();
        List<Company> companies = companyService.getAllByCompany();

        if (!companies.isEmpty()) {
            List<String> ids = companies.stream().map(Company::getId).collect(Collectors.toList());
            List<Outlet> outletList = outletService.getAllByCompanyIdIn(ids);
            for (Company company : companies) {
                List<Outlet> outlets = outletList.stream().filter(outlet -> company.getId().equals(outlet.getCompanyId())).collect(Collectors.toList());
                companyResponses.add(new CompanyResponse(company, outlets));
            }
        }
        return companyResponses;
    }

}
