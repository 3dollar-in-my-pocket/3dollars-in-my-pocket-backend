package com.depromeet.threedollar.batch.jobs.statistics

import java.time.LocalDate
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.COUNTS_ACTIVE_MEDAL
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.COUNTS_ACTIVE_MEDALS
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.COUNTS_DELETED_STORE
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.COUNTS_DELETE_STORE_REQUEST
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.COUNTS_MEDAL
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.COUNTS_MEDALS
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.COUNTS_MENU
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.COUNTS_MENUS
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.COUNTS_REVIEW
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.COUNTS_STORE
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.COUNTS_USER
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.COUNTS_VISIT_HISTORY
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.DAILY_STATISTICS_INFO
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalRepository
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuRepository
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreDeleteRequestRepository
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository
import com.depromeet.threedollar.domain.rds.user.domain.user.UserRepository
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryRepository
import com.depromeet.threedollar.external.client.slack.SlackWebhookApiClient
import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest

/**
 * 유저 서비스 일일 통계 배치 잡
 */

private const val DAILY_STATISTICS_JOB = "dailyStaticsJob"

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
    private val storeDeleteRequestRepository: StoreDeleteRequestRepository,

    private val slackNotificationApiClient: SlackWebhookApiClient,
) {

    @Bean
    fun dailyStaticsJob(): Job {
        return jobBuilderFactory[DAILY_STATISTICS_JOB]
            .incrementer(UniqueRunIdIncrementer())
            .start(notificationStatisticsInfoStep())
            .next(notificationUsersStatisticsStep())
            .next(notificationStoresStatisticsStep())
            .next(notificationMenusStatisticsStep())
            .next(notificationDeletedStoresStatisticsStep())
            .next(notificationStoreDeleteRequestsStatisticsStep())
            .next(notificationReviewsStatisticsStep())
            .next(notificationVisitHistoriesStatisticsStep())
            .next(notificationUserMedalsStatisticsStep())
            .next(notificationActiveUserMedalsStatisticsStep())
            .build()
    }

    @Bean
    fun notificationStatisticsInfoStep(): Step {
        return stepBuilderFactory[DAILY_STATISTICS_JOB + "_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                slackNotificationApiClient.postStatisticsMessage(
                    PostSlackMessageRequest.of(DAILY_STATISTICS_INFO.messageFormat.format(yesterday))
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun notificationUsersStatisticsStep(): Step {
        return stepBuilderFactory[DAILY_STATISTICS_JOB + "_user_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_USER,
                    totalCounts = userRepository.countAllUsers(),
                    todayCounts = userRepository.countUsersBetweenDate(yesterday, yesterday),
                    weekendCounts = userRepository.countUsersBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun notificationStoresStatisticsStep(): Step {
        return stepBuilderFactory[DAILY_STATISTICS_JOB + "_store_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_STORE,
                    totalCounts = storeRepository.countAllActiveStores(),
                    todayCounts = storeRepository.countActiveStoresBetweenDate(yesterday, yesterday),
                    weekendCounts = storeRepository.countActiveStoresBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun notificationMenusStatisticsStep(): Step {
        return stepBuilderFactory[DAILY_STATISTICS_JOB + "_store_menu_step"]
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

    @Bean
    fun notificationDeletedStoresStatisticsStep(): Step {
        return stepBuilderFactory[DAILY_STATISTICS_JOB + "_deletedStore_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_DELETED_STORE,
                    totalCounts = storeRepository.countAllDeletedStores(),
                    todayCounts = storeRepository.countDeletedStoresBetweenDate(yesterday, yesterday),
                    weekendCounts = storeRepository.countDeletedStoresBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun notificationStoreDeleteRequestsStatisticsStep(): Step {
        return stepBuilderFactory[DAILY_STATISTICS_JOB + "_storeDeleteRequest_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_DELETE_STORE_REQUEST,
                    totalCounts = storeDeleteRequestRepository.count(),
                    todayCounts = storeDeleteRequestRepository.countBetweenDate(yesterday, yesterday),
                    weekendCounts = storeDeleteRequestRepository.countBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun notificationReviewsStatisticsStep(): Step {
        return stepBuilderFactory[DAILY_STATISTICS_JOB + "_review_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_REVIEW,
                    totalCounts = reviewRepository.countActiveReviews(),
                    todayCounts = reviewRepository.countActiveReviewsBetweenDate(yesterday, yesterday),
                    weekendCounts = reviewRepository.countActiveReviewsBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun notificationVisitHistoriesStatisticsStep(): Step {
        return stepBuilderFactory[DAILY_STATISTICS_JOB + "_visitHistory_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = COUNTS_VISIT_HISTORY,
                    totalCounts = visitHistoryRepository.countAllVisitHistoriese(),
                    todayCounts = visitHistoryRepository.countVisitHistoriesBetweenDate(yesterday, yesterday),
                    weekendCounts = visitHistoryRepository.countVisitHistoriesBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun notificationUserMedalsStatisticsStep(): Step {
        return stepBuilderFactory[DAILY_STATISTICS_JOB + "_userMedal_step"]
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

    @Bean
    fun notificationActiveUserMedalsStatisticsStep(): Step {
        return stepBuilderFactory[DAILY_STATISTICS_JOB + "_active_userMedal_step"]
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
    ) {
        slackNotificationApiClient.postStatisticsMessage(
            PostSlackMessageRequest.of(messageType.messageFormat.format(totalCounts, todayCounts, weekendCounts))
        )
    }

}
