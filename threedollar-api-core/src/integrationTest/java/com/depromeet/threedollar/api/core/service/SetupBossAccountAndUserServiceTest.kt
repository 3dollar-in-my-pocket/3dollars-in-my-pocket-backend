package com.depromeet.threedollar.api.core.service

import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionRepository
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalRepository
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalRepository
import com.depromeet.threedollar.domain.rds.user.domain.user.UserCreator
import com.depromeet.threedollar.domain.rds.user.domain.user.UserRepository
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal abstract class SetupBossAccountAndUserServiceTest {

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
        userId = userRepository.save(UserCreator.create("social-id", UserSocialType.KAKAO, "디프만")).id
        bossId = bossAccountRepository.save(BossAccountCreator.create(
            socialId = "social-id-test",
            socialType = BossAccountSocialType.KAKAO,
            name = "통합 테스트용 사장님 이름")
        ).id
    }

    protected fun cleanup() {
        medalAcquisitionConditionRepository.deleteAllInBatch()
        userMedalRepository.deleteAllInBatch()
        medalRepository.deleteAllInBatch()
        userRepository.deleteAllInBatch()
        bossAccountRepository.deleteAll()
    }

}