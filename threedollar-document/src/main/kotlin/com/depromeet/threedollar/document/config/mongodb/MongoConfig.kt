package com.depromeet.threedollar.document.config.mongodb

import com.depromeet.threedollar.document.ThreeDollarDocumentRoot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

@EnableMongoAuditing
@Configuration
@EnableMongoRepositories(basePackageClasses = [ThreeDollarDocumentRoot::class])
class MongoConfig(
    private val mongoDbFactory: MongoDatabaseFactory,
    private val mongoMappingContext: MongoMappingContext
) {

    @Bean
    fun mappingMongoConverter(): MappingMongoConverter {
        val dbRefResolver = DefaultDbRefResolver(mongoDbFactory)
        val converter = MappingMongoConverter(dbRefResolver, mongoMappingContext)
        converter.setTypeMapper(DefaultMongoTypeMapper(null))
        return converter
    }

}
