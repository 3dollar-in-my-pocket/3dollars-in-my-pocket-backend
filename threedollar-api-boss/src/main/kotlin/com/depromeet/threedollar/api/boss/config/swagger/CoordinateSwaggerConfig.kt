package com.depromeet.threedollar.api.boss.config.swagger

import java.util.*
import java.util.stream.Collectors
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import com.depromeet.threedollar.api.core.config.resolver.GeoCoordinate
import com.depromeet.threedollar.api.core.config.resolver.MapCoordinate
import springfox.documentation.builders.RequestParameterBuilder
import springfox.documentation.service.ParameterType
import springfox.documentation.service.RequestParameter
import springfox.documentation.service.ResolvedMethodParameter
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.OperationBuilderPlugin
import springfox.documentation.spi.service.contexts.OperationContext
import springfox.documentation.swagger.common.SwaggerPluginSupport

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1001)
class CoordinateSwaggerConfig : OperationBuilderPlugin {

    override fun apply(context: OperationContext) {
        val parameters = context.parameters
        if (hasAnnotation(parameters, GeoCoordinate::class.java)) {
            context.operationBuilder()
                .requestParameters(queryParameter("latitude", "longitude"))
                .build()
        }
        if (hasAnnotation(parameters, MapCoordinate::class.java)) {
            context.operationBuilder()
                .requestParameters(queryParameter("mapLatitude", "mapLongitude"))
                .build()
        }
    }

    private fun hasAnnotation(parameters: List<ResolvedMethodParameter>, annotation: Class<out Annotation?>): Boolean {
        return parameters.stream()
            .anyMatch { parameter: ResolvedMethodParameter -> parameter.hasParameterAnnotation(annotation) }
    }

    private fun queryParameter(vararg parameterNames: String): List<RequestParameter> {
        return Arrays.stream(parameterNames)
            .map { name: String -> this.queryParameter(name) }
            .collect(Collectors.toList())
    }

    private fun queryParameter(name: String): RequestParameter {
        return RequestParameterBuilder()
            .name(name)
            .required(true)
            .`in`(ParameterType.QUERY)
            .build()
    }

    override fun supports(delimiter: DocumentationType): Boolean {
        return true
    }

}
