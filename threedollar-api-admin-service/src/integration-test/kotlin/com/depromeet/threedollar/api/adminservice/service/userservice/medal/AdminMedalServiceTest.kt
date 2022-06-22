package com.depromeet.threedollar.api.adminservice.service.userservice.medal

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import com.depromeet.threedollar.api.adminservice.IntegrationTest
import com.depromeet.threedollar.api.adminservice.service.userservice.medal.dto.request.AddMedalRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.medal.dto.request.UpdateMedalRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionCondition
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionType
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalRepository

internal class AdminMedalServiceTest(
    private val adminMedalService: AdminMedalService,
    private val medalRepository: MedalRepository,
    private val medalAcquisitionConditionRepository: MedalAcquisitionConditionRepository,
): IntegrationTest() {

    @Nested
    inner class AddMedalTest {

        @Test
        fun `새로운 메달을 추가한다`() {
            // given
            val name = "메달 이름"
            val introduction = "메달에 대한 소개"
            val activationIconUrl = "https://activation.png"
            val disableIconUrl = "https://disable.png"
            val conditionType = MedalAcquisitionConditionType.ADD_REVIEW
            val conditionCount = 3
            val acquisitionDescription = "획득 조건 설명"

            val request = AddMedalRequest(
                name = name,
                introduction = introduction,
                activationIconUrl = activationIconUrl,
                disableIconUrl = disableIconUrl,
                conditionType = conditionType,
                conditionCount = conditionCount,
                acquisitionDescription = acquisitionDescription
            )

            // when
            adminMedalService.addMedal(request)

            // then
            val medals = medalRepository.findAll()
            val medalAcquisitionConditions = medalAcquisitionConditionRepository.findAll()
            assertAll({
                assertThat(medals).hasSize(1)
                assertMedal(medal = medals[0], name = name, introduction = introduction, activationIconUrl = activationIconUrl, disableIconUrl = disableIconUrl)

                assertThat(medalAcquisitionConditions).hasSize(1)
                assertMedalAcquisitionCondition(medalAcquisitionCondition = medalAcquisitionConditions[0], medalId = medals[0].id, conditionType = conditionType, conditionCount = conditionCount, acquisitionDescription = acquisitionDescription)
            })

        }

    }

    @Nested
    inner class UpdateMedalTest {

        @Test
        fun `특정 메달의 정보를 수정한다`() {
            // given
            val name = "메달 이름"
            val introduction = "메달에 대한 소개"
            val activationIconUrl = "https://activation.png"
            val disableIconUrl = "https://disable.png"

            val request = UpdateMedalRequest(
                name = name,
                introduction = introduction,
                activationIconUrl = activationIconUrl,
                disableIconUrl = disableIconUrl
            )

            val medal = MedalFixture.create("메달 이름", MedalAcquisitionConditionType.ADD_STORE, 3)
            medalRepository.save(medal)

            // when
            adminMedalService.updateMedal(medal.id, request)

            // then
            val medals = medalRepository.findAll()
            val medalAcquisitionConditions = medalAcquisitionConditionRepository.findAll()
            assertAll({
                assertThat(medals).hasSize(1)
                assertMedal(medal = medals[0], name = name, introduction = introduction, activationIconUrl = activationIconUrl, disableIconUrl = disableIconUrl)

                assertThat(medalAcquisitionConditions).hasSize(1)
            })
        }

        @Test
        fun `존재하지 않는 메달을 수정하려하면 NotFound 에러가 발생한다`() {
            // given
            val notFoundMedalId = -1L

            val request = UpdateMedalRequest(
                name = "메달 이름",
                introduction = "메달 소개",
                activationIconUrl = "https://activation.png",
                disableIconUrl = "https://disable.png"
            )

            // when & then
            assertThatThrownBy { adminMedalService.updateMedal(notFoundMedalId, request) }.isInstanceOf(NotFoundException::class.java)
        }

    }

    private fun assertMedal(medal: Medal, name: String, introduction: String, activationIconUrl: String, disableIconUrl: String) {
        assertThat(medal.name).isEqualTo(name)
        assertThat(medal.introduction).isEqualTo(introduction)
        assertThat(medal.medalImage.activationIconUrl).isEqualTo(activationIconUrl)
        assertThat(medal.medalImage.disableIconUrl).isEqualTo(disableIconUrl)
    }

    private fun assertMedalAcquisitionCondition(medalAcquisitionCondition: MedalAcquisitionCondition, medalId: Long, conditionType: MedalAcquisitionConditionType, conditionCount: Int, acquisitionDescription: String) {
        assertThat(medalAcquisitionCondition.medal.id).isEqualTo(medalId)
        assertThat(medalAcquisitionCondition.conditionType).isEqualTo(conditionType)
        assertThat(medalAcquisitionCondition.count).isEqualTo(conditionCount)
        assertThat(medalAcquisitionCondition.description).isEqualTo(acquisitionDescription)
    }

}
