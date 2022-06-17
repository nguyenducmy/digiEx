package com.supertomato.restaurant.service;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.entity.Ingredient;
import com.supertomato.restaurant.entity.IngredientHistory;

import java.util.Date;
import java.util.List;

public interface IngredientHistoryService {

    List<IngredientHistory> getAllByIngredientIdInAndDispensedTimeBetween(List<String> ingredientIds, Date startDate, Date endDate);


    void save(IngredientHistory ingredientHistory);

    void saveAll(List<IngredientHistory> ingredientHistories);

    List<IngredientHistory> getAllByIngredientIdAndDispensedTimeBetween(String ingredientId, Date startDate, Date endDate, String sortField, boolean isAsc);


}
