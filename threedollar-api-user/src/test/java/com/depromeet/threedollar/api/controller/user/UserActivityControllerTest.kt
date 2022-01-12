package com.depromeet.threedollar.api.controller.user

import com.depromeet.threedollar.api.controller.SetupUserControllerTest
import com.depromeet.threedollar.domain.user.domain.medal.MedalCreator
import com.depromeet.threedollar.domain.user.domain.medal.UserMedalCreator
import com.depromeet.threedollar.domain.user.domain.review.ReviewCreator
import com.depromeet.threedollar.domain.user.domain.review.ReviewRepository
import com.depromeet.threedollar.domain.user.domain.store.StoreCreator
import com.depromeet.threedollar.domain.user.domain.store.StoreRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.get

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
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

    @Test
    fun `유저의 회원 정보를 조회한다`() {
        // when & then
        mockMvc.get("/api/v1/user/activity") {
            header(HttpHeaders.AUTHORIZATION, token)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data.userId") { value(testUser.id) }
                    jsonPath("$.data.name") { value(testUser.name) }
                    jsonPath("$.data.socialType") { value(testUser.socialType.toString()) }
                }
            }
    }

    @Test
    fun `유저의 회원 정보를 조회시 활성화중인 메달이 있는 경우 함께 조회된다`() {
        // given
        val medal = MedalCreator.create(
            "붕어빵 전문가",
            "우리 동네 붕어에 대해서는 내가 척척 박사",
            "http://medal-image.png",
            "http://medal-image-disable.png"
        )
        medalRepository.save(medal)
        userMedalRepository.save(UserMedalCreator.createActive(medal, testUser))

        // when & then
        mockMvc.get("/api/v1/user/activity") {
            header(HttpHeaders.AUTHORIZATION, token)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data.medal.name") { value(medal.name) }
                    jsonPath("$.data.medal.iconUrl") { value(medal.activationIconUrl) }
                    jsonPath("$.data.medal.disableIconUrl") { value(medal.disableIconUrl) }
                }
            }
    }

    @Test
    fun `유저의 제보한 가게 개수 조회한다`() {
        // given
        storeRepository.saveAll(
            listOf(
                StoreCreator.createWithDefaultMenu(testUser.id, "제보한 가게 1"),
                StoreCreator.createWithDefaultMenu(testUser.id, "제보한 가게 2")
            )
        )

        // when & then
        mockMvc.get("/api/v1/user/activity") {
            header(HttpHeaders.AUTHORIZATION, token)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data.activity.storesCount") { value(2) }
                }
            }
    }

    @Test
    fun `유저가 작성한 리뷰 개수 조회한다`() {
        // given
        val store = StoreCreator.create(testUser.id, "가게")
        storeRepository.save(store)

        reviewRepository.saveAll(
            listOf(
                ReviewCreator.create(store.id, testUser.id, "리뷰 1", 4),
                ReviewCreator.create(store.id, testUser.id, "리뷰 2", 3),
                ReviewCreator.create(store.id, testUser.id, "리뷰 3", 2)
            )
        )

        // when & then
        mockMvc.get("/api/v1/user/activity") {
            header(HttpHeaders.AUTHORIZATION, token)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data.activity.reviewsCount") { value(3) }
                }
            }
    }

    @Test
    fun `유저가 보유한 메달 개수 조회한다`() {
        // given
        val medalOne = MedalCreator.create(
            "붕어빵 전문가",
            "우리 동네 붕어에 대해서는 내가 척척 박사",
            "http://medal-image.png",
            "http://medal-image-disable.png"
        )
        val medalTwo = MedalCreator.create(
            "붕친맨",
            "앗, 이정도면 붕어빵 척척박사는 넘어섰네요",
            "http://medal-image.png",
            "http://medal-image-disable.png"
        )
        medalRepository.saveAll(listOf(medalOne, medalTwo))

        userMedalRepository.saveAll(
            listOf(
                UserMedalCreator.createActive(medalOne, testUser),
                UserMedalCreator.createInActive(medalTwo, testUser)
            )
        )

        // when & then
        mockMvc.get("/api/v1/user/activity") {
            header(HttpHeaders.AUTHORIZATION, token)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data.activity.medalsCounts") { value(2) }
                }
            }
    }

}