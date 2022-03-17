package com.depromeet.threedollar.api.user.controller.advice;

import com.depromeet.threedollar.api.user.controller.HealthCheckController;
import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.common.exception.model.ServiceUnAvailableException;
import com.depromeet.threedollar.common.exception.model.TooManyRequestsException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ControllerExceptionAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private HealthCheckController healthCheckController;

    @Test
    void 잘못된_HTTP_Method로_요청이_들어온경우_Method_Not_Allowed를_반환한다() throws Exception {
        // when & then
        mockMvc.perform(post("/ping"))
            .andDo(print())
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.METHOD_NOT_ALLOWED.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.METHOD_NOT_ALLOWED.getMessage()));
    }

    @Test
    void 일시적으로_너무_많은_요청이_들어온경우_Too_Many_Requests_응답을_반환한다() throws Exception {
        // given
        when(healthCheckController.healthCheck()).thenThrow(new TooManyRequestsException("일시적으로 너무 많은 요청이 들어왔습니다"));

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isTooManyRequests())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.TOO_MANY_REQUESTS.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.TOO_MANY_REQUESTS.getMessage()));
    }

    @Test
    void 서버_내부적으로_에러가_발생한경우_Interanl_Server를_반환한다() throws Exception {
        // given
        when(healthCheckController.healthCheck()).thenThrow(new InternalServerException("서버 내부에서 에러가 발생하였습니다"));

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.INTERNAL_SERVER.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.INTERNAL_SERVER.getMessage()));
    }

    @Test
    void 외부_시스템_연동중_에러가_발생한경우_Internal_Server를_반환한다() throws Exception {
        // given
        when(healthCheckController.healthCheck()).thenThrow(new BadGatewayException("외부 시스템 연동중 에러가 발생하였습니다"));

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isBadGateway())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.BAD_GATEWAY.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.BAD_GATEWAY.getMessage()));
    }

    @Test
    void 해당_API가_점검중인경우_Service_Unavilable_에러가_발생한다() throws Exception {
        // given
        when(healthCheckController.healthCheck()).thenThrow(new ServiceUnAvailableException("해당 API는 점검중입니다"));

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isServiceUnavailable())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.SERVICE_UNAVAILABLE.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.SERVICE_UNAVAILABLE.getMessage()));
    }

}
