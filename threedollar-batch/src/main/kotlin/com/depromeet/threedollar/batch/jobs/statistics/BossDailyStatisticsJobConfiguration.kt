package com.depromeet.threedollar.batch.jobs.statistics

import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
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

private const val BOSS_DAILY_STATISTICS_JOB = "bossStatisticsJob"

/**
 * 사장님 서비스 일일 통계 배치 잡
 */
@Configuration
class BossDailyStatisticsJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val slackNotificationApiClient: SlackWebhookApiClient,
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreFeedbackRepository: BossStoreFeedbackRepository
) {

    @Bean(name = [BOSS_DAILY_STATISTICS_JOB])
    fun bossDailyStatisticsJob(): Job {
        return jobBuilderFactory[BOSS_DAILY_STATISTICS_JOB]
            .incrementer(UniqueRunIdIncrementer())
            .start(bossStatisticsStep())
            .next(bossAccountStatisticsStep())
            .next(bossStoresStatisticsStep())
            .next(bossStoreFeedbacksStatisticsStep())
            .build()
    }

    @Bean(name = [BOSS_DAILY_STATISTICS_JOB + "_step"])
    fun bossStatisticsStep(): Step {
        return stepBuilderFactory[BOSS_DAILY_STATISTICS_JOB + "_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                slackNotificationApiClient.postStatisticsMessage(
                    PostSlackMessageRequest.of(BossDailyStatisticsMessageFormat.DAILY_STATISTICS_INFO.messageFormat.format(yesterday))
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [BOSS_DAILY_STATISTICS_JOB + "_bossAccountStep"])
    fun bossAccountStatisticsStep(): Step {
        return stepBuilderFactory[BOSS_DAILY_STATISTICS_JOB + "_bossAccountStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = BossDailyStatisticsMessageFormat.BOSS_ACCOUNT_STATISTICS,
                    totalCounts = bossAccountRepository.countAllBossAccounts(),
                    todayCounts = bossAccountRepository.countBossAccountsBetweenDate(yesterday, yesterday),
                    weekendCounts = bossAccountRepository.countBossAccountsBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [BOSS_DAILY_STATISTICS_JOB + "_bossStoreStep"])
    fun bossStoresStatisticsStep(): Step {
        return stepBuilderFactory[BOSS_DAILY_STATISTICS_JOB + "_bossStoreStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = BossDailyStatisticsMessageFormat.BOSS_STORE_STATISTICS,
                    totalCounts = bossStoreRepository.countAllBossStores(),
                    todayCounts = bossStoreRepository.countBossStoresBetweenDate(yesterday, yesterday),
                    weekendCounts = bossStoreRepository.countBossStoresBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [BOSS_DAILY_STATISTICS_JOB + "_bossStoreFeedbackStep"])
    fun bossStoreFeedbacksStatisticsStep(): Step {
        return stepBuilderFactory[BOSS_DAILY_STATISTICS_JOB + "_bossStoreFeedbackStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = BossDailyStatisticsMessageFormat.BOSS_STORE_FEEDBACK_STATISTICS,
                    totalCounts = bossStoreFeedbackRepository.countAllBossStoreFeedbacks(),
                    todayCounts = bossStoreFeedbackRepository.countBossStoreFeedbacksBetweenDate(yesterday, yesterday),
                    weekendCounts = bossStoreFeedbackRepository.countBossStoreFeedbacksBetweenDate(yesterday.minusWeeks(1), yesterday)
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    private fun sendStatisticsNotification(
        messageType: BossDailyStatisticsMessageFormat,
        totalCounts: Long,
        todayCounts: Long,
        weekendCounts: Long
    ) {
        slackNotificationApiClient.postStatisticsMessage(
            PostSlackMessageRequest.of(messageType.messageFormat.format(totalCounts, todayCounts, weekendCounts))
        )
    }

}