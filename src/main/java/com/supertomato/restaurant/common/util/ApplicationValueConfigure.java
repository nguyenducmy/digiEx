package com.supertomato.restaurant.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author DiGiEx
 */
@Component
public class ApplicationValueConfigure {
    @Value("${aws.ses.smtp.username}")
    public String AWS_SES_SMTP_USERNAME;

    @Value("${aws.ses.smtp.password}")
    public String AWS_SES_SMTP_PASSWORD;

    @Value("${aws.ses.smtp.host}")
    public String AWS_SES_SMTP_HOST;

    @Value("${aws.ses.smtp.port}")
    public String AWS_SES_SMTP_PORT;

    @Value("${aws.ses.smtp.from}")
    public String AWS_SES_SMTP_FROM;

    @Value("${aws.ses.smtp.sender.name}")
    public String AWS_SES_SMTP_SENDER_NAME;
//
//    @Value("${email.logo.url}")
//    public String EMAIL_LOGO_URL;
//
//    @Value("${email.forgot.password.url}")
//    public String EMAIL_FORGOT_PASSWORD_URL;
//
    @Value("${change.password.url}")
    public String CHANGE_PASSWORD_URL;

}
