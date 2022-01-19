package com.depromeet.threedollar.document.boss.document.account

import com.depromeet.threedollar.document.common.document.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_account_v1")
class BossAccount(
    val name: String,
    val socialInfo: BossAccountSocialInfo,
    val businessNumber: BusinessNumber? = null,
    val phoneNumber: PhoneNumber? = null
) : BaseDocument()
