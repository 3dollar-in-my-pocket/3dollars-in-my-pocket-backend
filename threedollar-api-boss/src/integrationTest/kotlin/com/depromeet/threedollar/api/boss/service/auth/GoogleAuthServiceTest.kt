package com.depromeet.threedollar.api.boss.service.auth

import com.depromeet.threedollar.api.boss.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.api.boss.service.auth.policy.GoogleAuthService
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.boot.test.context.SpringBootTest

private const val SOCIAL_ID = "social-id"
private val SOCIAL_TYPE = BossAccountSocialType.GOOGLE

@SpringBootTest
internal class GoogleAuthServiceOneTest(
    private val bossAccountRepository: BossAccountRepository,
    private val registrationRepository: RegistrationRepository
) : FunSpec({

    lateinit var authService: AuthService

    afterEach {
        withContext(Dispatchers.IO) {
            registrationRepository.deleteAll()
            bossAccountRepository.deleteAll()
        }
    }

    beforeEach {
        authService = GoogleAuthService(bossAccountRepository, registrationRepository, StubGoogleAuthApiClient())
    }

    context("구글 로그인") {
        test("구글 로그인이 성공하면 사장님 계정의 ID가 반환된다") {
            // given
            val bossAccount = BossAccountCreator.create(
                name = "사장님",
                socialId = SOCIAL_ID,
                socialType = SOCIAL_TYPE
            )
            withContext(Dispatchers.IO) {
                bossAccountRepository.save(bossAccount)
            }

            // when
            val bossId = authService.login(LoginRequest("token", SOCIAL_TYPE))

            // then
            bossId shouldBe bossAccount.id
        }

        test("구글 로그인시 가입한 유저가 아니면 404 에러 발생") {
            // when & then
            shouldThrowExactly<NotFoundException> {
                authService.login(LoginRequest("token", SOCIAL_TYPE))
            }
        }

        test("구글 로그인시 가입 승인 대기중인 유저면 Registration Id를 반환한다") {
            // given
            val registration = RegistrationCreator.create(SOCIAL_ID, SOCIAL_TYPE)
            withContext(Dispatchers.IO) {
                registrationRepository.save(registration)
            }

            // when
            val bossId = authService.login(LoginRequest(token = "token", socialType = SOCIAL_TYPE))

            // then
            bossId shouldBe registration.id
        }
    }

})

private class StubGoogleAuthApiClient : GoogleAuthApiClient {
    override fun getProfileInfo(accessToken: String?): GoogleProfileInfoResponse {
        return GoogleProfileInfoResponse.testInstance(SOCIAL_ID)
    }
}
