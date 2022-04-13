package com.depromeet.threedollar.domain.mongo.config.mongo

import com.depromeet.threedollar.domain.mongo.ThreeDollarDomainMongoRoot
import com.mongodb.ReadPreference
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoAuditing
@EnableMongoRepositories(basePackageClasses = [ThreeDollarDomainMongoRoot::class])
@Configuration
class MongoConfig(
    private val mongoDbFactory: MongoDatabaseFactory,
    private val mongoMappingContext: MongoMappingContext
) {

    @Bean
    fun mongoTemplate(mongoDatabaseFactory: MongoDatabaseFactory): MongoTemplate {
        val mongoTemp = MongoTemplate(mongoDatabaseFactory, mappingMongoConverter())
        mongoTemp.setReadPreference(ReadPreference.secondaryPreferred())
        return mongoTemp
    }

    @Bean
    fun mappingMongoConverter(): MappingMongoConverter {
        val dbRefResolver = DefaultDbRefResolver(mongoDbFactory)
        val converter = MappingMongoConverter(dbRefResolver, mongoMappingContext)
        converter.setTypeMapper(DefaultMongoTypeMapper(null))
        return converter
    }

}

