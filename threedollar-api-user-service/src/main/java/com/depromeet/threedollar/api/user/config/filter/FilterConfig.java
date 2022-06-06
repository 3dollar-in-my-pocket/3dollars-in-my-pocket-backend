package com.depromeet.threedollar.api.user.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FilterConfig {

    @Profile({"dev", "prod"})
    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilter() {
        FilterRegistrationBean<RequestLoggingFilter> filter = new FilterRegistrationBean<>(new RequestLoggingFilter());
        filter.addUrlPatterns("/v1/*", "/v2/*", "/v3/*");
        filter.setOrder(1);
        return filter;
    }

}
