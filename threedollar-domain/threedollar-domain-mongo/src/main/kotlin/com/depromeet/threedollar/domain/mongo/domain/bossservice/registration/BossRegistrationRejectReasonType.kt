package com.depromeet.threedollar.domain.mongo.domain.bossservice.registration

import com.depromeet.threedollar.common.model.EnumModel

enum class BossRegistrationRejectReasonType(
    private val description: String,
) : EnumModel {

    INVALID_BUSINESS_NUMBER(description = "확인되지 않는 사업자 번호로 인해 반려되었습니다"),
    INVALID_CERTIFICATION_PHOTO_URL(description = "확인되지 않은 인증 사진으로 인해 반려되었습니다"),
    ;

    override fun getDescription(): String {
        return description
    }

    override fun getKey(): String {
        return this.name
    }

}
