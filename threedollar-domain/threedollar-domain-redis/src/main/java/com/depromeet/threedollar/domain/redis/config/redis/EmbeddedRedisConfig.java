package com.depromeet.threedollar.domain.redis.config.redis;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.common.utils.ProcessUtils;
import com.depromeet.threedollar.domain.redis.config.redis.property.RedisProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * 로컬 및 테스트용 임베디드 Redis Server 설정.
 */
@Slf4j
@RequiredArgsConstructor
@Profile({"local", "integration-test"})
@Configuration
public class EmbeddedRedisConfig {

    private final RedisProperty property;

    private RedisServer redisServer;

    @Value("${spring.redis.port}")
    private int port;

    @PostConstruct
    public void startRedis() {
        try {
            if (redisServer == null || !redisServer.isActive()) {
                port = ProcessUtils.isRunningPort(port) ? ProcessUtils.findAvailableRandomPort() : port;
                redisServer = RedisServer.builder()
                    .port(port)
                    .setting("maxmemory 128M")
                    .setting("daemonize no")
                    .setting("appendonly no")
                    .build();
                redisServer.start();
                log.info("임베디드 레디스 서버가 기동되었습니다. port: {}", port);
            }
        } catch (IOException e) {
            throw new InternalServerException(String.format("임베디드 서버 시작 중 에러가 발생하였습니다. message: (%s)", e.getMessage()));
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
            log.info("임베디드 레디스 서버가 종료됩니다");
        }
    }

    @Bean
    public RedisConnectionFactory embeddedRedisConnectionFactory() {
        return new LettuceConnectionFactory(property.getHost(), port);
    }

}
