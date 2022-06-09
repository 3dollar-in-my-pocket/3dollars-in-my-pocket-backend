package com.depromeet.threedollar.domain.mongo.domain.bossservice.account

import java.time.LocalDateTime
import java.time.ZoneOffset
import org.springframework.data.mongodb.core.mapping.Document
import com.depromeet.threedollar.common.model.BusinessNumber
import com.depromeet.threedollar.domain.mongo.core.model.BaseDocument

/**
 * TODO: 개선 필요
 * 가입 승인 대기인 상태 ~ 가입 승인이 되었을 때, 세션을 유지하기 위해, Registration.Id -> Boss.Id로 공유해서 사용한다.
 * 다만 Id를 수동으로 주입해주어서, @createDate가 작동하지 않으니(@Version을 해주면 되지만), 애플리케이션에서 생성한다.
 * - BossAccount.status로 사장님 계정 상태를 관리하면, 그에 대한 로직들이 더 많을 것으로 예상되어서 이 방법을 사용했는데, 개선이 필요해보임.
 */
@Document("boss_account_v1")
class BossAccount(
    var name: String,
    val socialInfo: BossAccountSocialInfo,
    val businessNumber: BusinessNumber,
    var isSetupNotification: Boolean,
) : BaseDocument() {

    fun updateInfo(name: String, isSetupNotification: Boolean) {
        this.name = name
        this.isSetupNotification = isSetupNotification
    }

    companion object {
        fun of(
            bossId: String,
            name: String,
            socialId: String,
            socialType: BossAccountSocialType,
            businessNumber: BusinessNumber,
            isSetupNotification: Boolean = false,
        ): BossAccount {
            val bossAccount = BossAccount(
                name = name,
                socialInfo = BossAccountSocialInfo.of(socialId, socialType),
                businessNumber = businessNumber,
                isSetupNotification = isSetupNotification
            )
            bossAccount.id = bossId
            bossAccount.createdAt = LocalDateTime.now(ZoneOffset.UTC)
            return bossAccount
        }
    }

}
