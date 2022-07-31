package com.depromeet.threedollar.domain.rds.domain.userservice.medal

import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object MedalAcquisitionConditionFixture {

    @JvmOverloads
    @JvmStatic
    fun create(
        medal: Medal,
        conditionType: MedalAcquisitionConditionType = MedalAcquisitionConditionType.ADD_REVIEW,
        count: Int = 3,
        description: String = "메달 획득 조건에 대한 설명",
    ): MedalAcquisitionCondition {
        return MedalAcquisitionCondition.builder()
            .medal(medal)
            .conditionType(conditionType)
            .count(count)
            .description(description)
            .build()
    }

}
