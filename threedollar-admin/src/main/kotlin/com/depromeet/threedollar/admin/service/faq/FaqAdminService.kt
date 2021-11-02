package com.depromeet.threedollar.admin.service.faq

import com.depromeet.threedollar.admin.service.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.admin.service.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.application.config.cache.CacheType
import com.depromeet.threedollar.application.service.faq.dto.response.FaqResponse
import com.depromeet.threedollar.common.exception.ErrorCode
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.domain.faq.Faq
import com.depromeet.threedollar.domain.domain.faq.FaqRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FaqAdminService(
    private val faqRepository: FaqRepository
) {

    @CacheEvict(allEntries = true, value = [CacheType.CacheKey.FAQS])
    @Transactional
    fun addFaq(request: AddFaqRequest): FaqResponse {
        return FaqResponse.of(faqRepository.save(request.toEntity()))
    }

    @CacheEvict(allEntries = true, value = [CacheType.CacheKey.FAQS])
    @Transactional
    fun updateFaq(faqId: Long, request: UpdateFaqRequest): FaqResponse {
        val faq = findFaqById(faqRepository, faqId)
        faq.update(request.question, request.answer, request.category)
        return FaqResponse.of(faq)
    }

    @CacheEvict(allEntries = true, value = [CacheType.CacheKey.FAQS])
    @Transactional
    fun deleteFaq(faqId: Long) {
        val faq = findFaqById(faqRepository, faqId)
        faqRepository.delete(faq)
    }

}

fun findFaqById(faqRepository: FaqRepository, faqId: Long): Faq {
    return faqRepository.findFaqById(faqId)
        ?: throw NotFoundException("해당하는 Faq ($faqId)를 찾을 수 없습니다", ErrorCode.NOT_FOUND_FAQ_EXCEPTION)
}
