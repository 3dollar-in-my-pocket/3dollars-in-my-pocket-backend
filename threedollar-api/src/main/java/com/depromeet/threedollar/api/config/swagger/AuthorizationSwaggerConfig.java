package com.depromeet.threedollar.api.config.swagger;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.List;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class AuthorizationSwaggerConfig implements OperationBuilderPlugin {

    @Override
    public void apply(OperationContext context) {
        if (context.findAnnotation(Auth.class).isPresent()) {
            context.operationBuilder()
                .parameters(List.of(authorizationHeader()))
                .authorizations(List.of(SecurityReference.builder()
                    .reference("Authorization")
                    .scopes(authorizationScopes())
                    .build()))
                .build();
        }
    }

    private Parameter authorizationHeader() {
        return new ParameterBuilder()
            .name("Authorization")
            .modelRef(new ModelRef("string"))
            .parameterType("header")
            .required(false)
            .build();
    }

    private AuthorizationScope[] authorizationScopes() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("", "");
        return authorizationScopes;
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

}
