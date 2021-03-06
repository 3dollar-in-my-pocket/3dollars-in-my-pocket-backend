package com.depromeet.threedollar.domain.mongo.domain.commonservice.device.repository

import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.Device
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.where
import org.springframework.stereotype.Repository

@Repository
class DeviceRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : DeviceRepositoryCustom {

    override fun findDeviceByAccountIdAndType(accountId: String, accountType: AccountType): Device? {
        return mongoTemplate.findOne(Query()
            .addCriteria(Device::accountId isEqualTo accountId)
            .addCriteria(Device::accountType isEqualTo accountType)
        )
    }

    override fun existsDeviceByAccountIdAndType(accountId: String, accountType: AccountType): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(Device::accountId isEqualTo accountId)
            .addCriteria(Device::accountType isEqualTo accountType), Device::class.java
        )
    }

    override fun findAllDevicesByAccountIdsAndType(accountIds: List<String>, accountType: AccountType): List<Device> {
        return mongoTemplate.find(Query()
            .addCriteria(where(Device::accountId).`in`(accountIds))
            .addCriteria(where(Device::accountType).isEqualTo(accountType)), Device::class.java
        )
    }

}
