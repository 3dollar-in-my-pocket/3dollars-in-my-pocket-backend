package com.depromeet.threedollar.batch.jobs.migration

import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * S3 버킷을 마이그레이션 하는 배치.
 */
private const val JOB_NAME = "migrationS3BucketJob"

@Configuration
class S3BucketMigrationConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val storeImageRepository: StoreImageRepository,
) {

    @Bean(name = [JOB_NAME])
    fun migrationS3BucketJob(): Job {
        return jobBuilderFactory.get(JOB_NAME)
            .incrementer(UniqueRunIdIncrementer())
            .start(migrationS3BucketStep("beforePrefix", "afterPrefix"))
            .build()
    }

    @Bean(name = [JOB_NAME + "_step"])
    @JobScope
    fun migrationS3BucketStep(
        @Value("#{jobParameters[beforePrefix]}") beforePrefix: String,
        @Value("#{jobParameters[afterPrefix]}") afterPrefix: String,
    ): Step {
        return stepBuilderFactory[JOB_NAME + "_step"]
            .tasklet { _, _ ->
                val storeImages = storeImageRepository.findAll().asSequence()
                    .filter { storeImage -> storeImage.url.startsWith(beforePrefix) }
                    .map { storeImage ->
                        val url = storeImage.url.split(beforePrefix)[1]
                        storeImage.updateUrl(afterPrefix + url)
                        storeImage
                    }
                    .toList()
                storeImageRepository.saveAll(storeImages)
                RepeatStatus.FINISHED
            }
            .build()
    }

}
