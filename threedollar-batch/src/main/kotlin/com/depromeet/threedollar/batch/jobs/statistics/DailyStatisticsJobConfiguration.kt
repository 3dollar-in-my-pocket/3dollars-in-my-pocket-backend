package com.depromeet.threedollar.batch.jobs.statistics

import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.batch.jobs.statistics.StaticsMessageType.*
import com.depromeet.threedollar.domain.domain.menu.MenuRepository
import com.depromeet.threedollar.domain.domain.review.ReviewRepository
import com.depromeet.threedollar.domain.domain.store.StoreRepository
import com.depromeet.threedollar.domain.domain.user.UserRepository
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
    private val slackApiClient: SlackApiClient
) {

    @Bean
    fun dailyStaticsJob(): Job {
        return jobBuilderFactory.get("dailyStaticsJob")
            .incrementer(UniqueRunIdIncrementer())
            .start(countsNewUserStep())
            .next(countsNewStoresStep())
            .next(countsActiveMenuStep())
            .next(countsNewReviewsStep())
            .build()
    }

    @Bean
    fun countsNewUserStep(): Step {
        return stepBuilderFactory["countsNewUserStep"]
            .tasklet { _, _ ->
                val totalCounts = userRepository.findUsersCount()

                val yesterday = LocalDate.now().minusDays(1)
                val todayCounts = userRepository.findUsersCountBetweenDate(yesterday, yesterday)
                val thisWeeksCount = userRepository.findUsersCountBetweenDate(yesterday.minusWeeks(1), yesterday)

                slackApiClient.postMessage(
                    PostSlackMessageRequest.of(
                        COUNTS_USER.messageFormat.format(yesterday, totalCounts, todayCounts, thisWeeksCount)
                    )
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun countsNewStoresStep(): Step {
        return stepBuilderFactory["countsNewStoresStep"]
            .tasklet { _, _ ->
                val totalCounts = storeRepository.findActiveStoresCounts()

                val yesterday = LocalDate.now().minusDays(1)
                val todayCounts = storeRepository.findActiveStoresCountsBetweenDate(yesterday, yesterday)
                val thisWeeksCount =
                    storeRepository.findActiveStoresCountsBetweenDate(yesterday.minusWeeks(1), yesterday)

                val todayDeletedCounts = storeRepository.findDeletedStoresCountsByDate(yesterday, yesterday)

                slackApiClient.postMessage(
                    PostSlackMessageRequest.of(
                        COUNTS_STORE.messageFormat.format(totalCounts, todayCounts, thisWeeksCount, todayDeletedCounts)
                    )
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
                val totalCounts = reviewRepository.findActiveReviewsCounts()

                val yesterday = LocalDate.now().minusDays(1)
                val todayCounts = reviewRepository.findReviewsCountBetweenDate(yesterday, yesterday)
                val thisWeeksCount = reviewRepository.findReviewsCountBetweenDate(yesterday.minusWeeks(1), yesterday)

                slackApiClient.postMessage(
                    PostSlackMessageRequest.of(
                        COUNTS_REVIEW.messageFormat.format(totalCounts, todayCounts, thisWeeksCount)
                    )
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

}
