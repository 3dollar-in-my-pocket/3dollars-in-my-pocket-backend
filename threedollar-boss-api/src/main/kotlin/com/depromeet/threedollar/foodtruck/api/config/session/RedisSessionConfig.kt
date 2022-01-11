package com.depromeet.threedollar.foodtruck.api.config.session

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.data.redis.config.ConfigureRedisAction
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

/**
 * 만료시간: 7일
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60 * 60 * 24 * 7)
@Configuration
class RedisSessionConfig {

    @Bean
    fun configureRedisAction(): ConfigureRedisAction {
        return ConfigureRedisAction.NO_OP
    }

}
