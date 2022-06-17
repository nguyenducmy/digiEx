package com.supertomato.restaurant.controller.model.request;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.Category;
import com.supertomato.restaurant.common.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIngredient {

    private String id;
    private String name;
    private Double portion;
    private Double acceptableDeviation;
    private Integer ordinalNumber;
    private ProductType productType;
    private Category category;
    private AppStatus status;

}
