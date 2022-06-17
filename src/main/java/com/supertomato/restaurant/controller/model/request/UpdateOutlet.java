package com.supertomato.restaurant.controller.model.request;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.util.ParamError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Size;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOutlet {
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String name;

    @Size(max = 16, message = ParamError.MAX_LENGTH)
    @Size(min = 1, message = ParamError.MIN_LENGTH)
    private String outletPrefix;

    private String kitchenPassword;

    private AppStatus startLineStatus;

    private AppStatus endLineStatus;

    private AppStatus masterIngredientsStatus;
}
