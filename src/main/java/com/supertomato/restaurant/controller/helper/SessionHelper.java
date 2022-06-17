package com.supertomato.restaurant.controller.helper;

import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.common.util.DateUtil;
import com.supertomato.restaurant.common.util.UniqueID;
import com.supertomato.restaurant.entity.Outlet;
import com.supertomato.restaurant.entity.Session;
import com.supertomato.restaurant.entity.User;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Quy Duong
 */
@Component
public class SessionHelper {

    SimpleDateFormat sdf = new SimpleDateFormat(Constant.API_FORMAT_DATE);

    public Session createSession(User user, boolean keepLogin) {
        //Create new session
        Session session = new Session();
        session.setId(UniqueID.getUUID());
        session.setUserId(user.getId());
        session.setCreatedDate(DateUtil.convertToUTC(new Date()));
        if(keepLogin){
            try {
                session.setExpiryDate(sdf.parse("12/31/9999 00:00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            session.setExpiryDate(DateUtil.addHoursToJavaUtilDate(new Date(), 36));
        }
        return session;
    }

    public Session createSession(Outlet outlet) {
        //Create new session
        Session session = new Session();
        session.setId(UniqueID.getUUID());
        String outletId = outlet.getId();
        session.setOutletId(outletId);
        session.setCreatedDate(DateUtil.convertToUTC(new Date()));

            try {
                session.setExpiryDate(sdf.parse("12/31/9999 00:00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        return session;
    }

}
