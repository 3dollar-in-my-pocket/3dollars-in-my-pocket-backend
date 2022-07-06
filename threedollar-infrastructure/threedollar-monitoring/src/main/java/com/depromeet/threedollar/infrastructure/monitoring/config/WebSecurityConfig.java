package com.depromeet.threedollar.infrastructure.monitoring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String MONITORING_MATCHERS = "/monitoring/**";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
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
            .httpStrictTransportSecurity().disable();
    }

}
