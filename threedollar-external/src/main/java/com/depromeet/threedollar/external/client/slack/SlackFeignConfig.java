package com.depromeet.threedollar.external.client.slack;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class SlackFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new SlackApiErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 2000, 3);
    }

    /**
     * https://api.slack.com/changelog/2016-05-17-changes-to-errors-for-incoming-webhooks
     */
    public static class SlackApiErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            FeignException exception = FeignException.errorStatus(methodKey, response);
            switch (response.status()) {
                case 400:
                case 403:
                case 404:
                case 410:
                    return new InternalServerException(String.format("Slack API 호출 중 클라이언트 에러가 발생하였습니다. status: (%s) message: (%s)", response.status(), response.body()));
                default:
                    return new RetryableException(response.status(), exception.getMessage(), response.request().httpMethod(), exception, null, response.request());
            }
        }

    }

}
