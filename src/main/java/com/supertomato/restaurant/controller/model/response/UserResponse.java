package com.supertomato.restaurant.controller.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.entity.Company;
import com.supertomato.restaurant.entity.Outlet;
import com.supertomato.restaurant.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole userRole;
    private AppStatus status;

    @JsonProperty(value = "is_user_root")
    private boolean isUserRoot;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date createdDate;
    private List<Outlet> outlets;

    private List<CompanyResponse> companies;

    public UserResponse(User user, List<Outlet> outlets) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.isUserRoot = user.isUserRoot();
        this.email = user.getEmail();
        this.userRole = user.getUserRole();
        this.status = user.getStatus();
        this.createdDate = user.getCreatedDate();
        this.outlets = outlets;

    }
    public UserResponse(List<CompanyResponse> companies,User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.isUserRoot = user.isUserRoot();
        this.userRole = user.getUserRole();
        this.status = user.getStatus();
        this.createdDate = user.getCreatedDate();
        this.companies = companies;

    }
}
