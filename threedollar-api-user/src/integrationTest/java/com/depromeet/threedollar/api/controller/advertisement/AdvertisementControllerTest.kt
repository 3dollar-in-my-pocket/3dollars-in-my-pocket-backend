package com.depromeet.threedollar.api.controller.advertisement

import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementCreator
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPositionType
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime

@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
class AdvertisementControllerTest(
    private val mockMvc: MockMvc,
    private val advertisementRepository: AdvertisementRepository
) {

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
            AdvertisementPositionType.SPLASH,
            platform,
            "광고 타이틀",
            "광고 내용\n광고 내용",
            "https://pop-up-image.png",
            "https://my-link.com",
            "#ffffff",
            "#000000",
            LocalDateTime.of(2021, 1, 1, 0, 0),
            LocalDateTime.now().plusDays(1),
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
            AdvertisementPositionType.SPLASH,
            platform,
            "광고 타이틀",
            "광고 내용\n광고 내용",
            "https://pop-up-image.png",
            "https://my-link.com",
            "#ffffff",
            "#000000",
            LocalDateTime.of(2021, 1, 1, 0, 0),
            LocalDateTime.now().plusDays(1),
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
