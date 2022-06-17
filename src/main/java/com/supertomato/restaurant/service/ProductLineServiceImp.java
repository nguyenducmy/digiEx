package com.supertomato.restaurant.service;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.entity.ProductLine;
import com.supertomato.restaurant.repository.ProductLineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductLineServiceImp implements ProductLineService {

    final ProductLineRepository productLineRepository;

    public ProductLineServiceImp(ProductLineRepository productLineRepository) {
        this.productLineRepository = productLineRepository;
    }


    @Override
    public List<ProductLine> getAllByOutletId(String outletIds) {
        return productLineRepository.findAllByOutletIdAndStatus(outletIds, AppStatus.ACTIVE);
    }

    @Override
    public void saveAll(List<ProductLine> productLines) {
        productLineRepository.saveAll(productLines);
    }

    @Override
    public ProductLine getById(String id) {
        return productLineRepository.findByIdAndStatus(id, AppStatus.ACTIVE);
    }

    @Override
    public void save(ProductLine productLine) {
        productLineRepository.save(productLine);
    }
}
