package com.depromeet.threedollar.external.client.slack;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest;

@FeignClient(
    name = "SlackWebhookApiClient",
    url = "${external.client.slack.webhook.base-url}",
    configuration = {
        SlackFeignConfig.class
    },
    primary = false
)
public interface SlackWebhookApiClient {

    @Retryable(backoff = @Backoff(value = 1000), value = BadGatewayException.class)
    @PostMapping("${slack.token.monitoring}")
    void postMonitoringMessage(PostSlackMessageRequest request);

    @Retryable(backoff = @Backoff(value = 1000), value = BadGatewayException.class)
    @PostMapping("${slack.token.daily-statistics}")
    void postStatisticsMessage(PostSlackMessageRequest request);

    @Retryable(backoff = @Backoff(value = 1000), value = BadGatewayException.class)
    @PostMapping("${slack.token.boss-manager}")
    void postBossManagerMessage(PostSlackMessageRequest request);

}
