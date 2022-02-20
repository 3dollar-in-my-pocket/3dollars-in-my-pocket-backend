package com.depromeet.threedollar.external.client.kakao;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class KaKaoFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new KakaoApiErrorDecoder();
    }

    /**
     * https://developers.kakao.com/docs/latest/ko/reference/rest-api-reference#response-code
     */
    public static class KakaoApiErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            switch (response.status()) {
                case 400:
                    return new InvalidException(String.format("Kakao API 호출 중 필수 파라미터가 요청되지 않았습니다. status: (%s) message: (%s)", response.status(), response.body()), ErrorCode.INVALID_MISSING_AUTH_TOKEN);
                case 401:
                case 403:
                    return new InvalidException(String.format("Kakao Auth API 호출 중 잘못된 토큰이 입력되었습니다. status: (%s) message: (%s)", response.status(), response.body()), ErrorCode.INVALID_AUTH_TOKEN);
                default:
                    return new BadGatewayException(String.format("Kakao API 연동 중 카카오 쪽 에러가 발생하였습니다. status: (%s) message: (%s)", response.status(), response.body()));
            }
        }

    }

}
