package com.depromeet.threedollar.external.config.feign;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.common.exception.model.InvalidException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignDefaultConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    public static class FeignErrorDecoder implements ErrorDecoder {

        private static final int MIN_CLIENT_ERROR_STATUS_CODE = 400;
        private static final int MAX_CLIENT_ERROR_STATUS_CODE = 499;

        @Override
        public Exception decode(String methodKey, Response response) {
            if (isClientError(response.status())) {
                return new InvalidException(String.format("외부 API 호출 중 클라이언트 에러(%s)가 발생하였습니다. message: (%s)", response.status(), response.body()));
            }
            return new InternalServerException(String.format("외부 API 연동 중 서버 에러(%s)가 발생하였습니다. message: (%s)", response.status(), response.body()));
        }

        private boolean isClientError(int status) {
            return MIN_CLIENT_ERROR_STATUS_CODE <= status && status <= MAX_CLIENT_ERROR_STATUS_CODE;
        }

    }

}
