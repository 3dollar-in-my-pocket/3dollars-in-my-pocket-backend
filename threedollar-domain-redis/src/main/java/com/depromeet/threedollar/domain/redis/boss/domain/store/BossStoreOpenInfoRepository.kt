package com.depromeet.threedollar.domain.redis.boss.domain.store

import org.springframework.data.repository.CrudRepository

interface BossStoreOpenInfoRepository : CrudRepository<BossStoreOpenInfo, String> {

    override fun findAll(): List<BossStoreOpenInfo>

}
