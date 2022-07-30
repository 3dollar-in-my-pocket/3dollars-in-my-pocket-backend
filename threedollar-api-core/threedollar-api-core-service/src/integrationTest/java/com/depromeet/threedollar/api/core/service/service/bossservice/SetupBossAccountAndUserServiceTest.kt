package com.depromeet.threedollar.api.core.service.service.bossservice

import com.depromeet.threedollar.api.core.service.IntegrationTest
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

internal abstract class SetupBossAccountAndUserServiceTest : IntegrationTest() {

    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    protected lateinit var userMedalRepository: UserMedalRepository

    @Autowired
    protected lateinit var medalRepository: MedalRepository

    @Autowired
    protected lateinit var medalAcquisitionConditionRepository: MedalAcquisitionConditionRepository

    @Autowired
    protected lateinit var bossAccountRepository: BossAccountRepository

    protected var userId: Long = -1L

    protected lateinit var bossId: String

    @BeforeEach
    fun setup() {
        userId = userRepository.save(UserFixture.create("social-id", UserSocialType.KAKAO, "디프만")).id
        bossId = bossAccountRepository.save(BossAccountFixture.create(
            socialId = "social-id-test",
            socialType = BossAccountSocialType.KAKAO,
            name = "통합 테스트용 사장님 이름")
        ).id
    }

    override fun cleanup() {
        super.cleanup()
        medalAcquisitionConditionRepository.deleteAllInBatch()
        userMedalRepository.deleteAllInBatch()
        medalRepository.deleteAllInBatch()
        userRepository.deleteAllInBatch()
        bossAccountRepository.deleteAll()
    }

}
