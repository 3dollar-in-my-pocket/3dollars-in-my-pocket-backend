package com.depromeet.threedollar.api.core.service.config.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("integration-test")
@Configuration
public class DisableCacheConfig {

    @Bean
    public CacheManager getNoOpCacheManager() {
        return new NoOpCacheManager();
    }

}
