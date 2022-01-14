package com.depromeet.threedollar.document.boss.document.account

import com.depromeet.threedollar.document.common.document.AuditingTimeDocument
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document("boss_account_v1")
class BossAccount(
    val name: String,
    val socialInfo: SocialInfo,
    val businessNumber: String,
    val phoneNumber: String
) : AuditingTimeDocument() {

    @MongoId
    lateinit var id: String

}
