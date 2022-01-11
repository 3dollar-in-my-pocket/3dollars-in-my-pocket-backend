package com.depromeet.threedollar.foodtruck.api.controller

import com.depromeet.threedollar.domain.boss.domain.account.BossAccount
import com.depromeet.threedollar.domain.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.foodtruck.api.config.session.SessionConstants
import com.depromeet.threedollar.foodtruck.api.controller.auth.dto.response.LoginResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession

@RestController
class LocalTestController(
    private val bossAccountRepository: BossAccountRepository,
    private val httpSession: HttpSession
) {

    @GetMapping("/test-token")
    fun getTestBossAccountToken(): ApiResponse<LoginResponse> {
        val bossAccount = bossAccountRepository.findBossAccountBySocialTypeAndSocialId(boss.socialInfo.socialId, boss.socialInfo.socialType)
            ?: bossAccountRepository.save(boss)
        httpSession.setAttribute(SessionConstants.BOSS_ACCOUNT_ID, bossAccount.id)
        return ApiResponse.success(LoginResponse(httpSession.id, bossAccount.id))
    }

    companion object {
        private val boss = BossAccount.of("test-social-id", BossAccountSocialType.NAVER, "테스트 계정")
    }

}
