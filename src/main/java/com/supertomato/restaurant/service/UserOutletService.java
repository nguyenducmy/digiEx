package com.supertomato.restaurant.service;

import com.supertomato.restaurant.entity.UserOutlet;

import java.util.List;

public interface UserOutletService {


    List<UserOutlet> saveAll(List<UserOutlet> userOutlets);

    void deleteAll(List<UserOutlet> userOutlets);


    List<UserOutlet> getAllByUserId(String userId);

    List<UserOutlet> getAllByUserIdIn(List<String> userIds);

    List<UserOutlet> getAllByOutletIdIn(List<String> outletIds);

    List<UserOutlet> getAllByOutletId(String outletId);

    List<UserOutlet> getAllByUserIdAndCompanyId(String userId, String companyId);

    List<UserOutlet> getAllByCompanyIdInAndUserIdNot( List<String> companyIds, String userId);

    List<UserOutlet> getAllByCompanyId(String companyId);

    UserOutlet getByOutletIdAndUserId(String outletId, String userId);


}
