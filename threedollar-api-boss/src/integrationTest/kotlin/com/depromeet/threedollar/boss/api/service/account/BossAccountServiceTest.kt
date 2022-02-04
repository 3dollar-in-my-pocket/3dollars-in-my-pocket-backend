package com.depromeet.threedollar.boss.api.service.account

import com.depromeet.threedollar.boss.api.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.account.BossAccountCreator
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.account.PushSettingsStatus
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class BossAccountServiceTest(
    private val bossAccountService: BossAccountService,
    private val bossAccountRepository: BossAccountRepository
) {

    @AfterEach
    fun cleanUp() {
        bossAccountRepository.deleteAll()
    }

    @Test
    fun `사장님이 계정 정보를 수정한다`() {
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
    fun `사장님이 계정 정보를 수정할때 존재하지 않는 사장님이면 NotFoundException`() {
        // given
        val name = "새로운 이름"
        val request = UpdateBossAccountInfoRequest(name, PushSettingsStatus.OFF)

        // when & then
        assertThatThrownBy { bossAccountService.updateBossAccountInfo("Not Found Boss Id", request) }.isInstanceOf(NotFoundException::class.java)
    }

}
