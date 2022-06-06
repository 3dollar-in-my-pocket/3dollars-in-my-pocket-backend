package com.depromeet.threedollar.api.adminservice.controller.userservice.advertisement

import java.time.LocalDateTime
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import com.depromeet.threedollar.api.adminservice.controller.SetupAdminControllerTest
import com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.dto.request.AddAdvertisementRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.dto.request.UpdateAdvertisementRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.dto.response.AdvertisementResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementCreator
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementRepository

internal class AdminAdvertisementControllerTest(
    private val advertisementRepository: AdvertisementRepository,
) : SetupAdminControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        advertisementRepository.deleteAll()
    }

    @Test
    fun `새로운 광고를 추가합니다`() {
        // given
        val request = AddAdvertisementRequest(
            position = AdvertisementPositionType.MAIN_PAGE_CARD,
            platform = AdvertisementPlatformType.ALL,
            title = "가슴속 3천원이 익산동에 떴다",
            subTitle = "익선동에서 붕어빵 팔아요",
            imageUrl = "https://image.png",
            linkUrl = "https://link.com",
            bgColor = "#ffffff",
            fontColor = "#000000",
            startDateTime = LocalDateTime.of(2022, 5, 1, 0, 0),
            endDateTime = LocalDateTime.of(2022, 5, 10, 0, 0)
        )

        // when & then
        mockMvc.post("/v1/user/advertisement") {
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

    @Test
    fun `기존 광고를 수정합니다`() {
        // given
        val advertisement = AdvertisementCreator.create(
            positionType = AdvertisementPositionType.MAIN_PAGE_CARD,
            platformType = AdvertisementPlatformType.ALL,
            title = "붕어빵 팔아요!",
            subTitle = "붕어빵 팔아요 부제",
            imageUrl = "https://image-a.png",
            linkUrl = "https://link-a.com",
            bgColor = "#000000",
            fontColor = "#ffffff",
            startDateTime = LocalDateTime.of(2022, 5, 3, 0, 0),
            endDateTime = LocalDateTime.of(2022, 5, 5, 0, 0)
        )
        advertisementRepository.save(advertisement)

        val request = UpdateAdvertisementRequest(
            position = AdvertisementPositionType.MAIN_PAGE_CARD,
            platform = AdvertisementPlatformType.ALL,
            title = "가슴속 3천원이 익산동에 떴다",
            subTitle = "익선동에서 붕어빵 팔아요",
            imageUrl = "https://image.png",
            linkUrl = "https://link.com",
            bgColor = "#ffffff",
            fontColor = "#000000",
            startDateTime = LocalDateTime.of(2022, 5, 1, 0, 0),
            endDateTime = LocalDateTime.of(2022, 5, 10, 0, 0)
        )

        // when & then
        mockMvc.put("/v1/user/advertisement/${advertisement.id}") {
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

    @Test
    fun `기존 광고를 삭제합니다`() {
        // given
        val advertisement = AdvertisementCreator.create(
            positionType = AdvertisementPositionType.MAIN_PAGE_CARD,
            platformType = AdvertisementPlatformType.ALL,
            title = "붕어빵 팔아요!",
            subTitle = "붕어빵 팔아요 부제",
            imageUrl = "https://image-a.png",
            linkUrl = "https://link-a.com",
            bgColor = "#000000",
            fontColor = "#ffffff",
            startDateTime = LocalDateTime.of(2022, 5, 3, 0, 0),
            endDateTime = LocalDateTime.of(2022, 5, 5, 0, 0)
        )
        advertisementRepository.save(advertisement)

        // when & then
        mockMvc.delete("/v1/user/advertisement/${advertisement.id}") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
    }

    @Test
    fun `광고 목록을 조회합니다`() {
        // given
        val advertisement = AdvertisementCreator.create(
            positionType = AdvertisementPositionType.MAIN_PAGE_CARD,
            platformType = AdvertisementPlatformType.ALL,
            title = "붕어빵 팔아요!",
            subTitle = "붕어빵 팔아요 부제",
            imageUrl = "https://image-a.png",
            linkUrl = "https://link-a.com",
            bgColor = "#000000",
            fontColor = "#ffffff",
            startDateTime = LocalDateTime.of(2022, 5, 3, 0, 0),
            endDateTime = LocalDateTime.of(2022, 5, 5, 0, 0)
        )
        advertisementRepository.save(advertisement)

        // when & then
        mockMvc.get("/v1/user/advertisements") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            param("page", "1")
            param("size", "5")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data.totalCounts") { value(1) }
                jsonPath("$.data.contents", hasSize<AdvertisementResponse>(1))
                jsonPath("$.data.contents[0].advertisementId") { value(advertisement.id) }
                jsonPath("$.data.contents[0].positionType") { value(advertisement.positionType.name) }
                jsonPath("$.data.contents[0].platformType") { value(advertisement.platformType.name) }
                jsonPath("$.data.contents[0].title") { value(advertisement.detail.title) }
                jsonPath("$.data.contents[0].subTitle") { value(advertisement.detail.subTitle) }
                jsonPath("$.data.contents[0].imageUrl") { value(advertisement.detail.imageUrl) }
                jsonPath("$.data.contents[0].linkUrl") { value(advertisement.detail.linkUrl) }
                jsonPath("$.data.contents[0].startDateTime") { value("2022-05-03T00:00:00") }
                jsonPath("$.data.contents[0].endDateTime") { value("2022-05-05T00:00:00") }
            }
    }

}
