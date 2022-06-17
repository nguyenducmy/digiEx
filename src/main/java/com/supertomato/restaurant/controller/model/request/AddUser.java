package com.supertomato.restaurant.controller.model.request;


import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.util.ParamError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddUser {



    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String firstName;

    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String lastName;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String email;

    @NotNull(message = ParamError.FIELD_NAME)
    private UserRole userRole;

    private  List<String> outletIds;
}
