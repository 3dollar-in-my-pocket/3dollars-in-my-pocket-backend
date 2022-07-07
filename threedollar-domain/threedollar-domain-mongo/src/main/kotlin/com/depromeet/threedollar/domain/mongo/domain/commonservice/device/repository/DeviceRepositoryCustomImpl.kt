package com.depromeet.threedollar.domain.mongo.domain.commonservice.device.repository

import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.Device
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
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

}
