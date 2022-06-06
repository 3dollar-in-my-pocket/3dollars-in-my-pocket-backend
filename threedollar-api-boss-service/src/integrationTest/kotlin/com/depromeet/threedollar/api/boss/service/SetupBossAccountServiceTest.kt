package com.depromeet.threedollar.api.boss.service

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountCreator

@SpringBootTest
internal abstract class SetupBossAccountServiceTest {

    @Autowired
    protected lateinit var bossAccountRepository: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository

    protected lateinit var boss: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount
    protected lateinit var bossId: String

    @BeforeEach
    fun setup() {
        boss = bossAccountRepository.save(BossAccountCreator.create(
            socialId = "social-id-test",
            socialType = com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType.APPLE,
            name = "통합 테스트 사장님 계정"
        ))
        bossId = boss.id
    }

    protected fun cleanup() {
        bossAccountRepository.deleteAll()
    }

}
