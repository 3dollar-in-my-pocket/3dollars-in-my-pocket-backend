package com.depromeet.threedollar.api.admin.service.commonservice.admin

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import com.depromeet.threedollar.api.admin.service.commonservice.admin.dto.request.AddAdminRequest
import com.depromeet.threedollar.api.admin.service.commonservice.admin.dto.request.UpdateMyAdminInfoRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminCreator
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminRepository

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class AdminServiceTest(
    private val adminService: AdminService,
    private val adminRepository: AdminRepository,
) {

    @AfterEach
    fun cleanUp() {
        adminRepository.deleteAllInBatch()
    }

    @Nested
    inner class GetMyAdminInfoTest {

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

    }

    @Nested
    inner class UpdateMyAdminInfoTest {

        @Test
        fun `관리자_정보를_수정할때_해당하는 관리자가 없는 경우 NotFoundException`() {
            // given
            val notFoundAdminId = -1L
            val request = UpdateMyAdminInfoRequest(
                name = "토끼"
            )

            // when & then
            assertThatThrownBy { adminService.updateMyAdminInfo(notFoundAdminId, request) }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `새로운 관리자를 신규 등록한다`() {
            // given
            val adminId = 100000L

            val request = AddAdminRequest(
                name = "토끼",
                email = "will.seungho@gmail.com"
            )

            // when
            adminService.addAdmin(request, adminId = adminId)

            // then
            val admins = adminRepository.findAll()
            assertAll({
                assertThat(admins).hasSize(1)
                assertThat(admins[0].email).isEqualTo(request.email)
                assertThat(admins[0].name).isEqualTo(request.name)
                assertThat(admins[0].creatorAdminId).isEqualTo(adminId)
            })
        }

    }

    @Nested
    inner class RegisterAdminTest {

        @Test
        fun `새로운 관리자 등록시 이미 존재하는 이메일인경우 ConflictException`() {
            // given
            val email = "will.seungho@gmail.com"

            val admin = AdminCreator.create(
                email = email,
                name = "토끼"
            )
            adminRepository.save(admin)

            val request = AddAdminRequest(
                name = "토끼",
                email = email
            )

            // when & then
            assertThatThrownBy { adminService.addAdmin(request, 10000L) }.isInstanceOf(ConflictException::class.java)
        }

    }

}
