package com.depromeet.threedollar.external.client.slack;

import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Profile({"local", "local-docker", "integration-test"})
@Component
public class DummySlackWebhookApiClient implements SlackWebhookApiClient {

    @Override
    public void postMessage(PostSlackMessageRequest request) {

    }

}
