package com.depromeet.threedollar.external.client.google;

import org.springframework.context.annotation.Bean;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class GoogleFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new GoogleApiErrorDecoder();
    }

    private static class GoogleApiErrorDecoder implements ErrorDecoder {

        private static final int MIN_CLIENT_ERROR_STATUS_CODE = 400;
        private static final int MAX_CLIENT_ERROR_STATUS_CODE = 499;

        @Override
        public Exception decode(String methodKey, Response response) {
            FeignException exception = FeignException.errorStatus(methodKey, response);
            if (isClientError(exception.status())) {
                return new InvalidException(String.format("Google API 호출 중 클라이언트 에러(%s)가 발생하였습니다. message: (%s)", exception.status(), exception.getMessage()), ErrorCode.INVALID_AUTH_TOKEN);
            }
            throw new BadGatewayException(String.format("슬랙 API 호출중 에러(%s)가 발생하였습니다. message: (%s) ", response.status(), exception.getMessage()));
        }

        private boolean isClientError(int status) {
            return MIN_CLIENT_ERROR_STATUS_CODE <= status && status <= MAX_CLIENT_ERROR_STATUS_CODE;
        }

    }

}
