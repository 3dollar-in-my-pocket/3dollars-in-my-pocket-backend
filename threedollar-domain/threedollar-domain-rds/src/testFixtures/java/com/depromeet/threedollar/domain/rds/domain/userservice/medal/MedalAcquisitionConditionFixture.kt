package com.depromeet.threedollar.domain.rds.domain.userservice.medal

import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object MedalAcquisitionConditionFixture {

    @JvmStatic
    fun create(
        medal: Medal,
        conditionType: MedalAcquisitionConditionType,
        count: Int,
        description: String,
    ): MedalAcquisitionCondition {
        return MedalAcquisitionCondition.builder()
            .medal(medal)
            .conditionType(conditionType)
            .count(count)
            .description(description)
            .build()
    }

}
