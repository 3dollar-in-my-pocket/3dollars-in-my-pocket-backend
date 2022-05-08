package com.depromeet.threedollar.api.admin.controller.user.review

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import com.depromeet.threedollar.api.admin.controller.SetupAdminControllerTest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewCreator
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreCreator
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository
import com.depromeet.threedollar.domain.rds.user.domain.user.UserCreator
import com.depromeet.threedollar.domain.rds.user.domain.user.UserRepository
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType

internal class AdminUserReviewControllerTest(
    private val reviewRepository: ReviewRepository,
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository
) : SetupAdminControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        reviewRepository.deleteAll()
    }

    @DisplayName("DELETE /admin/v1/user/review/{REVIEW_ID}")
    @Nested
    inner class DeleteReviewByAdminApiTest {

        @Test
        fun `관리자가 특정 리뷰를 삭제한다`() {
            // given
            val review = ReviewCreator.builder()
                .storeId(10000L)
                .userId(1L)
                .contents("욕설")
                .rating(1)
                .build()
            reviewRepository.save(review)

            mockMvc.delete("/v1/user/review/${review.id}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data") { value(ApiResponse.OK.data) }
                }
        }

    }

    @DisplayName("GET /admin/v1/user/review/{REVIEW_ID}")
    @Nested
    inner class GetReviewInfoApiTest {

        @Test
        fun `관리자가 특정 리뷰를 조회한다`() {
            // given
            val user = UserCreator.builder()
                .socialId("social-id")
                .socialType(UserSocialType.GOOGLE)
                .name("닉네임")
                .build()
            userRepository.save(user)

            val store = StoreCreator.builder()
                .userId(user.id)
                .storeName("가게 이름")
                .build()
            storeRepository.save(store)

            val review = ReviewCreator.builder()
                .storeId(store.id)
                .userId(user.id)
                .contents("너무 맛있어요")
                .rating(5)
                .build()
            reviewRepository.save(review)

            mockMvc.get("/v1/user/review/${review.id}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.reviewId") { value(review.id) }
                    jsonPath("$.data.contents") { value(review.contents) }
                    jsonPath("$.data.rating") { value(review.rating) }
                    jsonPath("$.data.user.userId") { value(user.id) }
                    jsonPath("$.data.user.socialType") { value(user.socialType.name) }
                    jsonPath("$.data.user.name") { value(user.name) }
                    jsonPath("$.data.store.storeId") { value(store.id) }
                    jsonPath("$.data.store.latitude") { value(store.latitude) }
                    jsonPath("$.data.store.longitude") { value(store.longitude) }
                    jsonPath("$.data.store.storeName") { value(store.name) }
                    jsonPath("$.data.store.rating") { value(store.rating) }
                }
        }

        @Test
        fun `관리자가 특정 리뷰를 조회할때 유저나 가게가 없는경우 해당 정보에 null이 반환된다`() {
            // given
            val review = ReviewCreator.builder()
                .storeId(-1L)
                .userId(-1L)
                .contents("맛있어요")
                .rating(3)
                .build()
            reviewRepository.save(review)

            mockMvc.get("/v1/user/review/${review.id}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.reviewId") { value(review.id) }
                    jsonPath("$.data.contents") { value(review.contents) }
                    jsonPath("$.data.rating") { value(review.rating) }
                    jsonPath("$.data.user") { value(null) }
                    jsonPath("$.data.store") { value(null) }
                }
        }

        @Test
        fun `관리자가 특정 리뷰를 조회할때 해당하는 리뷰가 존재하지 않는경우 404에러가 발생한다`() {
            // given
            val reviewId = -1L

            // when & then
            mockMvc.get("/v1/user/review/${reviewId}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    jsonPath("$.resultCode") { value(ErrorCode.NOTFOUND_REVIEW.code) }
                    jsonPath("$.message") { value(ErrorCode.NOTFOUND_REVIEW.message) }
                }
        }

    }

}
