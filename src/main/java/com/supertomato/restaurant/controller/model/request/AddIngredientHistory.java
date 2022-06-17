package com.supertomato.restaurant.controller.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddIngredientHistory {
    private String ingredientId;
    private Double portion;
    private Date dispensedTime;
    @JsonProperty(value = "is_deviation")
    private boolean isDeviation;

}
