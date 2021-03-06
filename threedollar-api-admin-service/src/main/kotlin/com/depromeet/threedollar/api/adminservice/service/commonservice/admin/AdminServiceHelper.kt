package com.depromeet.threedollar.api.adminservice.service.commonservice.admin

import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.Admin
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminRepository

object AdminServiceHelper {

    fun validateNotExistsEmail(adminRepository: AdminRepository, email: String) {
        if (adminRepository.existsAdminByEmail(email)) {
            throw ConflictException("이미 관리자로 등록된 이메일($email) 입니다.", ErrorCode.E409_DUPLICATE_EMAIL)
        }
    }

    fun findAdminById(adminRepository: AdminRepository, adminId: Long): Admin {
        return adminRepository.findAdminById(adminId)
            ?: throw NotFoundException("해당하는 관리자 ($adminId)는 존재하지 않습니다", ErrorCode.E404_NOT_EXISTS_ADMIN)
    }

}
