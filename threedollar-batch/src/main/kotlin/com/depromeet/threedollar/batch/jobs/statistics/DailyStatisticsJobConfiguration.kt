package com.depromeet.threedollar.batch.jobs.statistics

import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.domain.domain.menu.MenuRepository
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
    private val slackApiClient: SlackApiClient
) {

    @Bean
    fun dailyStaticsJob(): Job {
        return jobBuilderFactory.get("dailyStaticsJob")
            .incrementer(UniqueRunIdIncrementer())
            .start(countsNewUserStep())
            .next(countsNewStoreStep())
            .build()
    }

    @Bean
    fun countsNewUserStep(): Step {
        return stepBuilderFactory["countsNewUserStep"]
            .tasklet { _, _ ->
                val totalCounts = userRepository.findUsersCount()

                val yesterday = LocalDate.now().minusDays(1)
                val todayCounts = userRepository.findUsersCountByDate(yesterday, yesterday)
                val thisWeeksCount = userRepository.findUsersCountByDate(yesterday.minusWeeks(1), yesterday)

                slackApiClient.postMessage(createSlackMessage(yesterday, totalCounts, todayCounts, thisWeeksCount))
                RepeatStatus.FINISHED
            }
            .build()
    }

    private fun createSlackMessage(
        date: LocalDate,
        totalCounts: Long,
        todayCounts: Long,
        weekCounts: Long
    ): PostSlackMessageRequest {
        return PostSlackMessageRequest.of(
            String.format(
                DAILY_USER_COUNTS_MESSAGE_FORMAT,
                date,
                totalCounts,
                todayCounts,
                weekCounts,
            )
        )
    }

    @Bean
    fun countsNewStoreStep(): Step {
        return stepBuilderFactory["countsNewStoreStep"]
            .tasklet { _, _ ->
                val result = menuRepository.countsGroupByMenu()
                val message = result.asSequence()
                    .sortedByDescending { it.counts }
                    .joinToString(separator = "\n") {
                        MENU_DESCRIPTION.format(it.category.name, it.counts)
                    }

                slackApiClient.postMessage(PostSlackMessageRequest.of(ACTIVE_MENU_COUNTS_MESSAGE_FORMAT.format(message)))
                RepeatStatus.FINISHED
            }
            .build()
    }

    companion object {
        private const val DAILY_USER_COUNTS_MESSAGE_FORMAT = "" +
            "[가슴속 삼천원 %s 통계 정보를 알려드립니다]\n" +
            "1. 회원 가입 수\n" +
            "- 총 %s명이 가입하고 있습니다\n" +
            "- 금일 %s명이 가입하였습니다\n" +
            "- 금주 %s명이 가입하였습니다"

        private const val ACTIVE_MENU_COUNTS_MESSAGE_FORMAT = "" +
            "2. 활성화된 메뉴 정보\n%s"

        private const val MENU_DESCRIPTION = "" +
            "- %s: %s개가 활성화되어 있습니다"
    }

}
