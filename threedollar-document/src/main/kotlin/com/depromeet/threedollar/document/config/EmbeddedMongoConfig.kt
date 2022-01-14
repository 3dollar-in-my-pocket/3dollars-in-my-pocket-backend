package com.depromeet.threedollar.document.config

import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

@Profile("local")
@Import(EmbeddedMongoAutoConfiguration::class)
@Configuration
class EmbeddedMongoConfig
