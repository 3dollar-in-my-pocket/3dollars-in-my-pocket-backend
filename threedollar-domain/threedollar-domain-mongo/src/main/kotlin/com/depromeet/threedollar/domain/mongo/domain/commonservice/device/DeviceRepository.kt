package com.depromeet.threedollar.domain.mongo.domain.commonservice.device

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.repository.DeviceRepositoryCustom

interface DeviceRepository : MongoRepository<Device, String>, DeviceRepositoryCustom
