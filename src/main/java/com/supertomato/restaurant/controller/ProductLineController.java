package com.supertomato.restaurant.controller;

import com.supertomato.restaurant.auth.AuthorizeValidator;
import com.supertomato.restaurant.common.enums.ProductType;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.controller.model.request.UpdateProductLine;
import com.supertomato.restaurant.controller.model.response.ProductLineResponse;
import com.supertomato.restaurant.controller.util.APIStatus;
import com.supertomato.restaurant.controller.util.RestAPIResponse;
import com.supertomato.restaurant.entity.ProductLine;
import com.supertomato.restaurant.service.ProductLineService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(APIName.PRODUCT_LINE)
public class ProductLineController extends AbstractBaseController {

    final ProductLineService productLineService;


    public ProductLineController(ProductLineService productLineService) {
        this.productLineService = productLineService;
    }

    @AuthorizeValidator(UserRole.SUPER_ADMIN)
    @RequestMapping(path = APIName.UPDATE_ID, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> UpdateProductLine(
            @PathVariable("id") String id,
            @RequestBody @Valid UpdateProductLine updateProductLine
    ) {

        ProductLine productLine = productLineService.getById(id);
        Validator.notNull(productLine, APIStatus.NOT_FOUND, "ProductLine not found");

        if (!updateProductLine.getProductTypes().isEmpty()) {

            long productTypes = updateProductLine.getProductTypes().stream().distinct().count();
            if (productTypes != updateProductLine.getProductTypes().size()){
                throw new ApplicationException(APIStatus.BAD_PARAMS, "Product Type is duplicate");
            }

            String productType = updateProductLine.getProductTypes().stream().map(String::valueOf).collect(Collectors.joining(","));
            productLine.setProductType(productType);
            productLineService.save(productLine);
        }
        return responseUtil.successResponse(new ProductLineResponse(productLine));
    }


}
