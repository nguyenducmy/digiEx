package com.supertomato.restaurant.repository;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.controller.model.response.OutletResponse;
import com.supertomato.restaurant.entity.Outlet;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface OutletRepository extends JpaRepository<Outlet, String>, JpaSpecificationExecutor<Outlet> {

    List<Outlet> findAllByCompanyIdAndStatusOrderByCreatedDateDesc(String companyId, AppStatus status);

    List<Outlet> findAllByCompanyIdInAndStatusOrderByCreatedDateDesc(List<String> companyIds, AppStatus status);

    List<Outlet> findAllByIdInAndStatus(List<String> ids, AppStatus status);

    Outlet findByIdAndStatus(String id, AppStatus status);

    List<Outlet> findAllByCompanyIdAndStatus(String companyId, AppStatus status, Sort sort);

    @Query(value = "SELECT new com.supertomato.restaurant.controller.model.response.OutletResponse(company, outlet) from Company company, Outlet outlet"
            + " where outlet.status = :status and outlet.companyId = :companyId and outlet.companyId = company.id")
    List<OutletResponse> getAllOutletByCompanyId(@Param(value = "companyId") String companyId,
                                                 @Param(value = "status") AppStatus status,
                                                 Sort sort);

    @Query(value = "SELECT new com.supertomato.restaurant.controller.model.response.OutletResponse(company, outlet) from Company company, Outlet outlet"
            + " where outlet.status = :status and outlet.id in (:outletIds) and outlet.companyId = company.id")
    List<OutletResponse> getAllOutlet(@Param(value = "outletIds") List<String> outletIds,
                                      @Param(value = "status") AppStatus status,
                                      Sort sort);

    Outlet findOutletByIdIn(String id);

    Outlet findByIdAndCompanyIdAndStatus(String id, String companyId, AppStatus status);

    Outlet findByCompanyIdAndOutletPrefixAndStatus(String companyId, String outletPrefix, AppStatus status);

    Outlet findByKitchenAccountAndStatus(String kitchenAccount, AppStatus status);

    List<Outlet> findAllByStatus(AppStatus status);

}
