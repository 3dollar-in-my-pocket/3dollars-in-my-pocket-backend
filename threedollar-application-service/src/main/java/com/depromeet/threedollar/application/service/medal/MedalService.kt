package com.depromeet.threedollar.application.service.medal

import com.depromeet.threedollar.application.service.medal.dto.response.MedalResponse
import com.depromeet.threedollar.domain.config.cache.CacheType.CacheKey.MEDALS
import com.depromeet.threedollar.domain.domain.medal.MedalRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MedalService(
    private val medalRepository: MedalRepository
) {

    @Cacheable(key = "'ALL'", value = [MEDALS])
    @Transactional(readOnly = true)
    fun getAllMedals(): List<MedalResponse> {
        return medalRepository.findAllActiveMedals()
            .map { MedalResponse.of(it) }
    }

}
