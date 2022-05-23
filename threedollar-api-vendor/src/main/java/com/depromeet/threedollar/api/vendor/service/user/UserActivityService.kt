package com.depromeet.threedollar.api.vendor.service.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.vendor.service.user.dto.response.UserWithActivityResponse
import com.depromeet.threedollar.domain.rds.vendor.domain.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreRepository
import com.depromeet.threedollar.domain.rds.vendor.domain.user.UserRepository

@Service
class UserActivityService(
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository,
) {

    @Transactional(readOnly = true)
    fun getUserActivity(userId: Long?): UserWithActivityResponse {
        return UserWithActivityResponse.of(
            user = UserServiceUtils.findUserById(userRepository, userId),
            storesCount = storeRepository.countByUserId(userId),
            reviewsCount = reviewRepository.countByUserId(userId)
        )
    }

}
