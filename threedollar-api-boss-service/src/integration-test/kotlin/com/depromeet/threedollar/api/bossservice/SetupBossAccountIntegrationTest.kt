package com.depromeet.threedollar.api.bossservice

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType

internal abstract class SetupBossAccountIntegrationTest : IntegrationTest() {

    @Autowired
    protected lateinit var bossAccountRepository: BossAccountRepository

    protected lateinit var boss: BossAccount
    protected lateinit var bossId: String

    @BeforeEach
    fun setup() {
        boss = bossAccountRepository.save(BossAccountFixture.create(
            socialId = "social-id-test",
            socialType = BossAccountSocialType.APPLE,
            name = "통합 테스트 사장님 계정"
        ))
        bossId = boss.id
    }

    override fun cleanup() {
        super.cleanup()
        bossAccountRepository.deleteAll()
    }

}
