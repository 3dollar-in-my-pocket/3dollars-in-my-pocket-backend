package com.depromeet.threedollar.api.userservice.service.user

import com.depromeet.threedollar.api.userservice.service.user.dto.response.UserWithActivityResponse
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserActivityService(
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository,
) {

    @Transactional(readOnly = true)
    fun getUserActivity(userId: Long?): UserWithActivityResponse {
        return UserWithActivityResponse.of(
            user = UserServiceHelper.findUserById(userRepository, userId),
            storesCount = storeRepository.countByUserId(userId),
            reviewsCount = reviewRepository.countByUserId(userId)
        )
    }

}
