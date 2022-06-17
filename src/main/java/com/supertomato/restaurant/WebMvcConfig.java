package com.supertomato.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.supertomato.restaurant.auth.AuthHelper;
import com.supertomato.restaurant.auth.AuthSessionResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.TimeZone;

/**
 * @author DiGiEx
 */
@Configuration
@EnableJpaRepositories
@EnableJpaAuditing(modifyOnCreate = false)
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private AuthHelper authHelper;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowedOrigins("*");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthSessionResolver(authHelper));
        WebMvcConfigurer.super.addArgumentResolvers(argumentResolvers);
    }

    /**
     * Register the Hibernate4Module into an ObjectMapper, then set this
     * custom-configured ObjectMapper to the MessageConverter and return it to
     * be added to the HttpMessageConverters of our application
     *
     * @return
     */
    public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        //Registering Hibernate5Module to support lazy objects
        Hibernate4Module module = new Hibernate4Module();
        //module.disable(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION);
        objectMapper.registerModule(module);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);

        messageConverter.setObjectMapper(objectMapper);
        return messageConverter;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }
}
