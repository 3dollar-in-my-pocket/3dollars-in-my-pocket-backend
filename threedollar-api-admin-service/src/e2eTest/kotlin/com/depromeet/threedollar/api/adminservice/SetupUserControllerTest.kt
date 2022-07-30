package com.depromeet.threedollar.api.adminservice

import com.depromeet.threedollar.domain.rds.domain.userservice.user.User
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

internal abstract class SetupUserControllerTest : SetupAdminControllerTest() {

    @Autowired
    protected lateinit var userRepository: UserRepository

    protected lateinit var user: User

    @BeforeEach
    fun setupUser() {
        user = userRepository.save(UserFixture.create("social-id", UserSocialType.APPLE, "닉네임"))
    }

}
