package com.supertomato.restaurant.repository;


import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.entity.IngredientHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface IngredientHistoryRepository extends JpaRepository<IngredientHistory, String>, JpaSpecificationExecutor<IngredientHistory> {

    List<IngredientHistory> findAllByIngredientIdInAndDispensedTimeBetweenOrderByCreatedDateDesc(List<String> ingredientIds, Date startDate, Date endDate);


    List<IngredientHistory> findAllByIngredientIdAndDispensedTimeBetween(String ingredientId, Date startDate, Date endDate, Sort sort);


}
