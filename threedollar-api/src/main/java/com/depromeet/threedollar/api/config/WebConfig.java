package com.depromeet.threedollar.api.config;

import com.depromeet.threedollar.api.config.interceptor.AuthInterceptor;
import com.depromeet.threedollar.api.config.interceptor.UserMetaInterceptor;
import com.depromeet.threedollar.api.config.resolver.GeoCoordinateArgumentResolver;
import com.depromeet.threedollar.api.config.resolver.MapCoordinateArgumentResolver;
import com.depromeet.threedollar.api.config.resolver.UserIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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
    private final UserMetaInterceptor userMetaInterceptor;

    private final UserIdResolver userIdResolver;
    private final GeoCoordinateArgumentResolver geoCoordinateArgumentResolver;
    private final MapCoordinateArgumentResolver mapCoordinateArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
        registry.addInterceptor(userMetaInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(List.of(userIdResolver, geoCoordinateArgumentResolver, mapCoordinateArgumentResolver));
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
        messageSource.setCacheSeconds(10);
        return messageSource;
    }

}
