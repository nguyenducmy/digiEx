package com.supertomato.restaurant.controller.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.Category;
import com.supertomato.restaurant.common.enums.ProductType;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.entity.Ingredient;
import com.supertomato.restaurant.entity.Outlet;
import com.supertomato.restaurant.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientResponse {

    private String id;

    private String name;

    private double portion;

    private double acceptableDeviation;

    private int ordinalNumber;

    private int module;

    private String outletId;

    private ProductType productType;

    private Category category;

    private AppStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date createdDate;

    private List<Outlet> outlets;


}
