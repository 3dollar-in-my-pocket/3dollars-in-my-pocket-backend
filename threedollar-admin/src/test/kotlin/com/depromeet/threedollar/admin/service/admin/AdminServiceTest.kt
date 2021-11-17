package com.depromeet.threedollar.admin.service.admin

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.domain.admin.AdminRepository
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
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
    fun 자신의_관리자_정보를_조회시_해당하는_관리자가_없는경우_NOT_FOUND_EXCEPTION() {
        // given
        val notFoundAdminId = -1L

        // when & then
        assertThatThrownBy { adminService.getMyAdminInfo(notFoundAdminId) }.isInstanceOf(NotFoundException::class.java)
    }

}
