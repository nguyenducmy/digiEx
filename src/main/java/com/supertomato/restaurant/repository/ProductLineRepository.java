package com.supertomato.restaurant.repository;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.entity.ProductLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ProductLineRepository extends JpaRepository<ProductLine, String>, JpaSpecificationExecutor<ProductLine> {

    List<ProductLine> findAllByOutletIdAndStatus(String outletId, AppStatus status);

    ProductLine findByIdAndStatus(String id, AppStatus status);


}
