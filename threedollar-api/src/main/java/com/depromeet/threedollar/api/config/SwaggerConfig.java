package com.depromeet.threedollar.api.config;

import com.depromeet.threedollar.api.config.resolver.UserId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static springfox.documentation.builders.RequestHandlerSelectors.withClassAnnotation;

@Profile("!prod")
@Import(BeanValidatorPluginsConfiguration.class)
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .ignoredParameterTypes(UserId.class)
            .select()
            .apis(withClassAnnotation(RestController.class))
            .paths(PathSelectors.ant("/api/**"))
            .build()
            .useDefaultResponseMessages(false)
            .globalResponseMessage(RequestMethod.GET, this.createGlobalResponseMessages())
            .globalResponseMessage(RequestMethod.POST, this.createGlobalResponseMessages())
            .globalResponseMessage(RequestMethod.PUT, this.createGlobalResponseMessages())
            .globalResponseMessage(RequestMethod.DELETE, this.createGlobalResponseMessages());
    }

    private List<ResponseMessage> createGlobalResponseMessages() {
        return Stream.of(
            HttpStatus.BAD_REQUEST,
            HttpStatus.UNAUTHORIZED,
            HttpStatus.CONFLICT,
            HttpStatus.FORBIDDEN,
            HttpStatus.NOT_FOUND,
            HttpStatus.INTERNAL_SERVER_ERROR,
            HttpStatus.BAD_GATEWAY,
            HttpStatus.SERVICE_UNAVAILABLE
        )
            .map(this::createResponseMessage)
            .collect(Collectors.toList());
    }

    private ResponseMessage createResponseMessage(HttpStatus httpStatus) {
        return new ResponseMessageBuilder()
            .code(httpStatus.value())
            .message(httpStatus.getReasonPhrase())
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("가슴 속 3천원 API")
            .description("가슴 속 3천원 API 입니다.")
            .build();
    }

}
