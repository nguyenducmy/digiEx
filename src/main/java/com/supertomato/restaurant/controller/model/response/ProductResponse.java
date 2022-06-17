package com.supertomato.restaurant.controller.model.response;

import com.supertomato.restaurant.controller.model.response.ProductLineResponse;
import com.supertomato.restaurant.entity.ProductLine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponse {

    private List<ProductLineResponse> productLines;
    private List<ModuleResponse> modules;

    public ProductResponse(List<ProductLineResponse> productLines,
                           List<ModuleResponse> modules) {
        this.productLines = productLines;
        this.modules = modules;
    }

}
