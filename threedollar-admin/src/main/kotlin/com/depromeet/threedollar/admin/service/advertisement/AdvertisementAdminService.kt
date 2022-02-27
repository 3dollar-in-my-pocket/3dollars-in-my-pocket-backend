package com.depromeet.threedollar.admin.service.advertisement

import com.depromeet.threedollar.admin.service.advertisement.dto.request.AddAdvertisementRequest
import com.depromeet.threedollar.admin.service.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.admin.service.advertisement.dto.request.UpdateAdvertisementRequest
import com.depromeet.threedollar.admin.service.advertisement.dto.response.AdvertisementsWithPagingResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.type.CacheType.CacheKey.ADVERTISEMENT
import com.depromeet.threedollar.domain.user.domain.advertisement.Advertisement
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdvertisementAdminService(
    private val advertisementRepository: AdvertisementRepository
) {

    @CacheEvict(key = "{#request.position, #request.platform}", value = [ADVERTISEMENT])
    @Transactional
    fun addAdvertisement(request: AddAdvertisementRequest) {
        advertisementRepository.save(request.toEntity())
    }

    @CacheEvict(key = "{#request.position, #request.platform}", value = [ADVERTISEMENT])
    @Transactional
    fun updateAdvertisement(advertisementId: Long, request: UpdateAdvertisementRequest) {
        val advertisement = findAdvertisementById(advertisementId)
        request.let {
            advertisement.update(it.position, it.platform, it.title, it.subTitle, it.imageUrl,
                it.linkUrl, it.bgColor, it.fontColor, it.startDateTime, it.endDateTime)
        }
    }

    @CacheEvict(allEntries = true, value = [ADVERTISEMENT])
    @Transactional
    fun deleteAdvertisement(advertisementId: Long) {
        val advertisement = findAdvertisementById(advertisementId)
        advertisementRepository.delete(advertisement)
    }

    @Transactional(readOnly = true)
    fun retrieveAdvertisements(request: RetrieveAdvertisementsRequest): AdvertisementsWithPagingResponse {
        return AdvertisementsWithPagingResponse.of(
            advertisements = advertisementRepository.findAllWithPage(request.size, request.page - 1),
            totalCounts = advertisementRepository.findAllCounts()
        )
    }

    fun findAdvertisementById(advertisementId: Long): Advertisement {
        return advertisementRepository.findByIdOrNull(advertisementId)
            ?: throw NotFoundException("해당하는 id (${advertisementId})을 가진 광고는 존재하지 않습니다.")
    }

}

