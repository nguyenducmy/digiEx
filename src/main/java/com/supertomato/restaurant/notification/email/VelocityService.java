package com.supertomato.restaurant.notification.email;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * @author DiGiEx
 */
@Component
@Slf4j
public class VelocityService {

    public String createTemplate(ModelMap model, String template){
        try {
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            velocityEngine.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
            velocityEngine.init();
            String mailBody = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/" + template, "UTF-8", model);
            return mailBody;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Email body fail", e);
            return null;
        }
    }
}
