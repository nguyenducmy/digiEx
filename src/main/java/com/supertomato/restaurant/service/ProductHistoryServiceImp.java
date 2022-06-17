package com.supertomato.restaurant.service;

import com.supertomato.restaurant.entity.ProductHistory;
import com.supertomato.restaurant.repository.ProductHistoryRepository;
import com.supertomato.restaurant.repository.ProductLineRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductHistoryServiceImp implements ProductHistoryService {


    final ProductHistoryRepository productHistoryRepository;

    public ProductHistoryServiceImp(ProductHistoryRepository productHistoryRepository) {
        this.productHistoryRepository = productHistoryRepository;
    }

    @Override
    public void save(ProductHistory productHistory) {
        productHistoryRepository.save(productHistory);
    }

    @Override
    public void saveAll(List<ProductHistory> productHistories) {
        productHistoryRepository.saveAll(productHistories);
    }

    @Override
    public List<ProductHistory> getAllByOutletIdAndStartTimeBetween(String outletId, Date fromDate, Date toDate) {
        return productHistoryRepository.findAllByOutletIdAndStartTimeBetween(outletId, fromDate, toDate);
    }
}
