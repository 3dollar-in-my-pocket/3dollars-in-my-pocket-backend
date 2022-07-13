package com.depromeet.threedollar.batch.jobs.push

import com.depromeet.threedollar.batch.config.JobExceptionListener
import com.depromeet.threedollar.batch.config.UniqueRunIdIncrementer
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpenRepository
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceRepository
import com.depromeet.threedollar.infrastructure.external.client.slack.SlackWebhookApiClient
import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType
import com.depromeet.threedollar.infrastructure.sqs.provider.MessageSendProvider
import com.depromeet.threedollar.infrastructure.sqs.provider.dto.request.SendBulkPushRequest
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.listener.JobListenerFactoryBean
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private val log = KotlinLogging.logger {}

private const val JOB_NAME = "bossOpenStoreBackgroundPushJob"
private const val CHUNK_SIZE = 100

/**
 * 오픈 정보 갱신을 위해 오픈 중인 가게들에게 백그라운드 푸시를 발송하는 배치
 */
@Configuration
class BossOpenPushJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val bossStoreOpenRepository: BossStoreOpenRepository,
    private val messageSendProvider: MessageSendProvider,
    private val bossStoreRepository: BossStoreRepository,
    private val deviceRepository: DeviceRepository,
    private val slackWebhookApiClient: SlackWebhookApiClient,
) {

    @Bean(name = [JOB_NAME])
    fun bossOpenStoreBackgroundPushJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .incrementer(UniqueRunIdIncrementer())
            .listener(JobListenerFactoryBean.getListener(JobExceptionListener(slackWebhookApiClient)))
            .start(bossOpenStoreBackgroundStep())
            .build()
    }

    @Bean(name = [JOB_NAME + "_step"])
    fun bossOpenStoreBackgroundStep(): Step {
        return stepBuilderFactory[JOB_NAME + "_step"]
            .tasklet { _, _ ->
                var totalCounts = 0L
                var cursor: String? = null

                while (true) {
                    val bossStoreOpens = bossStoreOpenRepository.findAllLessThanCursorLimit(cursor = cursor, limit = CHUNK_SIZE)
                    if (bossStoreOpens.isEmpty()) {
                        break
                    }

                    val bossStoreIds: List<String> = bossStoreOpens.map { bossStoreOpen -> bossStoreOpen.bossStoreId }
                    val bossStores: List<BossStore> = bossStoreRepository.findAllById(bossStoreIds).toList()
                    val bossIds: Set<String> = bossStores.map { bossStore -> bossStore.bossId }.toSet()

                    val devices = deviceRepository.findAllDevicesByAccountIdsAndType(accountType = AccountType.BOSS_ACCOUNT, accountIds = bossIds.toList())

                    val request = SendBulkPushRequest.backgroundPush(
                        tokens = devices.map { device -> device.deviceInfo.pushToken }.toSet(),
                    )
                    messageSendProvider.sendToTopic(TopicType.BOSS_BULK_APP_PUSH, request)

                    totalCounts += bossStoreOpens.size
                    cursor = bossStoreOpens[bossStoreOpens.size - 1].id
                }

                log.info("영업 중인 가게들에 대해서 백그라운드 푸시를 발송합니다 총 갯수: {}", totalCounts)
                RepeatStatus.FINISHED
            }
            .build()
    }

}
