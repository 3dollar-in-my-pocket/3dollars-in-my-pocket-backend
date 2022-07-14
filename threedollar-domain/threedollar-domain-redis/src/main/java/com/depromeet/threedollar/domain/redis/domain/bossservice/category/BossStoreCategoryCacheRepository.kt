package com.depromeet.threedollar.domain.redis.domain.bossservice.category

import org.springframework.stereotype.Repository

@Repository
interface BossStoreCategoryCacheRepository {

    fun getAll(): List<BossStoreCategoryCacheModel>?

    fun set(categories: List<BossStoreCategoryCacheModel>)

}
