package com.depromeet.threedollar.domain.mongo.domain.bossservice.registration

import com.depromeet.threedollar.common.model.EnumModel

enum class BossRegistrationRejectReasonType(
    private val bodyFormat: String,
) : EnumModel {

    UNCLEAR_STORE_NAME(bodyFormat = "가게 이름이 명확하지 않아요."),
    MISLEADING_STORE_NAME(bodyFormat = "가게 이름이 오해의 소지가 있어 이번에는 아쉽게도 승인되지 않았어요."),
    INVALID_BUSINESS_NUMBER(bodyFormat = "사업자 등록 번호 조회 결과, 정확하지 않아 이번에는 아쉽게도 승인되지 않았어요."),
    INVALID_CONTACTS_NUMBER(bodyFormat = "전화 번호가 정확하지 않아 이번에는 아쉽게도 승인되지 않았어요."),
    UNMATCHED_STORE_CATEGORY(bodyFormat = "다른 정보들이랑 일치하지 않는 카테고리에요."),
    UNCLEAR_STORE_CERTIFICATION_PHOTO_URL(bodyFormat = "가게 인증 사진을 조금 더 멋있게 찍어볼까요?\n가게 인증 사진에 가게의 개성이 잘 드러나지 않아 아쉽게도 승인되지 않았어요."),
    INVISIBLE_STORE(bodyFormat = "가게 인증 사진에 가게가 잘 보이지 않아 아쉽게도 승인되지 않았어요."),
    ;

    override fun getDescription(): String {
        return bodyFormat
    }

    override fun getKey(): String {
        return this.name
    }

}
