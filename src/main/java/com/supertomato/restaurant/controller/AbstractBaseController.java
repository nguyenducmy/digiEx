package com.supertomato.restaurant.controller;

import com.google.gson.Gson;
import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.common.util.ApplicationValueConfigure;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.common.util.Validator;
import com.supertomato.restaurant.controller.util.APIStatus;
import com.supertomato.restaurant.controller.util.ResponseUtil;
import com.supertomato.restaurant.entity.Company;
import com.supertomato.restaurant.entity.UserOutlet;
import com.supertomato.restaurant.service.CompanyService;
import com.supertomato.restaurant.service.UserOutletService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author DiGiEx
 */
public abstract class AbstractBaseController {
    Gson gson = new Gson();

    @Autowired
    public ResponseUtil responseUtil;

    @Autowired
    ApplicationValueConfigure applicationValueConfigure;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserOutletService userOutletService;

    public SimpleDateFormat apiDateFormat = new SimpleDateFormat(Constant.API_FORMAT_DATE);

    public SimpleDateFormat apiDateTimeFormat = new SimpleDateFormat(Constant.API_FORMAT_DATE_TIME);

    public void validatePageSize(int page, int size) {
        if (page < 0 || size < 0) {
            throw new ApplicationException(APIStatus.BAD_REQUEST);
        }
    }

    public String checkCompanyId(String userId, String companyId) {
        Company company = companyService.getById(companyId.trim());
        Validator.notNull(company, APIStatus.NOT_FOUND, "Company not found");

        List<UserOutlet> userOutletList = userOutletService.getAllByUserIdAndCompanyId(userId, company.getId());
        if (!userOutletList.isEmpty()) {
            throw new ApplicationException(APIStatus.BAD_REQUEST, "Company Id Invalid");
        }


        return company.getId();
    }

}
