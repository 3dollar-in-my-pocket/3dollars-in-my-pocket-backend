package com.depromeet.threedollar.infrastructure.external.client.slack;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.common.exception.model.InternalServerException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class SlackFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new SlackApiErrorDecoder();
    }

    /**
     * <a href="https://api.slack.com/changelog/2016-05-17-changes-to-errors-for-incoming-webhooks">https://api.slack.com/changelog/2016-05-17-changes-to-errors-for-incoming-webhooks</a>
     */
    private static class SlackApiErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            FeignException exception = FeignException.errorStatus(methodKey, response);
            switch (response.status()) {
                case 400:
                case 403:
                case 404:
                case 410:
                    throw new InternalServerException(String.format("Slack API 호출 중 클라이언트 에러(%s)가 발생하였습니다. message: (%s)", response.status(), response.body()));
                default:
                    throw new BadGatewayException(String.format("슬랙 API 호출중 에러(%s)가 발생하였습니다. message: (%s) ", response.status(), exception.getMessage()));
            }
        }

    }

}
