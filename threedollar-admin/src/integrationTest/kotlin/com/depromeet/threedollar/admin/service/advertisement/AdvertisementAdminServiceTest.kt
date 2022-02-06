package com.depromeet.threedollar.admin.service.advertisement

import com.depromeet.threedollar.admin.service.advertisement.dto.request.AddAdvertisementRequest
import com.depromeet.threedollar.admin.service.advertisement.dto.request.UpdateAdvertisementRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.user.domain.advertisement.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class AdvertisementAdminServiceTest(
    private val advertisementAdminService: AdvertisementAdminService,
    private val advertisementRepository: AdvertisementRepository
) {

    @AfterEach
    fun cleanUp() {
        advertisementRepository.deleteAllInBatch()
    }

    @Test
    fun `새로운 광고를 추가합니다`() {
        // given
        val positionType = AdvertisementPositionType.SPLASH
        val platformType = AdvertisementPlatformType.IOS
        val title = "title"
        val subTitle = "subTitle"
        val imageUrl = "https://image-png"
        val linkUrl = "https://link.com"
        val bgColor = "#ffffff"
        val fontColor = "#000000"
        val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
        val endDateTime = LocalDateTime.of(2022, 1, 7, 0, 0)

        val request = AddAdvertisementRequest(
            positionType = positionType,
            platformType = platformType,
            title = title,
            subTitle = subTitle,
            imageUrl = imageUrl,
            linkUrl = linkUrl,
            bgColor = bgColor,
            fontColor = fontColor,
            startDateTime = startDateTime,
            endDateTime = endDateTime
        )

        // when
        advertisementAdminService.addAdvertisement(request)

        // then
        val advertisements = advertisementRepository.findAll()
        assertThat(advertisements).hasSize(1)
        advertisements[0].let {
            assertThat(it.positionType).isEqualTo(positionType)
            assertThat(it.platformType).isEqualTo(platformType)
            assertThat(it.detail.title).isEqualTo(title)
            assertThat(it.detail.subTitle).isEqualTo(subTitle)
            assertThat(it.detail.imageUrl).isEqualTo(imageUrl)
            assertThat(it.detail.linkUrl).isEqualTo(linkUrl)
            assertThat(it.detail.bgColor).isEqualTo(bgColor)
            assertThat(it.detail.fontColor).isEqualTo(fontColor)
            assertThat(it.startDateTime).isEqualTo(startDateTime)
            assertThat(it.endDateTime).isEqualTo(endDateTime)
        }
    }

    @Test
    fun `광고를 수정합니다`() {
        // given
        val advertisement = createAdvertisement()
        advertisementRepository.save(advertisement)

        val positionType = AdvertisementPositionType.SPLASH
        val platformType = AdvertisementPlatformType.IOS
        val title = "title"
        val subTitle = "subTitle"
        val imageUrl = "https://image-png"
        val linkUrl = "https://link.com"
        val bgColor = "#ffffff"
        val fontColor = "#000000"
        val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
        val endDateTime = LocalDateTime.of(2022, 1, 7, 0, 0)

        val request = UpdateAdvertisementRequest(
            positionType = positionType,
            platformType = platformType,
            title = title,
            subTitle = subTitle,
            imageUrl = imageUrl,
            linkUrl = linkUrl,
            bgColor = bgColor,
            fontColor = fontColor,
            startDateTime = startDateTime,
            endDateTime = endDateTime
        )

        // when
        advertisementAdminService.updateAdvertisement(advertisement.id, request)

        // then
        val advertisements = advertisementRepository.findAll()
        assertThat(advertisements).hasSize(1)
        advertisements[0].let {
            assertThat(it.positionType).isEqualTo(positionType)
            assertThat(it.platformType).isEqualTo(platformType)
            assertThat(it.detail.title).isEqualTo(title)
            assertThat(it.detail.subTitle).isEqualTo(subTitle)
            assertThat(it.detail.imageUrl).isEqualTo(imageUrl)
            assertThat(it.detail.linkUrl).isEqualTo(linkUrl)
            assertThat(it.detail.bgColor).isEqualTo(bgColor)
            assertThat(it.detail.fontColor).isEqualTo(fontColor)
            assertThat(it.startDateTime).isEqualTo(startDateTime)
            assertThat(it.endDateTime).isEqualTo(endDateTime)
        }
    }

    @Test
    fun `광고 수정시 존재하지 않는 경우 404 에러 발생`() {
        // given
        val request = UpdateAdvertisementRequest(
            positionType = AdvertisementPositionType.SPLASH,
            platformType = AdvertisementPlatformType.AOS,
            title = "title",
            subTitle = "",
            imageUrl = "imageUrl",
            linkUrl = "linkUrl",
            bgColor = "#ffffff",
            fontColor = "#000000",
            startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0),
            endDateTime = LocalDateTime.of(2022, 1, 3, 0, 0),
        )

        // when & then
        assertThatThrownBy { advertisementAdminService.updateAdvertisement(-1, request) }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `광고를 삭제한다`() {
        // given
        val advertisement = createAdvertisement()
        advertisementRepository.save(advertisement)

        // when
        advertisementAdminService.deleteAdvertisement(advertisement.id)

        // then
        val advertisements = advertisementRepository.findAll()
        assertThat(advertisements).isEmpty()
    }

    @Test
    fun `광고 삭제시 존재하지 않는 경우 404 에러 발생`() {
        // when & then
        assertThatThrownBy { advertisementAdminService.deleteAdvertisement(-1) }.isInstanceOf(NotFoundException::class.java)
    }

    private fun createAdvertisement(): Advertisement {
        return AdvertisementCreator.create(
            AdvertisementPositionType.SPLASH,
            AdvertisementPlatformType.AOS,
            "제목",
            "서브 타이틀",
            "imageUrl",
            "linkUrl",
            "#ffffff",
            "#000000",
            LocalDateTime.of(2022, 3, 1, 0, 0),
            LocalDateTime.of(2022, 3, 7, 0, 0))
    }

}
