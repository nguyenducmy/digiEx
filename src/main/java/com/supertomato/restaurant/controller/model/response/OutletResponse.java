package com.supertomato.restaurant.controller.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.entity.Company;
import com.supertomato.restaurant.entity.Ingredient;
import com.supertomato.restaurant.entity.Outlet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class OutletResponse {

    private String outletId;
    private String companyName;
    private String companyId;
    private String timeZone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String outletPrefix;
    private String kitchenAccount;
    private String kitchenPassword;
    private String outletName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date createdDate;
    private List<Ingredient> ingredients;

    public OutletResponse(Company company, Outlet outlet,
                          List<Ingredient> ingredients) {
        this.outletId = outlet.getId();
        this.companyName = company.getName();
        this.companyId = company.getId();
        this.timeZone = company.getTimezone();
        this.kitchenAccount = outlet.getKitchenAccount();
        this.kitchenPassword = outlet.getKitchenPassword();
        this.outletName = outlet.getName();
        this.createdDate = outlet.getCreatedDate();
        this.ingredients = ingredients;
    }

    public OutletResponse(Company company, Outlet outlet) {
        this.outletId = outlet.getId();
        this.companyName = company.getName();
        this.outletPrefix = outlet.getOutletPrefix();
        this.companyId = company.getId();
        this.timeZone = company.getTimezone();
        this.kitchenAccount = outlet.getKitchenAccount();
        this.kitchenPassword = outlet.getKitchenPassword();
        this.outletName = outlet.getName();
        this.createdDate = outlet.getCreatedDate();
    }


}
