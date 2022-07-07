package com.depromeet.threedollar.api.userservice.config;

import com.depromeet.threedollar.api.core.config.converter.DecodeIdConverter;
import com.depromeet.threedollar.api.core.config.resolver.DeviceLocationArgumentResolver;
import com.depromeet.threedollar.api.core.config.resolver.MapLocationArgumentResolver;
import com.depromeet.threedollar.api.userservice.config.interceptor.AuthInterceptor;
import com.depromeet.threedollar.api.userservice.config.interceptor.UserMetadataInterceptor;
import com.depromeet.threedollar.api.userservice.config.resolver.UserIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final UserMetadataInterceptor userMetaDataInterceptor;

    private final UserIdResolver userIdResolver;
    private final DeviceLocationArgumentResolver deviceLocationArgumentResolver;
    private final MapLocationArgumentResolver mapLocationArgumentResolver;

    private final DecodeIdConverter decodeIdConverter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
        registry.addInterceptor(userMetaDataInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(List.of(userIdResolver, deviceLocationArgumentResolver, mapLocationArgumentResolver));
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(validationMessageSource());
        return validatorFactoryBean;
    }

    @Bean
    public MessageSource validationMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/messages/validation");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(60);
        return messageSource;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(decodeIdConverter);
    }

}
