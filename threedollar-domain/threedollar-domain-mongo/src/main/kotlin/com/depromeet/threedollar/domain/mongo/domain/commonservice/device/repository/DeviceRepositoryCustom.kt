package com.depromeet.threedollar.domain.mongo.domain.commonservice.device.repository

import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.Device

interface DeviceRepositoryCustom {

    fun findDeviceByAccountIdAndType(accountId: String, accountType: AccountType): Device?

    fun existsDeviceByAccountIdAndType(accountId: String, accountType: AccountType): Boolean

    fun findAllDevicesByAccountIdsAndType(accountIds: List<String>, accountType: AccountType): List<Device>

}
