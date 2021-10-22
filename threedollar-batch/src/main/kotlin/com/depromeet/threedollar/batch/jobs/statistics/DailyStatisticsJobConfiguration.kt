package com.depromeet.threedollar.batch.jobs.statistics

import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
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
                        "[가슴속 삼천원 $yesterday 통계 정보를 알려드립니다]\n" +
                            "1. 회원 가입 수\n" +
                            "- 총 ${totalCounts}명이 가입하고 있습니다\n" +
                            "- 금일 ${todayCounts}명이 신규 가입하였습니다\n" +
                            "- 금주 ${thisWeeksCount}명이 신규 가입하였습니다"
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
                        "2. 활성 가게 수\n" +
                            "- 총 ${totalCounts}개의 가게가 활성화 되어 있습니다\n" +
                            "- 금일 ${todayCounts}개의 가게가 신규 등록되었습니다.\n" +
                            "- 금주 ${thisWeeksCount}개의 가게가 신규 등록되었습니다.\n" +
                            "- 금일 ${todayDeletedCounts}개의 가게가 삭제되었습니다"
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
                        "- ${it.category.categoryName}: ${it.counts}개가 활성화 되어 있습니다"
                    }

                slackApiClient.postMessage(PostSlackMessageRequest.of("3. 활성화된 메뉴 정보\n${message}"))
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
                        "4. 활성 리뷰 수\n" +
                            "- 총 ${totalCounts}개의 리뷰를 작성해주셨습니다.\n" +
                            "- 금일 ${todayCounts}개의 리뷰를 신규 작성해주셨습니다.\n" +
                            "- 금주 ${thisWeeksCount}개의 리뷰를 신규 작성해주셨습니다."
                    )
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

}
