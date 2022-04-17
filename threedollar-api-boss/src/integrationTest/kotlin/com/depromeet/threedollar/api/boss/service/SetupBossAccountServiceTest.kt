package com.depromeet.threedollar.api.boss.service

import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal abstract class SetupBossAccountServiceTest {

    @Autowired
    protected lateinit var bossAccountRepository: BossAccountRepository

    protected lateinit var bossId: String

    @BeforeEach
    fun setup() {
        bossId = bossAccountRepository.save(BossAccountCreator.create(
            socialId = "social-id-test",
            socialType = BossAccountSocialType.APPLE,
            name = "통합 테스트 사장님 계정"
        )).id
    }

    protected fun cleanup() {
        bossAccountRepository.deleteAll()
    }

}
