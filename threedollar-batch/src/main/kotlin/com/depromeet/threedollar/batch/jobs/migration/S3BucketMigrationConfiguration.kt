package com.depromeet.threedollar.batch.jobs.migration

import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.domain.domain.store.StoreImageRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3BucketMigrationConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val storeImageRepository: StoreImageRepository
) {

    @Bean
    fun migrationS3BucketJob(): Job {
        return jobBuilderFactory.get("migrationS3BucketJob")
            .incrementer(UniqueRunIdIncrementer())
            .start(migrationS3BucketStep("beforePrefix", "afterPrefix"))
            .build()
    }

    @Bean
    @JobScope
    fun migrationS3BucketStep(
        @Value("#{jobParameters[beforePrefix]}") beforePrefix: String,
        @Value("#{jobParameters[afterPrefix]}") afterPrefix: String
    ): Step {
        return stepBuilderFactory["migrationS3BucketStep"]
            .tasklet { _, _ ->
                val storeImages = storeImageRepository.findAll().asSequence()
                    .filter { it.url.startsWith(beforePrefix) }
                    .map {
                        val url = it.url.split(beforePrefix)[1]
                        it.updateUrl(afterPrefix + url)
                        it
                    }.toList()
                storeImageRepository.saveAll(storeImages)
                RepeatStatus.FINISHED
            }
            .build()
    }

}