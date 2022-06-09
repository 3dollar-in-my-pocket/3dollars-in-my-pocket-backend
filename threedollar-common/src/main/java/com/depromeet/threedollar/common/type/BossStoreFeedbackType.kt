package com.depromeet.threedollar.common.type

import com.depromeet.threedollar.common.model.EnumModel

enum class BossStoreFeedbackType(
    private val description: String,
    val emoji: String,
) : EnumModel {

    HANDS_ARE_FAST("손이 빠르세요", "👐"),
    FOOD_IS_DELICIOUS("음식이 맛있어요", "😋"),
    HYGIENE_IS_CLEAN("위생이 청결해요", "✨"),
    BOSS_IS_KIND("사장님이 친절해요", "🙏"),
    CAN_PAY_BY_CARD("카드 결제 가능해요", "💳"),
    GOOD_VALUE_FOR_MONEY("가성비가 좋아요", "💰"),
    GOOD_TO_EAT_IN_ONE_BITE("한입에 먹기 좋아요", "👌"),
    GOT_A_BONUS("덤도 주셨어요", "👍"),
    ;

    override fun getKey(): String {
        return name
    }

    override fun getDescription(): String {
        return description
    }

}
