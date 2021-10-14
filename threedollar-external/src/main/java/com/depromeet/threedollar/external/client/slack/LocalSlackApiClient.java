package com.depromeet.threedollar.external.client.slack;

import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Primary
@Profile({"local", "local-will"})
@Component
public class LocalSlackApiClient implements SlackApiClient {

    @Override
    public void postMessage(PostSlackMessageRequest request) {
    }

}
