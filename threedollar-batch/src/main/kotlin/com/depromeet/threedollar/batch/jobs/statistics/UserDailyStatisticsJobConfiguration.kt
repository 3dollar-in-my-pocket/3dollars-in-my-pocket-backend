package com.depromeet.threedollar.batch.jobs.statistics

import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.batch.jobs.statistics.UserDailyStatisticsMessageFormat.*
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalRepository
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuRepository
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository
import com.depromeet.threedollar.domain.rds.user.domain.user.UserRepository
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryRepository
import com.depromeet.threedollar.external.client.slack.SlackWebhookApiClient
import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

/**
 * 알일 통계 정보를 알려주는 슬랙 봇 관련 배치 잡
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

    private val slackNotificationApiClient: SlackWebhookApiClient
) {

    @Bean
    fun dailyStaticsJob(): Job {
        return jobBuilderFactory[DAILY_STATISTICS_JOB]
            .incrementer(UniqueRunIdIncrementer())
            .start(notificationStatisticsInfoStep())
            .next(notificationUsersStatisticsStep())
            .next(notificationStoresStatisticsStep())
            .next(notificationDeletedStoresStatisticsStep())
            .next(notificationMenusStatisticsStep())
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
                    COUNTS_USER,
                    userRepository.countAllUsers(),
                    userRepository.countUsersBetweenDate(yesterday, yesterday),
                    userRepository.countUsersBetweenDate(yesterday.minusWeeks(1), yesterday)
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
                    COUNTS_STORE,
                    storeRepository.countAllActiveStores(),
                    storeRepository.countActiveStoresBetweenDate(yesterday, yesterday),
                    storeRepository.countActiveStoresBetweenDate(yesterday.minusWeeks(1), yesterday)
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
                val todayDeletedCounts = storeRepository.countDeletedStoresBetweenDate(yesterday, yesterday)
                slackNotificationApiClient.postStatisticsMessage(
                    PostSlackMessageRequest.of(COUNTS_DELETED_STORE.messageFormat.format(todayDeletedCounts))
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
    fun notificationReviewsStatisticsStep(): Step {
        return stepBuilderFactory[DAILY_STATISTICS_JOB + "_review_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    COUNTS_REVIEW,
                    reviewRepository.countActiveReviews(),
                    reviewRepository.countActiveReviewsBetweenDate(yesterday, yesterday),
                    reviewRepository.countActiveReviewsBetweenDate(yesterday.minusWeeks(1), yesterday)
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
                    COUNTS_VISIT_HISTORY,
                    visitHistoryRepository.countAllVisitHistoriese(),
                    visitHistoryRepository.countVisitHistoriesBetweenDate(yesterday, yesterday),
                    visitHistoryRepository.countVisitHistoriesBetweenDate(yesterday.minusWeeks(1), yesterday)
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
        weekendCounts: Long
    ) {
        slackNotificationApiClient.postStatisticsMessage(
            PostSlackMessageRequest.of(messageType.messageFormat.format(totalCounts, todayCounts, weekendCounts))
        )
    }

}
