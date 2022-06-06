package com.depromeet.threedollar.api.adminservice.service.userservice.faq

import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.adminservice.service.userservice.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.api.core.service.userservice.faq.dto.response.FaqResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.type.CacheType.CacheKey.FAQS
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.Faq
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqRepository

@Service
class FaqAdminService(
    private val faqRepository: FaqRepository,
) {

    @CacheEvict(cacheNames = [FAQS], allEntries = true)
    @Transactional
    fun addFaq(request: AddFaqRequest): FaqResponse {
        return FaqResponse.of(faqRepository.save(request.toEntity()))
    }

    @CacheEvict(cacheNames = [FAQS], allEntries = true)
    @Transactional
    fun updateFaq(faqId: Long, request: UpdateFaqRequest): FaqResponse {
        val faq = findFaqById(faqId)
        faq.update(request.question, request.answer, request.category)
        return FaqResponse.of(faq)
    }

    @CacheEvict(cacheNames = [FAQS], allEntries = true)
    @Transactional
    fun deleteFaq(faqId: Long) {
        val faq = findFaqById(faqId)
        faqRepository.delete(faq)
    }

    private fun findFaqById(faqId: Long): Faq {
        return faqRepository.findFaqById(faqId)
            ?: throw NotFoundException("해당하는 Faq($faqId)는 존재하지 않습니다.", ErrorCode.NOT_FOUND_FAQ)
    }

}
