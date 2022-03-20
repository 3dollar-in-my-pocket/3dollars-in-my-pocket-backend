package com.depromeet.threedollar.external.client.slack;

import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
    name = "slackWebhookApiClient",
    url = "${external.client.slack.webhook.base-url}",
    configuration = {
        SlackFeignConfig.class
    },
    primary = false
)
public interface SlackWebhookApiClient {

    @PostMapping("${slack.token.monitoring}")
    void postMonitoringMessage(PostSlackMessageRequest request);

    @PostMapping("${slack.token.daily-statistics}")
    void postStatisticsMessage(PostSlackMessageRequest request);

    @PostMapping("${slack.token.boss-manager}")
    void postBossManagerMessage(PostSlackMessageRequest request);

}
