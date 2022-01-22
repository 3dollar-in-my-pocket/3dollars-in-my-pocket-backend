package com.depromeet.threedollar.domain.user.domain.medal

import com.depromeet.threedollar.domain.user.domain.ObjectMother

@ObjectMother
object MedalCreator {

    @JvmOverloads
    @JvmStatic
    fun create(
        name: String,
        conditionType: MedalAcquisitionConditionType = MedalAcquisitionConditionType.ADD_STORE,
        count: Int = 3
    ): Medal {
        return Medal.builder()
            .name(name)
            .introduction("메달 설명")
            .activationIconUrl("iconUrl")
            .disableIconUrl("disableUrl")
            .conditionType(conditionType)
            .count(count)
            .build()
    }

    @JvmOverloads
    @JvmStatic
    fun create(
        name: String,
        introduction: String,
        activationIconUrl: String,
        disableIconUrl: String,
        conditionType: MedalAcquisitionConditionType = MedalAcquisitionConditionType.ADD_STORE,
        count: Int = 3
    ): Medal {
        return Medal.builder()
            .name(name)
            .introduction(introduction)
            .activationIconUrl(activationIconUrl)
            .disableIconUrl(disableIconUrl)
            .conditionType(conditionType)
            .count(count)
            .build()
    }

}
