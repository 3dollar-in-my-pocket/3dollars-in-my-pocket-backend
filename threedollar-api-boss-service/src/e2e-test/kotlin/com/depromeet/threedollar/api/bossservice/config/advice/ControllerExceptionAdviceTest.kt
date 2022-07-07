package com.depromeet.threedollar.api.bossservice.config.advice

import com.depromeet.threedollar.api.bossservice.SetupControllerTest
import com.depromeet.threedollar.api.bossservice.controller.HealthCheckController
import com.depromeet.threedollar.common.exception.model.BadGatewayException
import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.common.exception.model.ServiceUnAvailableException
import com.depromeet.threedollar.common.exception.model.TooManyRequestsException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.mock.http.MockHttpInputMessage
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.multipart.MaxUploadSizeExceededException

internal class ControllerExceptionAdviceTest : SetupControllerTest() {

    @SpykBean
    private lateinit var healthCheckController: HealthCheckController

    @Test
    fun 잘못된_HTTP_Method로_요청이_들어온경우_Method_Not_Allowed를_반환한다() {
        // when & then
        mockMvc.perform(post("/ping"))
            .andDo(print())
            .andExpect(status().isMethodNotAllowed)
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.METHOD_NOT_ALLOWED.code))
            .andExpect(jsonPath("$.message").value(ErrorCode.METHOD_NOT_ALLOWED.message))
    }

    @Test
    fun 일시적으로_너무_많은_요청이_들어온경우_Too_Many_Requests_응답을_반환한다() {
        // given
        every { healthCheckController.healthCheck() } throws TooManyRequestsException("일시적으로 너무 많은 요청이 들어왔습니다")

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isTooManyRequests)
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.TOO_MANY_REQUESTS.code))
            .andExpect(jsonPath("$.message").value(ErrorCode.TOO_MANY_REQUESTS.message))
    }

    @Test
    fun 서버_내부적으로_에러가_발생한경우_Interanl_Server를_반환한다() {
        // given
        every { healthCheckController.healthCheck() } throws InternalServerException("서버 내부에서 에러가 발생하였습니다")

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.INTERNAL_SERVER.code))
            .andExpect(jsonPath("$.message").value(ErrorCode.INTERNAL_SERVER.message))
    }

    @Test
    fun 외부_시스템_연동중_에러가_발생한경우_Internal_Server를_반환한다() {
        // given
        every { healthCheckController.healthCheck() } throws BadGatewayException("외부 시스템 연동중 에러가 발생하였습니다")

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isBadGateway)
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.BAD_GATEWAY.code))
            .andExpect(jsonPath("$.message").value(ErrorCode.BAD_GATEWAY.message))
    }

    @Test
    fun 해당_API가_점검중인경우_Service_Unavilable_에러가_발생한다() {
        // given
        every { healthCheckController.healthCheck() } throws ServiceUnAvailableException("해당 API는 점검중입니다")

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isServiceUnavailable)
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.SERVICE_UNAVAILABLE.code))
            .andExpect(jsonPath("$.message").value(ErrorCode.SERVICE_UNAVAILABLE.message))
    }

    @Test
    fun HttpMessageNotReadable인경우_400에러가_발생한다() {
        // given
        every { healthCheckController.healthCheck() } throws HttpMessageNotReadableException("HttpMessageNotReadable", MockHttpInputMessage(ByteArray(10)))

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.INVALID.code))
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID.message))
    }

    @Test
    fun 최대_허용가능한_이미지_크기를_넘은경우_400에러가_발생한다() {
        // given
        every { healthCheckController.healthCheck() } throws MaxUploadSizeExceededException(5000)

        // when & then
        mockMvc.perform(get("/ping"))
            .andDo(print())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.INVALID_UPLOAD_FILE_SIZE.code))
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_UPLOAD_FILE_SIZE.message))
    }

}
