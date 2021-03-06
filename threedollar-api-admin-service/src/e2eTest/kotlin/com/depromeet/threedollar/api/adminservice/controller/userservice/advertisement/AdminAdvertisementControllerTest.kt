package com.depromeet.threedollar.api.adminservice.controller.userservice.advertisement

import com.depromeet.threedollar.api.adminservice.SetupAdminControllerTest
import com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement.dto.request.AddAdvertisementRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement.dto.request.UpdateAdvertisementRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement.dto.response.AdvertisementResponse
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementFixture
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementRepository
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.LocalDateTime

internal class AdminAdvertisementControllerTest(
    private val advertisementRepository: AdvertisementRepository,
) : SetupAdminControllerTest() {

    @Nested
    inner class AddAdvertisementApiTest {

        @Test
        fun `새로운 광고를 추가합니다`() {
            // given
            val request = AddAdvertisementRequest(
                applicationType = ApplicationType.USER_API,
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

    }

    @Nested
    inner class UdpateAdvertismentApiTest {

        @Test
        fun `기존 광고를 수정합니다`() {
            // given
            val advertisement = AdvertisementFixture.create(
                applicationType = ApplicationType.USER_API,
                positionType = AdvertisementPositionType.MAIN_PAGE_CARD,
                platformType = AdvertisementPlatformType.ALL,
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

    }

    @Nested
    inner class DeleteAdvertisement {

        @Test
        fun `기존 광고를 삭제합니다`() {
            // given
            val advertisement = AdvertisementFixture.create(
                applicationType = ApplicationType.USER_API,
                positionType = AdvertisementPositionType.MAIN_PAGE_CARD,
                platformType = AdvertisementPlatformType.ALL,
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

    }

    @Nested
    inner class RetrieveAdvertisements {

        @Test
        fun `광고 목록을 조회합니다`() {
            // given
            val advertisement = AdvertisementFixture.create(
                applicationType = ApplicationType.USER_API,
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
                param("applicationType", ApplicationType.USER_API.name)
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

        @Test
        fun `특정 서비스의 광고 목록을 조회합니다`() {
            // given
            val userServiceAdvertisment = AdvertisementFixture.create(
                applicationType = ApplicationType.USER_API,
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
            val bossServiceAdvertisement = AdvertisementFixture.create(
                applicationType = ApplicationType.BOSS_API,
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
            advertisementRepository.saveAll(listOf(userServiceAdvertisment, bossServiceAdvertisement))

            // when & then
            mockMvc.get("/v1/user/advertisements") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                param("applicationType", ApplicationType.USER_API.name)
                param("page", "1")
                param("size", "5")
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.totalCounts") { value(1) }
                    jsonPath("$.data.contents", hasSize<AdvertisementResponse>(1))
                    jsonPath("$.data.contents[0].advertisementId") { value(userServiceAdvertisment.id) }
                    jsonPath("$.data.contents[0].positionType") { value(userServiceAdvertisment.positionType.name) }
                    jsonPath("$.data.contents[0].platformType") { value(userServiceAdvertisment.platformType.name) }
                    jsonPath("$.data.contents[0].title") { value(userServiceAdvertisment.detail.title) }
                    jsonPath("$.data.contents[0].subTitle") { value(userServiceAdvertisment.detail.subTitle) }
                    jsonPath("$.data.contents[0].imageUrl") { value(userServiceAdvertisment.detail.imageUrl) }
                    jsonPath("$.data.contents[0].linkUrl") { value(userServiceAdvertisment.detail.linkUrl) }
                    jsonPath("$.data.contents[0].startDateTime") { value("2022-05-03T00:00:00") }
                    jsonPath("$.data.contents[0].endDateTime") { value("2022-05-05T00:00:00") }
                }
        }

    }

}
