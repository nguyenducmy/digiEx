package com.supertomato.restaurant.notification.email;

import com.supertomato.restaurant.common.util.ApplicationValueConfigure;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author DiGiEx
 */
@Slf4j
public class AWSEmailSender {

    private static AWSEmailSender instance;

    public static synchronized AWSEmailSender getInstance() {
        if (instance == null) {
            instance = new AWSEmailSender();
        }
        return instance;
    }

    public void doSendMail(ApplicationValueConfigure appConfig, String toEmail[], String subject, String body, String attachment, String senderName) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            List<Future<String>> listTask = new ArrayList<Future<String>>();
            Callable worker = new AWSCallableTaskSendRequest(appConfig, toEmail,subject, body, attachment, senderName);
            Future<String> submit = executor.submit(worker);
            listTask.add(submit);
            executor.shutdown();

        } catch (Exception e) {
            log.error("doSendMail error", e);
        }
    }
}
