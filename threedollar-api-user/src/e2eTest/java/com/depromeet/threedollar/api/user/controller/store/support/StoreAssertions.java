package com.depromeet.threedollar.api.user.controller.store.support;

import static com.depromeet.threedollar.api.user.controller.user.support.UserAssertions.assertUserInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import com.depromeet.threedollar.api.user.service.store.dto.response.MenuResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreDetailResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreWithVisitCountsResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.domain.rds.user.domain.TestHelper;
import com.depromeet.threedollar.domain.rds.user.domain.store.Menu;
import com.depromeet.threedollar.common.type.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.user.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@TestHelper
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StoreAssertions {

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

    public static void assertMenuResponse(MenuResponse response, Menu menu) {
        assertAll(
            () -> assertThat(response.getMenuId()).isEqualTo(menu.getId()),
            () -> assertThat(response.getCategory()).isEqualTo(menu.getCategory()),
            () -> assertThat(response.getName()).isEqualTo(menu.getName()),
            () -> assertThat(response.getPrice()).isEqualTo(menu.getPrice())
        );
    }

}
