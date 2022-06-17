package com.supertomato.restaurant.controller.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateModules {

    private String outletId;

    private List<UpdateIngredientRe> ingredients;
}
