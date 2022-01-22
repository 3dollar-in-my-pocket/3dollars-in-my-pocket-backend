package com.depromeet.threedollar.domain.user.domain.medal

import com.depromeet.threedollar.domain.user.domain.ObjectMother

@ObjectMother
object MedalCreator {

    @JvmOverloads
    @JvmStatic
    fun create(
        name: String,
        introduction: String = "introduction",
        activationIconUrl: String = "iconUrl",
        disableIconUrl: String = "disableIconUrl",
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
