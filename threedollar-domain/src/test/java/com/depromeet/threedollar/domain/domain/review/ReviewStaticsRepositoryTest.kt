package com.depromeet.threedollar.domain.domain.review

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
class ReviewStaticsRepositoryTest(
    private val reviewRepository: ReviewRepository
) {

    @Nested
    inner class FindActiveReviewsCounts {

        @Test
        fun 활성화된_전체_리뷰수를_조회한다() {
            val userId = 1000000L
            val storeId = 999999L
            val review1 = ReviewCreator.create(storeId, userId, "리뷰 1", 5)
            val review2 = ReviewCreator.create(storeId, userId, "리뷰 2", 4)
            reviewRepository.saveAll(listOf(review1, review2))

            // when
            val counts = reviewRepository.findActiveReviewsCounts()

            // then
            assertThat(counts).isEqualTo(2)
        }

        @Test
        fun 삭제된_리뷰는_전체_리뷰수에서_제외된다() {
            val review = ReviewCreator.create(1000000L, 2000000L, "리뷰 1", 5)
            review.delete()
            reviewRepository.save(review)

            // when
            val counts = reviewRepository.findActiveReviewsCounts()

            // then
            assertThat(counts).isEqualTo(0)
        }

    }

}
