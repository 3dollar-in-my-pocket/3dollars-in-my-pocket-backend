package com.depromeet.threedollar.api.admin.config.locale

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.*

@Configuration
class LocaleConfig {

    @Bean
    fun localeResolver(): LocaleResolver {
        val localeResolver = AcceptHeaderLocaleResolver()
        localeResolver.defaultLocale = Locale.KOREA
        return localeResolver
    }

}
