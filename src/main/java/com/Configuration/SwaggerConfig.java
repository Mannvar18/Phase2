package com.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
    private SecurityContext securityContext(){
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }
    private ApiKey apiKey(){
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "headers");
    }
    private ApiInfo apiInfo(){
        return new ApiInfo(
                "Automated Workstation Assigner",
                "Automated Workstation Assigner REST API Documentation",
                "1",
                "Terms of service",
                new Contact("Nineleaps", "www.Nineleaps.com", "Nineleaps@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList()
        );
    }
    private List<SecurityReference> defaultAuth(){
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}