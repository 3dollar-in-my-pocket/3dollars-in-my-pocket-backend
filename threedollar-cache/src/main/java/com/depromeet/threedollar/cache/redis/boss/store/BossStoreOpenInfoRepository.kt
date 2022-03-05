package com.depromeet.threedollar.cache.redis.boss.store

import org.springframework.data.repository.CrudRepository

interface BossStoreOpenInfoRepository : CrudRepository<BossStoreOpenInfo, String> {

    override fun findAll(): List<BossStoreOpenInfo>

}
