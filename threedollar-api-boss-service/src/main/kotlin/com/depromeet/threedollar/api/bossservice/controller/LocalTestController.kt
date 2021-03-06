package com.depromeet.threedollar.api.bossservice.controller

import com.depromeet.threedollar.api.bossservice.config.interceptor.Auth
import com.depromeet.threedollar.api.bossservice.config.resolver.BossId
import com.depromeet.threedollar.api.bossservice.config.session.SessionConstants
import com.depromeet.threedollar.api.bossservice.service.auth.dto.response.LoginResponse
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.category.BossStoreCategoryServiceHelper
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.BossStoreFeedbackService
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.BusinessNumber
import com.depromeet.threedollar.common.model.ContactsNumber
import com.depromeet.threedollar.common.model.UserMetaValue
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.common.utils.UserMetaSessionUtils
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistration
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationBossForm
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationStoreForm
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreAppearanceDay
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreMenu
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import io.swagger.annotations.ApiOperation
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.servlet.http.HttpSession

private val BOSS = BossAccount.of(
    bossId = "test" + UUID.randomUUID().toString(),
    name = "????????? ??????",
    socialId = "test-social-id",
    socialType = BossAccountSocialType.KAKAO,
    businessNumber = BusinessNumber.of("000-00-00000"),
)

@Profile("local", "local-docker", "integration-test", "dev")
@RestController
class LocalTestController(
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val bossStoreFeedbackService: BossStoreFeedbackService,
    private val httpSession: HttpSession,
) {

    @ApiOperation("[?????????] ????????? ????????? ????????? ????????? ?????? ????????????.")
    @GetMapping("/test-token")
    fun getTestBossAccountToken(): ApiResponse<LoginResponse> {
        val bossAccount =
            bossAccountRepository.findBossAccountBySocialInfo(BOSS.socialInfo.socialId, BOSS.socialInfo.socialType)
                ?: bossAccountRepository.save(BOSS)
        httpSession.setAttribute(SessionConstants.BOSS_ACCOUNT_ID, bossAccount.id)
        return ApiResponse.success(LoginResponse(httpSession.id, bossAccount.id))
    }

    @ApiOperation("[?????????] ????????? ?????? ????????? ???????????????")
    @PostMapping("/test-registration")
    fun addMockRegistration(
        @RequestParam bossName: String,
        @RequestParam storeName: String,
        @RequestParam categoriesIds: Set<String>,
        @RequestParam certificationPhotoUrl: String,
        @RequestParam contactsNumber: String,
        @RequestParam businessNumber: String,
    ): ApiResponse<MockRegistrationResponse> {
        val bossRegistration = BossRegistration.of(
            boss = RegistrationBossForm.of(
                socialId = UUID.randomUUID().toString(),
                socialType = BossAccountSocialType.KAKAO,
                name = bossName,
                businessNumber = businessNumber,
            ),
            store = RegistrationStoreForm.of(
                name = "?????? ??????",
                contactsNumber = contactsNumber,
                categoriesIds = categoriesIds,
                certificationPhotoUrl = certificationPhotoUrl
            )
        )
        bossRegistrationRepository.save(bossRegistration)

        httpSession.setAttribute(SessionConstants.BOSS_ACCOUNT_ID, bossRegistration.id)
        return ApiResponse.success(MockRegistrationResponse(
            token = httpSession.id,
            registrationId = bossRegistration.id
        ))
    }

    data class MockRegistrationResponse(
        val token: String,
        val registrationId: String,
    )

    @ApiOperation("[?????? ?????????] ?????? ??? ???????????? ???????????????.")
    @Auth
    @PostMapping("/test-store")
    fun addMockStoreData(
        @BossId bossId: String,
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam categoriesIds: Set<String>,
        @RequestParam randomBossId: String?,
    ): ApiResponse<String> {
        if (bossStoreRepository.findBossStoreByBossId(bossId) != null) {
            throw ConflictException("????????? ($bossId)??? ?????? ????????? ?????????????????????")
        }
        BossStoreCategoryServiceHelper.validateExistsCategories(bossStoreCategoryRepository, categoriesIds)
        val bossStore = bossStoreRepository.save(
            BossStore.of(
                bossId = randomBossId?.let { randomBossId } ?: bossId,
                name = "????????? ?????????",
                location = BossStoreLocation.of(latitude = latitude, longitude = longitude),
                imageUrl = "https://image.com",
                introduction = "??????",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                snsUrl = "https://sns.example.com",
                menus = listOf(
                    BossStoreMenu.of(
                        name = "????????? ????????? ?????????",
                        price = 5000,
                        imageUrl = "https://menu-one.png"
                    ),
                    BossStoreMenu.of(
                        name = "??? ????????? 2???",
                        price = 1000,
                        imageUrl = "https://menu-two.png"
                    ),
                    BossStoreMenu.of(
                        name = "????????? ????????? 2???",
                        price = 1000,
                        imageUrl = "https://menu-three.png"
                    ),
                ),
                appearanceDays = setOf(
                    BossStoreAppearanceDay.of(
                        dayOfTheWeek = DayOfTheWeek.MONDAY,
                        startTime = LocalTime.of(10, 0),
                        endTime = LocalTime.of(20, 0),
                        locationDescription = "??????????????? ????????? 0??? ??????"
                    ),
                    BossStoreAppearanceDay.of(
                        dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                        startTime = LocalTime.of(10, 0),
                        endTime = LocalTime.of(20, 0),
                        locationDescription = "??????????????? ????????? 0??? ??????"
                    )
                ),
                categoriesIds = categoriesIds
            )
        )
        return ApiResponse.success(bossStore.id)
    }

    @ApiOperation("[?????????] ?????? ??????????????? ???????????????")
    @PostMapping("/test-category")
    fun addMockStoreCategory(
        @RequestParam name: String,
        @RequestParam imageUrl: String,
        @RequestParam priority: Int,
    ): ApiResponse<String> {
        val category = BossStoreCategory.of(
            name = name,
            imageUrl = imageUrl,
            sequencePriority = priority
        )
        bossStoreCategoryRepository.save(category)
        return ApiResponse.success(category.id)
    }

    @ApiOperation("[?????? ?????????] ?????? ????????? ??????????????????.")
    @GetMapping("/test-error")
    fun testError() {
        throw InternalServerException("[???????????? ?????? ????????????] ?????? ????????? ?????????????????????")
    }

    @ApiOperation("[?????? ?????????] ????????? ???????????? ???????????????")
    @PostMapping("/test-feedback/{bossStoreId}")
    fun addTestFeedback(
        @PathVariable bossStoreId: String,
        @RequestParam feedbackTypes: Set<BossStoreFeedbackType>,
        @RequestParam date: LocalDate,
        @RequestParam userId: Long,
    ): ApiResponse<String> {
        bossStoreFeedbackService.addFeedback(bossStoreId, AddBossStoreFeedbackRequest(feedbackTypes), userId, date)
        return ApiResponse.OK
    }

    @ApiOperation("[?????? ?????????] ?????? ????????? ???????????????")
    @PutMapping("/test-registration/{registrationId}/apply")
    fun applyRegistrationForTest(
        @PathVariable registrationId: String,
    ): ApiResponse<String> {
        val registration = bossRegistrationRepository.findWaitingRegistrationById(registrationId)
            ?: throw NotFoundException("???????????? ?????? ??????($registrationId)??? ???????????? ????????????.")
        val bossAccount = registerNewBossAccount(registration)
        bossStoreRepository.save(registration.toBossStore(bossAccount.id))
        bossRegistrationRepository.save(registration)
        return ApiResponse.OK
    }

    @ApiOperation("?????? ????????? ???????????? ????????? ????????????")
    @GetMapping("/test-device")
    fun getBossDevice(): ApiResponse<UserMetaValue> {
        return ApiResponse.success(UserMetaSessionUtils.get())
    }

    private fun registerNewBossAccount(bossRegistration: BossRegistration): BossAccount {
        validateDuplicateRegistration(bossRegistration.boss.socialInfo)
        return bossAccountRepository.save(bossRegistration.toBossAccount())
    }

    private fun validateDuplicateRegistration(socialInfo: BossAccountSocialInfo) {
        if (bossAccountRepository.existsBossAccountBySocialInfo(
                socialId = socialInfo.socialId,
                socialType = socialInfo.socialType
            )
        ) {
            throw ConflictException("?????? ????????? ?????????(${socialInfo.socialId} - ${socialInfo.socialType})?????????")
        }
    }

}
