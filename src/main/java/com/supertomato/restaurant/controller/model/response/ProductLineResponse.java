package com.supertomato.restaurant.controller.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.ProductType;
import com.supertomato.restaurant.common.enums.TypeLine;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.entity.ProductLine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductLineResponse {


    private String id;
    private String name;
    private TypeLine typeLine;
    private List<ProductType> productType;
    private String outletId;
    private AppStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date createdDate;


    public ProductLineResponse(ProductLine productLine) {

        List<ProductType> productTypes = new ArrayList<>();

        if (productLine.getProductType() != null && !productLine.getProductType().isEmpty()) {

            List<String> productTypeList = Arrays.asList(productLine.getProductType().split(","));

            for (String productType : productTypeList) {

                switch (productType) {
                    case "DAILY_BOWL":
                        productTypes.add(ProductType.DAILY_BOWL);
                        break;
                    case "KEBAB":
                        productTypes.add(ProductType.KEBAB);
                        break;
                    case "QUESADILLA":
                        productTypes.add(ProductType.QUESADILLA);
                        break;
                    case "BURRITO":
                        productTypes.add(ProductType.BURRITO);
                        break;
                    case "NOT_APPLICABLE":
                        productTypes.add(ProductType.NOT_APPLICABLE);
                        break;
                }
            }
        }
        this.id = productLine.getId();
        this.name = productLine.getName();
        this.typeLine = productLine.getTypeLine();
        this.productType = productTypes;
        this.outletId = productLine.getOutletId();
        this.status = productLine.getStatus();
        this.createdDate = productLine.getCreatedDate();
    }
}
