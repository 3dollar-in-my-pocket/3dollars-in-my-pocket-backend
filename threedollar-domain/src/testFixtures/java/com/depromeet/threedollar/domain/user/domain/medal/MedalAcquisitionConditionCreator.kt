package com.depromeet.threedollar.domain.user.domain.medal

import com.depromeet.threedollar.domain.user.domain.ObjectMother

@ObjectMother
object MedalAcquisitionConditionCreator {

    @JvmStatic
    fun create(
        medal: Medal,
        conditionType: MedalAcquisitionConditionType,
        count: Int,
        description: String
    ): MedalAcquisitionCondition {
        return MedalAcquisitionCondition.builder()
            .medal(medal)
            .conditionType(conditionType)
            .count(count)
            .description(description)
            .build()
    }

}
