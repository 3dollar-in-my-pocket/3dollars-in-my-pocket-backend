package com.depromeet.threedollar.api.boss.controller

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
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationBossForm
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationStoreForm
import com.depromeet.threedollar.domain.mongo.boss.domain.store.*
import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber
import com.depromeet.threedollar.domain.mongo.common.domain.TimeInterval
import io.swagger.annotations.ApiOperation
import org.springframework.context.annotation.Profile
import org.springframework.data.geo.Point
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.servlet.http.HttpSession

private val BOSS = BossAccount.of(
    bossId = "test" + UUID.randomUUID().toString(),
    name = "테스트 계정",
    socialId = "test-social-id",
    socialType = BossAccountSocialType.KAKAO,
    businessNumber = BusinessNumber.of("000-12-12345"),
    isSetupNotification = false
)

@Profile("local", "local-docker", "integration-test", "dev")
@RestController
class LocalTestController(
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreLocationRepository: BossStoreLocationRepository,
    private val registrationRepository: RegistrationRepository,
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
        val registration = Registration(
            boss = RegistrationBossForm(
                socialInfo = BossAccountSocialInfo(
                    socialId = UUID.randomUUID().toString(),
                    socialType = BossAccountSocialType.KAKAO
                ),
                name = bossName,
                businessNumber = BusinessNumber.of(businessNumber),
            ),
            store = RegistrationStoreForm(
                name = "가게 이름",
                contactsNumber = ContactsNumber.of(contactsNumber),
                categoriesIds = categoriesIds,
                certificationPhotoUrl = certificationPhotoUrl
            )
        )
        registrationRepository.save(registration)
        return ApiResponse.OK
    }

    @ApiOperation("[개발 서버용] 가게 목 데이터를 추가합니다.")
    @Auth
    @PostMapping("/test-store")
    fun addMockStoreData(
        @BossId bossId: String,
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam categoriesIds: Set<String>
    ): ApiResponse<String> {
        BossStoreCategoryServiceUtils.validateExistsCategories(bossStoreCategoryRepository, categoriesIds)
        val bossStore = bossStoreRepository.save(
            BossStore(
                bossId = "test${UUID.randomUUID()}",
                name = "행복한 붕어빵",
                imageUrl = "https://image.com",
                introduction = "소개",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                snsUrl = "https://sns.example.com",
                menus = listOf(
                    BossStoreMenu(
                        name = "아저씨 못난이 핫도그",
                        price = 5000,
                        imageUrl = "https://menu-one.png",
                        groupName = "아저씨 핫도그"
                    ),
                    BossStoreMenu(
                        name = "팥 붕어빵 2개",
                        price = 1000,
                        imageUrl = "https://menu-two.png",
                        groupName = "붕어빵"
                    ),
                    BossStoreMenu(
                        name = "슈크림 붕어빵 2개",
                        price = 1000,
                        imageUrl = "https://menu-three.png",
                        groupName = "붕어빵"
                    ),
                ),
                appearanceDays = setOf(
                    BossStoreAppearanceDay(
                        dayOfTheWeek = DayOfTheWeek.MONDAY,
                        openingHours = TimeInterval(
                            startTime = LocalTime.of(10, 0),
                            endTime = LocalTime.of(20, 0)
                        ),
                        locationDescription = "서울특별시 강남역 0번 출구"
                    ),
                    BossStoreAppearanceDay(
                        dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                        openingHours = TimeInterval(
                            startTime = LocalTime.of(10, 0),
                            endTime = LocalTime.of(20, 0)
                        ),
                        locationDescription = "서울특별시 강남역 0번 출구"
                    )
                ),
                categoriesIds = categoriesIds
            )
        )

        bossStoreLocationRepository.save(
            BossStoreLocation(
                bossStoreId = bossStore.id,
                location = Point(longitude, latitude),
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
        val category = BossStoreCategory(
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
        val registration = registrationRepository.findWaitingRegistrationById(registrationId)
            ?: throw NotFoundException("존재하지 않는 Registration")
        val bossAccount = registerNewBossAccount(registration)
        bossStoreRepository.save(registration.toBossStore(bossAccount.id))
        registration.approve()
        registrationRepository.save(registration)
        return ApiResponse.OK
    }

    private fun registerNewBossAccount(registration: Registration): BossAccount {
        validateDuplicateRegistration(registration.boss.socialInfo)
        return bossAccountRepository.save(registration.toBossAccount())
    }

    private fun validateDuplicateRegistration(socialInfo: BossAccountSocialInfo) {
        if (bossAccountRepository.existsBossAccountBySocialInfo(socialId = socialInfo.socialId, socialType = socialInfo.socialType)) {
            throw ConflictException("이미 가입한 사장님(${socialInfo.socialId} - ${socialInfo.socialType})입니다")
        }
    }

}
