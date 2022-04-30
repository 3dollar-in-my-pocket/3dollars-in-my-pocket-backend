package com.depromeet.threedollar.domain.rds.user.domain.medal

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

@TestFixture
object MedalCreator {

    @JvmOverloads
    @JvmStatic
    fun create(
        name: String,
        conditionType: MedalAcquisitionConditionType = MedalAcquisitionConditionType.ADD_STORE,
        conditionCount: Int = 3
    ): Medal {
        return Medal.builder()
            .name(name)
            .introduction("메달 설명")
            .activationIconUrl("iconUrl")
            .disableIconUrl("disableUrl")
            .conditionType(conditionType)
            .conditionCount(conditionCount)
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
        conditionCount: Int = 3,
        acquisitionDescription: String? = null
    ): Medal {
        return Medal.builder()
            .name(name)
            .introduction(introduction)
            .activationIconUrl(activationIconUrl)
            .disableIconUrl(disableIconUrl)
            .conditionType(conditionType)
            .conditionCount(conditionCount)
            .acquisitionDescription(acquisitionDescription)
            .build()
    }

}
