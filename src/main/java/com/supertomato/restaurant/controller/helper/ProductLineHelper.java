package com.supertomato.restaurant.controller.helper;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.ProductType;
import com.supertomato.restaurant.common.enums.TypeLine;
import com.supertomato.restaurant.common.util.UniqueID;
import com.supertomato.restaurant.entity.ProductLine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductLineHelper {

    public List<ProductLine> addProductLine(List<ProductLine> productLines, String outletId) {


        ProductLine productLineBowl = new ProductLine();
        productLineBowl.setId(UniqueID.getUUID());
        productLineBowl.setName("Bowl");
        productLineBowl.setOutletId(outletId);
        productLineBowl.setStatus(AppStatus.ACTIVE);
        productLineBowl.setTypeLine(TypeLine.START_LINE);
        productLineBowl.setProductType(ProductType.NOT_APPLICABLE.toString());
        productLines.add(productLineBowl);

        ProductLine productLineFunnel = new ProductLine();
        productLineFunnel.setId(UniqueID.getUUID());
        productLineFunnel.setName("Funnel");
        productLineFunnel.setOutletId(outletId);
        productLineFunnel.setStatus(AppStatus.ACTIVE);
        productLineFunnel.setTypeLine(TypeLine.START_LINE);
        productLineFunnel.setProductType(ProductType.NOT_APPLICABLE.toString());
        productLines.add(productLineFunnel);

        ProductLine productLineWithLid = new ProductLine();
        productLineWithLid.setId(UniqueID.getUUID());
        productLineWithLid.setName("With lid");
        productLineWithLid.setOutletId(outletId);
        productLineWithLid.setStatus(AppStatus.ACTIVE);
        productLineWithLid.setTypeLine(TypeLine.END_LINE);
        productLineWithLid.setProductType(ProductType.NOT_APPLICABLE.toString());
        productLines.add(productLineWithLid);

        ProductLine productLineWithoutLid = new ProductLine();
        productLineWithoutLid.setId(UniqueID.getUUID());
        productLineWithoutLid.setName("Without lid");
        productLineWithoutLid.setOutletId(outletId);
        productLineWithoutLid.setStatus(AppStatus.ACTIVE);
        productLineWithoutLid.setTypeLine(TypeLine.END_LINE);
        productLineWithoutLid.setProductType(ProductType.NOT_APPLICABLE.toString());
        productLines.add(productLineWithoutLid);

        return productLines;
    }


}
