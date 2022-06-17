package com.supertomato.restaurant.controller.model.request;

import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.util.ParamError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author DiGiEx
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = ParamError.FIELD_NAME)
    private String email;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String passwordHash;

    private boolean keepLogin;
}
