package com.depromeet.threedollar.api.admin.service.user.advertisement

import com.depromeet.threedollar.api.admin.service.user.advertisement.dto.request.AddAdvertisementRequest
import com.depromeet.threedollar.api.admin.service.user.advertisement.dto.request.UpdateAdvertisementRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class AdminAdvertisementServiceTest(
    private val adminAdvertisementService: AdminAdvertisementService,
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
        val title = "광고 제목"
        val subTitle = "광고 서브 제목"
        val imageUrl = "https://adv-image-png"
        val linkUrl = "https://adv-link.com"
        val bgColor = "#ffffff"
        val fontColor = "#000000"
        val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
        val endDateTime = LocalDateTime.of(2022, 1, 7, 0, 0)

        val request = AddAdvertisementRequest(
            position = positionType,
            platform = platformType,
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
        adminAdvertisementService.addAdvertisement(request)

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
        val title = "광고 제목"
        val subTitle = "광고 서브 제목"
        val imageUrl = "https://ad-image.png"
        val linkUrl = "https://ad-link.com"
        val bgColor = "#ffffff"
        val fontColor = "#000000"
        val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
        val endDateTime = LocalDateTime.of(2022, 1, 7, 0, 0)

        val request = UpdateAdvertisementRequest(
            position = positionType,
            platform = platformType,
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
        adminAdvertisementService.updateAdvertisement(advertisement.id, request)

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
            position = AdvertisementPositionType.SPLASH,
            platform = AdvertisementPlatformType.AOS,
            title = "가슴속 3천원과 함께하는 광고",
            subTitle = "광고 내용 설명",
            imageUrl = "https://image.jpeg",
            linkUrl = "https://advertisement-link.com",
            bgColor = "#ffffff",
            fontColor = "#000000",
            startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0),
            endDateTime = LocalDateTime.of(2022, 1, 3, 0, 0),
        )

        // when & then
        assertThatThrownBy { adminAdvertisementService.updateAdvertisement(-1, request) }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `광고를 삭제한다`() {
        // given
        val advertisement = createAdvertisement()
        advertisementRepository.save(advertisement)

        // when
        adminAdvertisementService.deleteAdvertisement(advertisement.id)

        // then
        val advertisements = advertisementRepository.findAll()
        assertThat(advertisements).isEmpty()
    }

    @Test
    fun `광고 삭제시 존재하지 않는 경우 404 에러 발생`() {
        // when & then
        assertThatThrownBy { adminAdvertisementService.deleteAdvertisement(-1) }.isInstanceOf(NotFoundException::class.java)
    }

    private fun createAdvertisement(): Advertisement {
        return AdvertisementCreator.create(
            positionType = AdvertisementPositionType.SPLASH,
            platformType = AdvertisementPlatformType.AOS,
            title = "가슴속 3천원이 익산동에 출몰",
            subTitle = "붕어빵 먹으러 오세요",
            imageUrl = "https://adv-image.png",
            linkUrl = "https://link.com",
            bgColor = "#ffffff",
            fontColor = "#000000",
            startDateTime = LocalDateTime.of(2022, 3, 1, 0, 0),
            endDateTime = LocalDateTime.of(2022, 3, 7, 0, 0))
    }

}
