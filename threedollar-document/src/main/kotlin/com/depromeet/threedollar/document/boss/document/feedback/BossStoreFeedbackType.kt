package com.depromeet.threedollar.document.boss.document.feedback

import com.depromeet.threedollar.common.model.EnumModel

enum class BossStoreFeedbackType(
    private val description: String
) : EnumModel {

    COFFEE_IS_DELICIOUS("커피가 맛있어요"),
    ;

    override fun getKey(): String {
        return name
    }

    override fun getDescription(): String {
        return description
    }

}
