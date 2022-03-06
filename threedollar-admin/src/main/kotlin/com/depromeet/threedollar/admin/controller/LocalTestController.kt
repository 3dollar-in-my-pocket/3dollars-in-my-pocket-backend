package com.depromeet.threedollar.admin.controller

import com.depromeet.threedollar.admin.config.session.SessionConstants.ADMIN_ID
import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.domain.user.domain.admin.Admin
import com.depromeet.threedollar.domain.user.domain.admin.AdminRepository
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession

private const val TEST_ADMIN_EMAIL = "test.admin@test.com"
private const val TEST_ADMIN_NAME = "테스트 관리자"

@Profile("local", "local-docker", "dev", "integration-test")
@RestController
class LocalTestController(
    private val adminRepository: AdminRepository,
    private val httpSession: HttpSession
) {

    @GetMapping("/test-token")
    fun getTestToken(): ApiResponse<String> {
        val admin = adminRepository.findAdminByEmail(TEST_ADMIN_EMAIL)
            ?: adminRepository.save(Admin.newInstance(TEST_ADMIN_EMAIL, TEST_ADMIN_NAME))
        httpSession.setAttribute(ADMIN_ID, admin.id)
        return ApiResponse.success(httpSession.id)
    }

}
