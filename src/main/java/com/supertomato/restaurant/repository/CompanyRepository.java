package com.supertomato.restaurant.repository;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CompanyRepository extends JpaRepository<Company, String>, JpaSpecificationExecutor<Company> {

    Company findByIdAndStatus(String id, AppStatus status);

    Page<Company> findAllByStatus(AppStatus status, Pageable pageable);

    List<Company> findAllByIdIn(List<String> companyIds);

    List<Company> findAllByIdInAndStatus(List<String> ids, AppStatus status);

    List<Company> findAllByStatus(AppStatus status);

    Company findFirstByCompanyPrefixAndStatus(String companyPrefix, AppStatus status);

}
