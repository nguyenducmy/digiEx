package com.supertomato.restaurant.repository;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface IngredientRepository extends JpaRepository<Ingredient, String>, JpaSpecificationExecutor<Ingredient> {
    List<Ingredient> findAllByStatusOrderByModuleDesc(AppStatus status);

    List<Ingredient> findAllByOutletIdOrderByOrdinalNumber(String outletId);

    List<Ingredient> findAllByIdIn(List<String> ingredientIds);

    @Query("select ingredient from Ingredient ingredient where ingredient.outletId = :outletId and (ingredient.name like :searchKey) order by ingredient.ordinalNumber")
    List<Ingredient> getListIngredient(@Param(value = "outletId") String outletId, @Param("searchKey") String searchKey);


    List<Ingredient> findByModule(int module);

    List<Ingredient> findByModuleAndStatus(int module, AppStatus status);

    Ingredient findByIdAndStatus(String id, AppStatus status);

    List<Ingredient> findAllByIdInAndModuleAndStatus(List<String> ids, int module, AppStatus status);

    List<Ingredient> findByOutletIdAndStatusOrderByModuleDesc(String outletId, AppStatus status);


    List<Ingredient> findAllByOutletIdAndStatus(String outletId, AppStatus status);


    @Query("select ingredient from Ingredient ingredient where ingredient.outletId = :outletId and (ingredient.name like :searchKey)")
    List<Ingredient> getAllIngredient(@Param(value = "outletId") String outletId, @Param("searchKey") String searchKey);

    List<Ingredient> findAllByIdInAndOutletIdAndStatus(List<String> ids, String outletId, AppStatus status);

    Ingredient findByIdAndOutletIdAndStatus(String id, String outletId, AppStatus status);



}
