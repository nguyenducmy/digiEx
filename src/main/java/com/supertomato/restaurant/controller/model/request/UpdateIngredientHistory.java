package com.supertomato.restaurant.controller.model.request;

import com.supertomato.restaurant.common.util.ParamError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class UpdateIngredientHistory {

    @NotBlank(message = ParamError.FIELD_NAME)
    private String ingredientId;

    @Min(value = 1, message = ParamError.MIN_VALUE)
    @Max(value = 999999, message = ParamError.MAX_VALUE)
    private Double portion;
}
