package com.supertomato.restaurant.controller.model.response;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.entity.Company;
import com.supertomato.restaurant.entity.Ingredient;
import com.supertomato.restaurant.entity.Outlet;
import com.supertomato.restaurant.entity.ProductLine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SessionKitchenResponse {

    private String authToken;
    private String outletId;
    private String companyName;
    private String companyId;
    private String timeZone;
    private String kitchenAccount;
    private String kitchenPassword;
    private String outletName;
    private AppStatus startLineStatus;
    private AppStatus endLineStatus;

    private List<ProductLine> productLines;
    public SessionKitchenResponse(Company company, String authToken,
                                  Outlet outlet, List<ProductLine> productLines) {
        this.authToken = authToken;
        this.outletId = outlet.getId();
        this.companyName = company.getName();
        this.companyId = company.getId();
        this.timeZone = company.getTimezone();
        this.kitchenAccount = outlet.getKitchenAccount();
        this.kitchenPassword = outlet.getKitchenPassword();
        this.outletName = outlet.getName();
        this.startLineStatus = outlet.getStartLineStatus();
        this.endLineStatus = outlet.getEndLineStatus();
        this.productLines = productLines;
    }

}
