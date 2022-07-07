package com.depromeet.threedollar.api.adminservice.service.commonservice.auth

import com.depromeet.threedollar.api.adminservice.service.commonservice.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.utils.HttpHeaderUtils
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminRepository
import com.depromeet.threedollar.infrastructure.external.client.google.GoogleAuthApiClient
import org.springframework.stereotype.Service

@Service
class GoogleAuthService(
    private val adminRepository: AdminRepository,
    private val googleAuthApiClient: GoogleAuthApiClient,
) : AuthService {

    override fun login(request: LoginRequest): Long {
        val googleProfileInfo = googleAuthApiClient.getProfileInfo(HttpHeaderUtils.withBearerToken(request.token))
        val admin = adminRepository.findAdminByEmail(googleProfileInfo.email)
            ?: throw NotFoundException("해당하는 관리자(${googleProfileInfo.email})은 존재하지 않습니다", ErrorCode.NOT_FOUND_ADMIN)
        return admin.id
    }

}
