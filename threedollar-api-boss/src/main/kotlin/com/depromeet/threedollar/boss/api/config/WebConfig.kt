package com.depromeet.threedollar.boss.api.config

import com.depromeet.threedollar.boss.api.config.interceptor.AuthInterceptor
import com.depromeet.threedollar.boss.api.config.resolver.BossAccountIdResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
        private val authInterceptor: AuthInterceptor,
        private val bossAccountIdResolver: BossAccountIdResolver
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/api/**")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(bossAccountIdResolver)
    }

}
