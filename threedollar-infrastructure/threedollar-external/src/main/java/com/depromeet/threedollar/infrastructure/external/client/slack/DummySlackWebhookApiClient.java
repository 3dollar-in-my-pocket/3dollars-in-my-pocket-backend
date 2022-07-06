package com.depromeet.threedollar.infrastructure.external.client.slack;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.depromeet.threedollar.infrastructure.external.client.slack.dto.request.PostSlackMessageRequest;

@Primary
@Profile({"local", "local-docker", "integration-test"})
@Component
public class DummySlackWebhookApiClient implements SlackWebhookApiClient {

    @Override
    public void postMonitoringMessage(PostSlackMessageRequest request) {
    }

    @Override
    public void postStatisticsMessage(PostSlackMessageRequest request) {
    }

    @Override
    public void postBossManagerMessage(PostSlackMessageRequest request) {
    }

}
