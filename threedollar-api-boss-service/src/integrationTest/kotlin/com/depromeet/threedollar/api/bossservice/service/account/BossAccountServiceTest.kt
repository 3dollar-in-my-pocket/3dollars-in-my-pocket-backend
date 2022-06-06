package com.depromeet.threedollar.api.bossservice.service.account

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import com.depromeet.threedollar.api.bossservice.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.BusinessNumber
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountCreator
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossWithdrawalAccount
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossWithdrawalAccountRepository

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class BossAccountServiceTest(
    private val bossAccountService: BossAccountService,
    private val bossAccountRepository: BossAccountRepository,
    private val bossWithdrawalAccountRepository: BossWithdrawalAccountRepository,
) {

    @AfterEach
    fun cleanUp() {
        bossAccountRepository.deleteAll()
        bossWithdrawalAccountRepository.deleteAll()
    }

    @Nested
    inner class UpdateBossAccountInfoTest {

        @Test
        fun `사장님의 계정 정보를 수정한다`() {
            // given
            val name = "새로운 이름"
            val isSetupNotification = true

            val bossAccount = BossAccountCreator.create(
                socialId = "social-id",
                socialType = BossAccountSocialType.GOOGLE,
                name = "사장님 이름",
                isSetupNotification = false
            )
            bossAccountRepository.save(bossAccount)

            val request = UpdateBossAccountInfoRequest(name, isSetupNotification)

            // when
            bossAccountService.updateBossAccountInfo(bossAccount.id, request)

            // then
            val bossAccounts = bossAccountRepository.findAll()
            assertAll({
                assertThat(bossAccounts).hasSize(1)
                assertBossAccount(
                    bossAccount = bossAccounts[0],
                    name = name,
                    isSetupNotification = isSetupNotification,
                    bossAccountId = bossAccount.id,
                    socialInfo = bossAccount.socialInfo,
                    businessNumber = bossAccount.businessNumber
                )
            })
        }

        @Test
        fun `사장님의 계정 정보를 수정할때 존재하지 않는 사장님이면 NotFoundException이 발생한다`() {
            // given
            val name = "새로운 이름"
            val request = UpdateBossAccountInfoRequest(name = name, isSetupNotification = false)

            // when & then
            assertThatThrownBy {
                bossAccountService.updateBossAccountInfo(
                    bossId = "Not Found Boss Id",
                    request = request
                )
            }.isInstanceOf(NotFoundException::class.java)
        }

    }

    @Nested
    inner class SignOutTest {

        @Test
        fun `회원탈퇴시 BossAccount 계정 정보가 삭제된다`() {
            // given
            val bossAccount = BossAccountCreator.create(
                socialId = "socialId",
                socialType = BossAccountSocialType.APPLE
            )
            bossAccountRepository.save(bossAccount)

            // when
            bossAccountService.signOut(bossAccount.id)

            // then
            val bossAccounts = bossAccountRepository.findAll()
            assertThat(bossAccounts).isEmpty()
        }

        @Test
        fun `회원탈퇴시 계정정보가 BossWithdrawalAccount에 백업된다`() {
            // given
            val socialId = "auth-social-id"
            val socialType = BossAccountSocialType.APPLE
            val name = "강승호"
            val isSetupNotification = false
            val businessNumber = BusinessNumber.of("000-00-00000")

            val bossAccount = BossAccountCreator.create(
                socialId = socialId,
                socialType = socialType,
                name = name,
                businessNumber = businessNumber,
                isSetupNotification = isSetupNotification
            )
            bossAccountRepository.save(bossAccount)

            // when
            bossAccountService.signOut(bossAccount.id)

            // then
            val withdrawalAccounts = bossWithdrawalAccountRepository.findAll()
            assertAll({
                assertThat(withdrawalAccounts).hasSize(1)
                assertWithdrawalAccount(
                    withdrawalAccount = withdrawalAccounts[0],
                    name = name,
                    socialInfo = BossAccountSocialInfo.of(socialId, socialType),
                    isSetupNotification = isSetupNotification,
                    businessNumber = businessNumber,
                )
                withdrawalAccounts[0]?.let {
                    assertThat(it.backupInfo.bossId).isEqualTo(bossAccount.id)
                    assertThat(it.backupInfo.bossCreatedAt).isEqualToIgnoringNanos(bossAccount.createdAt)
                }
            })
        }

    }

    private fun assertBossAccount(bossAccount: BossAccount, name: String, isSetupNotification: Boolean, bossAccountId: String, socialInfo: BossAccountSocialInfo, businessNumber: BusinessNumber) {
        assertThat(bossAccount.name).isEqualTo(name)
        assertThat(bossAccount.isSetupNotification).isEqualTo(isSetupNotification)
        assertThat(bossAccount.id).isEqualTo(bossAccountId)
        assertThat(bossAccount.socialInfo).isEqualTo(socialInfo)
        assertThat(bossAccount.businessNumber).isEqualTo(businessNumber)
    }


    private fun assertWithdrawalAccount(withdrawalAccount: BossWithdrawalAccount, name: String, socialInfo: BossAccountSocialInfo, isSetupNotification: Boolean, businessNumber: BusinessNumber) {
        assertThat(withdrawalAccount.name).isEqualTo(name)
        assertThat(withdrawalAccount.socialInfo).isEqualTo(socialInfo)
        assertThat(withdrawalAccount.isSetupNotification).isEqualTo(isSetupNotification)
        assertThat(withdrawalAccount.businessNumber).isEqualTo(businessNumber)
    }

}
