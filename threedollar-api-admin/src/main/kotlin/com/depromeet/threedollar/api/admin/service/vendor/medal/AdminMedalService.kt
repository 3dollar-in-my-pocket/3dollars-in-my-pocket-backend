package com.depromeet.threedollar.api.admin.service.vendor.medal

import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.admin.service.vendor.medal.dto.request.AddMedalRequest
import com.depromeet.threedollar.api.admin.service.vendor.medal.dto.request.UpdateMedalRequest
import com.depromeet.threedollar.api.core.service.vendor.medal.dto.response.MedalResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.type.CacheType.CacheKey.MEDALS
import com.depromeet.threedollar.domain.rds.vendor.domain.medal.Medal
import com.depromeet.threedollar.domain.rds.vendor.domain.medal.MedalRepository

@Service
class AdminMedalService(
    private val medalRepository: MedalRepository,
) {

    @CacheEvict(cacheNames = [MEDALS], allEntries = true)
    @Transactional
    fun addMedal(request: AddMedalRequest) {
        medalRepository.save(request.toEntity())
    }

    @CacheEvict(cacheNames = [MEDALS], allEntries = true)
    @Transactional
    fun updateMedal(medalId: Long, request: UpdateMedalRequest) {
        val medal = findMedalById(medalId)
        request.let {
            medal.update(it.name, it.introduction, it.activationIconUrl, it.disableIconUrl)
        }
    }

    @Transactional(readOnly = true)
    fun retrieveMedals(): List<MedalResponse> {
        return medalRepository.findAllActiveMedals()
            .map { MedalResponse.of(it) }
    }

    private fun findMedalById(medalId: Long): Medal {
        return medalRepository.findMedalById(medalId)
            ?: throw NotFoundException("해당하는 메달($medalId)은 존재하지 않습니다", ErrorCode.NOTFOUND_MEDAL)
    }

}
