package com.depromeet.threedollar.api.core.service.service.userservice.medal

import com.depromeet.threedollar.api.core.service.service.userservice.medal.dto.response.MedalResponse
import com.depromeet.threedollar.common.type.CacheType.CacheKey.MEDALS
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MedalService(
    private val medalRepository: MedalRepository,
) {

    @Cacheable(cacheNames = [MEDALS], key = "'ALL'")
    @Transactional(readOnly = true)
    fun getAllMedals(): List<MedalResponse> {
        val activeMedals = medalRepository.findAllActiveMedals()
        return activeMedals.map { medal -> MedalResponse.of(medal) }
    }

}
