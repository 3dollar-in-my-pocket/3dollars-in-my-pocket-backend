package com.depromeet.threedollar.domain.user.domain.store;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreCreator {

    public static Store create(Long userId, String storeName) {
        return create(userId, storeName, 34.0, 126.0);
    }

    public static Store create(Long userId, String storeName, double latitude, double longitude) {
        return create(userId, storeName, latitude, longitude, 0);
    }

    public static Store create(Long userId, String storeName, double latitude, double longitude, double rating) {
        return Store.builder()
            .userId(userId)
            .name(storeName)
            .type(StoreType.STORE)
            .latitude(latitude)
            .longitude(longitude)
            .rating(rating)
            .build();
    }

    public static Store createWithDefaultMenu(Long userId, String storeName) {
        return createWithDefaultMenu(userId, storeName, StoreType.STORE, 34.0, 126.0);
    }

    public static Store createWithDefaultMenu(Long userId, String storeName, double latitude, double longitude) {
        return createWithDefaultMenu(userId, storeName, latitude, longitude, 3);
    }

    public static Store createWithDefaultMenuAndPromotion(Long userId, String storeName, double latitude, double longitude, StorePromotion promotion) {
        return createWithDefaultMenuWithPromotion(userId, storeName, StoreType.CONVENIENCE_STORE, latitude, longitude, 3, promotion);
    }

    public static Store createWithDefaultMenu(Long userId, String storeName, double latitude, double longitude, double rating) {
        return createWithDefaultMenu(userId, storeName, StoreType.STORE, latitude, longitude, rating);
    }

    private static Store createWithDefaultMenu(Long userId, String storeName, StoreType storeType, double latitude, double longitude) {
        return createWithDefaultMenu(userId, storeName, storeType, latitude, longitude, 3.0);
    }

    private static Store createWithDefaultMenu(Long userId, String storeName, StoreType storeType, double latitude, double longitude, double rating) {
        Store store = Store.builder()
            .userId(userId)
            .name(storeName)
            .type(storeType)
            .latitude(latitude)
            .longitude(longitude)
            .rating(rating)
            .build();
        store.addMenus(List.of(MenuCreator.create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)));
        return store;
    }

    private static Store createWithDefaultMenuWithPromotion(Long userId, String storeName, StoreType storeType, double latitude, double longitude, double rating, StorePromotion promotion) {
        Store store = Store.builder()
            .userId(userId)
            .name(storeName)
            .type(storeType)
            .latitude(latitude)
            .longitude(longitude)
            .rating(rating)
            .promotion(promotion)
            .build();
        store.addMenus(List.of(MenuCreator.create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)));
        return store;
    }

    public static Store createDeleted(Long userId, String storeName) {
        return createDeleted(userId, storeName, 34.0, 126.0);
    }

    public static Store createDeleted(Long userId, String storeName, double latitude, double longitude) {
        Store store = create(userId, storeName, latitude, longitude);
        store.delete();
        return store;
    }

    public static Store createDeletedWithDefaultMenu(Long userId, String storeName) {
        return createDeletedWithDefaultMenu(userId, storeName, 34.0, 126.0);
    }

    public static Store createDeletedWithDefaultMenu(Long userId, String storeName, double latitude, double longitude) {
        Store store = createDeleted(userId, storeName, latitude, longitude);
        store.addMenus(List.of(MenuCreator.create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)));
        return store;
    }

}
