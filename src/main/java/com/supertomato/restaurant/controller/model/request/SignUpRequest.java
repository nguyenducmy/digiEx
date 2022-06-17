package com.supertomato.restaurant.controller.model.request;

import com.supertomato.restaurant.common.util.ParamError;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {
    @NotBlank(message = ParamError.FIELD_NAME)
    private String email;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String passwordHash; // hash md5

    @Size(max = 250, message = ParamError.MAX_LENGTH)
    private String dialCode;

}
