package com.depromeet.threedollar.admin.service

import com.depromeet.threedollar.domain.domain.admin.AdminCreator
import com.depromeet.threedollar.domain.domain.admin.AdminRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal abstract class AdminSetUpTest {

    @Autowired
    private lateinit var adminRepository: AdminRepository

    protected var adminId: Long = 0

    @BeforeEach
    fun setup() {
        val admin = AdminCreator.create("admin@gmail.com", "name")
        adminRepository.save(admin)
        adminId = admin.id
    }

    protected fun cleanup() {
        adminRepository.deleteAllInBatch()
    }

}
