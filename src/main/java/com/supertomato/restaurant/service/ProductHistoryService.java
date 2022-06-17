package com.supertomato.restaurant.service;

import com.supertomato.restaurant.entity.ProductHistory;

import java.util.Date;
import java.util.List;

public interface ProductHistoryService {

    void save(ProductHistory productHistory);

    void saveAll(List<ProductHistory> productHistories);


    List<ProductHistory> getAllByOutletIdAndStartTimeBetween(String outletId, Date fromDate, Date toDate);

}
