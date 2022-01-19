package com.depromeet.threedollar.admin.service.popup

import com.depromeet.threedollar.admin.service.popup.dto.request.AddPopupRequest
import com.depromeet.threedollar.admin.service.popup.dto.request.UpdatePopupRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.user.domain.popup.Popup
import com.depromeet.threedollar.domain.user.domain.popup.PopupPlatformType
import com.depromeet.threedollar.domain.user.domain.popup.PopupPositionType
import com.depromeet.threedollar.domain.user.domain.popup.PopupRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class PopupAdminServiceTest(
    private val popupAdminService: PopupAdminService,
    private val popupRepository: PopupRepository
) {

    @AfterEach
    fun cleanUp() {
        popupRepository.deleteAllInBatch()
    }

    @Test
    fun `새로운 팝업을 추가합니다`() {
        // given
        val positionType = PopupPositionType.SPLASH
        val platformType = PopupPlatformType.IOS
        val imageUrl = "https://image-png"
        val linkUrl = "https://link.com"
        val priority = 1
        val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
        val endDateTime = LocalDateTime.of(2022, 1, 7, 0, 0)

        val request = AddPopupRequest(
            positionType = positionType,
            platformType = platformType,
            imageUrl = imageUrl,
            linkUrl = linkUrl,
            priority = priority,
            startDateTime = startDateTime,
            endDateTime = endDateTime
        )

        // when
        popupAdminService.addPopup(request)

        // then
        val popups = popupRepository.findAll()
        assertThat(popups).hasSize(1)
        popups[0].let {
            assertThat(it.positionType).isEqualTo(positionType)
            assertThat(it.platformType).isEqualTo(platformType)
            assertThat(it.imageUrl).isEqualTo(imageUrl)
            assertThat(it.linkUrl).isEqualTo(linkUrl)
            assertThat(it.priority).isEqualTo(priority)
            assertThat(it.startDateTime).isEqualTo(startDateTime)
            assertThat(it.endDateTime).isEqualTo(endDateTime)
        }
    }

    @Test
    fun `팝업을 수정합니다`() {
        // given
        val popup = createPopup()
        popupRepository.save(popup)

        val positionType = PopupPositionType.SPLASH
        val platformType = PopupPlatformType.IOS
        val imageUrl = "https://image-png"
        val linkUrl = "https://link.com"
        val priority = 1
        val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
        val endDateTime = LocalDateTime.of(2022, 1, 7, 0, 0)

        val request = UpdatePopupRequest(
            positionType = positionType,
            platformType = platformType,
            imageUrl = imageUrl,
            linkUrl = linkUrl,
            priority = priority,
            startDateTime = startDateTime,
            endDateTime = endDateTime
        )

        // when
        popupAdminService.updatePopup(popup.id, request)

        // then
        val popups = popupRepository.findAll()
        assertThat(popups).hasSize(1)
        popups[0].let {
            assertThat(it.positionType).isEqualTo(positionType)
            assertThat(it.platformType).isEqualTo(platformType)
            assertThat(it.imageUrl).isEqualTo(imageUrl)
            assertThat(it.linkUrl).isEqualTo(linkUrl)
            assertThat(it.priority).isEqualTo(priority)
            assertThat(it.startDateTime).isEqualTo(startDateTime)
            assertThat(it.endDateTime).isEqualTo(endDateTime)
        }
    }

    @Test
    fun `팝업 수정시 존재하지 않는 경우 404 에러 발생`() {
        // given
        val request = UpdatePopupRequest(
            positionType = PopupPositionType.SPLASH,
            platformType = PopupPlatformType.AOS,
            imageUrl = "imageUrl",
            linkUrl = "linkUrl",
            priority = 10,
            startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0),
            endDateTime = LocalDateTime.of(2022, 1, 3, 0, 0),
        )

        // when & then
        assertThatThrownBy { popupAdminService.updatePopup(-1, request) }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `팝업을 삭제한다`() {
        // given
        val popup = createPopup()
        popupRepository.save(popup)

        // when
        popupAdminService.deletePopup(popup.id)

        // then
        val popups = popupRepository.findAll()
        assertThat(popups).isEmpty()
    }

    @Test
    fun `팝업 삭제시 존재하지 않는 경우 404 에러 발생`() {
        // when & then
        assertThatThrownBy { popupAdminService.deletePopup(-1) }.isInstanceOf(NotFoundException::class.java)
    }

    private fun createPopup(): Popup {
        return Popup.newInstance(
            PopupPositionType.SPLASH,
            PopupPlatformType.AOS,
            "imageUrl",
            "linkUrl",
            LocalDateTime.of(2022, 3, 1, 0, 0),
            LocalDateTime.of(2022, 3, 7, 0, 0),
            10)
    }

}
