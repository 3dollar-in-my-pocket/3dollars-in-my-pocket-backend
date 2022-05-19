package com.depromeet.threedollar.api.admin.service.user.medal

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import com.depromeet.threedollar.api.admin.service.user.medal.dto.request.AddMedalRequest
import com.depromeet.threedollar.api.admin.service.user.medal.dto.request.UpdateMedalRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionRepository
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalCreator
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalRepository

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class AdminMedalServiceTest(
    private val adminMedalService: AdminMedalService,
    private val medalRepository: MedalRepository,
    private val medalAcquisitionConditionRepository: MedalAcquisitionConditionRepository,
) {

    @AfterEach
    fun cleanUp() {
        medalAcquisitionConditionRepository.deleteAllInBatch()
        medalRepository.deleteAllInBatch()
    }

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
                medals[0]?.let {
                    assertThat(it.name).isEqualTo(name)
                    assertThat(it.introduction).isEqualTo(introduction)
                    assertThat(it.medalImage.activationIconUrl).isEqualTo(activationIconUrl)
                    assertThat(it.medalImage.disableIconUrl).isEqualTo(disableIconUrl)
                }

                assertThat(medalAcquisitionConditions).hasSize(1)
                medalAcquisitionConditions[0]?.let {
                    assertThat(it.medal.id).isEqualTo(medals[0].id)
                    assertThat(it.conditionType).isEqualTo(conditionType)
                    assertThat(it.count).isEqualTo(conditionCount)
                    assertThat(it.description).isEqualTo(acquisitionDescription)
                }
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

            val medal = MedalCreator.create("메달 이름", MedalAcquisitionConditionType.ADD_STORE, 3)
            medalRepository.save(medal)

            // when
            adminMedalService.updateMedal(medal.id, request)

            // then
            val medals = medalRepository.findAll()
            val medalAcquisitionConditions = medalAcquisitionConditionRepository.findAll()
            assertAll({
                assertThat(medals).hasSize(1)
                medals[0]?.let {
                    assertThat(it.name).isEqualTo(name)
                    assertThat(it.introduction).isEqualTo(introduction)
                    assertThat(it.medalImage.activationIconUrl).isEqualTo(activationIconUrl)
                    assertThat(it.medalImage.disableIconUrl).isEqualTo(disableIconUrl)
                }

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

}
