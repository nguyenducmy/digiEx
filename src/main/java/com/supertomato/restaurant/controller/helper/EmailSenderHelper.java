package com.supertomato.restaurant.controller.helper;

import com.google.gson.Gson;
import com.supertomato.restaurant.common.util.ApplicationValueConfigure;
import com.supertomato.restaurant.entity.User;
import com.supertomato.restaurant.notification.email.AWSEmailSender;
import com.supertomato.restaurant.notification.email.VelocityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

/**
 * @author DiGiEx
 */
@Component
@Slf4j
public class EmailSenderHelper {

    Gson gson = new Gson();

    final ApplicationValueConfigure applicationValueConfigure;
    final VelocityService velocityService;

    public EmailSenderHelper(ApplicationValueConfigure applicationValueConfigure, VelocityService velocityService) {
        this.applicationValueConfigure = applicationValueConfigure;
        this.velocityService = velocityService;
    }


    /**
     * Send Sign up confirmation email
     *
     * @param user
     */
    @Async
    public void confirmEmail(User user) {
        if (user.getEmail() != null && !"".equals(user.getEmail())) {
            String subject = "Welcome to Supertomato!";
            ModelMap model = new ModelMap();
            String link = applicationValueConfigure.CHANGE_PASSWORD_URL + user.getActiveCode();
            model.put("link", link);
            String body = velocityService.createTemplate(model, "confirm-email.vm");
            String[] emailsNotify = {user.getEmail()};
            AWSEmailSender.getInstance().doSendMail(applicationValueConfigure, emailsNotify, subject, body, null, applicationValueConfigure.AWS_SES_SMTP_SENDER_NAME);
        }
    }

    /**
     * Send Sign up confirmation email
     *
     * @param user
     */
    @Async
    public void changePassword(User user) {
        if (user.getEmail() != null && !"".equals(user.getEmail())) {
            String subject = "Change Password";
            ModelMap model = new ModelMap();
            String link = applicationValueConfigure.CHANGE_PASSWORD_URL + user.getActiveCode();
            model.put("link", link);
            model.put("name", user.getFirstName() + " " + user.getLastName());
            String body = velocityService.createTemplate(model, "change-password.vm");
            String[] emailsNotify = {user.getEmail()};
            AWSEmailSender.getInstance().doSendMail(applicationValueConfigure, emailsNotify, subject, body, null, applicationValueConfigure.AWS_SES_SMTP_SENDER_NAME);
        }
    }


}