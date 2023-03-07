package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements WebMvcConfigurer {

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mindhub.homebanking.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
//                .securitySchemes(List.of(apiKey()))
//                .securityContexts(List.of(securityContext()))
                ;
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Homebanking API")
                .description("Demo API for homebanking")
                .contact(new Contact("José Maria Pereyra", "https://github.com/jomadev94", "joma.dev94@gmail.com"))
                .version("1.0")
                .build()
                ;
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        // use setStatusCode(HttpStatus.XYZ) for any custom status code if required, e.g. MOVED_PERMANENTLY
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html").setStatusCode(HttpStatus.MOVED_PERMANENTLY);
    }

//    private ApiKey apiKey() {
//        return new ApiKey("JWT", "Authorization", "header");
//    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder().securityReferences(defaultAuth()).build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return List.of(new SecurityReference("JWT", authorizationScopes));
//    }


}
