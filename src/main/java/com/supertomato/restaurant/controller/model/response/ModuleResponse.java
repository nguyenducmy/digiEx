package com.supertomato.restaurant.controller.model.response;

import com.supertomato.restaurant.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModuleResponse {
    private int module;
    private List<Ingredient> ingredients;
    public ModuleResponse(int module, List<Ingredient> ingredients) {
        this.module = module;
        this.ingredients = ingredients;
    }

}
