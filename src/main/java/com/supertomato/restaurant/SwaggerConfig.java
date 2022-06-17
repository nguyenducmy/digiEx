package com.supertomato.restaurant;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.ArrayList;
import java.util.List;

import com.supertomato.restaurant.auth.AuthSession;
import com.supertomato.restaurant.common.util.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public WebMvcConfigurerAdapter forwardToIndex() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/api").setViewName(
                        "forward:/swagger-ui.html");
            }
        };
    }

    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.supertomato.restaurant"))
                .paths(apiPaths())
                .build()
                .globalOperationParameters(apiHeader())
                .ignoredParameterTypes()
                .ignoredParameterTypes(AuthSession.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Supertomato Restaurant Service Restful API")
                .description("Documents with Swagger 2")
                .termsOfServiceUrl("https://digiex.asia")
                .license("")
                .licenseUrl("")
                .version("1.0")
                .build();
    }

    private Predicate<String> apiPaths() {
        return Predicates.or(
                regex("/manage.*"),
                regex("/api.*"));
    }
    
    private List<Parameter> apiHeader (){
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder()
                .name(Constant.HEADER_TOKEN)
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build());
        
        return parameters;
    }
}
