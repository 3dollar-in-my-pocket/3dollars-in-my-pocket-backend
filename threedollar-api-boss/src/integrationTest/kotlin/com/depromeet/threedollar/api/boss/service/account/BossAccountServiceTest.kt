package com.depromeet.threedollar.api.boss.service.account

import com.depromeet.threedollar.api.boss.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.mongo.boss.domain.account.*
import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class BossAccountServiceTest(
    private val bossAccountService: BossAccountService,
    private val bossAccountRepository: BossAccountRepository,
    private val bossWithdrawalAccountRepository: BossWithdrawalAccountRepository
) : FunSpec({

    afterEach {
        withContext(Dispatchers.IO) {
            bossAccountRepository.deleteAll()
            bossWithdrawalAccountRepository.deleteAll()
        }
    }

    context("사장님 자신의 회원 정보를 수정한다") {
        test("사장님 자신의 회원 정보를 수정하면 DB에 해당 변경된 정보가 반영된다") {
            // given
            val name = "새로운 이름"
            val isSetupNotification = true

            val bossAccount = BossAccountCreator.create(
                socialId = "social-id",
                socialType = BossAccountSocialType.GOOGLE,
                name = "사장님 이름",
                isSetupNotification = false
            )
            withContext(Dispatchers.IO) {
                bossAccountRepository.save(bossAccount)
            }

            // when
            withContext(Dispatchers.IO) {
                bossAccountService.updateBossAccountInfo(bossAccount.id, UpdateBossAccountInfoRequest(name, isSetupNotification))
            }

            // then
            val bossAccounts = bossAccountRepository.findAll()
            bossAccounts shouldHaveSize 1
            bossAccounts[0].also {
                it.name shouldBe name
                it.isSetupNotification shouldBe isSetupNotification
                it.id shouldBe bossAccount.id
                it.socialInfo shouldBe bossAccount.socialInfo
                it.businessNumber shouldBe bossAccount.businessNumber
            }
        }

        test("사장님의 계정 정보를 수정할때 존재하지 않는 사장님이면 NotFoundException이 발생한다") {
            // given
            val name = "사장님 이름"
            val request = UpdateBossAccountInfoRequest(name = name, isSetupNotification = false)

            // when & then
            shouldThrowExactly<NotFoundException> {
                bossAccountService.updateBossAccountInfo("notFound", request)
            }
        }
    }

    context("사장님의 회원탈퇴 기능") {
        test("회원탈퇴시, 기존의 BossAccount 계정 정보가 DB에서 삭제된다") {
            // given
            val bossAccount = BossAccountCreator.create(
                socialId = "socialId",
                socialType = BossAccountSocialType.APPLE
            )
            withContext(Dispatchers.IO) {
                bossAccountRepository.save(bossAccount)
            }

            // when
            withContext(Dispatchers.IO) {
                bossAccountService.signOut(bossAccount.id)
            }

            // then
            val bossAccounts = bossAccountRepository.findAll()
            bossAccounts shouldHaveSize 0
        }

        test("회원탈퇴시, 계정 정보가 BossWithdrawalAccount 테이블에 백업된다") {
            // given
            val socialId = "auth-social-id"
            val socialType = BossAccountSocialType.APPLE
            val name = "강승호"
            val isSetupNotification = false
            val businessNumber = BusinessNumber.of("123-12-12345")

            val bossAccount = BossAccountCreator.create(
                socialId = socialId,
                socialType = socialType,
                name = name,
                businessNumber = businessNumber,
                isSetupNotification = isSetupNotification
            )
            withContext(Dispatchers.IO) {
                bossAccountRepository.save(bossAccount)
            }

            // when
            withContext(Dispatchers.IO) {
                bossAccountService.signOut(bossAccount.id)
            }

            // then
            val withdrawalAccounts = bossWithdrawalAccountRepository.findAll()
            withdrawalAccounts shouldHaveSize 1
            withdrawalAccounts[0].also {
                it.name shouldBe name
                it.socialInfo shouldBe BossAccountSocialInfo.of(socialId, socialType)
                it.isSetupNotification shouldBe isSetupNotification
                it.businessNumber shouldBe businessNumber
                it.backupInfo.bossId shouldBe bossAccount.id
            }
        }
    }

})
