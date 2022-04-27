package com.depromeet.threedollar.api.user.config.swagger;

import com.depromeet.threedollar.api.core.config.resolver.GeoCoordinate;
import com.depromeet.threedollar.api.core.config.resolver.MapCoordinate;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1001)
public class CoordinateSwaggerConfig implements OperationBuilderPlugin {

    @Override
    public void apply(OperationContext context) {
        List<ResolvedMethodParameter> parameters = context.getParameters();
        if (hasAnnotation(parameters, GeoCoordinate.class)) {
            context.operationBuilder()
                .requestParameters(queryParameter("latitude", "longitude"))
                .build();
        }
        if (hasAnnotation(parameters, MapCoordinate.class)) {
            context.operationBuilder()
                .requestParameters(queryParameter("mapLatitude", "mapLongitude"))
                .build();
        }
    }

    private boolean hasAnnotation(List<ResolvedMethodParameter> parameters, Class<? extends Annotation> annotation) {
        return parameters.stream()
            .anyMatch(parameter -> parameter.hasParameterAnnotation(annotation));
    }

    private List<RequestParameter> queryParameter(String... parameterNames) {
        return Arrays.stream(parameterNames)
            .map(this::queryParameter)
            .collect(Collectors.toList());
    }

    private RequestParameter queryParameter(String name) {
        return new RequestParameterBuilder()
            .name(name)
            .required(true)
            .in(ParameterType.QUERY)
            .build();
    }

    @Override
    public boolean supports(@NotNull DocumentationType delimiter) {
        return true;
    }

}
