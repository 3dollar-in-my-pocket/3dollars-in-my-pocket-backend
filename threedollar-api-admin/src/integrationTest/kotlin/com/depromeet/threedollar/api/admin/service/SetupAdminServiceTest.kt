package com.depromeet.threedollar.api.admin.service

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import com.depromeet.threedollar.domain.rds.user.domain.admin.AdminCreator
import com.depromeet.threedollar.domain.rds.user.domain.admin.AdminRepository

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal abstract class SetupAdminServiceTest {

    @Autowired
    private lateinit var adminRepository: AdminRepository

    protected var adminId: Long = 0

    @BeforeEach
    fun setup() {
        val admin = AdminCreator.create(
            email = "will.seungho@gmail.com",
            name = "관리자 계정"
        )
        adminRepository.save(admin)
        adminId = admin.id
    }

    protected fun cleanup() {
        adminRepository.deleteAllInBatch()
    }

}
