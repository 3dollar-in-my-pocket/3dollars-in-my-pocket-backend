package com.depromeet.threedollar.api.user.controller.user

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get
import com.depromeet.threedollar.api.user.controller.SetupUserControllerTest
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalStatus
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalCreator
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalCreator
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewCreator
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreCreator

internal class UserActivityControllerTest(
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository,
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
            val medal = MedalCreator.create(
                name = "붕어빵 전문가",
                introduction = "우리 동네 붕어에 대해서는 내가 척척 박사",
                activationIconUrl = "https://medal-image.png",
                disableIconUrl = "https://medal-image-disable.png"
            )
            medalRepository.save(medal)
            userMedalRepository.save(UserMedalCreator.create(medal, user))

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
        fun `유저가 제보한 가게 개수 조회한다`() {
            // given
            storeRepository.saveAll(
                listOf(
                    StoreCreator.createWithDefaultMenu(user.id, "제보한 가게 1"),
                    StoreCreator.createWithDefaultMenu(user.id, "제보한 가게 2")
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
        fun `유저가 작성한 리뷰 개수도 조회한다`() {
            // given
            val store = StoreCreator.create(user.id, "가게")
            storeRepository.save(store)

            reviewRepository.saveAll(
                listOf(
                    ReviewCreator.create(store.id, user.id, "리뷰 1", 4),
                    ReviewCreator.create(store.id, user.id, "리뷰 2", 3),
                    ReviewCreator.create(store.id, user.id, "리뷰 3", 2)
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
        fun `유저가 보유한 메달 개수도 조회한다`() {
            // given
            val medalOne = MedalCreator.create(
                name = "붕어빵 전문가",
                introduction = "우리 동네 붕어에 대해서는 내가 척척 박사",
                activationIconUrl = "https://medal-image.png",
                disableIconUrl = "https://medal-image-disable.png"
            )
            val medalTwo = MedalCreator.create(
                name = "붕친맨",
                introduction = "앗, 이정도면 붕어빵 척척박사는 넘어섰네요",
                activationIconUrl = "https://medal-image.png",
                disableIconUrl = "https://medal-image-disable.png"
            )
            medalRepository.saveAll(listOf(medalOne, medalTwo))

            userMedalRepository.saveAll(
                listOf(
                    UserMedalCreator.create(medalOne, user),
                    UserMedalCreator.create(medalTwo, user, UserMedalStatus.IN_ACTIVE)
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
            val medal = MedalCreator.create(
                name = "붕어빵 전문가",
                introduction = "우리 동네 붕어에 대해서는 내가 척척 박사",
                activationIconUrl = "https://activation--image.png",
                disableIconUrl = "https://disable-image-disable.png"
            )
            medalRepository.save(medal)
            userMedalRepository.save(UserMedalCreator.create(medal, user))

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
        fun `유저의 제보한 가게 개수도 조회한다`() {
            // given
            storeRepository.saveAll(
                listOf(
                    StoreCreator.createWithDefaultMenu(user.id, "제보한 가게 1"),
                    StoreCreator.createWithDefaultMenu(user.id, "제보한 가게 2")
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
        fun `유저가 작성한 리뷰 개수도 조회한다`() {
            // given
            val store = StoreCreator.create(user.id, "가게")
            storeRepository.save(store)

            reviewRepository.saveAll(
                listOf(
                    ReviewCreator.create(store.id, user.id, "리뷰 1", 4),
                    ReviewCreator.create(store.id, user.id, "리뷰 2", 3),
                    ReviewCreator.create(store.id, user.id, "리뷰 3", 2)
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
        fun `유저가 보유한 메달 개수도 조회한다`() {
            // given
            val medalOne = MedalCreator.create(
                name = "붕어빵 전문가",
                introduction = "우리 동네 붕어에 대해서는 내가 척척 박사",
                activationIconUrl = "https://medal-image.png",
                disableIconUrl = "https://medal-image-disable.png"
            )
            val medalTwo = MedalCreator.create(
                name = "붕친맨",
                introduction = "앗, 이정도면 붕어빵 척척박사는 넘어섰네요",
                activationIconUrl = "https://medal-image-active-medal-image.png",
                disableIconUrl = "https://medal-image-disable.png"
            )
            medalRepository.saveAll(listOf(medalOne, medalTwo))

            userMedalRepository.saveAll(
                listOf(
                    UserMedalCreator.create(medalOne, user),
                    UserMedalCreator.create(medalTwo, user, UserMedalStatus.IN_ACTIVE)
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
