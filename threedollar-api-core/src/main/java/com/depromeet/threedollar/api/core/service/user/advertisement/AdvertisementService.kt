package com.depromeet.threedollar.api.core.service.user.advertisement

import com.depromeet.threedollar.api.core.service.user.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.api.core.service.user.advertisement.dto.response.AdvertisementResponse
import com.depromeet.threedollar.common.type.CacheType.CacheKey.ADVERTISEMENTS
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AdvertisementService(
    private val advertisementRepository: AdvertisementRepository
) {

    @Cacheable(key = "{#request.position, #request.platform}", value = [ADVERTISEMENTS])
    @Transactional(readOnly = true)
    fun getAdvertisements(request: RetrieveAdvertisementsRequest): List<AdvertisementResponse> {
        val advertisements = advertisementRepository.findActivatedAdvertisementsByPositionAndPlatformAfterDate(request.position, request.platform, LocalDateTime.now())
        return advertisements.map { AdvertisementResponse.of(it.detail) }
    }

}
