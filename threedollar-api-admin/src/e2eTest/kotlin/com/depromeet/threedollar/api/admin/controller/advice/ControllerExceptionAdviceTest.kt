package com.depromeet.threedollar.api.admin.controller.advice

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.mock.http.MockHttpInputMessage
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.multipart.MaxUploadSizeExceededException
import com.depromeet.threedollar.api.admin.controller.HealthCheckController
import com.depromeet.threedollar.api.admin.controller.SetupControllerTest
import com.depromeet.threedollar.common.exception.model.BadGatewayException
import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.common.exception.model.ServiceUnAvailableException
import com.depromeet.threedollar.common.exception.model.TooManyRequestsException
import com.depromeet.threedollar.common.exception.type.ErrorCode

internal class ControllerExceptionAdviceTest : SetupControllerTest() {

    @SpyBean
    private lateinit var healthCheckController: HealthCheckController

    @Test
    fun 잘못된_HTTP_Method로_요청이_들어온경우_Method_Not_Allowed를_반환한다() {
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/ping"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed)
            .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ErrorCode.METHOD_NOT_ALLOWED.code))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.METHOD_NOT_ALLOWED.message))
    }

    @Test
    fun 일시적으로_너무_많은_요청이_들어온경우_Too_Many_Requests_응답을_반환한다() {
        // given
        Mockito.`when`(healthCheckController.healthCheck()).thenThrow(TooManyRequestsException("일시적으로 너무 많은 요청이 들어왔습니다"))

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isTooManyRequests)
            .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ErrorCode.TOO_MANY_REQUESTS.code))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.TOO_MANY_REQUESTS.message))
    }

    @Test
    @Throws(Exception::class)
    fun 서버_내부적으로_에러가_발생한경우_Interanl_Server를_반환한다() {
        // given
        Mockito.`when`(healthCheckController.healthCheck()).thenThrow(InternalServerException("서버 내부에서 에러가 발생하였습니다"))

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ErrorCode.INTERNAL_SERVER.code))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.INTERNAL_SERVER.message))
    }

    @Test
    fun 외부_시스템_연동중_에러가_발생한경우_Internal_Server를_반환한다() {
        // given
        Mockito.`when`(healthCheckController.healthCheck()).thenThrow(BadGatewayException("외부 시스템 연동중 에러가 발생하였습니다"))

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadGateway)
            .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ErrorCode.BAD_GATEWAY.code))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.BAD_GATEWAY.message))
    }

    @Test
    fun 해당_API가_점검중인경우_Service_Unavilable_에러가_발생한다() {
        // given
        Mockito.`when`(healthCheckController.healthCheck()).thenThrow(ServiceUnAvailableException("해당 API는 점검중입니다"))

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isServiceUnavailable)
            .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ErrorCode.SERVICE_UNAVAILABLE.code))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.SERVICE_UNAVAILABLE.message))
    }

    @Test
    fun HttpMessageNotReadable인경우_400에러가_발생한다() {
        // given
        Mockito.`when`(healthCheckController.healthCheck()).thenThrow(HttpMessageNotReadableException("HttpMessageNotReadable", MockHttpInputMessage(ByteArray(10))))

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ErrorCode.INVALID.code))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.INVALID.message))
    }

    @Test
    fun 최대_허용가능한_이미지_크기를_넘은경우_400에러가_발생한다() {
        // given
        Mockito.`when`(healthCheckController.healthCheck()).thenThrow(MaxUploadSizeExceededException(5000))

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ErrorCode.INVALID_UPLOAD_FILE_SIZE.code))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.INVALID_UPLOAD_FILE_SIZE.message))
    }

}