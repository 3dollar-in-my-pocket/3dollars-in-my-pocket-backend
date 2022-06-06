package com.depromeet.threedollar.api.user.service.store.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.common.model.Location;
import com.depromeet.threedollar.domain.rds.domain.TestHelper;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.DeleteReasonType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Menu;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreDeleteRequest;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@TestHelper
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreAssertions {

    public static void assertStore(Store store, double latitude, double longitude, String storeName, StoreType storeType, Long userId) {
        assertAll(
            () -> assertThat(store.getLocation()).isEqualTo(Location.of(latitude, longitude)),
            () -> assertThat(store.getLatitude()).isEqualTo(latitude),
            () -> assertThat(store.getLongitude()).isEqualTo(longitude),
            () -> assertThat(store.getName()).isEqualTo(storeName),
            () -> assertThat(store.getType()).isEqualTo(storeType),
            () -> assertThat(store.getUserId()).isEqualTo(userId)
        );
    }

    public static void assertMenu(Menu menu, String menuName, String price, UserMenuCategoryType type) {
        assertAll(
            () -> assertThat(menu.getName()).isEqualTo(menuName),
            () -> assertThat(menu.getPrice()).isEqualTo(price),
            () -> assertThat(menu.getCategory()).isEqualTo(type)
        );
    }

    public static void assertMenu(Menu menu, Long storeId, String menuName, String price, UserMenuCategoryType type) {
        assertAll(
            () -> assertThat(menu.getStore().getId()).isEqualTo(storeId),
            () -> assertThat(menu.getName()).isEqualTo(menuName),
            () -> assertThat(menu.getPrice()).isEqualTo(price),
            () -> assertThat(menu.getCategory()).isEqualTo(type)
        );
    }

    public static void assertStoreDeleteRequest(StoreDeleteRequest storeDeleteRequest, Long storeId, Long userId, DeleteReasonType type) {
        assertAll(
            () -> assertThat(storeDeleteRequest.getStore().getId()).isEqualTo(storeId),
            () -> assertThat(storeDeleteRequest.getUserId()).isEqualTo(userId),
            () -> assertThat(storeDeleteRequest.getReason()).isEqualTo(type)
        );
    }

}
