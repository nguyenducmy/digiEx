package com.supertomato.restaurant.repository;

import com.supertomato.restaurant.entity.UserOutlet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserOutletRepository extends JpaRepository<UserOutlet, String>, JpaSpecificationExecutor<UserOutlet> {

    List<UserOutlet> findAllByUserId(String userId);

    List<UserOutlet> findAllByUserIdIn(List<String> userIds);

    List<UserOutlet> findAllByOutletIdIn(List<String> outletIds);

    List<UserOutlet> findAllByOutletId(String outletId);

    List<UserOutlet> findAllByUserIdAndCompanyId(String userId, String companyId);

    List<UserOutlet> findAllByCompanyIdInAndUserIdNot(List<String> companyIds, String userId);

    List<UserOutlet> findAllByCompanyId(String companyId);
    UserOutlet findFirstByOutletIdAndUserId(String outletId, String userId);

}
