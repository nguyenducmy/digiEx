package com.supertomato.restaurant.service;

import com.supertomato.restaurant.entity.Session;
import com.supertomato.restaurant.entity.SessionKitchen;

import java.util.List;

/**
 * @author DiGiEx
 */
public interface SessionKitchenService {
    SessionKitchen save(SessionKitchen sessionKitchen);

    SessionKitchen findById(String id);

    void delete(SessionKitchen sessionKitchen);

    List<SessionKitchen> getAllByOutletId(String outletId);

    void deleteAll(List<SessionKitchen> sessionKitchens);


}
