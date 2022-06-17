package com.supertomato.restaurant.service;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.entity.Ingredient;

import java.util.List;

public interface IngredientService {

    List<Ingredient> saveIngredient(List<Ingredient> ingredients);

    List<Ingredient> getAllByIngredient();

    List<Ingredient> getAllByOutletId(String outletId, String searchKey);

    List<Ingredient> getByModule(int module);

    void deleteAll(List<Ingredient> ingredients);

    Ingredient getById(String id);

    List<Ingredient> findByModule(int module);

    List<Ingredient> getAllByIdInAndModule(List<String> ids, int module);

    List<Ingredient> getByOutletId(String outletId);

    List<Ingredient> getIngredientAllByOutletId(String outletId, String searchKey);

    List<Ingredient> getAllByIdIns(List<String> ingredientIds);

    List<Ingredient> getAllByOutletIdAndStatus (String outletId);

    List<Ingredient> getAllByIdInAndOutletId(List<String> ids, String outletId);

    Ingredient getByIdAndOutletId(String id, String outletId);

}
