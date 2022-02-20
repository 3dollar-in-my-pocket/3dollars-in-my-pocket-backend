package com.depromeet.threedollar.external.client.google;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class GoogleFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new GoogleApiErrorDecoder();
    }

    public static class GoogleApiErrorDecoder implements ErrorDecoder {

        private static final int MIN_CLIENT_ERROR_STATUS_CODE = 400;
        private static final int MAX_CLIENT_ERROR_STATUS_CODE = 499;

        @Override
        public Exception decode(String methodKey, Response response) {
            if (isClientError(response.status())) {
                return new InvalidException(String.format("Google API 호출 중 클라이언트 에러(%s)가 발생하였습니다. message: (%s)", response.status(), response.body()), ErrorCode.INVALID_AUTH_TOKEN);
            }
            return new BadGatewayException(String.format("Google API 연동 중 Google 쪽 에러(%s)가 발생하였습니다. message: (%s)", response.status(), response.body()));
        }

        private boolean isClientError(int status) {
            return MIN_CLIENT_ERROR_STATUS_CODE <= status && status <= MAX_CLIENT_ERROR_STATUS_CODE;
        }

    }

}
