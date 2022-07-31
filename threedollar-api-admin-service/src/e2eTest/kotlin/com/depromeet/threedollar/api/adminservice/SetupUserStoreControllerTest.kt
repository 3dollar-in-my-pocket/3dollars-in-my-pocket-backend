package com.depromeet.threedollar.api.adminservice

import com.depromeet.threedollar.common.type.UserMenuCategoryType
import com.depromeet.threedollar.domain.rds.domain.userservice.store.MenuFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

internal abstract class SetupUserStoreControllerTest : SetupUserControllerTest() {

    @Autowired
    protected lateinit var storeRepository: StoreRepository

    protected lateinit var store: Store

    @BeforeEach
    fun setUpStore() {
        store = StoreFixture.create(userId = user.id, storeName = "가슴속 3천원 붕어빵")
        store.addMenus(listOf(MenuFixture.create(store = store, name = "팥 붕어빵", price = "3개에 천원", category = UserMenuCategoryType.BUNGEOPPANG)))
        storeRepository.save(store)
    }

}
