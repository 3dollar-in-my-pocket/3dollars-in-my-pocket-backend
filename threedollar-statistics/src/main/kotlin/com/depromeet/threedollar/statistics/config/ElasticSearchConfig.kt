package com.depromeet.threedollar.statistics.config

import com.depromeet.threedollar.statistics.ThreeDollarConsumerApplication
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.RestClients
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@EnableElasticsearchRepositories(basePackageClasses = [ThreeDollarConsumerApplication::class])
@Configuration
class ElasticSearchConfig(
    private val elasticSearchProperties: ElasticSearchProperties
) : AbstractElasticsearchConfiguration() {

    override fun elasticsearchClient(): RestHighLevelClient {
        val clientConfiguration = ClientConfiguration.builder()
            .connectedTo(elasticSearchProperties.host)
            .build()
        return RestClients.create(clientConfiguration).rest()
    }

    @Bean
    fun elasticsearchOperations(): ElasticsearchOperations {
        return ElasticsearchRestTemplate(elasticsearchClient())
    }

}
