package com.supertomato.restaurant.service;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.controller.model.response.OutletResponse;
import com.supertomato.restaurant.entity.Outlet;
import com.supertomato.restaurant.repository.OutletRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutletServiceImpl implements OutletService {


    final OutletRepository outletRepository;

    public OutletServiceImpl(OutletRepository outletRepository) {
        this.outletRepository = outletRepository;
    }


    @Override
    public List<Outlet> saveAll(List<Outlet> outlets) {
        return outletRepository.saveAll(outlets);
    }

    @Override
    public Outlet save(Outlet outlet) {
        return outletRepository.save(outlet);
    }

    @Override
    public List<Outlet> getAllByCompanyId(String companyId) {
        return outletRepository.findAllByCompanyIdAndStatusOrderByCreatedDateDesc(companyId, AppStatus.ACTIVE);
    }

    @Override
    public List<Outlet> getAllByCompanyIdIn(List<String> companyIds) {
        return outletRepository.findAllByCompanyIdInAndStatusOrderByCreatedDateDesc(companyIds, AppStatus.ACTIVE);
    }

    @Override
    public List<Outlet> getAllByIdIn(List<String> ids) {
        return outletRepository.findAllByIdInAndStatus(ids, AppStatus.ACTIVE);
    }

    @Override
    public List<OutletResponse> getAllByCompanyId(String companyId, List<String> outletIds, boolean isAsc, String sortField) {

        String properties = "";
        switch (sortField) {
            case Constant.SORT_BY_OUTLET_PRE:
                properties = "outletPrefix";
                break;
            case Constant.SORT_BY_OUTLET_NAME:
                properties = "name";
                break;
            case Constant.SORT_BY_KITCHEN_LOGIN_EMAIL:
                properties = "kitchenLoginEmail";
                break;
            default:
                properties = "createdDate";
                break;
        }
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, properties);

        if (companyId != null && !companyId.trim().isEmpty()) {
            return outletRepository.getAllOutletByCompanyId(companyId, AppStatus.ACTIVE, sort);
        } else {
            return outletRepository.getAllOutlet(outletIds, AppStatus.ACTIVE, sort);
        }
    }

    @Override
    public Outlet getById(String id) {
        return outletRepository.findByIdAndStatus(id, AppStatus.ACTIVE);
    }

    @Override
    public Outlet getOutletByIdIn(String id) {
        return outletRepository.findOutletByIdIn(id);
    }

    @Override
    public Outlet getByIdAndCompanyId(String id, String companyId) {
        return outletRepository.findByIdAndCompanyIdAndStatus(id, companyId, AppStatus.ACTIVE);
    }

    @Override
    public Outlet getByCompanyIdAndOutletPrefix(String companyId, String outletPrefix) {
        return outletRepository.findByCompanyIdAndOutletPrefixAndStatus(companyId, outletPrefix, AppStatus.ACTIVE);
    }

    @Override
    public Outlet getByKitchenAccount(String kitchenAccount) {
        return outletRepository.findByKitchenAccountAndStatus(kitchenAccount, AppStatus.ACTIVE);
    }

    @Override
    public List<Outlet> getAllByOutlet() {
        return outletRepository.findAllByStatus(AppStatus.ACTIVE);
    }

}
