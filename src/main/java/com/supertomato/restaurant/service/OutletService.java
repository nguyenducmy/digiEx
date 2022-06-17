package com.supertomato.restaurant.service;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.controller.model.response.OutletResponse;
import com.supertomato.restaurant.entity.Outlet;

import java.util.List;

public interface OutletService {

    List<Outlet> saveAll(List<Outlet> outlets);

    Outlet save(Outlet outlet);

    List<Outlet> getAllByCompanyId(String companyId);

    List<Outlet> getAllByCompanyIdIn(List<String> companyIds);

    List<Outlet> getAllByIdIn(List<String> ids);

    List<OutletResponse> getAllByCompanyId(String companyId, List<String> outletIds, boolean isAsc, String sortField);

    Outlet getById(String id);

    Outlet getOutletByIdIn(String id);

    Outlet getByIdAndCompanyId(String id, String companyId);

    Outlet getByCompanyIdAndOutletPrefix(String companyId, String outletPrefix);

    Outlet getByKitchenAccount(String kitchenAccount);

    List<Outlet> getAllByOutlet();


}
