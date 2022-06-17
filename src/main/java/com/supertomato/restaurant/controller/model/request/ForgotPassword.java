package com.supertomato.restaurant.controller.model.request;

import com.supertomato.restaurant.common.util.ParamError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPassword {
    @NotBlank(message = ParamError.FIELD_NAME)
    private String email;
}
