package com.depromeet.threedollar.document.boss.document.account

import com.depromeet.threedollar.common.model.EnumModel

enum class PushSettingsStatus(
    private val description: String
) : EnumModel {

    ON("푸시 알림 활성화"),
    OFF("푸시 알림 비활성화"),
    ;

    override fun getKey(): String {
        return name
    }

    override fun getDescription(): String {
        return description
    }

}
