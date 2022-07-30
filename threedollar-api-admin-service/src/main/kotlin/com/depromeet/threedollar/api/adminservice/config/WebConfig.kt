package com.depromeet.threedollar.api.adminservice.config

import com.depromeet.threedollar.api.adminservice.config.interceptor.AuthInterceptor
import com.depromeet.threedollar.api.adminservice.config.resolver.AccountIdResolver
import com.depromeet.threedollar.api.core.common.config.converter.DecodeIdConverter
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.format.FormatterRegistry
import org.springframework.validation.Validator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val authInterceptor: AuthInterceptor,
    private val adminIdResolver: AccountIdResolver,
    private val decodeIdConverter: DecodeIdConverter,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(adminIdResolver)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("https://admin.threedollars.co.kr", "https://admin.dev.threedollars.co.kr", "http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD")
            .allowedHeaders("*")
            .allowCredentials(true)
    }

    override fun getValidator(): Validator {
        val validatorFactoryBean = LocalValidatorFactoryBean()
        validatorFactoryBean.setValidationMessageSource(validationMessageSource())
        return validatorFactoryBean
    }

    @Bean
    fun validationMessageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasename("classpath:/messages/validation")
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setCacheSeconds(60)
        return messageSource
    }

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(decodeIdConverter)
    }

}
