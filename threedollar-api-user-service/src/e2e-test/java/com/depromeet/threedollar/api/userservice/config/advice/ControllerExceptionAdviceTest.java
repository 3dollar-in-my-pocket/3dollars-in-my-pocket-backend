package com.depromeet.threedollar.api.userservice.config.advice;

import com.depromeet.threedollar.api.userservice.ControllerTest;
import com.depromeet.threedollar.api.userservice.controller.HealthCheckController;
import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.common.exception.model.ServiceUnAvailableException;
import com.depromeet.threedollar.common.exception.model.TooManyRequestsException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ControllerExceptionAdviceTest extends ControllerTest {

    @SpyBean
    private HealthCheckController healthCheckController;

    @Test
    void 잘못된_HTTP_Method로_요청이_들어온경우_Method_Not_Allowed를_반환한다() throws Exception {
        // when & then
        mockMvc.perform(post("/ping"))
            .andDo(print())
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.E405_METHOD_NOT_ALLOWED.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.E405_METHOD_NOT_ALLOWED.getMessage()));
    }

    @Test
    void 일시적으로_너무_많은_요청이_들어온경우_Too_Many_Requests_응답을_반환한다() throws Exception {
        // given
        when(healthCheckController.healthCheck()).thenThrow(new TooManyRequestsException("일시적으로 너무 많은 요청이 들어왔습니다"));

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isTooManyRequests())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.E429_TOO_MANY_REQUESTS.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.E429_TOO_MANY_REQUESTS.getMessage()));
    }

    @Test
    void 서버_내부적으로_에러가_발생한경우_Interanl_Server를_반환한다() throws Exception {
        // given
        when(healthCheckController.healthCheck()).thenThrow(new InternalServerException("서버 내부에서 에러가 발생하였습니다"));

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.E500_INTERNAL_SERVER.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.E500_INTERNAL_SERVER.getMessage()));
    }

    @Test
    void 외부_시스템_연동중_에러가_발생한경우_Internal_Server를_반환한다() throws Exception {
        // given
        when(healthCheckController.healthCheck()).thenThrow(new BadGatewayException("외부 시스템 연동중 에러가 발생하였습니다"));

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isBadGateway())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.E502_BAD_GATEWAY.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.E502_BAD_GATEWAY.getMessage()));
    }

    @Test
    void 해당_API가_점검중인경우_Service_Unavilable_에러가_발생한다() throws Exception {
        // given
        when(healthCheckController.healthCheck()).thenThrow(new ServiceUnAvailableException("해당 API는 점검중입니다"));

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isServiceUnavailable())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.E503_SERVICE_UNAVAILABLE.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.E503_SERVICE_UNAVAILABLE.getMessage()));
    }

    @Test
    void HttpMessageNotReadable인경우_400에러가_발생한다() throws Exception {
        // given
        when(healthCheckController.healthCheck()).thenThrow(new HttpMessageNotReadableException("HttpMessageNotReadable", new MockHttpInputMessage(new byte[10])));

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.E400_INVALID.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.E400_INVALID.getMessage()));
    }

    @Test
    void 최대_허용가능한_이미지_크기를_넘은경우_400에러가_발생한다() throws Exception {
        // given
        when(healthCheckController.healthCheck()).thenThrow(new MaxUploadSizeExceededException(5000));

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.E400_INVALID_FILE_SIZE_TOO_LARGE.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.E400_INVALID_FILE_SIZE_TOO_LARGE.getMessage()));
    }

    @Test
    void 지정되지_않은_에러들은_디폴트로_500에러가_발생한다() throws Exception {
        // given
        when(healthCheckController.healthCheck()).thenThrow(new IllegalArgumentException("디폴트 에러"));

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.E500_INTERNAL_SERVER.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.E500_INTERNAL_SERVER.getMessage()));
    }

}
