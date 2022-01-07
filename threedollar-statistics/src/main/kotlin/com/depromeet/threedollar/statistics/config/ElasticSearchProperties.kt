package com.depromeet.threedollar.statistics.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("threedollars.elasticsearch.node")
@ConstructorBinding
data class ElasticSearchProperties(
    val host: String
)
