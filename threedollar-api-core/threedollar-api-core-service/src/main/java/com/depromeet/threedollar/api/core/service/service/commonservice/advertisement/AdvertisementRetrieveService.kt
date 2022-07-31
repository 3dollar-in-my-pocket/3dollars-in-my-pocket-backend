package com.depromeet.threedollar.api.core.service.service.commonservice.advertisement

import com.depromeet.threedollar.api.core.service.service.commonservice.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.api.core.service.service.commonservice.advertisement.dto.response.AdvertisementResponse
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.type.CacheType.CacheKey.ADVERTISEMENTS
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AdvertisementRetrieveService(
    private val advertisementRepository: AdvertisementRepository,
) {

    @Cacheable(cacheNames = [ADVERTISEMENTS], key = "{#applicationType, #request.position, #request.platform}")
    @Transactional(readOnly = true)
    fun getAdvertisements(applicationType: ApplicationType, request: RetrieveAdvertisementsRequest): List<AdvertisementResponse> {
        val advertisements = advertisementRepository.findActivatedAdvertisementsByPositionAndPlatformAfterDate(applicationType, request.position, request.platform, LocalDateTime.now())
        return advertisements.map { advertisement -> AdvertisementResponse.of(advertisement.detail) }
    }

}
