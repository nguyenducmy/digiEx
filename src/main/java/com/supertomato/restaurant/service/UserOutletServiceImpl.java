package com.supertomato.restaurant.service;

import com.supertomato.restaurant.entity.UserOutlet;
import com.supertomato.restaurant.repository.UserOutletRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOutletServiceImpl implements UserOutletService {


    final UserOutletRepository userOutletRepository;

    public UserOutletServiceImpl(UserOutletRepository userOutletRepository) {
        this.userOutletRepository = userOutletRepository;
    }


    @Override
    public List<UserOutlet> saveAll(List<UserOutlet> userOutlets) {
        return userOutletRepository.saveAll(userOutlets);
    }

    @Override
    public void deleteAll(List<UserOutlet> userOutlets) {
        userOutletRepository.deleteAll(userOutlets);
    }

    @Override
    public List<UserOutlet> getAllByUserId(String userId) {
        return userOutletRepository.findAllByUserId(userId);
    }

    @Override
    public List<UserOutlet> getAllByUserIdIn(List<String> userIds) {
        return userOutletRepository.findAllByUserIdIn(userIds);
    }

    @Override
    public List<UserOutlet> getAllByOutletIdIn(List<String> outletIds) {
        return userOutletRepository.findAllByOutletIdIn(outletIds);
    }

    @Override
    public List<UserOutlet> getAllByOutletId(String outletId) {
        return userOutletRepository.findAllByOutletId(outletId);
    }

    @Override
    public List<UserOutlet> getAllByUserIdAndCompanyId(String userId, String companyId) {
        return userOutletRepository.findAllByUserIdAndCompanyId(userId, companyId);
    }

    @Override
    public List<UserOutlet> getAllByCompanyIdInAndUserIdNot(List<String> companyIds, String userId) {
        return userOutletRepository.findAllByCompanyIdInAndUserIdNot(companyIds, userId);
    }

    @Override
    public List<UserOutlet> getAllByCompanyId(String companyId) {
        return userOutletRepository.findAllByCompanyId(companyId);
    }

    @Override
    public UserOutlet getByOutletIdAndUserId(String outletId, String userId) {
        return userOutletRepository.findFirstByOutletIdAndUserId(outletId, userId);
    }
}
