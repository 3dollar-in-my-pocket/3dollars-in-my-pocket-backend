package com.depromeet.threedollar.api.adminservice.service.userservice.advertisement

import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.dto.request.AddAdvertisementRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.dto.request.UpdateAdvertisementRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.dto.response.AdvertisementsWithPagingResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.type.CacheType.CacheKey.ADVERTISEMENTS
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.Advertisement
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementDetail
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementRepository

@Service
class AdminAdvertisementService(
    private val advertisementRepository: AdvertisementRepository,
) {

    @CacheEvict(cacheNames = [ADVERTISEMENTS], key = "{#request.position, #request.platform}")
    @Transactional
    fun addAdvertisement(request: AddAdvertisementRequest) {
        advertisementRepository.save(request.toEntity())
    }

    @CacheEvict(cacheNames = [ADVERTISEMENTS], key = "{#request.position, #request.platform}")
    @Transactional
    fun updateAdvertisement(advertisementId: Long, request: UpdateAdvertisementRequest) {
        val advertisement = findAdvertisementById(advertisementId)
        request.let {
            val detail = AdvertisementDetail.of(it.title, it.subTitle, it.imageUrl, it.linkUrl, it.bgColor, it.fontColor)
            advertisement.update(it.position, it.platform, it.startDateTime, it.endDateTime, detail)
        }
    }

    @CacheEvict(cacheNames = [ADVERTISEMENTS], allEntries = true)
    @Transactional
    fun deleteAdvertisement(advertisementId: Long) {
        val advertisement = findAdvertisementById(advertisementId)
        advertisementRepository.delete(advertisement)
    }

    @Transactional(readOnly = true)
    fun retrieveAdvertisements(request: RetrieveAdvertisementsRequest): AdvertisementsWithPagingResponse {
        return AdvertisementsWithPagingResponse.of(
            advertisements = advertisementRepository.findAllByPositionAndPlatformWithPaging(request.size, request.page - 1, request.platform, request.position),
            totalCounts = advertisementRepository.findAllCounts()
        )
    }

    fun findAdvertisementById(advertisementId: Long): Advertisement {
        return advertisementRepository.findAdvertisementById(advertisementId)
            ?: throw NotFoundException("해당하는 광고(${advertisementId})는 존재하지 않습니다.", ErrorCode.NOT_FOUND_ADVERTISEMENT)
    }

}
