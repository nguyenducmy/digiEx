package com.supertomato.restaurant.service;

import com.supertomato.restaurant.entity.Session;

/**
 * @author DiGiEx
 */
public interface SessionService {
    Session save(Session session);
    Session findById(String id);
    void delete(Session session);
    void deleteAllSessionsByUserId(String userId);
}
