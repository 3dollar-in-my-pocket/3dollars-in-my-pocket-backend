package com.depromeet.threedollar.domain.redis.domain.bossservice.category

interface BossStoreCategoryCacheRepository {

    fun getCache(): List<BossStoreCategoryCacheModel>?

    fun setCache(categories: List<BossStoreCategoryCacheModel>)

    fun clean()

}
