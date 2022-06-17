package com.supertomato.restaurant.repository;

import com.supertomato.restaurant.entity.Session;
import com.supertomato.restaurant.entity.SessionKitchen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author DiGiEx
 */
@Repository
@Transactional
public interface SessionKitchenRepository extends JpaRepository<SessionKitchen, String> {

    SessionKitchen findOneById(String id);

    List<SessionKitchen> findAllByOutletId(String outletId);

}
