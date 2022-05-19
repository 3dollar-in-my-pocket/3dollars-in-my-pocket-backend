package com.depromeet.threedollar.api.admin.controller.user.medal

import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import com.depromeet.threedollar.api.admin.controller.SetupAdminControllerTest
import com.depromeet.threedollar.api.admin.service.user.medal.dto.request.AddMedalRequest
import com.depromeet.threedollar.api.admin.service.user.medal.dto.request.UpdateMedalRequest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.user.medal.dto.response.MedalResponse
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalCreator
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalRepository

internal class AdminMedalControllerTest(
    private val medalRepository: MedalRepository,
) : SetupAdminControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        medalRepository.deleteAll()
    }

    @DisplayName("POST /v1/user/medal")
    @Test
    fun `새로운 메달을 추가한다`() {
        // given
        val request = AddMedalRequest(
            name = "메달 이름",
            introduction = "메달 소개",
            activationIconUrl = "https://activate-icon.png",
            disableIconUrl = "https://disable-icon.png",
            conditionType = MedalAcquisitionConditionType.NO_CONDITION,
            conditionCount = 0,
            acquisitionDescription = "기본적으로 제공하는 메달"
        )

        // when & then
        mockMvc.post("/v1/user/medal") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
    }

    @DisplayName("PUT /v1/user/medal/{MEDAL_ID}")
    @Test
    fun `새로운 메달을 수정한다`() {
        // given
        val medal = MedalCreator.create(
            name = "메달 이름",
            introduction = "메달 소개",
            activationIconUrl = "https://activate-icon.png",
            disableIconUrl = "https://disable-icon.png",
            conditionType = MedalAcquisitionConditionType.NO_CONDITION,
            conditionCount = 0,
            acquisitionDescription = "기본적으로 제공하는 메달"
        )
        medalRepository.save(medal)

        val request = UpdateMedalRequest(
            name = "수정 이후 메달 이름",
            introduction = "수정 이후 메달 소개",
            activationIconUrl = "https://changed-activate-icon.png",
            disableIconUrl = "https://changed-disable-icon.png"
        )

        // when & then
        mockMvc.put("/v1/user/medal/${medal.id}") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
    }

    @DisplayName("GET /v1/user/medals")
    @Test
    fun `전체 메달 목록을 조회합니다`() {
        // given
        val medal = MedalCreator.create(
            name = "메달 이름",
            introduction = "메달 소개",
            activationIconUrl = "https://activate-icon.png",
            disableIconUrl = "https://disable-icon.png",
            conditionType = MedalAcquisitionConditionType.NO_CONDITION,
            conditionCount = 0,
            acquisitionDescription = "기본적으로 제공하는 메달"
        )
        medalRepository.save(medal)

        // when & then
        mockMvc.get("/v1/user/medals") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data", hasSize<MedalResponse>(1))
                jsonPath("$.data[0].medalId") { value(medal.id) }
                jsonPath("$.data[0].name") { value(medal.name) }
                jsonPath("$.data[0].iconUrl") { value(medal.activationIconUrl) }
                jsonPath("$.data[0].disableIconUrl") { value(medal.disableIconUrl) }
                jsonPath("$.data[0].introduction") { value(medal.introduction) }
                jsonPath("$.data[0].acquisition.description") { value(medal.acquisitionCondition.description) }
            }
    }

}
