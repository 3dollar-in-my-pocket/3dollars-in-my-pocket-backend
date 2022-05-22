package com.depromeet.threedollar.api.user.config.swagger;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.depromeet.threedollar.api.user.config.interceptor.Auth;

import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class AuthorizationSwaggerConfig implements OperationBuilderPlugin {

    @Override
    public void apply(OperationContext context) {
        if (context.findAnnotation(Auth.class).isPresent()) {
            context.operationBuilder()
                .requestParameters(List.of(authorizationHeader()))
                .authorizations(List.of(SecurityReference.builder()
                    .reference(HttpHeaders.AUTHORIZATION)
                    .scopes(authorizationScopes())
                    .build()))
                .build();
        }
    }

    private RequestParameter authorizationHeader() {
        return new RequestParameterBuilder()
            .name(HttpHeaders.AUTHORIZATION)
            .required(false)
            .in(ParameterType.HEADER)
            .build();
    }

    private AuthorizationScope[] authorizationScopes() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("", "");
        return authorizationScopes;
    }

    @Override
    public boolean supports(@NotNull DocumentationType delimiter) {
        return true;
    }

}
