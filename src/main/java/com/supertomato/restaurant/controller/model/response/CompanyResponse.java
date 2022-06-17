package com.supertomato.restaurant.controller.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.entity.Company;
import com.supertomato.restaurant.entity.Outlet;
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
public class CompanyResponse {
    private String id;
    private String name;
    private String timezone;
    private AppStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date createdDate;
    private List<Outlet> outlets;

    public CompanyResponse(Company company, List<Outlet> outlets) {
        this.id = company.getId();
        this.name = company.getName();
        this.timezone = company.getTimezone();
        this.status = company.getStatus();
        this.createdDate = company.getCreatedDate();
        this.outlets = outlets;
    }

    public CompanyResponse(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.timezone = company.getTimezone();
        this.status = company.getStatus();
        this.createdDate = company.getCreatedDate();
    }

}
