package com.depromeet.threedollar.external.client.slack;

import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
    name = "slackApiClient",
    url = "https://hooks.slack.com/services",
    configuration = {
        SlackFallbackConfiguration.class
    },
    primary = false
)
public interface SlackNotificationApiClient {

    @PostMapping("${slack.token}")
    void postMessage(PostSlackMessageRequest request);

}
