package com.depromeet.threedollar.document.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@EnableAutoConfiguration(exclude = [
    EmbeddedMongoAutoConfiguration::class
])
@Profile("!local")
@Configuration
class EmbeddedMongoConfig
