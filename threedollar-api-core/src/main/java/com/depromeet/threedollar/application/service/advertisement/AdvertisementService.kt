package com.depromeet.threedollar.application.service.advertisement

import com.depromeet.threedollar.application.service.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.application.service.advertisement.dto.response.AdvertisementResponse
import com.depromeet.threedollar.common.type.CacheType.CacheKey.ADVERTISEMENT
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AdvertisementService(
    private val advertisementRepository: AdvertisementRepository
) {

    @Cacheable(key = "{#request.position, #request.platform}", value = [ADVERTISEMENT])
    @Transactional(readOnly = true)
    fun retrieveAdvertisements(request: RetrieveAdvertisementsRequest): List<AdvertisementResponse> {
        return advertisementRepository.findActivatedAdvertisementsByPositionAndPlatformAfterDate(request.position, request.platform, LocalDateTime.now()).asSequence()
            .map { AdvertisementResponse.of(it.detail) }
            .shuffled()
            .toList()
    }

}
