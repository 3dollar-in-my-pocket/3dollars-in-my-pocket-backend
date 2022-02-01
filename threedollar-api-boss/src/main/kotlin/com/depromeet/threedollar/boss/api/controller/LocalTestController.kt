package com.depromeet.threedollar.boss.api.controller

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.config.resolver.Auth
import com.depromeet.threedollar.boss.api.config.resolver.BossId
import com.depromeet.threedollar.boss.api.config.session.SessionConstants
import com.depromeet.threedollar.boss.api.service.auth.dto.response.LoginResponse
import com.depromeet.threedollar.boss.api.service.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.document.boss.document.account.*
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategory
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.document.store.*
import com.depromeet.threedollar.document.common.document.BusinessNumber
import com.depromeet.threedollar.document.common.document.ContactsNumber
import com.depromeet.threedollar.document.common.document.TimeInterval
import io.swagger.annotations.ApiOperation
import org.springframework.data.geo.Point
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime
import javax.servlet.http.HttpSession

@RestController
class LocalTestController(
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreLocationRepository: BossStoreLocationRepository,
    private val httpSession: HttpSession
) {

    @ApiOperation("[개발용] 사장님 서버용 테스트 토큰을 발급 받습니다.")
    @GetMapping("/test-token")
    fun getTestBossAccountToken(): ApiResponse<LoginResponse> {
        val bossAccount =
            bossAccountRepository.findBossAccountBySocialInfo(BOSS.socialInfo.socialId, BOSS.socialInfo.socialType)
                ?: bossAccountRepository.save(BOSS)
        httpSession.setAttribute(SessionConstants.BOSS_ACCOUNT_ID, bossAccount.id)
        return ApiResponse.success(LoginResponse(httpSession.id, bossAccount.id))
    }

    @Auth
    @PostMapping("/test-store")
    fun addMockStoreData(
        @BossId bossId: String,
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam categoriesIds: Set<String>
    ): BossStore {
        BossStoreCategoryServiceUtils.validateExistsCategories(bossStoreCategoryRepository, categoriesIds)
        val bossStore = bossStoreRepository.save(
            BossStore(
                bossId = bossId,
                name = "행복한 붕어빵",
                imageUrl = "https://image.com",
                introduction = "소개",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                snsUrl = "https://sns.example.com",
                menus = listOf(
                    BossStoreMenu(
                        name = "팥붕 3개",
                        price = 1000,
                        imageUrl = "menu.img",
                        tag = "붕어빵"
                    ),
                    BossStoreMenu(
                        name = "슈붕 3개",
                        price = 1000,
                        imageUrl = "menu.img",
                        tag = "붕어빵"
                    ),
                ),
                appearanceDays = setOf(
                    BossStoreAppearanceDay(
                        day = DayOfTheWeek.MONDAY,
                        openTime = TimeInterval(
                            startTime = LocalTime.of(10, 0),
                            endTime = LocalTime.of(20, 0)
                        ),
                        locationDescription = "강남역 주변"
                    ),
                    BossStoreAppearanceDay(
                        day = DayOfTheWeek.WEDNESDAY,
                        openTime = TimeInterval(
                            startTime = LocalTime.of(10, 0),
                            endTime = LocalTime.of(20, 0)
                        ),
                        locationDescription = "강남역 주변"
                    )
                ),
                categoriesIds = categoriesIds,
                status = BossStoreStatus.ACTIVE
            )
        )

        bossStoreLocationRepository.save(BossStoreLocation(
            bossStoreId = bossStore.id,
            location = Point(longitude, latitude),
        ))
        return bossStore
    }

    @PostMapping("/test-category")
    fun addMockStoreCategory(
        @RequestParam title: String,
        @RequestParam priority: Int
    ): ApiResponse<String> {
        val category = BossStoreCategory(
            title = title,
            sequencePriority = priority
        )
        bossStoreCategoryRepository.save(category)
        return ApiResponse.SUCCESS
    }

    companion object {
        private val BOSS = BossAccount(
            name = "테스트 계정",
            socialInfo = BossAccountSocialInfo("test-social-id", BossAccountSocialType.KAKAO),
            businessNumber = BusinessNumber.of("01-123-1234")
        )
    }

}
