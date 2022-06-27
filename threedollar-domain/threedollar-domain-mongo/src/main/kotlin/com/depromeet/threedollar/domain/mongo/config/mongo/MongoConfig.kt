package com.depromeet.threedollar.domain.mongo.config.mongo

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoDatabaseUtils
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import com.depromeet.threedollar.domain.mongo.ThreeDollarDomainMongoRoot
import com.mongodb.ReadPreference

private const val PRIMARY_READ_MONGO_TEMPLATE = "primaryReadMongoTemplate"
private const val SECONDARY_PREFERRED_READ_MONGO_TEMPLATE = "secondaryPreferredReadMongoTemplate"
private const val ROUTING_MONGO_TEMPLATE = "routingMongoTemplate"

@EnableMongoAuditing
@EnableMongoRepositories(
    basePackageClasses = [ThreeDollarDomainMongoRoot::class],
    mongoTemplateRef = ROUTING_MONGO_TEMPLATE
)
@Configuration
class MongoConfig(
    private val mongoDatabaseFactory: MongoDatabaseFactory,
    private val mongoMappingContext: MongoMappingContext,
) {

    @Primary
    @Bean(name = [ROUTING_MONGO_TEMPLATE])
    fun routingMongoTemplate(
        @Qualifier(PRIMARY_READ_MONGO_TEMPLATE) primaryReadMongoTemplate: MongoTemplate,
        @Qualifier(SECONDARY_PREFERRED_READ_MONGO_TEMPLATE) secondaryPreferredReadMongoTemplate: MongoTemplate,
    ): MongoTemplate {
        if (MongoDatabaseUtils.isTransactionActive(mongoDatabaseFactory)) {
            return primaryReadMongoTemplate
        }
        return secondaryPreferredReadMongoTemplate
    }

    @Bean(name = [PRIMARY_READ_MONGO_TEMPLATE])
    fun primaryMongoTemplate(): MongoTemplate {
        val mongoTemplate = MongoTemplate(mongoDatabaseFactory, mappingMongoConverter())
        mongoTemplate.setReadPreference(ReadPreference.primary())
        return mongoTemplate
    }

    @Bean(name = [SECONDARY_PREFERRED_READ_MONGO_TEMPLATE])
    fun secondaryPreferenceMongoTemplate(): MongoTemplate {
        val mongoTemplate = MongoTemplate(mongoDatabaseFactory, mappingMongoConverter())
        mongoTemplate.setReadPreference(ReadPreference.secondaryPreferred())
        return mongoTemplate
    }

    @Bean
    fun mappingMongoConverter(): MappingMongoConverter {
        val dbRefResolver = DefaultDbRefResolver(mongoDatabaseFactory)
        val converter = MappingMongoConverter(dbRefResolver, mongoMappingContext)
        converter.setTypeMapper(DefaultMongoTypeMapper(null))
        return converter
    }

    @Profile("local-docker", "dev", "staging", "prod")
    @Bean
    fun mongoTransactionManager(): MongoTransactionManager {
        return MongoTransactionManager(mongoDatabaseFactory)
    }

}

