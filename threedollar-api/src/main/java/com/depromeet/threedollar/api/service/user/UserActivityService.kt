package com.depromeet.threedollar.api.service.user

import com.depromeet.threedollar.api.service.user.dto.response.UserWithActivityResponse
import com.depromeet.threedollar.domain.domain.review.ReviewRepository
import com.depromeet.threedollar.domain.domain.store.StoreRepository
import com.depromeet.threedollar.domain.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserActivityService(
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository
) {

    @Transactional(readOnly = true)
    fun getUserActivity(userId: Long?): UserWithActivityResponse {
        return UserWithActivityResponse.of(
            UserServiceUtils.findUserById(userRepository, userId),
            storeRepository.findCountsByUserId(userId),
            reviewRepository.findCountsByUserId(userId)
        )
    }

}
