package com.supertomato.restaurant.repository;

import com.supertomato.restaurant.entity.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface ProductHistoryRepository extends JpaRepository<ProductHistory,String>, JpaSpecificationExecutor<ProductHistory> {

    List<ProductHistory> findAllByOutletIdAndStartTimeBetween(String outletId, Date fromDate, Date toDate);

}
