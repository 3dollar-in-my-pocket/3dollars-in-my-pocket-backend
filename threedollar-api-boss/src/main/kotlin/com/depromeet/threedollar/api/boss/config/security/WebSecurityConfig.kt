package com.depromeet.threedollar.api.boss.config.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@EnableWebSecurity
@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            "/h2-console/**",
            "/swagger-resources/**",
            "swagger-ui/**",
            "/v2/api-docs",
            "/webjars/**"
        )
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/monitoring/**").authenticated()
                    .anyRequest().permitAll()
            .and()
                .httpBasic()
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .csrf().disable()
    }

}
