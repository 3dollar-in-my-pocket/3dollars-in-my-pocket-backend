package com.depromeet.threedollar.batch.jobs.migration

import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.domain.collection.medal.MedalObtainCollection
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionType
import com.depromeet.threedollar.domain.domain.medal.MedalRepository
import com.depromeet.threedollar.domain.domain.review.ReviewRepository
import com.depromeet.threedollar.domain.domain.store.MenuCategoryType
import com.depromeet.threedollar.domain.domain.store.StoreRepository
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestRepository
import com.depromeet.threedollar.domain.domain.user.User
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository
import com.depromeet.threedollar.domain.domain.visit.VisitType
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

@Configuration
class GiveMedalsToUserByUserActivity(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory,
    private val medalRepository: MedalRepository,
    private val storeRepository: StoreRepository,
    private val storeDeleteRequestRepository: StoreDeleteRequestRepository,
    private val visitHistoryRepository: VisitHistoryRepository,
    private val reviewRepository: ReviewRepository
) {

    @Bean
    fun giveMedalsToUserByUserActivityJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .incrementer(UniqueRunIdIncrementer())
            .start(giveMedalsToUserByUserActivityStep())
            .build()
    }

    @Bean
    fun giveMedalsToUserByUserActivityStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_step"]
            .chunk<User, User>(CHUNK_SIZE)
            .reader(giveMedalsToUserByUserActivityReader())
            .processor(giveMedalsToUserByUserActivityProcessor())
            .writer(giveMedalsToUserByUserActivityWriter())
            .build()
    }

    @Bean
    fun giveMedalsToUserByUserActivityReader(): JpaCursorItemReader<User> {
        return JpaCursorItemReaderBuilder<User>()
            .name(JOB_NAME + "_reader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT u FROM User u left join fetch u.userMedals")
            .build()
    }

    @Bean
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
                        storeRepository.findCountsByUserId(user.id)
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
                        storeDeleteRequestRepository.findCountsByUserId(user.id)
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
                        reviewRepository.findCountsByUserId(user.id)
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
                        visitHistoryRepository.findCountsByUserIdAndCategory(
                            user.id,
                            MenuCategoryType.BUNGEOPPANG
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
                        visitHistoryRepository.findCountsByUserIdAndVisitType(
                            user.id,
                            VisitType.NOT_EXISTS
                        )
                    )
                )
            }
            user
        }
    }

    @Bean
    fun giveMedalsToUserByUserActivityWriter(): JpaItemWriter<User> {
        val itemWriter = JpaItemWriter<User>()
        itemWriter.setEntityManagerFactory(entityManagerFactory)
        return itemWriter
    }

    companion object {
        private const val JOB_NAME = "giveMedalsToUserByUserActivityJob"
        private const val CHUNK_SIZE = 4
    }

}
