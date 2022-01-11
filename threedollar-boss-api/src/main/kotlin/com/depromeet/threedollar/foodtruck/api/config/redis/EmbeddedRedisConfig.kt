package com.depromeet.threedollar.foodtruck.api.config.redis

import com.depromeet.threedollar.common.utils.ProcessUtils
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * 로컬 및 테스트용 임베디드 Redis Server 설정.
 */
@Slf4j
@RequiredArgsConstructor
@Profile("local")
@Configuration
class EmbeddedRedisConfig(
    private val properties: RedisProperties
) {

    private lateinit var redisServer: RedisServer
    private var port: Int = 0

    @PostConstruct
    fun startRedis() {
        port = if (ProcessUtils.isRunningPort(properties.port)) ProcessUtils.findAvailableRandomPort() else properties.port
        redisServer = RedisServer(port)
        redisServer.start()
    }

    @PreDestroy
    fun stopRedis() {
        redisServer.stop()
    }

    @Bean
    fun embeddedRedisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(properties.host, port)
    }

}
