package com.supertomato.restaurant.controller.model.request;

import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.util.ParamError;
import com.supertomato.restaurant.entity.Outlet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UpdateUser {
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String firstName;

    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String lastName;

    private String email;

    private UserRole userRole;

    private List<String> outletIds;
}
