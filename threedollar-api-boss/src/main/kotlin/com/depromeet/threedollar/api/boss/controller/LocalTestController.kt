package com.depromeet.threedollar.api.boss.controller

import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.servlet.http.HttpSession
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.boss.config.interceptor.Auth
import com.depromeet.threedollar.api.boss.config.resolver.BossId
import com.depromeet.threedollar.api.boss.config.session.SessionConstants
import com.depromeet.threedollar.api.boss.service.auth.dto.response.LoginResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.boss.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.api.core.service.boss.feedback.BossStoreFeedbackService
import com.depromeet.threedollar.api.core.service.boss.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistration
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationBossForm
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationStoreForm
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreAppearanceDay
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreMenu
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.common.model.BusinessNumber
import com.depromeet.threedollar.common.model.ContactsNumber
import io.swagger.annotations.ApiOperation

private val BOSS = BossAccount.of(
    bossId = "test" + UUID.randomUUID().toString(),
    name = "테스트 계정",
    socialId = "test-social-id",
    socialType = BossAccountSocialType.KAKAO,
    businessNumber = BusinessNumber.of("000-00-00000"),
    isSetupNotification = false
)

@Profile("local", "local-docker", "integration-test", "dev")
@RestController
class LocalTestController(
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val bossStoreFeedbackService: BossStoreFeedbackService,
    private val httpSession: HttpSession
) {

    @ApiOperation("[개발용] 사장님 서버용 테스트 토큰을 발급 받습니다.")
    @GetMapping("/test-token")
    fun getTestBossAccountToken(): ApiResponse<LoginResponse> {
        val bossAccount = bossAccountRepository.findBossAccountBySocialInfo(BOSS.socialInfo.socialId, BOSS.socialInfo.socialType)
            ?: bossAccountRepository.save(BOSS)
        httpSession.setAttribute(SessionConstants.BOSS_ACCOUNT_ID, bossAccount.id)
        return ApiResponse.success(LoginResponse(httpSession.id, bossAccount.id))
    }

    @ApiOperation("[개발용] 사장님 서버용 테스트 토큰을 발급 받습니다.")
    @PostMapping("/test-registration")
    fun addMockRegistration(
        @RequestParam bossName: String,
        @RequestParam storeName: String,
        @RequestParam categoriesIds: Set<String>,
        @RequestParam certificationPhotoUrl: String,
        @RequestParam contactsNumber: String,
        @RequestParam businessNumber: String
    ): ApiResponse<String> {
        val bossRegistration = BossRegistration.of(
            boss = RegistrationBossForm.of(
                socialId = UUID.randomUUID().toString(),
                socialType = BossAccountSocialType.KAKAO,
                name = bossName,
                businessNumber = businessNumber,
            ),
            store = RegistrationStoreForm.of(
                name = "가게 이름",
                contactsNumber = contactsNumber,
                categoriesIds = categoriesIds,
                certificationPhotoUrl = certificationPhotoUrl
            )
        )
        bossRegistrationRepository.save(bossRegistration)
        return ApiResponse.OK
    }

    @ApiOperation("[개발 서버용] 가게 목 데이터를 추가합니다.")
    @Auth
    @PostMapping("/test-store")
    fun addMockStoreData(
        @BossId bossId: String,
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam categoriesIds: Set<String>,
        @RequestParam randomBossId: String?
    ): ApiResponse<String> {
        BossStoreCategoryServiceUtils.validateExistsCategories(bossStoreCategoryRepository, categoriesIds)
        val bossStore = bossStoreRepository.save(
            BossStore.of(
                bossId = randomBossId?.let { randomBossId } ?: bossId,
                name = "행복한 붕어빵",
                location = BossStoreLocation.of(latitude = latitude, longitude = longitude),
                imageUrl = "https://image.com",
                introduction = "소개",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                snsUrl = "https://sns.example.com",
                menus = listOf(
                    BossStoreMenu.of(
                        name = "아저씨 못난이 핫도그",
                        price = 5000,
                        imageUrl = "https://menu-one.png"
                    ),
                    BossStoreMenu.of(
                        name = "팥 붕어빵 2개",
                        price = 1000,
                        imageUrl = "https://menu-two.png"
                    ),
                    BossStoreMenu.of(
                        name = "슈크림 붕어빵 2개",
                        price = 1000,
                        imageUrl = "https://menu-three.png"
                    ),
                ),
                appearanceDays = setOf(
                    BossStoreAppearanceDay.of(
                        dayOfTheWeek = DayOfTheWeek.MONDAY,
                        startTime = LocalTime.of(10, 0),
                        endTime = LocalTime.of(20, 0),
                        locationDescription = "서울특별시 강남역 0번 출구"
                    ),
                    BossStoreAppearanceDay.of(
                        dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                        startTime = LocalTime.of(10, 0),
                        endTime = LocalTime.of(20, 0),
                        locationDescription = "서울특별시 강남역 0번 출구"
                    )
                ),
                categoriesIds = categoriesIds
            )
        )
        return ApiResponse.success(bossStore.id)
    }

    @ApiOperation("[개발용] 가게 카테고리를 추가합니다")
    @PostMapping("/test-category")
    fun addMockStoreCategory(
        @RequestParam name: String,
        @RequestParam priority: Int
    ): ApiResponse<String> {
        val category = BossStoreCategory.of(
            name = name,
            sequencePriority = priority
        )
        bossStoreCategoryRepository.save(category)
        return ApiResponse.success(category.id)
    }

    @ApiOperation("[개발 서버용] 서버 에러를 발생시킵니다.")
    @GetMapping("/test-error")
    fun testError() {
        throw InternalServerException("[개발환경 에러 테스트용] 서버 에러가 발생하였습니다")
    }

    @ApiOperation("[개발 서버용] 가게에 피드백을 추가합니다")
    @PostMapping("/test-feedback/{bossStoreId}")
    fun addTestFeedback(
        @PathVariable bossStoreId: String,
        @RequestParam feedbackTypes: Set<BossStoreFeedbackType>,
        @RequestParam date: LocalDate,
        @RequestParam userId: Long
    ): ApiResponse<String> {
        bossStoreFeedbackService.addFeedback(bossStoreId, AddBossStoreFeedbackRequest(feedbackTypes), userId, date)
        return ApiResponse.OK
    }

    @ApiOperation("[개발 서버용] 가입 신청을 승인합니다")
    @PutMapping("/test-registration/{registrationId}/apply")
    fun applyRegistrationForTest(
        @PathVariable registrationId: String
    ): ApiResponse<String> {
        val registration = bossRegistrationRepository.findWaitingRegistrationById(registrationId)
            ?: throw NotFoundException("해당하는 가입 신청($registrationId)은 존재하지 않습니다.")
        val bossAccount = registerNewBossAccount(registration)
        bossStoreRepository.save(registration.toBossStore(bossAccount.id))
        registration.approve()
        bossRegistrationRepository.save(registration)
        return ApiResponse.OK
    }

    private fun registerNewBossAccount(bossRegistration: BossRegistration): BossAccount {
        validateDuplicateRegistration(bossRegistration.boss.socialInfo)
        return bossAccountRepository.save(bossRegistration.toBossAccount())
    }

    private fun validateDuplicateRegistration(socialInfo: BossAccountSocialInfo) {
        if (bossAccountRepository.existsBossAccountBySocialInfo(socialId = socialInfo.socialId, socialType = socialInfo.socialType)) {
            throw ConflictException("이미 가입한 사장님(${socialInfo.socialId} - ${socialInfo.socialType})입니다")
        }
    }

}
