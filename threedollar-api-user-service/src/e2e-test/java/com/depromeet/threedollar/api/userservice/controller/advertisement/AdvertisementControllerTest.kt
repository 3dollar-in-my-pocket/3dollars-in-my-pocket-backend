package com.depromeet.threedollar.api.userservice.controller.advertisement

import java.time.LocalDateTime
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import com.depromeet.threedollar.api.core.service.commonservice.advertisement.dto.response.AdvertisementResponse
import com.depromeet.threedollar.api.userservice.SetupControllerTest
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementCreator
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementRepository

internal class AdvertisementControllerTest(
    private val advertisementRepository: AdvertisementRepository,
) : SetupControllerTest() {

    @DisplayName("GET /api/v1/popups")
    @Test
    fun `(DEPRECATED) 특정_플랫폼에_활성화중인 광고 목록을 필터링해서_조회한다`() {
        // given
        val platform = AdvertisementPlatformType.AOS
        val advertisement = AdvertisementCreator.create(
            applicationType = ApplicationType.USER_API,
            positionType = AdvertisementPositionType.SPLASH,
            platformType = platform,
            title = "가슴속 3천원 팀원 이야기",
            subTitle = "월 100명에서 IOS 차트 1위가 된\n가슴속 3천원 팀의 솔직 담백 토크",
            imageUrl = "https://pop-up-image.png",
            linkUrl = "https://my-link.com",
            bgColor = "#ffffff",
            fontColor = "#000000",
            startDateTime = LocalDateTime.of(2021, 1, 1, 0, 0),
            endDateTime = LocalDateTime.now().plusDays(1),
        )
        advertisementRepository.save(advertisement)

        // when & then
        mockMvc.get("/v1/popups") {
            param("platform", platform.toString())
        }.andExpect {
            status { isOk() }

            jsonPath("$.data[0].title") { value(advertisement.detail.title) }
            jsonPath("$.data[0].subTitle") { value(advertisement.detail.subTitle) }
            jsonPath("$.data[0].imageUrl") { value(advertisement.detail.imageUrl) }
            jsonPath("$.data[0].linkUrl") { value(advertisement.detail.linkUrl) }
            jsonPath("$.data[0].bgColor") { value(advertisement.detail.bgColor) }
            jsonPath("$.data[0].fontColor") { value(advertisement.detail.fontColor) }
        }

    }

    @DisplayName("GET /api/v1/advertisements")
    @Test
    fun `특정_플랫폼에_활성화중인 광고 목록을 필터링해서_조회한다`() {
        // given
        val platform = AdvertisementPlatformType.AOS
        val advertisement = AdvertisementCreator.create(
            applicationType = ApplicationType.USER_API,
            positionType = AdvertisementPositionType.SPLASH,
            platformType = platform,
            title = "가슴속 3천원 팀원 이야기",
            subTitle = "월 100명에서 IOS 차트 1위가 된\n가슴속 3천원 팀의 솔직 담백 토크",
            imageUrl = "https://pop-up-image.png",
            linkUrl = "https://my-link.com",
            bgColor = "#ffffff",
            fontColor = "#000000",
            startDateTime = LocalDateTime.of(2021, 1, 1, 0, 0),
            endDateTime = LocalDateTime.now().plusDays(1),
        )
        advertisementRepository.save(advertisement)

        // when & then
        mockMvc.get("/v1/advertisements") {
            param("platform", platform.toString())
        }.andExpect {
            status { isOk() }

            jsonPath("$.data", hasSize<AdvertisementResponse>(1))
            jsonPath("$.data[0].title") { value(advertisement.detail.title) }
            jsonPath("$.data[0].subTitle") { value(advertisement.detail.subTitle) }
            jsonPath("$.data[0].imageUrl") { value(advertisement.detail.imageUrl) }
            jsonPath("$.data[0].linkUrl") { value(advertisement.detail.linkUrl) }
            jsonPath("$.data[0].bgColor") { value(advertisement.detail.bgColor) }
            jsonPath("$.data[0].fontColor") { value(advertisement.detail.fontColor) }
        }

    }

}
