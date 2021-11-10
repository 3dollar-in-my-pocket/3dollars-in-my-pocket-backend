package com.depromeet.threedollar.api.config.swagger;

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
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
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
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
            .docExpansion(DocExpansion.LIST)
            .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .securitySchemes(authorization())
            .ignoredParameterTypes(UserId.class)
            .select()
            .apis(withClassAnnotation(RestController.class))
            .paths(PathSelectors.ant("/**"))
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
            .description("인증 토큰이 필요한 API는 오른쪽에 [Authorize] 자물쇠를 클릭해서 토큰을 넣어두면 쉽게 테스트할 수 있습니다")
            .build();
    }

    private List<ApiKey> authorization() {
        return List.of(new ApiKey("Authorization", "Authorization", "header"));
    }

}
