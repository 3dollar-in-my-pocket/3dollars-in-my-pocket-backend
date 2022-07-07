package com.depromeet.threedollar.domain.mongo.domain.commonservice.device

import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.repository.DeviceRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface DeviceRepository : MongoRepository<Device, String>, DeviceRepositoryCustom
