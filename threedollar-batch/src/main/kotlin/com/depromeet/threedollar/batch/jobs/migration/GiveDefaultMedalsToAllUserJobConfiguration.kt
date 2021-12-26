package com.depromeet.threedollar.batch.jobs.migration

import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.domain.collection.medal.MedalObtainCollection
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionType
import com.depromeet.threedollar.domain.domain.medal.MedalRepository
import com.depromeet.threedollar.domain.domain.user.User
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
import javax.persistence.EntityManagerFactory

/**
 * 마이그레이션을 위해 모든 유저에게 기본 획득 메달을 제공하는 배치
 */
@Configuration
class GiveDefaultMedalsToAllUserJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory,
    private val medalRepository: MedalRepository
) {

    @Bean
    fun giveDefaultMedalsToUsersJob(): Job {
        return jobBuilderFactory.get(JOB_NAME)
            .incrementer(UniqueRunIdIncrementer())
            .start(giveDefaultMedalsToUsersJobStep())
            .build()
    }

    @Bean
    fun giveDefaultMedalsToUsersJobStep(): Step {
        return stepBuilderFactory.get(JOB_NAME + "_step")
            .chunk<User, User>(CHUNK_SIZE)
            .reader(giveDefaultMedalsToUserJobReader())
            .processor(giveDefaultMedalsToUserJobProcessor())
            .writer(giveDefaultMedalsToUserJobWriter())
            .build()
    }

    @Bean
    fun giveDefaultMedalsToUserJobReader(): JpaCursorItemReader<User> {
        return JpaCursorItemReaderBuilder<User>()
            .name(JOB_NAME + "_reader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT u FROM User u")
            .build()
    }

    @Bean
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

    @Bean
    fun giveDefaultMedalsToUserJobWriter(): JpaItemWriter<User> {
        val itemWriter = JpaItemWriter<User>()
        itemWriter.setEntityManagerFactory(entityManagerFactory)
        return itemWriter
    }

    companion object {
        private const val JOB_NAME = "giveDefaultMedalsToUsersJob"
        private const val CHUNK_SIZE = 1000
    }

}