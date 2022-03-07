package com.depromeet.threedollar.api.admin.service.user.faq

import com.depromeet.threedollar.api.admin.service.user.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.api.admin.service.user.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.api.core.service.faq.dto.response.FaqResponse
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.type.CacheType.CacheKey.FAQS
import com.depromeet.threedollar.domain.rds.user.domain.faq.Faq
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FaqAdminService(
    private val faqRepository: FaqRepository
) {

    @CacheEvict(allEntries = true, value = [FAQS])
    @Transactional
    fun addFaq(request: AddFaqRequest): FaqResponse {
        return FaqResponse.of(faqRepository.save(request.toEntity()))
    }

    @CacheEvict(allEntries = true, value = [FAQS])
    @Transactional
    fun updateFaq(faqId: Long, request: UpdateFaqRequest): FaqResponse {
        val faq = findFaqById(faqId)
        faq.update(request.question, request.answer, request.category)
        return FaqResponse.of(faq)
    }

    @CacheEvict(allEntries = true, value = [FAQS])
    @Transactional
    fun deleteFaq(faqId: Long) {
        val faq = findFaqById(faqId)
        faqRepository.delete(faq)
    }

    private fun findFaqById(faqId: Long): Faq {
        return faqRepository.findFaqById(faqId)
            ?: throw NotFoundException("해당하는 Faq ($faqId)를 찾을 수 없습니다", ErrorCode.NOTFOUND_FAQ)
    }

}
