package com.depromeet.threedollar.api.bossservice.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.format.FormatterRegistry
import org.springframework.validation.Validator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import com.depromeet.threedollar.api.bossservice.config.interceptor.AuthInterceptor
import com.depromeet.threedollar.api.bossservice.config.interceptor.UserMetadataInterceptor
import com.depromeet.threedollar.api.bossservice.config.resolver.BossIdResolver
import com.depromeet.threedollar.api.core.config.converter.DecodeIdConverter
import com.depromeet.threedollar.api.core.config.resolver.DeviceLocationArgumentResolver
import com.depromeet.threedollar.api.core.config.resolver.MapLocationArgumentResolver

@Configuration
class WebConfig(
    private val authInterceptor: AuthInterceptor,
    private val bossIdResolver: BossIdResolver,
    private val mapLocationArgumentResolver: MapLocationArgumentResolver,
    private val deviceLocationArgumentResolver: DeviceLocationArgumentResolver,
    private val userMetadataInterceptor: UserMetadataInterceptor,
    private val decodeIdConverter: DecodeIdConverter,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
        registry.addInterceptor(userMetadataInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.addAll(listOf(bossIdResolver, mapLocationArgumentResolver, deviceLocationArgumentResolver))
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
