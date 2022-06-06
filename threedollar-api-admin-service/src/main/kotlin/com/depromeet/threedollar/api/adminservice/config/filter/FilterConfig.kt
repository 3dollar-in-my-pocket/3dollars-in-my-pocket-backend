package com.depromeet.threedollar.api.adminservice.config.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class FilterConfig {

    @Profile("dev", "prod")
    @Bean
    fun requestLoggingFilter(): FilterRegistrationBean<RequestLoggingFilter> {
        val filter: FilterRegistrationBean<RequestLoggingFilter> = FilterRegistrationBean(RequestLoggingFilter())
        filter.addUrlPatterns("/v1/*")
        filter.order = 1
        return filter
    }

}
