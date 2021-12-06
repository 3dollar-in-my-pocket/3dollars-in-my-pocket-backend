package com.depromeet.threedollar.api.controller.medal

import com.depromeet.threedollar.api.service.medal.dto.response.MedalResponse
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionRepository
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionType
import com.depromeet.threedollar.domain.domain.medal.MedalCreator
import com.depromeet.threedollar.domain.domain.medal.MedalRepository
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
class MedalControllerTest(
    private val mockMvc: MockMvc,
    private val medalRepository: MedalRepository,
    private val medalAcquisitionConditionRepository: MedalAcquisitionConditionRepository
) {

    @AfterEach
    fun cleanUp() {
        medalAcquisitionConditionRepository.deleteAllInBatch()
        medalRepository.deleteAllInBatch()
    }

    @Test
    fun 모든_메달_목록을_조회한다() {
        // given
        val medalOne = MedalCreator.create("붕어빵 챌린저", "붕어빵 챌린저 아이콘 URL", MedalAcquisitionConditionType.ADD_STORE, 1)
        val medalTwo = MedalCreator.create("붕어빵 전문가", "붕어빵 전문가 아이콘 URL", MedalAcquisitionConditionType.ADD_STORE, 3)
        medalRepository.saveAll(listOf(medalOne, medalTwo))

        // when & then
        mockMvc.get("/api/v1/medals")
            .andDo {
                print()
            }
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data") { hasSize<MedalResponse>(2) }

                    jsonPath("$.data[0].medalId") { value(medalOne.id) }
                    jsonPath("$.data[0].name") { value(medalOne.name) }
                    jsonPath("$.data[0].iconUrl") { value(medalOne.iconUrl) }

                    jsonPath("$.data[1].medalId") { value(medalTwo.id) }
                    jsonPath("$.data[1].name") { value(medalTwo.name) }
                    jsonPath("$.data[1].iconUrl") { value(medalTwo.iconUrl) }
                }
            }
    }

}
