package com.depromeet.threedollar.api.config.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"dev", "prod"})
@Getter
@Setter
@ConfigurationProperties("spring.redis")
@Component
public class RedisProperties {

    private String host;

    private int port;

}
