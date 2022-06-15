package com.depromeet.threedollar.api.adminservice

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminCreator
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminRepository

internal abstract class SetupAdminIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var adminRepository: AdminRepository

    protected var adminId: Long = 0

    @BeforeEach
    fun setup() {
        val admin = AdminCreator.create("admin@gmail.com", "name")
        adminRepository.save(admin)
        adminId = admin.id
    }

}
