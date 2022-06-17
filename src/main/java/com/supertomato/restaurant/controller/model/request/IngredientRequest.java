package com.supertomato.restaurant.controller.model.request;

import com.supertomato.restaurant.common.util.ParamError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Getter
@Setter
@AllArgsConstructor
public class IngredientRequest {

    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String name;

    @Min(value = 1, message = ParamError.MIN_VALUE)
    @Max(value = 999999, message = ParamError.MAX_VALUE)
    private double portion;

    @Min(value = 0, message = ParamError.MIN_VALUE)
    @Max(value = 999, message = ParamError.MAX_VALUE)
    private double acceptableDeviation;

    @Min(value = 1, message = ParamError.MIN_VALUE)
    @Max(value = 4, message = ParamError.MAX_VALUE)
    private int ordinalNumber;
}