package com.depromeet.threedollar.api.adminservice.service.userservice.advertisement

import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import com.depromeet.threedollar.api.adminservice.IntegrationTest
import com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement.AdminAdvertisementService
import com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement.dto.request.AddAdvertisementRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement.dto.request.UpdateAdvertisementRequest
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.Advertisement
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementCreator
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementRepository

internal class AdminAdvertisementServiceTest(
    private val adminAdvertisementService: AdminAdvertisementService,
    private val advertisementRepository: AdvertisementRepository,
) : IntegrationTest() {

    @Nested
    inner class AddAdvertisementTest {

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
                applicationType = ApplicationType.USER_API,
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
        fun `광고 추가시 해당 서비스에서 지원하지 않는 광고 위치인경우 ForbiddenException이 발생한다`() {
            val request = AddAdvertisementRequest(
                applicationType = ApplicationType.BOSS_API,
                position = AdvertisementPositionType.SPLASH,
                platform = AdvertisementPlatformType.AOS,
                title = "광고 제목",
                subTitle = "광고 서브제목",
                imageUrl = "https://imageUrl.png",
                linkUrl = "https://link.com",
                bgColor = "#000000",
                fontColor = "#000000",
                startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0),
                endDateTime = LocalDateTime.of(2022, 1, 3, 0, 0)
            )

            // when & then
            assertThatThrownBy { adminAdvertisementService.addAdvertisement(request) }.isInstanceOf(ForbiddenException::class.java)
        }

    }

    @Nested
    inner class UpdateAdvertisementTest {

        @Test
        fun `광고를 수정합니다`() {
            // given
            val advertisement = createMockAdvertisement()
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

    }

    @Nested
    inner class DeleteAdvertisementTest {

        @Test
        fun `광고를 삭제한다`() {
            // given
            val advertisement = createMockAdvertisement()
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

    }

    private fun createMockAdvertisement(): Advertisement {
        return AdvertisementCreator.create(
            applicationType = ApplicationType.USER_API,
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
