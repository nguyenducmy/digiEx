package com.supertomato.restaurant.controller.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientHistoryResponse {

    private String id;
    private String ingredientId;
    private double portion;
    private AppStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date dispensedTime;

    private List<Ingredient> ingredients;
}
