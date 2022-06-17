package com.supertomato.restaurant.controller.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TopIngredient {

    public TopIngredient(String id, String ingredient, double portion, double volume) {
        this.id = id;
        this.ingredient = ingredient;
        this.portion = portion;
        this.volume = volume;
    }

    private String id;
    private String ingredient;
    private double portion;
    private double volume;


}
