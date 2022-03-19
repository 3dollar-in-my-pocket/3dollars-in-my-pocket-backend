package com.depromeet.threedollar.external.client.naver;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class NaverFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new NaverApiErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 2000, 3);
    }

    /**
     * https://developers.naver.com/docs/login/profile/profile.md
     */
    private static class NaverApiErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            FeignException exception = FeignException.errorStatus(methodKey, response);
            switch (response.status()) {
                case 401:
                case 403:
                case 404:
                    return new InvalidException(String.format("네이버 Auth API 호출 중 잘못된 토큰이 입력되었습니다. status: (%s) message: (%s)", response.status(), response.body()), ErrorCode.INVALID_AUTH_TOKEN);
                default:
                    return new RetryableException(response.status(), exception.getMessage(), response.request().httpMethod(), exception, null, response.request());
            }
        }

    }

}
