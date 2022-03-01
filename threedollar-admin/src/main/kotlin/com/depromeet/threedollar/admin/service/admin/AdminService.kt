package com.depromeet.threedollar.admin.service.admin

import com.depromeet.threedollar.admin.service.admin.dto.response.AdminInfoResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.user.domain.admin.Admin
import com.depromeet.threedollar.domain.user.domain.admin.AdminRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminService(
    private val adminRepository: AdminRepository
) {

    @Transactional(readOnly = true)
    fun getMyAdminInfo(adminId: Long): AdminInfoResponse {
        val admin = findAdminById(adminId)
        return AdminInfoResponse.of(admin)
    }

    private fun findAdminById(adminId: Long): Admin {
        return adminRepository.findAdminById(adminId)
            ?: throw NotFoundException("해당하는 관리자 ($adminId)는 존재하지 않습니다", ErrorCode.NOTFOUND_ADMIN)
    }

}
