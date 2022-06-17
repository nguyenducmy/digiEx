package com.supertomato.restaurant.controller.model.request;

import com.supertomato.restaurant.common.util.ParamError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OutletRequest {

    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String name;

    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 16, message = ParamError.MAX_LENGTH)
    @Size(min = 1, message = ParamError.MIN_LENGTH)
    private String outletPrefix;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String kitchenPassword;

}
