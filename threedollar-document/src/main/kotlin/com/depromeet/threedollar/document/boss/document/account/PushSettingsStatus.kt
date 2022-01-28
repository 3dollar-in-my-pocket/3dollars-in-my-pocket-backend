package com.depromeet.threedollar.document.boss.document.account

import com.depromeet.threedollar.common.model.EnumModel

enum class PushSettingsStatus(
    private val description: String
) : EnumModel {

    ON("ON"),
    OFF("OFF"),
    ;

    override fun getKey(): String {
        return name
    }

    override fun getDescription(): String {
        return description
    }

}
