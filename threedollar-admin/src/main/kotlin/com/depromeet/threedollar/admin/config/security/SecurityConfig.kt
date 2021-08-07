package com.depromeet.threedollar.admin.config.security

import de.codecentric.boot.admin.server.config.AdminServerProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
class SecuritySecureConfig(
    private val adminServer: AdminServerProperties
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        val contextPath = adminServer.contextPath
        val successHandler = SavedRequestAwareAuthenticationSuccessHandler()
        successHandler.setTargetUrlParameter("redirectTo")
        successHandler.setDefaultTargetUrl("/")
        http.authorizeRequests()
            .antMatchers(
                "/test-token",
                "/ping",
                "/admin/**",
                "$contextPath/login",
                "$contextPath/instances"
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("$contextPath/login").successHandler(successHandler).and()
            .logout().logoutUrl("$contextPath/logout").and()
            .httpBasic().and()
            .csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringAntMatchers(
                "/test-token",
                "/ping",
                "/admin/**",
                "$contextPath/instances"
            )
    }

}
