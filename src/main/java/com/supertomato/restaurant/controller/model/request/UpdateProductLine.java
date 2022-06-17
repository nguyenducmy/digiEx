package com.supertomato.restaurant.controller.model.request;

import com.supertomato.restaurant.common.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductLine
{
private List<ProductType> productTypes;
        }