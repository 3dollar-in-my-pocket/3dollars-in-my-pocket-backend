package com.depromeet.threedollar.api.controller.popup

import com.depromeet.threedollar.domain.user.domain.popup.PopupCreator
import com.depromeet.threedollar.domain.user.domain.popup.PopupPlatformType
import com.depromeet.threedollar.domain.user.domain.popup.PopupPositionType
import com.depromeet.threedollar.domain.user.domain.popup.PopupRepository
import org.junit.jupiter.api.AfterEach
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
class PopupControllerTest(
    private val mockMvc: MockMvc,
    private val popupRepository: PopupRepository
) {

    @AfterEach
    fun cleanUp() {
        popupRepository.deleteAllInBatch()
    }

    @Test
    fun 활성화중인_팝업_목록을_확인한다() {
        // given
        val platform = PopupPlatformType.AOS
        val popup = PopupCreator.create(
            PopupPositionType.SPLASH,
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
        popupRepository.save(popup)

        // when & then
        mockMvc.get("/api/v1/popups") {
            param("platform", platform.toString())
        }.andExpect {
            status { isOk() }
            content {
                jsonPath("$.data[0].title") { value(popup.detail.title) }
                jsonPath("$.data[0].subTitle") { value(popup.detail.subTitle) }
                jsonPath("$.data[0].imageUrl") { value(popup.detail.imageUrl) }
                jsonPath("$.data[0].linkUrl") { value(popup.detail.linkUrl) }
                jsonPath("$.data[0].bgColor") { value(popup.detail.bgColor) }
                jsonPath("$.data[0].fontColor") { value(popup.detail.fontColor) }
            }
        }

    }

}
