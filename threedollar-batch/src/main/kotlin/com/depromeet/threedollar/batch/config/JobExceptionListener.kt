package com.depromeet.threedollar.batch.config

import com.depromeet.threedollar.common.type.template.SlackMessageTemplateType
import com.depromeet.threedollar.infrastructure.external.client.slack.SlackWebhookApiClient
import com.depromeet.threedollar.infrastructure.external.client.slack.dto.request.PostSlackMessageRequest
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

@Component
class JobExceptionListener(
    private val slackWebhookApiClient: SlackWebhookApiClient,
) : JobExecutionListener {

    override fun beforeJob(jobExecution: JobExecution) {

    }

    override fun afterJob(jobExecution: JobExecution) {
        if (BatchStatus.COMPLETED != jobExecution.status) {
            val request = PostSlackMessageRequest.of(
                SlackMessageTemplateType.BATCH_FAILED_MESSAGE.generateMessage(
                    jobExecution.jobInstance.jobName,
                    jobExecution.status,
                )
            )
            slackWebhookApiClient.postMonitoringMessage(request)
        }
    }

}
