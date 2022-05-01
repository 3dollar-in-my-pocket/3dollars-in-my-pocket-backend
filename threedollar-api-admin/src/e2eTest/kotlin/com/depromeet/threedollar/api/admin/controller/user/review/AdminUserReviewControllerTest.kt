package com.depromeet.threedollar.api.admin.controller.user.review

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.delete
import com.depromeet.threedollar.api.admin.controller.SetupAdminControllerTest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewCreator
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository

internal class AdminUserReviewControllerTest(
    private val reviewRepository: ReviewRepository
) : SetupAdminControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        reviewRepository.deleteAll()
    }

    @DisplayName("DELETE /admin/v1/user/review/{REVIEW_ID}")
    @Test
    fun `관리자가 특정 리뷰를 삭제한다`() {
        // given
        val review = ReviewCreator.create(
            storeId = 10000L,
            userId = 1L,
            rating = 1,
            contents = "욕설"
        )
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
