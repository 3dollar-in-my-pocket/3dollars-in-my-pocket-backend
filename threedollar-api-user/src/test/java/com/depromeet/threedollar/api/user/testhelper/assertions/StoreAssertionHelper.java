package com.depromeet.threedollar.api.user.testhelper.assertions;

import com.depromeet.threedollar.api.user.service.store.dto.response.*;
import com.depromeet.threedollar.domain.rds.common.domain.Location;
import com.depromeet.threedollar.domain.rds.user.domain.TestHelper;
import com.depromeet.threedollar.domain.rds.user.domain.store.*;
import com.depromeet.threedollar.domain.rds.user.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.depromeet.threedollar.api.user.testhelper.assertions.UserAssertionHelper.assertUserInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestHelper
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StoreAssertionHelper {

    /**
     * Store
     */
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

    public static void assertStoreInfoResponse(StoreInfoResponse response, Store store) {
        assertStoreInfoResponse(response, store.getLatitude(), store.getLongitude(), store.getName(), store.getMenuCategoriesSortedByCounts());
        assertAll(
            () -> assertThat(response.getStoreId()).isEqualTo(store.getId()),
            () -> assertThat(response.getRating()).isEqualTo(store.getRating()),
            () -> assertThat(response.getIsDeleted()).isEqualTo(store.isDeleted())
        );
    }

    public static void assertStoreInfoResponse(StoreInfoResponse response, double latitude, double longitude, String storeName, List<MenuCategoryType> categories) {
        assertAll(
            () -> assertThat(response.getLatitude()).isEqualTo(latitude),
            () -> assertThat(response.getLongitude()).isEqualTo(longitude),
            () -> assertThat(response.getStoreName()).isEqualTo(storeName),
            () -> assertThat(response.getCategories()).isEqualTo(categories)
        );
    }

    public static void assertStoreWithVisitsResponse(StoreWithVisitCountsResponse response, Store store) {
        assertAll(
            () -> assertThat(response.getStoreId()).isEqualTo(store.getId()),
            () -> assertThat(response.getStoreName()).isEqualTo(store.getName()),
            () -> assertThat(response.getLatitude()).isEqualTo(store.getLatitude()),
            () -> assertThat(response.getLongitude()).isEqualTo(store.getLongitude()),
            () -> assertThat(response.getRating()).isEqualTo(store.getRating()),
            () -> assertThat(response.getCategories()).isEqualTo(store.getMenuCategoriesSortedByCounts()),
            () -> assertThat(response.getIsDeleted()).isEqualTo(store.isDeleted())
        );
    }

    public static void assertStoreWithVisitsResponse(StoreWithVisitCountsResponse response, Long storeId, double latitude, double longitude, String name, double rating) {
        assertAll(
            () -> assertThat(response.getStoreId()).isEqualTo(storeId),
            () -> assertThat(response.getLatitude()).isEqualTo(latitude),
            () -> assertThat(response.getLongitude()).isEqualTo(longitude),
            () -> assertThat(response.getStoreName()).isEqualTo(name),
            () -> assertThat(response.getRating()).isEqualTo(rating)
        );
    }

    public static void assertStoreWithVisitsAndDistanceResponse(StoreWithVisitsAndDistanceResponse response, Long storeId, double latitude, double longitude, String name, double rating) {
        assertAll(
            () -> assertThat(response.getStoreId()).isEqualTo(storeId),
            () -> assertThat(response.getLatitude()).isEqualTo(latitude),
            () -> assertThat(response.getLongitude()).isEqualTo(longitude),
            () -> assertThat(response.getStoreName()).isEqualTo(name),
            () -> assertThat(response.getRating()).isEqualTo(rating)
        );
    }

    public static void assertStoreDetailInfoResponse(StoreDetailResponse response, Store store, User user) {
        assertAll(
            () -> assertThat(response.getStoreId()).isEqualTo(store.getId()),
            () -> assertThat(response.getLatitude()).isEqualTo(store.getLatitude()),
            () -> assertThat(response.getLongitude()).isEqualTo(store.getLongitude()),
            () -> assertThat(response.getStoreName()).isEqualTo(store.getName()),
            () -> assertThat(response.getStoreType()).isEqualTo(store.getType()),
            () -> assertThat(response.getRating()).isEqualTo(store.getRating()),
            () -> assertUserInfoResponse(response.getUser(), user)
        );
    }

    /**
     * Menu
     */
    public static void assertMenu(Menu menu, String menuName, String price, MenuCategoryType type) {
        assertAll(
            () -> assertThat(menu.getName()).isEqualTo(menuName),
            () -> assertThat(menu.getPrice()).isEqualTo(price),
            () -> assertThat(menu.getCategory()).isEqualTo(type)
        );
    }

    public static void assertMenu(Menu menu, Long storeId, String menuName, String price, MenuCategoryType type) {
        assertAll(
            () -> assertThat(menu.getStore().getId()).isEqualTo(storeId),
            () -> assertThat(menu.getName()).isEqualTo(menuName),
            () -> assertThat(menu.getPrice()).isEqualTo(price),
            () -> assertThat(menu.getCategory()).isEqualTo(type)
        );
    }

    public static void assertMenuResponse(MenuResponse response, Menu menu) {
        assertAll(
            () -> assertThat(response.getMenuId()).isEqualTo(menu.getId()),
            () -> assertThat(response.getCategory()).isEqualTo(menu.getCategory()),
            () -> assertThat(response.getName()).isEqualTo(menu.getName()),
            () -> assertThat(response.getPrice()).isEqualTo(menu.getPrice())
        );
    }

    /**
     * StoreDeleteRequest
     */
    public static void assertStoreDeleteRequest(StoreDeleteRequest storeDeleteRequest, Long storeId, Long userId, DeleteReasonType type) {
        assertAll(
            () -> assertThat(storeDeleteRequest.getStore().getId()).isEqualTo(storeId),
            () -> assertThat(storeDeleteRequest.getUserId()).isEqualTo(userId),
            () -> assertThat(storeDeleteRequest.getReason()).isEqualTo(type)
        );
    }

}
