package com.depromeet.threedollar.api.adminservice

import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminFixture
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

internal abstract class SetupAdminIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var adminRepository: AdminRepository

    protected var adminId: Long = 0

    @BeforeEach
    fun setup() {
        val admin = AdminFixture.create("admin@gmail.com", "name")
        adminRepository.save(admin)
        adminId = admin.id
    }

}
