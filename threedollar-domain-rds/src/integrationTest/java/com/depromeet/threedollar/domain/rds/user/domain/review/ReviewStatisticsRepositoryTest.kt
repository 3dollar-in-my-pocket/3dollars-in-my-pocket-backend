package com.depromeet.threedollar.domain.rds.user.domain.review

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
class ReviewStatisticsRepositoryTest(
    private val reviewRepository: ReviewRepository
) {

    @Nested
    inner class FindActiveReviewsCounts {

        @Test
        fun 활성화된_전체_리뷰수를_조회한다() {
            // given
            val review1 = ReviewCreator.create(
                    storeId = 1L,
                    userId = 100L,
                    contents = "리뷰 1",
                    rating = 5
            )
            val review2 = ReviewCreator.create(
                    storeId = 2L,
                    userId = 10L,
                    contents = "리뷰 2",
                    rating = 4
            )
            reviewRepository.saveAll(listOf(review1, review2))

            // when
            val counts = reviewRepository.findActiveReviewsCounts()

            // then
            assertThat(counts).isEqualTo(2)
        }

        @Test
        fun 삭제된_리뷰는_전체_리뷰수에서_제외된다() {
            // given
            val review = ReviewCreator.createDeleted(
                    storeId = 1L,
                    userId = 100L,
                    contents = "리뷰 1",
                    rating = 5
            )
            reviewRepository.save(review)

            // when
            val counts = reviewRepository.findActiveReviewsCounts()

            // then
            assertThat(counts).isEqualTo(0)
        }

    }

}
