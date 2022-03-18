package com.depromeet.threedollar.api.boss.service.account

import com.depromeet.threedollar.api.boss.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.mongo.boss.domain.account.*
import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

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
            val pushSettingsStatus = PushSettingsStatus.ON

            val bossAccount = BossAccountCreator.create(
                socialId = "social-id",
                socialType = BossAccountSocialType.GOOGLE,
                name = "사장님 이름",
                pushSettingsStatus = PushSettingsStatus.OFF
            )
            bossAccountRepository.save(bossAccount)

            // when
            bossAccountService.updateBossAccountInfo(bossAccount.id, UpdateBossAccountInfoRequest(name, pushSettingsStatus))

            // then
            val bossAccounts = bossAccountRepository.findAll()
            assertAll({
                assertThat(bossAccounts).hasSize(1)
                assertThat(bossAccounts[0].name).isEqualTo(name)
                assertThat(bossAccounts[0].pushSettingsStatus).isEqualTo(pushSettingsStatus)
                assertThat(bossAccounts[0].id).isEqualTo(bossAccount.id)
                assertThat(bossAccounts[0].socialInfo).isEqualTo(bossAccount.socialInfo)
                assertThat(bossAccounts[0].businessNumber).isEqualTo(bossAccount.businessNumber)
            })
        }

        @Test
        fun `사장님의 계정 정보를 수정할때 존재하지 않는 사장님이면 NotFoundException이 발생한다`() {
            // given
            val name = "새로운 이름"
            val request = UpdateBossAccountInfoRequest(name, PushSettingsStatus.OFF)

            // when & then
            assertThatThrownBy { bossAccountService.updateBossAccountInfo("Not Found Boss Id", request) }.isInstanceOf(NotFoundException::class.java)
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
            val pushSettingsStatus = PushSettingsStatus.OFF
            val businessNumber = BusinessNumber.of("123-12-12345")

            val bossAccount = BossAccountCreator.create(
                socialId = socialId,
                socialType = socialType,
                name = name,
                businessNumber = businessNumber,
                pushSettingsStatus = pushSettingsStatus
            )
            bossAccountRepository.save(bossAccount)

            // when
            bossAccountService.signOut(bossAccount.id)

            // then
            val withdrawalAccounts = bossWithdrawalAccountRepository.findAll()
            assertAll({
                assertThat(withdrawalAccounts).hasSize(1)
                withdrawalAccounts[0].let {
                    assertThat(it.name).isEqualTo(name)
                    assertThat(it.socialInfo).isEqualTo(BossAccountSocialInfo.of(socialId, socialType))
                    assertThat(it.pushSettingsStatus).isEqualTo(pushSettingsStatus)
                    assertThat(it.businessNumber).isEqualTo(businessNumber)

                    assertThat(it.backupInfo.bossId).isEqualTo(bossAccount.id)
                    assertThat(it.backupInfo.bossCreatedAt).isEqualToIgnoringNanos(bossAccount.createdAt)
                }
            })
        }

    }

}
