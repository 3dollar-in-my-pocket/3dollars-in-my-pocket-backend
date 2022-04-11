package com.depromeet.threedollar.external.client.kakao;

import org.springframework.context.annotation.Bean;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;

public class KaKaoFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new KakaoApiErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 2000, 3);
    }

    /**
     * https://developers.kakao.com/docs/latest/ko/reference/rest-api-reference#response-code
     */
    private static class KakaoApiErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            FeignException exception = FeignException.errorStatus(methodKey, response);
            switch (response.status()) {
                case 400:
                    return new InvalidException(String.format("Kakao API 호출 중 필수 파라미터가 요청되지 않았습니다. status: (%s) message: (%s)", response.status(), response.body()), ErrorCode.INVALID_MISSING_AUTH_TOKEN);
                case 401:
                case 403:
                    return new InvalidException(String.format("Kakao Auth API 호출 중 잘못된 토큰이 입력되었습니다. status: (%s) message: (%s)", response.status(), response.body()), ErrorCode.INVALID_AUTH_TOKEN);
                default:
                    return new RetryableException(response.status(), exception.getMessage(), response.request().httpMethod(), exception, null, response.request());
            }
        }

    }

}
