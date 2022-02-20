package com.depromeet.threedollar.external.client.slack;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.common.exception.model.InternalServerException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class SlackFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new SlackApiErrorDecoder();
    }

    /**
     * https://api.slack.com/changelog/2016-05-17-changes-to-errors-for-incoming-webhooks
     */
    public static class SlackApiErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            switch (response.status()) {
                case 400:
                case 403:
                case 404:
                case 410:
                    return new InternalServerException(String.format("Slack API 호출 중 클라이언트 에러가 발생하였습니다. status: (%s) message: (%s)", response.status(), response.body()));
                default:
                    return new BadGatewayException(String.format("Slack API 호출 중 Slack 서버 에러가 발생하였습니다. status: (%s) message: (%s)", response.status(), response.body()));
            }
        }

    }

}
