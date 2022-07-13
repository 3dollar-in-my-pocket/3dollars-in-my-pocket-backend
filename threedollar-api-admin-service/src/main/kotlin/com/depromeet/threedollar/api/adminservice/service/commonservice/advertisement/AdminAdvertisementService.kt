package com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement

import com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement.dto.request.AddAdvertisementRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement.dto.request.UpdateAdvertisementRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement.dto.response.AdvertisementsWithPagingResponse
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.type.CacheType.CacheKey.ADVERTISEMENTS
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.Advertisement
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementDetail
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminAdvertisementService(
    private val advertisementRepository: AdvertisementRepository,
) {

    @CacheEvict(cacheNames = [ADVERTISEMENTS], key = "{#request.position, #request.platform}")
    @Transactional
    fun addAdvertisement(request: AddAdvertisementRequest) {
        validateSupportedPosition(applicationType = request.applicationType, positionType = request.position)
        advertisementRepository.save(request.toEntity())
    }

    @CacheEvict(cacheNames = [ADVERTISEMENTS], key = "{#request.position, #request.platform}")
    @Transactional
    fun updateAdvertisement(advertisementId: Long, request: UpdateAdvertisementRequest) {
        val advertisement = findAdvertisementById(advertisementId)
        validateSupportedPosition(applicationType = advertisement.applicationType, positionType = request.position)
        request.let {
            val detail = AdvertisementDetail.of(it.title, it.subTitle, it.imageUrl, it.linkUrl, it.bgColor, it.fontColor)
            advertisement.update(it.position, it.platform, it.startDateTime, it.endDateTime, detail)
        }
    }

    private fun validateSupportedPosition(applicationType: ApplicationType, positionType: AdvertisementPositionType) {
        if (!positionType.isSupported(applicationType)) {
            throw ForbiddenException("해당 서비스(${applicationType})에서 지원하지 않는 광고 위치(${positionType})입니다", ErrorCode.E501_NOT_SUPPORTED_ADVERTISEMENT_POSITION)
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
        val advertisements = advertisementRepository.findAllByApplicationTypeAndPositionAndPlatformWithPaging(request.applicationType, request.size, request.page - 1, request.platform, request.position)
        return AdvertisementsWithPagingResponse.of(
            advertisements = advertisements,
            totalCounts = advertisementRepository.findAllCountsByApplicationTypeAndPlatformTypeAndPositionType(request.applicationType, request.platform, request.position)
        )
    }

    fun findAdvertisementById(advertisementId: Long): Advertisement {
        return advertisementRepository.findAdvertisementById(advertisementId)
            ?: throw NotFoundException("해당하는 광고(${advertisementId})는 존재하지 않습니다.", ErrorCode.E404_NOT_EXISTS_ADVERTISEMENT)
    }

}

