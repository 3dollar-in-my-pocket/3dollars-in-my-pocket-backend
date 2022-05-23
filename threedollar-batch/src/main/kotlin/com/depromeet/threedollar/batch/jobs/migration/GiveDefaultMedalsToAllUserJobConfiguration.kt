package com.depromeet.threedollar.batch.jobs.migration

import javax.persistence.EntityManagerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaCursorItemReader
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.domain.rds.vendor.collection.medal.MedalObtainCollection
import com.depromeet.threedollar.domain.rds.vendor.domain.medal.MedalAcquisitionConditionType
import com.depromeet.threedollar.domain.rds.vendor.domain.medal.MedalRepository
import com.depromeet.threedollar.domain.rds.vendor.domain.user.User

/**
 * 마이그레이션을 위해 모든 유저에게 기본 획득 메달을 제공하는 배치
 */
private const val JOB_NAME = "giveDefaultMedalsToUsersJob"
private const val CHUNK_SIZE = 1000

@Configuration
class GiveDefaultMedalsToAllUserJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory,
    private val medalRepository: MedalRepository,
) {

    @Bean(name = [JOB_NAME])
    fun giveDefaultMedalsToUsersJob(): Job {
        return jobBuilderFactory.get(JOB_NAME)
            .incrementer(UniqueRunIdIncrementer())
            .start(giveDefaultMedalsToUsersJobStep())
            .build()
    }

    @Bean(name = [JOB_NAME + "_step"])
    fun giveDefaultMedalsToUsersJobStep(): Step {
        return stepBuilderFactory.get(JOB_NAME + "_step")
            .chunk<User, User>(CHUNK_SIZE)
            .reader(giveDefaultMedalsToUserJobReader())
            .processor(giveDefaultMedalsToUserJobProcessor())
            .writer(giveDefaultMedalsToUserJobWriter())
            .build()
    }

    @Bean(name = [JOB_NAME + "_reader"])
    fun giveDefaultMedalsToUserJobReader(): JpaCursorItemReader<User> {
        return JpaCursorItemReaderBuilder<User>()
            .name(JOB_NAME + "_reader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT u FROM User u")
            .build()
    }

    @Bean(name = [JOB_NAME + "_processor"])
    fun giveDefaultMedalsToUserJobProcessor(): ItemProcessor<User, User> {
        return ItemProcessor<User, User> { user ->
            val medalObtainCollection = MedalObtainCollection.of(
                medalRepository.findAllByConditionType(MedalAcquisitionConditionType.NO_CONDITION),
                MedalAcquisitionConditionType.NO_CONDITION,
                user
            )
            if (medalObtainCollection.hasMoreMedalsCanBeObtained()) {
                val medalsSatisfyCondition = medalObtainCollection.satisfyMedalsCanBeObtainedByDefault
                user.addMedals(medalsSatisfyCondition)
                user.updateActivatedMedal(medalsSatisfyCondition[0].id)
            }
            user
        }
    }

    @Bean(name = [JOB_NAME + "_writer"])
    fun giveDefaultMedalsToUserJobWriter(): JpaItemWriter<User> {
        val itemWriter = JpaItemWriter<User>()
        itemWriter.setEntityManagerFactory(entityManagerFactory)
        return itemWriter
    }

}
