package com.depromeet.threedollar.boss.api.service.registration

import com.depromeet.threedollar.boss.api.service.account.validateNotExistsBossAccount
import com.depromeet.threedollar.boss.api.service.registration.dto.request.ApplyRegistrationRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.registration.RegistrationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegistrationService(
    private val bossAccountRepository: BossAccountRepository,
    private val registrationRepository: RegistrationRepository
) {

    @Transactional
    fun applyRegistration(
        request: ApplyRegistrationRequest
    ) {
        // TODO 카테고리 validation
        validateDuplicateRegistration(request.socialId, request.socialType)
        val registration = request.toEntity()
        registration.addCategories(request.storeCategories)
        registrationRepository.save(registration)
        // TODO: 비동기적으로 가슴속 삼천원 슬랙에 알림을 보내는 로직 추가
    }

    private fun validateDuplicateRegistration(socialId: String, socialType: BossAccountSocialType) {
        validateNotExistsBossAccount(bossAccountRepository, socialId, socialType)

        if (registrationRepository.findRegistrationBySocialInfo(socialId, socialType) != null) {
            throw ConflictException("이미 가입 신청한 사장님 (${socialId} - (${socialType}) 입니다.")
        }
    }

}
