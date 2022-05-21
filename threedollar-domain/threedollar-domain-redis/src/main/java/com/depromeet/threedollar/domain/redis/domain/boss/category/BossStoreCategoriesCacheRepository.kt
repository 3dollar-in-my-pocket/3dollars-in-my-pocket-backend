package com.depromeet.threedollar.domain.redis.domain.boss.category

import java.time.Duration
import com.depromeet.threedollar.common.utils.JsonUtils
import com.depromeet.threedollar.domain.redis.core.StringRedisKey
import com.depromeet.threedollar.domain.redis.domain.boss.category.model.BossStoreCategoryCacheModel

class BossStoreCategoryKey : StringRedisKey<List<BossStoreCategoryCacheModel>> {

    override fun getKey(): String {
        return "boss:v2:store:categories"
    }

    override fun deserializeValue(value: String?): List<BossStoreCategoryCacheModel>? {
        return value?.let { JsonUtils.toList(it, BossStoreCategoryCacheModel::class.java) }
    }

    override fun serializeValue(value: List<BossStoreCategoryCacheModel>): String {
        return JsonUtils.toJson(value)
    }

    override fun getTtl(): Duration? {
        return Duration.ofHours(1)
    }

    companion object {
        fun of(): BossStoreCategoryKey {
            return BossStoreCategoryKey()
        }
    }

}
