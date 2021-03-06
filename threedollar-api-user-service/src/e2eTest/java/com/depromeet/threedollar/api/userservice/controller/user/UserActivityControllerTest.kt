package com.depromeet.threedollar.api.userservice.controller.user

import com.depromeet.threedollar.api.userservice.SetupUserControllerTest
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalStatus
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get

internal class UserActivityControllerTest(
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository,
    private val medalRepository: MedalRepository,
    private val userMedalRepository: UserMedalRepository,
) : SetupUserControllerTest() {

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
            val medal = MedalFixture.create(
                name = "붕어빵 전문가",
                introduction = "우리 동네 붕어에 대해서는 내가 척척 박사",
                activationIconUrl = "https://medal-image.png",
                disableIconUrl = "https://medal-image-disable.png"
            )
            medalRepository.save(medal)
            userMedalRepository.save(UserMedalFixture.create(medal, user))

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
                    StoreFixture.createWithDefaultMenu(userId = user.id),
                    StoreFixture.createWithDefaultMenu(userId = user.id)
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
            val store = StoreFixture.create(userId = user.id)
            storeRepository.save(store)

            reviewRepository.saveAll(
                listOf(
                    ReviewFixture.create(storeId = store.id, userId = user.id),
                    ReviewFixture.create(storeId = store.id, userId = user.id),
                    ReviewFixture.create(storeId = store.id, userId = user.id),
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
            val medalOne = MedalFixture.create(
                name = "붕어빵 전문가",
                introduction = "우리 동네 붕어에 대해서는 내가 척척 박사",
                activationIconUrl = "https://medal-image.png",
                disableIconUrl = "https://medal-image-disable.png"
            )
            val medalTwo = MedalFixture.create(
                name = "붕친맨",
                introduction = "앗, 이정도면 붕어빵 척척박사는 넘어섰네요",
                activationIconUrl = "https://medal-image.png",
                disableIconUrl = "https://medal-image-disable.png"
            )
            medalRepository.saveAll(listOf(medalOne, medalTwo))

            userMedalRepository.saveAll(
                listOf(
                    UserMedalFixture.create(medalOne, user),
                    UserMedalFixture.create(medalTwo, user, UserMedalStatus.IN_ACTIVE)
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
            val medal = MedalFixture.create(
                name = "붕어빵 전문가",
                introduction = "우리 동네 붕어에 대해서는 내가 척척 박사",
                activationIconUrl = "https://activation--image.png",
                disableIconUrl = "https://disable-image-disable.png"
            )
            medalRepository.save(medal)
            userMedalRepository.save(UserMedalFixture.create(medal, user))

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
                    StoreFixture.createWithDefaultMenu(userId = user.id),
                    StoreFixture.createWithDefaultMenu(userId = user.id)
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
            val store = StoreFixture.create(userId = user.id)
            storeRepository.save(store)

            reviewRepository.saveAll(
                listOf(
                    ReviewFixture.create(storeId = store.id, userId = user.id),
                    ReviewFixture.create(storeId = store.id, userId = user.id),
                    ReviewFixture.create(storeId = store.id, userId = user.id),
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
            val medalOne = MedalFixture.create(
                name = "붕어빵 전문가",
                introduction = "우리 동네 붕어에 대해서는 내가 척척 박사",
                activationIconUrl = "https://medal-image.png",
                disableIconUrl = "https://medal-image-disable.png"
            )
            val medalTwo = MedalFixture.create(
                name = "붕친맨",
                introduction = "앗, 이정도면 붕어빵 척척박사는 넘어섰네요",
                activationIconUrl = "https://medal-image-active-medal-image.png",
                disableIconUrl = "https://medal-image-disable.png"
            )
            medalRepository.saveAll(listOf(medalOne, medalTwo))

            userMedalRepository.saveAll(
                listOf(
                    UserMedalFixture.create(medalOne, user),
                    UserMedalFixture.create(medalTwo, user, UserMedalStatus.IN_ACTIVE)
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
