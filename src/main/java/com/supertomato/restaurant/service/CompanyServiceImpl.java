package com.supertomato.restaurant.service;


import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.entity.Company;
import com.supertomato.restaurant.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company getById(String id) {
        return companyRepository.findByIdAndStatus(id, AppStatus.ACTIVE);
    }

    @Override
    public Page<Company> getPageCompany(boolean isAsc, String sortField, int pageNumber, int pageSize) {

        String properties = "";
        switch (sortField) {
            case Constant.SORT_BY_NAME:
                properties = "name";
                break;
            case Constant.SORT_BY_TIMEZONE:
                properties = "timezone";
                break;
            default:
                properties = "createdDate";
                break;
        }
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, properties);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return companyRepository.findAllByStatus(AppStatus.ACTIVE, pageable);
    }

    @Override
    public List<Company> getAllByIdIn(List<String> ids) {
        return companyRepository.findAllByIdInAndStatus(ids, AppStatus.ACTIVE);
    }

    @Override
    public void deleteAll(Company company) {
        companyRepository.delete(company);
    }

    @Override
    public Company getByCompanyPrefix(String companyPrefix) {
        return companyRepository.findFirstByCompanyPrefixAndStatus(companyPrefix, AppStatus.ACTIVE);
    }

    @Override
    public List<Company> getAllByCompany() {
        return companyRepository.findAllByStatus(AppStatus.ACTIVE);
    }


}
