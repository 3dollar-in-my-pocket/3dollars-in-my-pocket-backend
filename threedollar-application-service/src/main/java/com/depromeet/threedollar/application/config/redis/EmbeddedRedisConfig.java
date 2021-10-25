package com.depromeet.threedollar.application.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

import static com.depromeet.threedollar.common.utils.ProcessUtils.findAvailableRandomPort;
import static com.depromeet.threedollar.common.utils.ProcessUtils.isRunningPort;

/**
 * 로컬 및 테스트용 임베디드 Redis Server 설정.
 */
@Slf4j
@RequiredArgsConstructor
@Profile({"local", "local-will"})
@Configuration
public class EmbeddedRedisConfig {

    private final RedisProperties properties;

    private RedisServer redisServer;

    private int port;

    @PostConstruct
    public void redisServer() throws IOException {
        port = isRunningPort(properties.getPort()) ? findAvailableRandomPort() : properties.getPort();
        redisServer = new RedisServer(port);
        redisServer.start();
        log.info("임베디드 레디스 서버가 기동되었습니다. port: {}", port);
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
            log.info("임베디드 레디스 서버가 종료됩니다");
        }
    }

    @Bean
    public RedisConnectionFactory embeddedRedisConnectionFactory() {
        return new LettuceConnectionFactory(properties.getHost(), port);
    }

}
