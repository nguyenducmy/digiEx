package com.supertomato.restaurant.controller.model.response;

import com.supertomato.restaurant.entity.Ingredient;
import com.supertomato.restaurant.entity.IngredientHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IngredientHistoryRes {
    private String id;
    private String name;
    private double portion;
    private double acceptableDeviation;
    private String outletId;
    List<IngredientHistory> ingredientHistories;
    public IngredientHistoryRes(Ingredient ingredient,
                                List<IngredientHistory> ingredientHistories) {
        this.id = ingredient.getId();
        this.name = ingredient.getName();
        this.portion = ingredient.getPortion();
        this.acceptableDeviation = ingredient.getAcceptableDeviation();
        this.outletId = ingredient.getOutletId();
        this.ingredientHistories = ingredientHistories;
    }
}
