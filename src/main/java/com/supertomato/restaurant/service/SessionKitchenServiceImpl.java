package com.supertomato.restaurant.service;

import com.supertomato.restaurant.entity.Session;
import com.supertomato.restaurant.entity.SessionKitchen;
import com.supertomato.restaurant.repository.SessionKitchenRepository;
import com.supertomato.restaurant.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DiGiEx
 */
@Service
public class SessionKitchenServiceImpl implements SessionKitchenService {

    final SessionKitchenRepository sessionKitchenRepository;

    public SessionKitchenServiceImpl(SessionKitchenRepository sessionKitchenRepository) {
        this.sessionKitchenRepository = sessionKitchenRepository;
    }

    @Override
    public SessionKitchen save(SessionKitchen sessionKitchen) {
        return sessionKitchenRepository.save(sessionKitchen);
    }

    @Override
    public SessionKitchen findById(String id) {
        return sessionKitchenRepository.findOneById(id);
    }

    @Override
    public void delete(SessionKitchen sessionKitchen) {
        sessionKitchenRepository.delete(sessionKitchen);
    }

    @Override
    public List<SessionKitchen> getAllByOutletId(String outletId) {
        return sessionKitchenRepository.findAllByOutletId(outletId);
    }

    @Override
    public void deleteAll(List<SessionKitchen> sessionKitchens) {
        sessionKitchenRepository.deleteAll(sessionKitchens);
    }

}
