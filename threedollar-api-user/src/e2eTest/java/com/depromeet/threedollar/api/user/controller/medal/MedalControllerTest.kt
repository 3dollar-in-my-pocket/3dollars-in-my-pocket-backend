package com.depromeet.threedollar.api.user.controller.medal

import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import com.depromeet.threedollar.api.core.service.user.medal.dto.response.MedalResponse
import com.depromeet.threedollar.api.user.controller.SetupControllerTest
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionRepository
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalCreator
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalRepository

class MedalControllerTest(
    private val medalRepository: MedalRepository,
    private val medalAcquisitionConditionRepository: MedalAcquisitionConditionRepository
) : SetupControllerTest() {

    @AfterEach
    fun cleanUp() {
        medalAcquisitionConditionRepository.deleteAllInBatch()
        medalRepository.deleteAllInBatch()
    }

    @Test
    fun 모든_메달_목록을_조회한다() {
        // given
        val medalOne = MedalCreator.create("붕어빵 챌린저")
        val medalTwo = MedalCreator.create("붕어빵 전문가")
        medalRepository.saveAll(listOf(medalOne, medalTwo))

        // when & then
        mockMvc.get("/v1/medals")
            .andDo {
                print()
            }
            .andExpect {
                status { isOk() }

                jsonPath("$.data", hasSize<MedalResponse>(2))

                jsonPath("$.data[0].medalId") { value(medalOne.id) }
                jsonPath("$.data[0].name") { value(medalOne.name) }
                jsonPath("$.data[0].iconUrl") { value(medalOne.activationIconUrl) }
                jsonPath("$.data[0].disableIconUrl") { value(medalOne.disableIconUrl) }
                jsonPath("$.data[0].introduction") { value(medalOne.introduction) }
                jsonPath("$.data[0].acquisition.description") { value(medalOne.acquisitionCondition.description) }

                jsonPath("$.data[1].medalId") { value(medalTwo.id) }
                jsonPath("$.data[1].name") { value(medalTwo.name) }
                jsonPath("$.data[1].iconUrl") { value(medalTwo.activationIconUrl) }
                jsonPath("$.data[1].disableIconUrl") { value(medalTwo.disableIconUrl) }
                jsonPath("$.data[1].introduction") { value(medalTwo.introduction) }
                jsonPath("$.data[1].acquisition.description") { value(medalTwo.acquisitionCondition.description) }
            }
    }

}
