package com.depromeet.threedollar.api.bossservice.service.account

import com.depromeet.threedollar.api.bossservice.IntegrationTest
import com.depromeet.threedollar.api.bossservice.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.BusinessNumber
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossWithdrawalAccount
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossWithdrawalAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceFixture
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class BossAccountServiceTest(
    private val bossAccountService: BossAccountService,
    private val bossAccountRepository: BossAccountRepository,
    private val bossWithdrawalAccountRepository: BossWithdrawalAccountRepository,
    private val deviceRepository: DeviceRepository,
) : IntegrationTest() {

    @AfterEach
    fun cleanUp() {
        bossAccountRepository.deleteAll()
        bossWithdrawalAccountRepository.deleteAll()
    }

    @Nested
    inner class UpdateBossAccountInfoTest {

        @Test
        fun `???????????? ?????? ????????? ????????????`() {
            // given
            val name = "????????? ??????"

            val bossAccount = BossAccountFixture.create()
            bossAccountRepository.save(bossAccount)

            val request = UpdateBossAccountInfoRequest(name)

            // when
            bossAccountService.updateBossAccountInfo(bossAccount.id, request)

            // then
            val bossAccounts = bossAccountRepository.findAll()
            assertAll({
                assertThat(bossAccounts).hasSize(1)
                assertBossAccount(
                    bossAccount = bossAccounts[0],
                    name = name,
                    bossAccountId = bossAccount.id,
                    socialInfo = bossAccount.socialInfo,
                    businessNumber = bossAccount.businessNumber
                )
            })
        }

        @Test
        fun `???????????? ?????? ????????? ???????????? ???????????? ?????? ??????????????? NotFoundException??? ????????????`() {
            // given
            val name = "????????? ??????"
            val request = UpdateBossAccountInfoRequest(name = name)

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
        fun `??????????????? BossAccount ?????? ????????? ????????????`() {
            // given
            val bossAccount = BossAccountFixture.create()
            bossAccountRepository.save(bossAccount)

            // when
            bossAccountService.signOut(bossAccount.id)

            // then
            val bossAccounts = bossAccountRepository.findAll()
            assertThat(bossAccounts).isEmpty()
        }

        @Test
        fun `??????????????? ??????????????? BossWithdrawalAccount??? ????????????`() {
            // given
            val socialId = "auth-social-id"
            val socialType = BossAccountSocialType.APPLE
            val name = "?????????"
            val businessNumber = BusinessNumber.of("000-00-00000")

            val bossAccount = BossAccountFixture.create(
                socialId = socialId,
                socialType = socialType,
                name = name,
                businessNumber = businessNumber,
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
                    businessNumber = businessNumber,
                )
                withdrawalAccounts[0]?.let {
                    assertThat(it.backupInfo.bossId).isEqualTo(bossAccount.id)
                    assertThat(it.backupInfo.bossCreatedAt).isEqualToIgnoringNanos(bossAccount.createdAt)
                }
            })
        }

        @Test
        fun `????????? ???????????????, ??????????????? ?????? ????????????`() {
            // given
            val bossAccount = BossAccountFixture.create()
            bossAccountRepository.save(bossAccount)

            val device = DeviceFixture.create(
                accountId = bossAccount.id,
                accountType = AccountType.BOSS_ACCOUNT,
            )
            deviceRepository.save(device)

            // when
            bossAccountService.signOut(bossAccount.id)

            // then
            val devices = deviceRepository.findAll()
            assertThat(devices).isEmpty()
        }

    }

    private fun assertBossAccount(bossAccount: BossAccount, name: String, bossAccountId: String, socialInfo: BossAccountSocialInfo, businessNumber: BusinessNumber) {
        assertThat(bossAccount.name).isEqualTo(name)
        assertThat(bossAccount.id).isEqualTo(bossAccountId)
        assertThat(bossAccount.socialInfo).isEqualTo(socialInfo)
        assertThat(bossAccount.businessNumber).isEqualTo(businessNumber)
    }


    private fun assertWithdrawalAccount(withdrawalAccount: BossWithdrawalAccount, name: String, socialInfo: BossAccountSocialInfo, businessNumber: BusinessNumber) {
        assertThat(withdrawalAccount.name).isEqualTo(name)
        assertThat(withdrawalAccount.socialInfo).isEqualTo(socialInfo)
        assertThat(withdrawalAccount.businessNumber).isEqualTo(businessNumber)
    }

}
