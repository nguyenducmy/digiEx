package com.supertomato.restaurant.service;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.entity.IngredientHistory;
import com.supertomato.restaurant.repository.IngredientHistoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IngredientHistoryServiceImp implements IngredientHistoryService {

    final IngredientHistoryRepository ingredientHistoryRepository;

    public IngredientHistoryServiceImp(IngredientHistoryRepository ingredientHistoryRepository) {
        this.ingredientHistoryRepository = ingredientHistoryRepository;
    }


    @Override
    public List<IngredientHistory> getAllByIngredientIdInAndDispensedTimeBetween(List<String> ingredientIds, Date startDate, Date endDate) {
        return ingredientHistoryRepository.findAllByIngredientIdInAndDispensedTimeBetweenOrderByCreatedDateDesc(ingredientIds, startDate, endDate);
    }


    @Override
    public void save(IngredientHistory ingredientHistory) {
        ingredientHistoryRepository.save(ingredientHistory);
    }

    @Override
    public void saveAll(List<IngredientHistory> ingredientHistories) {
        ingredientHistoryRepository.saveAll(ingredientHistories);
    }

    @Override
    public List<IngredientHistory> getAllByIngredientIdAndDispensedTimeBetween(
            String ingredientId, Date startDate, Date endDate, String sortField, boolean isAsc) {

        String properties = "";
        switch (sortField) {
            case Constant.SORT_BY_PORTION:
                properties = "portion";
                break;
            case Constant.SORT_BY_STATUS:
                properties = "status";
                break;
            default:
                properties = "dispensedTime";
                break;
        }
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, properties);

        return ingredientHistoryRepository.findAllByIngredientIdAndDispensedTimeBetween(ingredientId, startDate, endDate, sort);
    }

}
