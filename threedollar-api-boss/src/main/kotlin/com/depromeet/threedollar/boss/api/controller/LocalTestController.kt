package com.depromeet.threedollar.boss.api.controller

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.config.session.SessionConstants
import com.depromeet.threedollar.boss.api.service.auth.dto.response.LoginResponse
import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.account.SocialInfo
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession

@RestController
class LocalTestController(
    private val bossAccountRepository: BossAccountRepository,
    private val httpSession: HttpSession
) {

    @ApiOperation("[개발용] 사장님 서버용 테스트 토큰을 발급 받습니다.")
    @GetMapping("/test-token")
    fun getTestBossAccountToken(): ApiResponse<LoginResponse> {
        val bossAccount = bossAccountRepository.findBossAccountBySocialInfo(SocialInfo(boss.socialInfo.socialId, boss.socialInfo.socialType))
            ?: bossAccountRepository.save(boss)
        httpSession.setAttribute(SessionConstants.BOSS_ACCOUNT_ID, bossAccount.id)
        return ApiResponse.success(LoginResponse(httpSession.id, bossAccount.id))
    }

    companion object {
        private val boss = BossAccount("123", SocialInfo("test-social-id", BossAccountSocialType.NAVER), "1234", "1234")
    }

}
