package com.depromeet.threedollar.api.admin.service.admin

import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.rds.user.domain.admin.Admin
import com.depromeet.threedollar.domain.rds.user.domain.admin.AdminRepository

object AdminServiceUtils {

    fun validateNotExistsEmail(adminRepository: AdminRepository, email: String) {
        if (adminRepository.existsByEmail(email)) {
            throw ConflictException("이미 존재하는 이메일($email) 입니다.", ErrorCode.CONFLICT_EMAIL)
        }
    }

    fun findAdminById(adminRepository: AdminRepository, adminId: Long): Admin {
        return adminRepository.findAdminById(adminId)
            ?: throw NotFoundException("해당하는 관리자 ($adminId)는 존재하지 않습니다", ErrorCode.NOTFOUND_ADMIN)
    }

}
