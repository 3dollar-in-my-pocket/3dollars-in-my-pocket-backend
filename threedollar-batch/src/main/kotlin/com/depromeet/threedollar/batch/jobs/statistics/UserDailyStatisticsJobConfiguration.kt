package com.depromeet.threedollar.batch.jobs.statistics

import com.depromeet.threedollar.batch.config.JobExceptionListener
import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_ACTIVE_MEDAL
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_ACTIVE_MEDALS
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_DELETED_STORE
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_DELETE_STORE_REQUEST
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_MEDAL
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_MEDALS
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_MENU
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_MENUS
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_REVIEW
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_STORE
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_STORE_IMAGE
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_USER
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.COUNTS_VISIT_HISTORY
import com.depromeet.threedollar.batch.jobs.statistics.templates.UserDailyStatisticsMessageFormat.DAILY_STATISTICS_INFO
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.store.MenuRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreDeleteRequestRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryRepository
import com.depromeet.threedollar.infrastructure.external.client.slack.SlackWebhookApiClient
import com.depromeet.threedollar.infrastructure.external.client.slack.dto.request.PostSlackMessageRequest
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.listener.JobListenerFactoryBean
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

/**
 * 유저 서비스 일일 통계 배치 잡
 */

private const val JOB_NAME = "dailyStaticsJob"

@Configuration
class DailyStatisticsJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,

    private val userRepository: UserRepository,
    private val menuRepository: MenuRepository,
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository,
    private val visitHistoryRepository: VisitHistoryRepository,
    private val medalRepository: MedalRepository,
    private val storeImageRepository: StoreImageRepository,
    private val storeDeleteRequestRepository: StoreDeleteRequestRepository,

    private val slackNotificationApiClient: SlackWebhookApiClient,
) {

    @Bean(name = [JOB_NAME])
    fun dailyStaticsJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .incrementer(UniqueRunIdIncrementer())
            .listener(JobListenerFactoryBean.getListener(JobExceptionListener(slackNotificationApiClient)))
            .start(notificationStatisticsInfoStep())
            .next(notificationUsersStatisticsStep())
            .next(notificationStoresStatisticsStep())
            .next(notificationMenusStatisticsStep())
            .next(notificationStoreImageStatisticsStep())
            .next(notificationDeletedStoresStatisticsStep())
            .next(notificationStoreDeleteRequestsStatisticsStep())
            .next(notificationReviewsStatisticsStep())
            .next(notificationVisitHistoriesStatisticsStep())
            .next(notificationUserMedalsStatisticsStep())
            .next(notificationActiveUserMedalsStatisticsStep())
            .build()
    }

    @Bean(name = [JOB_NAME + "_step"])
    fun notificationStatisticsInfoStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                slackNotificationApiClient.postStatisticsMessage(
                    PostSlackMessageRequest.of(DAILY_STATISTICS_INFO.messageFormat.format(yesterday))
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_user_step"])
    fun notificationUsersStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_user_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_USER,
                    totalCounts = userRepository.countAllUsers(),
                    todayCounts = userRepository.countUsersBetweenDate(yesterday, yesterday),
                    weekendCounts = userRepository.countUsersBetweenDate(yesterday.minusWeeks(1), yesterday),
                    monthlyCounts = userRepository.countUsersBetweenDate(yesterday.minusMonths(1), yesterday),
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_store_step"])
    fun notificationStoresStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_store_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_STORE,
                    totalCounts = storeRepository.countAllActiveStores(),
                    todayCounts = storeRepository.countActiveStoresBetweenDate(yesterday, yesterday),
                    weekendCounts = storeRepository.countActiveStoresBetweenDate(yesterday.minusWeeks(1), yesterday),
                    monthlyCounts = storeRepository.countActiveStoresBetweenDate(yesterday.minusMonths(1), yesterday),
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_store_menu_step"])
    fun notificationMenusStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_store_menu_step"]
            .tasklet { _, _ ->
                val result = menuRepository.countMenus()
                val message = result.asSequence()
                    .sortedByDescending { it.counts }
                    .joinToString(separator = "\n") {
                        COUNTS_MENU.messageFormat.format(it.category.categoryName, it.counts)
                    }

                slackNotificationApiClient.postStatisticsMessage(
                    PostSlackMessageRequest.of(COUNTS_MENUS.messageFormat.format(message))
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_storeImage_step"])
    fun notificationStoreImageStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_storeImage_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_STORE_IMAGE,
                    totalCounts = storeImageRepository.count(),
                    todayCounts = storeImageRepository.countBetweenDate(yesterday, yesterday),
                    weekendCounts = storeImageRepository.countBetweenDate(yesterday.minusWeeks(1), yesterday),
                    monthlyCounts = storeImageRepository.countBetweenDate(yesterday.minusMonths(1), yesterday),
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_deletedStore_step"])
    fun notificationDeletedStoresStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_deletedStore_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_DELETED_STORE,
                    totalCounts = storeRepository.countAllDeletedStores(),
                    todayCounts = storeRepository.countDeletedStoresBetweenDate(yesterday, yesterday),
                    weekendCounts = storeRepository.countDeletedStoresBetweenDate(yesterday.minusWeeks(1), yesterday),
                    monthlyCounts = storeRepository.countDeletedStoresBetweenDate(yesterday.minusMonths(1), yesterday),
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_storeDeleteRequest_step"])
    fun notificationStoreDeleteRequestsStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_storeDeleteRequest_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_DELETE_STORE_REQUEST,
                    totalCounts = storeDeleteRequestRepository.count(),
                    todayCounts = storeDeleteRequestRepository.countBetweenDate(yesterday, yesterday),
                    weekendCounts = storeDeleteRequestRepository.countBetweenDate(yesterday.minusWeeks(1), yesterday),
                    monthlyCounts = storeDeleteRequestRepository.countBetweenDate(yesterday.minusMonths(1), yesterday),
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_review_step"])
    fun notificationReviewsStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_review_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_REVIEW,
                    totalCounts = reviewRepository.countActiveReviews(),
                    todayCounts = reviewRepository.countActiveReviewsBetweenDate(yesterday, yesterday),
                    weekendCounts = reviewRepository.countActiveReviewsBetweenDate(yesterday.minusWeeks(1), yesterday),
                    monthlyCounts = reviewRepository.countActiveReviewsBetweenDate(yesterday.minusMonths(1), yesterday),
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_visitHistory_step"])
    fun notificationVisitHistoriesStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_visitHistory_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_VISIT_HISTORY,
                    totalCounts = visitHistoryRepository.countAllVisitHistories(),
                    todayCounts = visitHistoryRepository.countVisitHistoriesBetweenDate(yesterday, yesterday),
                    weekendCounts = visitHistoryRepository.countVisitHistoriesBetweenDate(yesterday.minusWeeks(1), yesterday),
                    monthlyCounts = visitHistoryRepository.countVisitHistoriesBetweenDate(yesterday.minusMonths(1), yesterday),
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_userMedal_step"])
    fun notificationUserMedalsStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_userMedal_step"]
            .tasklet { _, _ ->
                val result = medalRepository.countsUserMedalGroupByMedalType()
                val message = result.asSequence()
                    .sortedByDescending { it.counts }
                    .joinToString(separator = "\n") {
                        COUNTS_MEDAL.messageFormat.format(it.medalName, it.counts)
                    }
                slackNotificationApiClient.postStatisticsMessage(
                    PostSlackMessageRequest.of(COUNTS_MEDALS.messageFormat.format(message))
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_active_userMedal_step"])
    fun notificationActiveUserMedalsStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_active_userMedal_step"]
            .tasklet { _, _ ->
                val result = medalRepository.countActiveMedalsGroupByMedalType()
                val message = result.asSequence()
                    .sortedByDescending { it.counts }
                    .joinToString(separator = "\n") {
                        COUNTS_ACTIVE_MEDAL.messageFormat.format(it.medalName, it.counts)
                    }
                slackNotificationApiClient.postStatisticsMessage(
                    PostSlackMessageRequest.of(COUNTS_ACTIVE_MEDALS.messageFormat.format(message))
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    private fun sendStatisticsNotification(
        messageType: UserDailyStatisticsMessageFormat,
        totalCounts: Long,
        todayCounts: Long,
        weekendCounts: Long,
        monthlyCounts: Long,
    ) {
        slackNotificationApiClient.postStatisticsMessage(
            PostSlackMessageRequest.of(messageType.messageFormat.format(totalCounts, todayCounts, weekendCounts, monthlyCounts))
        )
    }

}
