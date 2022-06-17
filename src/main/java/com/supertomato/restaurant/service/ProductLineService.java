package com.supertomato.restaurant.service;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.entity.ProductLine;

import java.util.List;

public interface ProductLineService {

    List<ProductLine> getAllByOutletId(String outletId);

    void saveAll(List<ProductLine> productLines);

    ProductLine getById(String id);

    void save(ProductLine productLine);


}
