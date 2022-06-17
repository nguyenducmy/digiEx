package com.supertomato.restaurant.controller.model.response;

import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.entity.Ingredient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class HistoryOfDeviation {

    private String id;
    private String name;
    private double totalPortion;
    private double totalVolume;
    private double portion;
    private double acceptableDeviation;
    private double acceptable;

    public HistoryOfDeviation(Ingredient ingredient,
                              double totalPortion,
                              double totalVolume,
                              double acceptable) {
        this.id = ingredient.getId();
        this.name = ingredient.getName();
        this.totalPortion = totalPortion;
        this.totalVolume = totalVolume;
        this.portion = ingredient.getPortion();
        this.acceptableDeviation = ingredient.getAcceptableDeviation();
        this.acceptable = Validator.round(acceptable);
    }

}
