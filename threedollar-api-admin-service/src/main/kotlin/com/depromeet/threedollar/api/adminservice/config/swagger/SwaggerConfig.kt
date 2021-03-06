package com.depromeet.threedollar.api.adminservice.config.swagger

import com.depromeet.threedollar.api.adminservice.config.resolver.AdminId
import com.depromeet.threedollar.common.model.LocationValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RestController
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseBuilder
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.Response
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.DocExpansion
import springfox.documentation.swagger.web.UiConfiguration
import springfox.documentation.swagger.web.UiConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.stream.Collectors
import java.util.stream.Stream

@Import(BeanValidatorPluginsConfiguration::class)
@EnableSwagger2
@Configuration
class SwaggerConfig {

    @Bean
    fun uiConfig(): UiConfiguration {
        return UiConfigurationBuilder.builder()
            .docExpansion(DocExpansion.LIST)
            .build()
    }

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .securitySchemes(authorization())
            .ignoredParameterTypes(AdminId::class.java, LocationValue::class.java)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(PathSelectors.ant("/**"))
            .build()
            .useDefaultResponseMessages(false)
            .globalResponses(HttpMethod.GET, createGlobalResponseMessages())
            .globalResponses(HttpMethod.POST, createGlobalResponseMessages())
            .globalResponses(HttpMethod.PUT, createGlobalResponseMessages())
            .globalResponses(HttpMethod.PATCH, createGlobalResponseMessages())
            .globalResponses(HttpMethod.DELETE, createGlobalResponseMessages())
    }

    private fun createGlobalResponseMessages(): List<Response> {
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
            .map { httpStatus: HttpStatus -> createResponseMessage(httpStatus) }
            .collect(Collectors.toList())
    }

    private fun createResponseMessage(httpStatus: HttpStatus): Response {
        return ResponseBuilder()
            .code(httpStatus.value().toString())
            .description(httpStatus.reasonPhrase)
            .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("?????? ??? 3?????? ????????? API")
            .description("?????? ????????? ????????? API??? ???????????? [Authorize] ???????????? ???????????? ????????? ???????????? ?????? ???????????? ??? ????????????")
            .build()
    }

    private fun authorization(): List<SecurityScheme> {
        return listOf(ApiKey("Bearer", "Authorization", "header"))
    }

}
