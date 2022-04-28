package com.depromeet.threedollar.api.user.controller.advertisement

import com.depromeet.threedollar.api.user.controller.SetupControllerTest
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementCreator
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPositionType
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime

class AdvertisementControllerTest(
    private val advertisementRepository: AdvertisementRepository
) : SetupControllerTest() {

    @AfterEach
    fun cleanUp() {
        advertisementRepository.deleteAllInBatch()
    }

    @DisplayName("GET /api/v1/popups")
    @Test
    fun `(DEPRECATED) 플랫폼에 해당하는 활성화중인 광고 목록을 조회한다`() {
        // given
        val platform = AdvertisementPlatformType.AOS
        val advertisement = AdvertisementCreator.create(
            positionType = AdvertisementPositionType.SPLASH,
            platformType = platform,
            title = "광고 타이틀",
            subTitle = "광고 내용\n광고 내용",
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
            content {
                jsonPath("$.data[0].title") { value(advertisement.detail.title) }
                jsonPath("$.data[0].subTitle") { value(advertisement.detail.subTitle) }
                jsonPath("$.data[0].imageUrl") { value(advertisement.detail.imageUrl) }
                jsonPath("$.data[0].linkUrl") { value(advertisement.detail.linkUrl) }
                jsonPath("$.data[0].bgColor") { value(advertisement.detail.bgColor) }
                jsonPath("$.data[0].fontColor") { value(advertisement.detail.fontColor) }
            }
        }

    }

    @DisplayName("GET /api/v1/advertisements")
    @Test
    fun `플랫폼에 해당하는 활성화중인 광고 목록을 조회한다`() {
        // given
        val platform = AdvertisementPlatformType.AOS
        val advertisement = AdvertisementCreator.create(
            positionType = AdvertisementPositionType.SPLASH,
            platformType = platform,
            title = "광고 타이틀",
            subTitle = "광고 내용\n광고 내용",
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
            content {
                jsonPath("$.data[0].title") { value(advertisement.detail.title) }
                jsonPath("$.data[0].subTitle") { value(advertisement.detail.subTitle) }
                jsonPath("$.data[0].imageUrl") { value(advertisement.detail.imageUrl) }
                jsonPath("$.data[0].linkUrl") { value(advertisement.detail.linkUrl) }
                jsonPath("$.data[0].bgColor") { value(advertisement.detail.bgColor) }
                jsonPath("$.data[0].fontColor") { value(advertisement.detail.fontColor) }
            }
        }

    }

}
