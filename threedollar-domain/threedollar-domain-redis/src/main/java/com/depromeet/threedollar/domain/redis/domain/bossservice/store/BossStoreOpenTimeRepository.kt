package com.depromeet.threedollar.domain.redis.domain.bossservice.store

import java.time.LocalDateTime
import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository

@Repository
class BossStoreOpenTimeRepository(
    private val bossStoreOpenRedisRepository: StringRedisRepository<BossStoreOpenTimeKey, LocalDateTime>,
) {

    fun get(bossStoreId: String): LocalDateTime? {
        val key = BossStoreOpenTimeKey(bossStoreId)
        return bossStoreOpenRedisRepository.get(key)
    }

    fun exists(bossStoreId: String): Boolean {
        val key = BossStoreOpenTimeKey(bossStoreId)
        return bossStoreOpenRedisRepository.get(key) != null
    }

    fun set(bossStoreId: String, openDateTime: LocalDateTime) {
        val key = BossStoreOpenTimeKey.of(bossStoreId)
        bossStoreOpenRedisRepository.set(key, openDateTime)
    }

    fun delete(bossStoreId: String) {
        val key = BossStoreOpenTimeKey.of(bossStoreId)
        bossStoreOpenRedisRepository.del(key)
    }

}
