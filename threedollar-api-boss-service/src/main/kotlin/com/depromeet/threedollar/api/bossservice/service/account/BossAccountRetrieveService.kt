package com.depromeet.threedollar.api.bossservice.service.account

import com.depromeet.threedollar.api.bossservice.service.account.dto.response.BossAccountInfoResponse
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceRepository
import org.springframework.stereotype.Service

@Service
class BossAccountRetrieveService(
    private val bossAccountRepository: BossAccountRepository,
    private val deviceRepository: DeviceRepository,
) {

    fun getBossAccountInfo(bossId: String): BossAccountInfoResponse {
        return BossAccountInfoResponse.of(
            bossAccount = BossAccountServiceHelper.findBossAccountByRegistrationId(bossAccountRepository, bossId),
            isSetupNotification = deviceRepository.existsDeviceByAccountIdAndType(accountId = bossId, accountType = AccountType.BOSS_ACCOUNT),
        )
    }

}
