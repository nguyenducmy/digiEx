package com.supertomato.restaurant.controller.model.request;

import com.supertomato.restaurant.common.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProduct {
    private Date endTime;
    private Date startTime;
    private ProductType productType;
    private long waitingTime;
    private String outletId;

}
