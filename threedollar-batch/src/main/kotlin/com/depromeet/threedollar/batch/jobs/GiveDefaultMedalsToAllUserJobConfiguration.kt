package com.depromeet.threedollar.batch.jobs

import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.domain.domain.medal.Medal
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionType
import com.depromeet.threedollar.domain.domain.medal.MedalRepository
import com.depromeet.threedollar.domain.domain.user.User
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.stream.Collectors
import javax.persistence.EntityManagerFactory

@Configuration
class GiveDefaultMedalsToAllUserJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory,
    private val medalRepository: MedalRepository
) {

    @Bean
    fun giveDefaultMedalsToUsersJob(): Job {
        return jobBuilderFactory.get("giveDefaultMedalsToUsersJob")
            .incrementer(UniqueRunIdIncrementer())
            .start(giveDefaultMedalsToUsersJobStep())
            .build()
    }

    @Bean
    fun giveDefaultMedalsToUsersJobStep(): Step {
        return stepBuilderFactory.get("giveDefaultMedalsToUsersJobStep")
            .chunk<User, User>(CHUNK_SIZE)
            .reader(userPagingItemReader())
            .processor(giveDefaultMedalsItemProcessor())
            .writer(userPagingItemWriter())
            .build()
    }

    @Bean
    fun userPagingItemReader(): JpaPagingItemReader<User> {
        return JpaPagingItemReaderBuilder<User>()
            .name("userPagingItemReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(CHUNK_SIZE)
            .queryString("SELECT u FROM User u")
            .build()
    }

    @Bean
    fun giveDefaultMedalsItemProcessor(): ItemProcessor<User, User> {
        return ItemProcessor<User, User> { user ->
            val medalsUserNotHeld = getMedalsUserNotHeldByCondition(user)
            if (hasMoreMedalsCanBeObtained(medalsUserNotHeld)) {
                user.addMedals(medalsUserNotHeld)
                user.updateActivatedMedal(medalsUserNotHeld[0].id)
            }
            user
        }
    }

    private fun getMedalsUserNotHeldByCondition(user: User): List<Medal> {
        val medals = medalRepository.findAllByConditionType(MedalAcquisitionConditionType.NO_CONDITION)
        val medalsUserObtains = user.userMedals.stream()
            .map { userMedal -> userMedal.medalId }
            .collect(Collectors.toList())
        return medals.stream()
            .filter { medal: Medal -> !medalsUserObtains.contains(medal.id) }
            .collect(Collectors.toList())
    }

    private fun hasMoreMedalsCanBeObtained(medals: List<Medal>): Boolean {
        return medals.isNotEmpty()
    }

    @Bean
    fun userPagingItemWriter(): JpaItemWriter<User> {
        val itemWriter = JpaItemWriter<User>()
        itemWriter.setEntityManagerFactory(entityManagerFactory)
        return itemWriter
    }

    companion object {
        private const val CHUNK_SIZE = 1000
    }

}
