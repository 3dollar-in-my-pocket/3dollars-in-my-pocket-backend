package com.depromeet.threedollar.boss.api.service.account

import com.depromeet.threedollar.boss.api.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.account.BossAccountCreator
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import org.assertj.core.api.Assertions
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

        val bossAccount = BossAccountCreator.create(
            socialId = "social-id",
            socialType = BossAccountSocialType.GOOGLE,
            name = "사장님 이름"
        )
        bossAccountRepository.save(bossAccount)

        // when
        bossAccountService.updateBossAccountInfo(bossAccount.id, UpdateBossAccountInfoRequest(name))

        // then
        val bossAccounts = bossAccountRepository.findAll()
        assertAll({
            Assertions.assertThat(bossAccounts).hasSize(1)
            Assertions.assertThat(bossAccounts[0].id).isEqualTo(bossAccount.id)
            Assertions.assertThat(bossAccounts[0].name).isEqualTo(name)
            Assertions.assertThat(bossAccounts[0].socialInfo).isEqualTo(bossAccount.socialInfo)
            Assertions.assertThat(bossAccounts[0].businessNumber).isEqualTo(bossAccount.businessNumber)
        })
    }

    @Test
    fun `사장님이 계정 정보를 수정할때 존재하지 않는 사장님이면 NotFoundException`() {
        // given
        val name = "새로운 이름"

        // when & then
        assertThatThrownBy { bossAccountService.updateBossAccountInfo("Not Found Boss Id", UpdateBossAccountInfoRequest(name)) }.isInstanceOf(NotFoundException::class.java)
    }

}
