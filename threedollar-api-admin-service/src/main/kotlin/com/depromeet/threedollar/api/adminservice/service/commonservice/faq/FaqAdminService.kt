package com.depromeet.threedollar.api.adminservice.service.commonservice.faq

import com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.response.FaqAdminResponse
import com.depromeet.threedollar.api.core.service.commonservice.faq.dto.response.FaqResponse
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.type.CacheType.CacheKey.FAQS
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.Faq
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FaqAdminService(
    private val faqRepository: FaqRepository,
) {

    @CacheEvict(cacheNames = [FAQS], allEntries = true)
    @Transactional
    fun addFaq(request: AddFaqRequest): FaqResponse {
        validateSupportedFaqCategory(applicationType = request.applicationType, category = request.category)
        return FaqResponse.of(faqRepository.save(request.toEntity()))
    }

    @CacheEvict(cacheNames = [FAQS], allEntries = true)
    @Transactional
    fun updateFaq(faqId: Long, request: UpdateFaqRequest): FaqResponse {
        val faq = findFaqById(faqId)
        validateSupportedFaqCategory(applicationType = faq.applicationType, category = request.category)
        faq.update(request.question, request.answer, request.category)
        return FaqResponse.of(faq)
    }

    private fun validateSupportedFaqCategory(applicationType: ApplicationType, category: FaqCategory) {
        if (!category.isSupported(applicationType)) {
            throw ForbiddenException("해당 서비스(${applicationType})에서 지원하지 않는 FAQ 카테고리(${category}) 입니다", ErrorCode.NOT_IMPLEMENTED_FAQ_CATEGORY)
        }
    }

    @CacheEvict(cacheNames = [FAQS], allEntries = true)
    @Transactional
    fun deleteFaq(faqId: Long) {
        val faq = findFaqById(faqId)
        faqRepository.delete(faq)
    }

    @Transactional(readOnly = true)
    fun retrieveAllFaqs(applicationType: ApplicationType?, category: FaqCategory?): List<FaqAdminResponse> {
        return faqRepository.findAllByApplicationTypeAndCategory(applicationType, category)
            .map { faq -> FaqAdminResponse.of(faq) }
    }

    private fun findFaqById(faqId: Long): Faq {
        return faqRepository.findFaqById(faqId)
            ?: throw NotFoundException("해당하는 Faq($faqId)는 존재하지 않습니다.", ErrorCode.NOT_FOUND_FAQ)
    }

}
