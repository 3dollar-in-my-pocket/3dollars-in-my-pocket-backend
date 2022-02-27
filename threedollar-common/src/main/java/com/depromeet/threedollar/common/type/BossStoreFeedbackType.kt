package com.depromeet.threedollar.common.type

import com.depromeet.threedollar.common.model.EnumModel

enum class BossStoreFeedbackType(
    private val description: String
) : EnumModel {

    FOOD_IS_DELICIOUS("음식이 맛있어요"),
    BOSS_IS_KIND("사장님이 친절해요"),
    EASY_TO_EAT("먹기 간편해요"),
    PRICE_IS_CHEAP("가격이 저렴해요"),
    THERE_ARE_PLACES_TO_EAT_AROUND("주변에 먹을 곳이 있어요"),
    PLATING_IS_BEAUTIFUL("플레이팅이 이뻐요"),
    ;

    override fun getKey(): String {
        return name
    }

    override fun getDescription(): String {
        return description
    }

    companion object {
        fun of(key: String): BossStoreFeedbackType? {
            return values().find { it.key == key }
        }
    }

}