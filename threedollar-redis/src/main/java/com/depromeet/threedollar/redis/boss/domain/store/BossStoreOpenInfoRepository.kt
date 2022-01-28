package com.depromeet.threedollar.redis.boss.domain.store

import org.springframework.data.repository.CrudRepository

interface BossStoreOpenInfoRepository : CrudRepository<BossStoreOpenInfo, String> {

    override fun findAll(): List<BossStoreOpenInfo>

}
