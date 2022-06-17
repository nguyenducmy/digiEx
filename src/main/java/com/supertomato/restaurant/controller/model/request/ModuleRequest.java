package com.supertomato.restaurant.controller.model.request;


import com.supertomato.restaurant.common.util.ParamError;
import com.supertomato.restaurant.entity.Outlet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModuleRequest {

    @NotBlank(message = ParamError.FIELD_NAME)
    private String outletId;

    @NotEmpty(message = ParamError.FIELD_NAME)
    private List<IngredientRequest> ingredients;

}
