package com.depromeet.threedollar.external.client.slack;

import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
    name = "slackWebhookApiClient",
    url = "${external.client.slack.webhook.base-url}",
    configuration = {
        SlackFallbackConfiguration.class
    },
    primary = false
)
public interface SlackWebhookApiClient {

    @PostMapping("${slack.token}")
    void postMessage(PostSlackMessageRequest request);

}
