package com.depromeet.threedollar.api.user.controller.user

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get
import com.depromeet.threedollar.api.user.controller.SetupUserControllerTest
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalCreator
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalCreator
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalStatus
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewCreator
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreCreator
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreWithMenuCreator

internal class UserActivityControllerTest(
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository
) : SetupUserControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        storeRepository.deleteAll()
        reviewRepository.deleteAllInBatch()
    }

    @DisplayName("GET /v1/user/me/activity")
    @Nested
    inner class GetMyUserActivityApiTest {

        @Test
        fun `유저의 활동정보가 포함된 회원 정보를 조회한다`() {
            // when & then
            mockMvc.get("/v1/user/me/activity") {
                header(HttpHeaders.AUTHORIZATION, token)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.userId") { value(user.id) }
                    jsonPath("$.data.name") { value(user.name) }
                    jsonPath("$.data.socialType") { value(user.socialType.toString()) }
                }
        }

        @Test
        fun `유저의 활동정보가 포함된 회원 정보를 조회시 활성화중인 메달이 있는 경우 함께 조회된다`() {
            // given
            val medal = MedalCreator.builder()
                .name("붕어빵 전문가")
                .acquisitionDescription("우리 동네 붕어에 대해서는 내가 척척 박사")
                .activationIconUrl("https://medal-image.png")
                .disableIconUrl("https://medal-image-disable.png")
                .build()
            medalRepository.save(medal)
            userMedalRepository.save(UserMedalCreator.builder()
                .medal(medal)
                .user(user)
                .status(UserMedalStatus.ACTIVE)
                .build()
            )

            // when & then
            mockMvc.get("/v1/user/me/activity") {
                header(HttpHeaders.AUTHORIZATION, token)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.medal.name") { value(medal.name) }
                    jsonPath("$.data.medal.iconUrl") { value(medal.activationIconUrl) }
                    jsonPath("$.data.medal.disableIconUrl") { value(medal.disableIconUrl) }
                }
        }

        @Test
        fun `유저의 제보한 가게 개수 조회한다`() {
            // given
            storeRepository.saveAll(
                listOf(
                    StoreWithMenuCreator.builder()
                        .userId(user.id)
                        .storeName("제보한 가게 1")
                        .build(),
                    StoreWithMenuCreator.builder()
                        .userId(user.id)
                        .storeName("제보한 가게 2")
                        .build()
                )
            )

            // when & then
            mockMvc.get("/v1/user/me/activity") {
                header(HttpHeaders.AUTHORIZATION, token)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.activity.storesCount") { value(2) }
                }
        }

        @Test
        fun `유저가 작성한 리뷰 개수 조회한다`() {
            // given
            val store = StoreCreator.builder()
                .userId(user.id)
                .storeName("제보한 가게 1")
                .build()
            storeRepository.save(store)

            reviewRepository.saveAll(
                listOf(
                    ReviewCreator.builder()
                        .storeId(store.id)
                        .userId(user.id)
                        .contents("리뷰 1")
                        .rating(4)
                        .build(),
                    ReviewCreator.builder()
                        .storeId(store.id)
                        .userId(user.id)
                        .contents("리뷰 2")
                        .rating(3)
                        .build(),
                    ReviewCreator.builder()
                        .storeId(store.id)
                        .userId(user.id)
                        .contents("리뷰 3")
                        .rating(2)
                        .build()
                )
            )

            // when & then
            mockMvc.get("/v1/user/me/activity") {
                header(HttpHeaders.AUTHORIZATION, token)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.activity.reviewsCount") { value(3) }
                }
        }

        @Test
        fun `유저가 보유한 메달 개수 조회한다`() {
            // given
            val medalOne = MedalCreator.builder()
                .name("붕어빵 전문가")
                .acquisitionDescription("우리 동네 붕어에 대해서는 내가 척척 박사")
                .activationIconUrl("https://medal-image.png")
                .disableIconUrl("https://medal-image-disable.png")
                .build()
            val medalTwo = MedalCreator.builder()
                .name("붕친맨")
                .acquisitionDescription("앗, 이정도면 붕어빵 척척박사는 넘어섰네요")
                .activationIconUrl("https://medal-image.png")
                .disableIconUrl("https://medal-image-disable.png")
                .build()
            medalRepository.saveAll(listOf(medalOne, medalTwo))

            userMedalRepository.saveAll(
                listOf(
                    UserMedalCreator.builder()
                        .medal(medalOne)
                        .user(user)
                        .build(),
                    UserMedalCreator.builder()
                        .medal(medalTwo)
                        .user(user)
                        .status(UserMedalStatus.IN_ACTIVE)
                        .build()
                )
            )

            // when & then
            mockMvc.get("/v1/user/me/activity") {
                header(HttpHeaders.AUTHORIZATION, token)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.activity.medalsCounts") { value(2) }
                }
        }

    }

    @DisplayName("GET /v1/user/activity")
    @Nested
    inner class GetUserActivityApiTest {

        @Test
        fun `유저의 활동정보가 포함된 회원 정보를 조회한다`() {
            // when & then
            mockMvc.get("/v1/user/activity") {
                header(HttpHeaders.AUTHORIZATION, token)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.userId") { value(user.id) }
                    jsonPath("$.data.name") { value(user.name) }
                    jsonPath("$.data.socialType") { value(user.socialType.toString()) }
                }
        }

        @Test
        fun `유저의 활동정보가 포함된 회원 정보를 조회시 활성화중인 메달이 있는 경우 함께 조회된다`() {
            // given
            val medal = MedalCreator.builder()
                .name("붕어빵 전문가")
                .acquisitionDescription("우리 동네 붕어에 대해서는 내가 척척 박사")
                .activationIconUrl("https://medal-image.png")
                .disableIconUrl("https://medal-image-disable.png")
                .build()
            medalRepository.save(medal)
            userMedalRepository.save(UserMedalCreator.builder()
                .medal(medal)
                .user(user)
                .build()
            )

            // when & then
            mockMvc.get("/v1/user/activity") {
                header(HttpHeaders.AUTHORIZATION, token)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.medal.name") { value(medal.name) }
                    jsonPath("$.data.medal.iconUrl") { value(medal.activationIconUrl) }
                    jsonPath("$.data.medal.disableIconUrl") { value(medal.disableIconUrl) }
                }
        }

        @Test
        fun `유저의 제보한 가게 개수 조회한다`() {
            // given
            storeRepository.saveAll(
                listOf(
                    StoreWithMenuCreator.builder()
                        .userId(user.id)
                        .storeName("제보한 가게 1")
                        .build(),
                    StoreWithMenuCreator.builder()
                        .userId(user.id)
                        .storeName("제보한 가게 2")
                        .build()
                )
            )

            // when & then
            mockMvc.get("/v1/user/activity") {
                header(HttpHeaders.AUTHORIZATION, token)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.activity.storesCount") { value(2) }
                }
        }

        @Test
        fun `유저가 작성한 리뷰 개수 조회한다`() {
            // given
            val store = StoreCreator.builder()
                .userId(user.id)
                .storeName("가게 이름")
                .build()
            storeRepository.save(store)

            reviewRepository.saveAll(
                listOf(
                    ReviewCreator.builder()
                        .storeId(store.id)
                        .userId(user.id)
                        .contents("리뷰 1")
                        .rating(4)
                        .build(),
                    ReviewCreator.builder()
                        .storeId(store.id)
                        .userId(user.id)
                        .contents("리뷰 2")
                        .rating(3)
                        .build(),
                    ReviewCreator.builder()
                        .storeId(store.id)
                        .userId(user.id)
                        .contents("리뷰 3")
                        .rating(2)
                        .build()
                )
            )

            // when & then
            mockMvc.get("/v1/user/activity") {
                header(HttpHeaders.AUTHORIZATION, token)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.activity.reviewsCount") { value(3) }
                }
        }

        @Test
        fun `유저가 보유한 메달 개수 조회한다`() {
            // given
            val medalOne = MedalCreator.builder()
                .name("붕어빵 전문가")
                .acquisitionDescription("우리 동네 붕어에 대해서는 내가 척척 박사")
                .activationIconUrl("https://medal-image.png")
                .disableIconUrl("https://medal-image-disable.png")
                .build()
            val medalTwo = MedalCreator.builder()
                .name("붕친맨")
                .acquisitionDescription("앗, 이정도면 붕어빵 척척박사는 넘어섰네요")
                .activationIconUrl("https://medal-image.png")
                .disableIconUrl("https://medal-image-disable.png")
                .build()
            medalRepository.saveAll(listOf(medalOne, medalTwo))

            userMedalRepository.saveAll(
                listOf(
                    userMedalRepository.save(UserMedalCreator.builder()
                        .medal(medalOne)
                        .user(user)
                        .status(UserMedalStatus.ACTIVE)
                        .build()
                    ),
                    userMedalRepository.save(UserMedalCreator.builder()
                        .medal(medalTwo)
                        .user(user)
                        .status(UserMedalStatus.IN_ACTIVE)
                        .build()
                    )
                )
            )

            // when & then
            mockMvc.get("/v1/user/activity") {
                header(HttpHeaders.AUTHORIZATION, token)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.activity.medalsCounts") { value(2) }
                }
        }

    }

}
