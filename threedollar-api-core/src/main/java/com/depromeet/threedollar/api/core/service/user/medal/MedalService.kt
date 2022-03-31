package com.depromeet.threedollar.api.core.service.user.medal

import com.depromeet.threedollar.api.core.service.user.medal.dto.response.MedalResponse
import com.depromeet.threedollar.common.type.CacheType.CacheKey.MEDALS
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MedalService(
    private val medalRepository: MedalRepository
) {

    @Cacheable(cacheNames = [MEDALS], key = "'ALL'")
    @Transactional(readOnly = true)
    fun getAllMedals(): List<MedalResponse> {
        return medalRepository.findAllActiveMedals()
            .map { MedalResponse.of(it) }
    }

}
