package com.depromeet.threedollar.batch.jobs.statistics

import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.batch.jobs.statistics.StatisticsMessageFormat.*
import com.depromeet.threedollar.domain.domain.medal.MedalRepository
import com.depromeet.threedollar.domain.domain.store.MenuRepository
import com.depromeet.threedollar.domain.domain.review.ReviewRepository
import com.depromeet.threedollar.domain.domain.store.StoreRepository
import com.depromeet.threedollar.domain.domain.user.UserRepository
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository
import com.depromeet.threedollar.external.client.slack.SlackApiClient
import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

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

    private val slackApiClient: SlackApiClient
) {

    @Bean
    fun dailyStaticsJob(): Job {
        return jobBuilderFactory.get("dailyStaticsJob")
            .incrementer(UniqueRunIdIncrementer())
            .start(notificationStatisticsInfo())
            .next(countsNewUserStep())
            .next(countsNewStoresStep())
            .next(countsDeletedStoresStep())
            .next(countsActiveMenuStep())
            .next(countsNewReviewsStep())
            .next(countsNewVisitHistoriesStep())
            .next(countsUserMedalGroupByMedal())
            .build()
    }


    @Bean
    fun notificationStatisticsInfo(): Step {
        return stepBuilderFactory[" notificationStatisticsInf"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                slackApiClient.postMessage(
                    PostSlackMessageRequest.of(DAILY_STATISTICS_INFO.messageFormat.format(yesterday))
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun countsNewUserStep(): Step {
        return stepBuilderFactory["countsNewUserStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    COUNTS_USER,
                    userRepository.findUsersCount(),
                    userRepository.findUsersCountBetweenDate(yesterday, yesterday),
                    userRepository.findUsersCountBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun countsNewStoresStep(): Step {
        return stepBuilderFactory["countsNewStoresStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    COUNTS_STORE,
                    storeRepository.findActiveStoresCounts(),
                    storeRepository.findActiveStoresCountsBetweenDate(yesterday, yesterday),
                    storeRepository.findActiveStoresCountsBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun countsDeletedStoresStep(): Step {
        return stepBuilderFactory["countsDeletedStoresStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                val todayDeletedCounts = storeRepository.findDeletedStoresCountsByDate(yesterday, yesterday)
                slackApiClient.postMessage(
                    PostSlackMessageRequest.of(COUNTS_DELETED_STORE.messageFormat.format(todayDeletedCounts))
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun countsActiveMenuStep(): Step {
        return stepBuilderFactory["countsActiveMenuStep"]
            .tasklet { _, _ ->
                val result = menuRepository.countsGroupByMenu()
                val message = result.asSequence()
                    .sortedByDescending { it.counts }
                    .joinToString(separator = "\n") {
                        COUNTS_MENU.messageFormat.format(it.category.categoryName, it.counts)
                    }

                slackApiClient.postMessage(PostSlackMessageRequest.of(COUNTS_MENUS.messageFormat.format(message)))
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun countsNewReviewsStep(): Step {
        return stepBuilderFactory["countsNewReviewsStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    COUNTS_REVIEW,
                    reviewRepository.findActiveReviewsCounts(),
                    reviewRepository.findReviewsCountBetweenDate(yesterday, yesterday),
                    reviewRepository.findReviewsCountBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun countsNewVisitHistoriesStep(): Step {
        return stepBuilderFactory["countsNewVisitHistoriesStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    COUNTS_VISIT_HISTORY,
                    visitHistoryRepository.findAllCounts(),
                    visitHistoryRepository.findCountsBetweenDate(yesterday, yesterday),
                    visitHistoryRepository.findCountsBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun countsUserMedalGroupByMedal(): Step {
        return stepBuilderFactory["countsUserMedalGroupByMedal"]
            .tasklet { _, _ ->
                val result = medalRepository.findUserMedalsCountsGroupByMedal()
                val message = result.asSequence()
                    .sortedByDescending { it.counts }
                    .joinToString(separator = "\n") {
                        COUNTS_MEDAL.messageFormat.format(it.medalName, it.counts)
                    }
                slackApiClient.postMessage(PostSlackMessageRequest.of(COUNTS_MEDALS.messageFormat.format(message)))
                RepeatStatus.FINISHED
            }
            .build()
    }

    private fun sendStatisticsNotification(
        messageType: StatisticsMessageFormat,
        totalCounts: Long,
        todayCounts: Long,
        weekendCounts: Long
    ) {
        slackApiClient.postMessage(
            PostSlackMessageRequest.of(
                messageType.messageFormat.format(totalCounts, todayCounts, weekendCounts)
            )
        )
    }

}
