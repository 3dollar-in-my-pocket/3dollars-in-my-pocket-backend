package com.depromeet.threedollar.common.type.template

import com.depromeet.threedollar.common.type.PushOptionsType

/**
 * 푸시 알림 메시지 템플릿
 */
enum class PushMessageTemplateType(
    val title: String,
    val body: String,
    val pushOptions: PushOptionsType,
) {

    BOSS_REGISTRATION_APPROVED_MESSAGE(
        title = "가슴속 3천원 사장님 가입 신청이 승인되었습니다",
        body = "가슴속 3천원 사장님 가입 신청이 승인되었습니다",
        pushOptions = PushOptionsType.PUSH,
    ),
    BOSS_REGISTRATION_DENIED_MESSAGE(
        title = "가슴속 3천원 사장님 가입 신청이 반려되었습니다",
        body = "가슴속 3천원 사장님 가입 신청이 반려되었습니다",
        pushOptions = PushOptionsType.PUSH,
    )

}
