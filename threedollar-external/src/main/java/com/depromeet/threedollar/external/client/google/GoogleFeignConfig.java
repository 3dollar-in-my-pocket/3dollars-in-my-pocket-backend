package com.depromeet.threedollar.external.client.google;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class GoogleFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new GoogleApiErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 2000, 3);
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
            return new RetryableException(response.status(), exception.getMessage(), response.request().httpMethod(), exception, null, response.request());
        }

        private boolean isClientError(int status) {
            return MIN_CLIENT_ERROR_STATUS_CODE <= status && status <= MAX_CLIENT_ERROR_STATUS_CODE;
        }

    }

}
