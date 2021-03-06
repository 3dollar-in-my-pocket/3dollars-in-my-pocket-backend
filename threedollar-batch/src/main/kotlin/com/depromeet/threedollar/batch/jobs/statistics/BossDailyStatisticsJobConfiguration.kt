package com.depromeet.threedollar.batch.jobs.statistics

import com.depromeet.threedollar.batch.config.JobExceptionListener
import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.batch.jobs.statistics.templates.BossDailyStatisticsMessageFormat
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossDeletedStoreRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
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

private const val JOB_NAME = "bossStatisticsJob"

/**
 * 사장님 서비스 일일 통계 배치 잡
 */
@Configuration
class BossDailyStatisticsJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val slackNotificationApiClient: SlackWebhookApiClient,
    private val bossAccountRepository: BossAccountRepository,
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreFeedbackRepository: BossStoreFeedbackRepository,
    private val bossDeletedStoreRepository: BossDeletedStoreRepository,
) {

    @Bean(name = [JOB_NAME])
    fun bossDailyStatisticsJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .incrementer(UniqueRunIdIncrementer())
            .listener(JobListenerFactoryBean.getListener(JobExceptionListener(slackNotificationApiClient)))
            .start(bossStatisticsStep())
            .next(bossAccountStatisticsStep())
            .next(bossRegistrationStatisticsStep())
            .next(bossStoresStatisticsStep())
            .next(deleteBossStoreStatisticsStep())
            .next(bossStoreFeedbacksStatisticsStep())
            .build()
    }

    @Bean(name = [JOB_NAME + "_step"])
    fun bossStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_step"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                slackNotificationApiClient.postStatisticsMessage(
                    PostSlackMessageRequest.of(BossDailyStatisticsMessageFormat.DAILY_STATISTICS_INFO.messageFormat.format(yesterday))
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_bossAccountStep"])
    fun bossAccountStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_bossAccountStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = BossDailyStatisticsMessageFormat.BOSS_ACCOUNT_STATISTICS,
                    totalCounts = bossAccountRepository.count(),
                    todayCounts = bossAccountRepository.countBossAccountsBetweenDate(yesterday, yesterday),
                    weekendCounts = bossAccountRepository.countBossAccountsBetweenDate(yesterday.minusWeeks(1), yesterday),
                    monthlyCounts = bossAccountRepository.countBossAccountsBetweenDate(yesterday.minusMonths(1), yesterday),
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_bossRegistrationStep"])
    fun bossRegistrationStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_bossRegistrationStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = BossDailyStatisticsMessageFormat.BOSS_REGISTRATION_STATISTICS,
                    totalCounts = bossRegistrationRepository.count(),
                    todayCounts = bossRegistrationRepository.countBossRegistrationsBetweenDate(yesterday, yesterday),
                    weekendCounts = bossRegistrationRepository.countBossRegistrationsBetweenDate(yesterday.minusWeeks(1), yesterday),
                    monthlyCounts = bossRegistrationRepository.countBossRegistrationsBetweenDate(yesterday.minusMonths(1), yesterday),
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_bossStoreStep"])
    fun bossStoresStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_bossStoreStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = BossDailyStatisticsMessageFormat.BOSS_STORE_STATISTICS,
                    totalCounts = bossStoreRepository.count(),
                    todayCounts = bossStoreRepository.countBossStoresBetweenDate(yesterday, yesterday),
                    weekendCounts = bossStoreRepository.countBossStoresBetweenDate(yesterday.minusWeeks(1), yesterday),
                    monthlyCounts = bossStoreRepository.countBossStoresBetweenDate(yesterday.minusMonths(1), yesterday),
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_deletedBossStoreLocationStep"])
    fun deleteBossStoreStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_deletedBossStoreLocationStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = BossDailyStatisticsMessageFormat.DELETED_BOSS_STORE_STATISTICS,
                    totalCounts = bossDeletedStoreRepository.count(),
                    todayCounts = bossDeletedStoreRepository.countDeletedBossStoresBetweenDate(yesterday, yesterday),
                    weekendCounts = bossDeletedStoreRepository.countDeletedBossStoresBetweenDate(yesterday.minusWeeks(1), yesterday),
                    monthlyCounts = bossDeletedStoreRepository.countDeletedBossStoresBetweenDate(yesterday.minusMonths(1), yesterday),
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean(name = [JOB_NAME + "_bossStoreFeedbackStep"])
    fun bossStoreFeedbacksStatisticsStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_bossStoreFeedbackStep"]
            .tasklet { _, _ ->
                val yesterday = LocalDate.now().minusDays(1)
                sendStatisticsNotification(
                    messageType = BossDailyStatisticsMessageFormat.BOSS_STORE_FEEDBACK_STATISTICS,
                    totalCounts = bossStoreFeedbackRepository.count(),
                    todayCounts = bossStoreFeedbackRepository.countBossStoreFeedbacksBetweenDate(yesterday, yesterday),
                    weekendCounts = bossStoreFeedbackRepository.countBossStoreFeedbacksBetweenDate(yesterday.minusWeeks(1), yesterday),
                    monthlyCounts = bossStoreFeedbackRepository.countBossStoreFeedbacksBetweenDate(yesterday.minusMonths(1), yesterday),
                )
                RepeatStatus.FINISHED
            }
            .build()
    }

    private fun sendStatisticsNotification(
        messageType: BossDailyStatisticsMessageFormat,
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
