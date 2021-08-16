package com.depromeet.threedollar.external.external.slack;

import com.depromeet.threedollar.common.exception.BadGatewayException;
import com.depromeet.threedollar.external.external.slack.dto.component.SlackApiCallerComponent;
import lombok.*;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Profile({"dev", "prod"})
@Primary
@RequiredArgsConstructor
@Component
public class WebClientSlackApiCallerImpl implements SlackApiCaller {

    private final WebClient webClient;
    private final SlackApiCallerComponent slackApiCallerComponent;

    @Override
    public void postMessage(String message) {
        webClient.post()
            .uri(slackApiCallerComponent.getUrl())
            .body(Mono.just(PostSlackMessageRequest.of(message)), PostSlackMessageRequest.class)
            .retrieve()
            .onStatus(HttpStatus::isError, errorResponse -> Mono.error(new BadGatewayException("슬랙 외부 API 호출 중 에러가 발생하였습니다.")))
            .bodyToMono(String.class)
            .block();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class PostSlackMessageRequest {

        private String text;

        public static PostSlackMessageRequest of(String text) {
            return new PostSlackMessageRequest(text);
        }

    }

}
