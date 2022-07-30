package com.depromeet.threedollar.api.adminservice

import com.depromeet.threedollar.common.type.UserMenuCategoryType
import com.depromeet.threedollar.domain.rds.domain.userservice.store.MenuFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

internal abstract class SetupUserStoreIntegrationTest : SetupUserIntegrationTest() {

    @Autowired
    protected lateinit var storeRepository: StoreRepository

    protected var storeId: Long = -1L

    protected lateinit var store: Store

    @BeforeEach
    fun setUpStore() {
        store = StoreFixture.create(userId, "디프만 붕어빵")
        store.addMenus(listOf(MenuFixture.create(store, "메뉴", "가격", UserMenuCategoryType.BUNGEOPPANG)))
        storeRepository.save(store)
        storeId = store.id
    }

}
