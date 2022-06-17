package com.supertomato.restaurant.controller.model.request;

import com.supertomato.restaurant.common.util.ParamError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class LoginKitchen {
    @NotBlank(message = ParamError.FIELD_NAME)
    private String kitchenAccount;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String kitchenPassword;


}
