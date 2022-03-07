package com.depromeet.threedollar.api.admin.config.swagger

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.OperationBuilderPlugin
import springfox.documentation.spi.service.contexts.OperationContext

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class SwaggerKotlinOperationDeprecatedReader : OperationBuilderPlugin {

    override fun apply(context: OperationContext) {
        val annotationOnMethod = context.findAnnotation(Deprecated::class.java)
        val annotationOnController = context.findControllerAnnotation(Deprecated::class.java)
        val deprecated = annotationOnMethod.isPresent || annotationOnController.isPresent
        context.operationBuilder().deprecated(if (deprecated) "true" else null)
    }

    override fun supports(delimiter: DocumentationType): Boolean = true

}
