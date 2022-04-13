package com.depromeet.threedollar.api.admin.config.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

private const val MONITORING_MATCHERS = "/monitoring/**"

@EnableWebSecurity
@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers(MONITORING_MATCHERS).authenticated()
                .anyRequest().permitAll()
            .and()
                .httpBasic().and()
                .formLogin().disable()
                .logout().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .headers()
                    .frameOptions().sameOrigin()
                    .cacheControl().and()
                    .xssProtection().and()
                    .httpStrictTransportSecurity().disable()
    }

}
