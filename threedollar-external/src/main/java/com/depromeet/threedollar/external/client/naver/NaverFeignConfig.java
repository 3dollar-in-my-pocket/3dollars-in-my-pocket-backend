package com.depromeet.threedollar.external.client.naver;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class NaverFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new NaverApiErrorDecoder();
    }

    /**
     * https://developers.naver.com/docs/login/profile/profile.md
     */
    public static class NaverApiErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            switch (response.status()) {
                case 401:
                case 403:
                case 404:
                    return new InvalidException(String.format("네이버 Auth API 호출 중 잘못된 토큰이 입력되었습니다. status: (%s) message: (%s)", response.status(), response.body()), ErrorCode.INVALID_AUTH_TOKEN);
                default:
                    return new BadGatewayException(String.format("네이버 API 연동 중 네이버 쪽 에러가 발생하였습니다. status: (%s) message: (%s)", response.status(), response.body()));
            }
        }

    }

}
