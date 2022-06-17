package com.supertomato.restaurant.service;

import com.supertomato.restaurant.entity.Session;
import com.supertomato.restaurant.repository.SessionRepository;
import org.springframework.stereotype.Service;

/**
 * @author DiGiEx
 */
@Service
public class SessionServiceImpl implements SessionService {

    final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    @Override
    public Session findById(String id) {
        return sessionRepository.findOneById(id);
    }

    @Override
    public void delete(Session session) {
        sessionRepository.delete(session);
    }

    @Override
    public void deleteAllSessionsByUserId(String userId) {
        sessionRepository.deleteSessionsByUserId(userId);
    }
}
