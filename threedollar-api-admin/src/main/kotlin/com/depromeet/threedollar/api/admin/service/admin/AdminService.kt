package com.depromeet.threedollar.api.admin.service.admin

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.admin.service.admin.dto.request.AddAdminRequest
import com.depromeet.threedollar.api.admin.service.admin.dto.request.RetrieveAdminsWithPagingRequest
import com.depromeet.threedollar.api.admin.service.admin.dto.request.UpdateMyAdminInfoRequest
import com.depromeet.threedollar.api.admin.service.admin.dto.response.AdminInfoResponse
import com.depromeet.threedollar.api.admin.service.admin.dto.response.AdminListInfoWithPagingResponse
import com.depromeet.threedollar.domain.rds.vendor.domain.admin.AdminRepository

@Service
class AdminService(
    private val adminRepository: AdminRepository,
) {

    @Transactional(readOnly = true)
    fun getMyAdminInfo(adminId: Long): AdminInfoResponse {
        val admin = AdminServiceUtils.findAdminById(adminRepository, adminId)
        return AdminInfoResponse.of(admin)
    }

    @Transactional
    fun updateMyAdminInfo(adminId: Long, request: UpdateMyAdminInfoRequest): AdminInfoResponse {
        val admin = AdminServiceUtils.findAdminById(adminRepository, adminId)
        request.let {
            admin.updateName(it.name)
        }
        return AdminInfoResponse.of(admin)
    }

    @Transactional
    fun addAdmin(request: AddAdminRequest, adminId: Long) {
        AdminServiceUtils.validateNotExistsEmail(adminRepository, request.email)
        adminRepository.save(request.toEntity(adminId))
    }

    @Transactional(readOnly = true)
    fun retrieveAdminsWithPaging(request: RetrieveAdminsWithPagingRequest): AdminListInfoWithPagingResponse {
        return AdminListInfoWithPagingResponse.of(
            admins = adminRepository.findAllWithPagination(request.page - 1, request.size),
            totalSize = adminRepository.count(),
            perSize = request.size
        )
    }

}
