package com.depromeet.threedollar.api.admin.service.admin

import com.depromeet.threedollar.api.admin.service.admin.dto.request.UpdateMyAdminInfoRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.rds.user.domain.admin.AdminCreator
import com.depromeet.threedollar.domain.rds.user.domain.admin.AdminRepository
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
        adminRepository.deleteAllInBatch()
    }

    @Test
    fun 자신의_관리자_정보를_조회시_해당하는_관리자가_없는경우_NOT_FOUND_EXCEPTION() {
        // given
        val notFoundAdminId = -1L

        // when & then
        assertThatThrownBy { adminService.getMyAdminInfo(notFoundAdminId) }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `자신의 관리자 정보를 수정한다`() {
        // given
        val name = "끼토끼토"
        val admin = AdminCreator.create(
            email = "will.seungho@gmail.com",
            name = "토끼"
        )
        adminRepository.save(admin)

        val request = UpdateMyAdminInfoRequest(
            name = name
        )

        // when
        adminService.updateMyAdminInfo(admin.id, request)

        // then
        val admins = adminRepository.findAll()
        assertAll({
            assertThat(admins).hasSize(1)
            assertThat(admins[0].name).isEqualTo(name)
            assertThat(admins[0].id).isEqualTo(admin.id)
            assertThat(admins[0].email).isEqualTo(admin.email)
        })
    }

    @Test
    fun `해당하는 관리자가 없는 경우 NotFoundException`() {
        // given
        val notFoundAdminId = -1L
        val request = UpdateMyAdminInfoRequest(
            name = "토끼"
        )

        // when & then
        assertThatThrownBy { adminService.updateMyAdminInfo(notFoundAdminId, request) }.isInstanceOf(NotFoundException::class.java)
    }

}
