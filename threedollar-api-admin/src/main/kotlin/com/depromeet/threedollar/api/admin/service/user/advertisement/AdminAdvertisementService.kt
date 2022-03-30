package com.depromeet.threedollar.api.admin.service.user.advertisement

import com.depromeet.threedollar.api.admin.service.user.advertisement.dto.request.AddAdvertisementRequest
import com.depromeet.threedollar.api.admin.service.user.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.api.admin.service.user.advertisement.dto.request.UpdateAdvertisementRequest
import com.depromeet.threedollar.api.admin.service.user.advertisement.dto.response.AdvertisementsWithPagingResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.type.CacheType.CacheKey.ADVERTISEMENTS
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.Advertisement
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementDetail
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminAdvertisementService(
    private val advertisementRepository: AdvertisementRepository
) {

    @CacheEvict(key = "{#request.position, #request.platform}", value = [ADVERTISEMENTS])
    @Transactional
    fun addAdvertisement(request: AddAdvertisementRequest) {
        advertisementRepository.save(request.toEntity())
    }

    @CacheEvict(key = "{#request.position, #request.platform}", value = [ADVERTISEMENTS])
    @Transactional
    fun updateAdvertisement(advertisementId: Long, request: UpdateAdvertisementRequest) {
        val advertisement = findAdvertisementById(advertisementId)
        request.let {
            val detail = AdvertisementDetail.of(it.title, it.subTitle, it.imageUrl, it.linkUrl, it.bgColor, it.fontColor)
            advertisement.update(it.position, it.platform, it.startDateTime, it.endDateTime, detail)
        }
    }

    @CacheEvict(allEntries = true, value = [ADVERTISEMENTS])
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
        return advertisementRepository.findAdvertisementById(advertisementId)
            ?: throw NotFoundException("해당하는 id (${advertisementId})을 가진 광고는 존재하지 않습니다.", ErrorCode.NOTFOUND_ADVERTISEMENT)
    }

}

