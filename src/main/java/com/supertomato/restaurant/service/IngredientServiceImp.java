package com.supertomato.restaurant.service;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.entity.Ingredient;
import com.supertomato.restaurant.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientServiceImp implements IngredientService {

    final IngredientRepository ingredientRepository;

    public IngredientServiceImp(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<Ingredient> saveIngredient(List<Ingredient> ingredients) {
        return ingredientRepository.saveAll(ingredients);
    }

    @Override
    public List<Ingredient> getAllByIngredient() {
        return ingredientRepository.findAllByStatusOrderByModuleDesc(AppStatus.ACTIVE);
    }

    @Override
    public List<Ingredient> getAllByOutletId(String outletId, String searchKey) {
        if (searchKey != null && !searchKey.trim().isEmpty()) {
            return ingredientRepository.getListIngredient(outletId, searchKey);
        } else {
            return ingredientRepository.findAllByOutletIdOrderByOrdinalNumber(outletId);
        }
    }


    @Override
    public List<Ingredient> getByModule(int module) {
        return ingredientRepository.findByModule(module);
    }

    @Override
    public void deleteAll(List<Ingredient> ingredients) {
        ingredientRepository.deleteAll(ingredients);
    }

    @Override
    public Ingredient getById(String id) {
        return ingredientRepository.findByIdAndStatus(id, AppStatus.ACTIVE);
    }


    @Override
    public List<Ingredient> findByModule(int module) {
        return ingredientRepository.findByModuleAndStatus(module, AppStatus.ACTIVE);
    }

    @Override
    public List<Ingredient> getAllByIdInAndModule(List<String> ids, int module) {
        return ingredientRepository.findAllByIdInAndModuleAndStatus(ids, module, AppStatus.ACTIVE);
    }

    @Override
    public List<Ingredient> getByOutletId(String outletId) {
        return ingredientRepository.findByOutletIdAndStatusOrderByModuleDesc(outletId, AppStatus.ACTIVE);
    }

    @Override
    public List<Ingredient> getIngredientAllByOutletId(String outletId, String searchKey) {
        return ingredientRepository.getAllIngredient(outletId, "%" + searchKey + "%");
    }

    @Override
    public List<Ingredient> getAllByIdIns(List<String> ingredientIds) {
        return ingredientRepository.findAllByIdIn(ingredientIds);
    }

    @Override
    public List<Ingredient> getAllByOutletIdAndStatus(String outletId) {
        return ingredientRepository.findAllByOutletIdAndStatus(outletId, AppStatus.ACTIVE);
    }

    @Override
    public List<Ingredient> getAllByIdInAndOutletId(List<String> ids, String outletId) {
        return ingredientRepository.findAllByIdInAndOutletIdAndStatus(ids, outletId, AppStatus.ACTIVE);
    }

    @Override
    public Ingredient getByIdAndOutletId(String id, String outletId) {
        return ingredientRepository.findByIdAndOutletIdAndStatus(id, outletId, AppStatus.ACTIVE);
    }


}
