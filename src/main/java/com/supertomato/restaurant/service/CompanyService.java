package com.supertomato.restaurant.service;


import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.entity.Company;
import org.springframework.data.domain.Page;

import java.util.List;


public interface CompanyService {

    Company save(Company company);

    Company getById(String id);

    Page<Company> getPageCompany(boolean isAsc, String sortField, int pageNumber, int pageSize);

    List<Company> getAllByIdIn(List<String> ids);

    List<Company> getAllByCompany();

    void deleteAll(Company company);

    Company getByCompanyPrefix(String companyPrefix);




}
