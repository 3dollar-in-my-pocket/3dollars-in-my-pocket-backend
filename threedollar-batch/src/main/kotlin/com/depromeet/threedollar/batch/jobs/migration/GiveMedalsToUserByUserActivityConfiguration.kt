package com.depromeet.threedollar.batch.jobs.migration

import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.common.type.UserMenuCategoryType
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionType
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.collection.MedalObtainCollection
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreDeleteRequestRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType
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
 * 마이그레이션을 위해 기존의 유저의 활동 이력을 통해 메달을 제공하는 배치
 */
private const val JOB_NAME = "giveMedalsToUserByUserActivityJob"
private const val CHUNK_SIZE = 4

@Configuration
class GiveMedalsToUserByUserActivity(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory,
    private val medalRepository: MedalRepository,
    private val storeRepository: StoreRepository,
    private val storeDeleteRequestRepository: StoreDeleteRequestRepository,
    private val visitHistoryRepository: VisitHistoryRepository,
    private val reviewRepository: ReviewRepository,
) {

    @Bean(name = [JOB_NAME])
    fun giveMedalsToUserByUserActivityJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .incrementer(UniqueRunIdIncrementer())
            .start(giveMedalsToUserByUserActivityStep())
            .build()
    }

    @Bean(name = [JOB_NAME + "_step"])
    fun giveMedalsToUserByUserActivityStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_step"]
            .chunk<User, User>(CHUNK_SIZE)
            .reader(giveMedalsToUserByUserActivityReader())
            .processor(giveMedalsToUserByUserActivityProcessor())
            .writer(giveMedalsToUserByUserActivityWriter())
            .build()
    }

    @Bean(name = [JOB_NAME + "_reader"])
    fun giveMedalsToUserByUserActivityReader(): JpaCursorItemReader<User> {
        return JpaCursorItemReaderBuilder<User>()
            .name(JOB_NAME + "_reader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT u FROM User u left join fetch u.userMedals")
            .build()
    }

    @Bean(name = [JOB_NAME + "_processor"])
    fun giveMedalsToUserByUserActivityProcessor(): ItemProcessor<User, User> {
        return ItemProcessor<User, User> { user ->
            val medalCanBeObtainedByAddStore = MedalObtainCollection.of(
                medalRepository.findAllByConditionType(MedalAcquisitionConditionType.ADD_STORE),
                MedalAcquisitionConditionType.ADD_STORE,
                user
            )
            if (medalCanBeObtainedByAddStore.hasMoreMedalsCanBeObtained()) {
                user.addMedals(
                    medalCanBeObtainedByAddStore.getSatisfyMedalsCanBeObtained(
                        storeRepository.countByUserId(user.id)
                    )
                )
            }

            val medalsCanBeObtainedByDeleteStore = MedalObtainCollection.of(
                medalRepository.findAllByConditionType(MedalAcquisitionConditionType.DELETE_STORE),
                MedalAcquisitionConditionType.DELETE_STORE,
                user
            )
            if (medalsCanBeObtainedByDeleteStore.hasMoreMedalsCanBeObtained()) {
                user.addMedals(
                    medalsCanBeObtainedByDeleteStore.getSatisfyMedalsCanBeObtained(
                        storeDeleteRequestRepository.countsByUserId(user.id)
                    )
                )
            }

            val medalsCanBeObtainedByAddReview = MedalObtainCollection.of(
                medalRepository.findAllByConditionType(MedalAcquisitionConditionType.ADD_REVIEW),
                MedalAcquisitionConditionType.ADD_REVIEW,
                user
            )
            if (medalsCanBeObtainedByAddReview.hasMoreMedalsCanBeObtained()) {
                user.addMedals(
                    medalsCanBeObtainedByAddReview.getSatisfyMedalsCanBeObtained(
                        reviewRepository.countByUserId(user.id)
                    )
                )
            }

            val medalsCanBeObtainedByVisitStore = MedalObtainCollection.of(
                medalRepository.findAllByConditionType(MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE),
                MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE,
                user
            )
            if (medalsCanBeObtainedByVisitStore.hasMoreMedalsCanBeObtained()) {
                user.addMedals(
                    medalsCanBeObtainedByVisitStore.getSatisfyMedalsCanBeObtained(
                        visitHistoryRepository.countByUserIdAndMenuCategoryType(
                            user.id,
                            UserMenuCategoryType.BUNGEOPPANG
                        )
                    )
                )
            }

            val medalsCanBeObtainedByVisitNotExistsStore = MedalObtainCollection.of(
                medalRepository.findAllByConditionType(MedalAcquisitionConditionType.VISIT_NOT_EXISTS_STORE),
                MedalAcquisitionConditionType.VISIT_NOT_EXISTS_STORE,
                user
            )
            if (medalsCanBeObtainedByVisitNotExistsStore.hasMoreMedalsCanBeObtained()) {
                user.addMedals(
                    medalsCanBeObtainedByVisitNotExistsStore.getSatisfyMedalsCanBeObtained(
                        visitHistoryRepository.countByUserIdAndVisitType(
                            user.id,
                            VisitType.NOT_EXISTS
                        )
                    )
                )
            }
            user
        }
    }

    @Bean(name = [JOB_NAME + "_writer"])
    fun giveMedalsToUserByUserActivityWriter(): JpaItemWriter<User> {
        val itemWriter = JpaItemWriter<User>()
        itemWriter.setEntityManagerFactory(entityManagerFactory)
        return itemWriter
    }

}
