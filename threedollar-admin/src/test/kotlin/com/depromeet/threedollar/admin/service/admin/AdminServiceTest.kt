package com.depromeet.threedollar.admin.service.admin

import com.depromeet.threedollar.admin.service.admin.dto.response.AdminInfoResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.domain.admin.AdminCreator
import com.depromeet.threedollar.domain.domain.admin.AdminRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class AdminServiceTest(
    private val adminService: AdminService,
    private val adminRepository: AdminRepository
) {

    @AfterEach
    fun cleanUp() {
        adminRepository.deleteAll()
    }

    @Test
    fun 자신의_관리자_정보를_조회한다() {
        // given
        val email = "will.seungho@gmail.com"
        val name = "강승호"
        val admin = adminRepository.save(AdminCreator.create(email, name))

        // when
        val response = adminService.getMyAdminInfo(admin.id)

        // then
        assertAll({
            assertAdminInfoResponse(response, email, name)
        })
    }

    @Test
    fun 자신의_관리자_정보를_조회시_해당하는_관리자가_없는경우_NOT_FOUND_EXCEPTION() {
        // given
        val notFoundAdminId = -1L

        // when & then
        assertThatThrownBy { adminService.getMyAdminInfo(notFoundAdminId) }.isInstanceOf(NotFoundException::class.java)
    }

    private fun assertAdminInfoResponse(response: AdminInfoResponse, email: String, name: String) {
        assertThat(response.email).isEqualTo(email)
        assertThat(response.name).isEqualTo(name)
    }

}
