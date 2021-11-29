package com.depromeet.threedollar.api.controller.popup

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.application.service.popup.dto.response.PopupResponse
import com.depromeet.threedollar.domain.domain.popup.PopupCreator
import com.depromeet.threedollar.domain.domain.popup.PopupPlatformType
import com.depromeet.threedollar.domain.domain.popup.PopupRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.javaunit.autoparams.AutoSource
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
class PopupControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val popupRepository: PopupRepository
) {

    @AfterEach
    fun cleanUp() {
        popupRepository.deleteAll()
    }

    @AutoSource
    @ParameterizedTest
    fun 활성화중인_팝업_목록을_확인한다(platform: PopupPlatformType) {
        // given
        val popup = PopupCreator.create(
            "https://pop-up-image.png",
            "https://my-link.com",
            LocalDateTime.of(2021, 1, 1, 0, 0),
            LocalDateTime.now().plusDays(1),
            platform
        )
        popupRepository.save(popup)

        // when
        val response = objectMapper.readValue(
            mockMvc.perform(
                get("/api/v1/popups")
                    .param("platform", platform.toString())
            )
                .andExpect(status().isOk)
                .andDo(print())
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8),
            object : TypeReference<ApiResponse<List<PopupResponse>>>() {}
        )

        // then
        assertAll({
            assertThat(response.data).hasSize(1)
            assertThat(response.data[0].imageUrl).isEqualTo(popup.imageUrl)
            assertThat(response.data[0].linkUrl).isEqualTo(popup.linkUrl)
        })
    }

}
