package com.depromeet.threedollar.api.adminservice

import com.depromeet.threedollar.domain.rds.domain.userservice.user.User
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

internal abstract class SetupUserIntegrationTest : SetupAdminIntegrationTest() {

    @Autowired
    protected lateinit var userRepository: UserRepository

    protected var userId: Long = -1L

    protected lateinit var user: User

    @BeforeEach
    override fun setup() {
        user = userRepository.save(UserFixture.create())
        userId = user.id
    }

}
