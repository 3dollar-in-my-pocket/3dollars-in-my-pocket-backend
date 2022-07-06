package com.depromeet.threedollar.external.client.kakao;

import org.springframework.context.annotation.Bean;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;

import feign.FeignException;
import feign.Logger;
import feign.Response;
import feign.codec.ErrorDecoder;

public class KaKaoFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new KakaoApiErrorDecoder();
    }

    /**
     * <a href="https://developers.kakao.com/docs/latest/ko/reference/rest-api-reference#response-code">https://developers.kakao.com/docs/latest/ko/reference/rest-api-reference#response-code</a>
     */
    private static class KakaoApiErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            FeignException exception = FeignException.errorStatus(methodKey, response);
            switch (response.status()) {
                case 400:
                    throw new InvalidException(String.format("카카오 API 호출 중 필수 파라미터가 요청되지 않았습니다. status: (%s) message: (%s)", response.status(), response.body()), ErrorCode.INVALID_MISSING_AUTH_TOKEN);
                case 401:
                case 403:
                    throw new InvalidException(String.format("카카오 API 호출 중 잘못된 토큰이 입력되었습니다. status: (%s) message: (%s)", response.status(), response.body()), ErrorCode.INVALID_AUTH_TOKEN);
                default:
                    throw new BadGatewayException(String.format("카카오 API 호출중 에러(%s)가 발생하였습니다. message: (%s) ", response.status(), exception.getMessage()));
            }
        }

    }

}
