package com.depromeet.threedollar.domain.domain.store;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreCreator {

    public static Store create(Long userId, String storeName) {
        return Store.builder()
            .userId(userId)
            .name(storeName)
            .type(StoreType.STORE)
            .latitude(33.0)
            .longitude(124.0)
            .build();
    }

    public static Store createWithDefaultMenu(Long userId, String storeName) {
        Store store = create(userId, storeName);
        store.addMenus(List.of(MenuCreator.create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)));
        return store;
    }

    public static Store create(Long userId, String storeName, double latitude, double longitude) {
        return Store.builder()
            .userId(userId)
            .name(storeName)
            .type(StoreType.STORE)
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }

    public static Store createWithDefaultMenu(Long userId, String storeName, double latitude, double longitude) {
        Store store = create(userId, storeName, latitude, longitude);
        store.addMenus(List.of(MenuCreator.create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)));
        return store;
    }

    public static Store create(Long userId, String storeName, StoreType storeType, double latitude, double longitude) {
        return Store.builder()
            .userId(userId)
            .name(storeName)
            .type(storeType)
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }

    public static Store createWithDefaultMenu(Long userId, String storeName, StoreType storeType, double latitude, double longitude) {
        Store store = create(userId, storeName, storeType, latitude, longitude);
        store.addMenus(List.of(MenuCreator.create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)));
        return store;
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

    public static Store createWithDefaultMenu(Long userId, String storeName, double latitude, double longitude, double rating) {
        Store store = create(userId, storeName, latitude, longitude, rating);
        store.addMenus(List.of(MenuCreator.create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)));
        return store;
    }

    public static Store createDeleted(Long userId, String storeName) {
        Store store = create(userId, storeName);
        store.delete();
        return store;
    }

    public static Store createDeletedWithDefaultMenu(Long userId, String storeName) {
        Store store = create(userId, storeName);
        store.addMenus(List.of(MenuCreator.create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)));
        store.delete();
        return store;
    }

    public static Store createDeleted(Long userId, String storeName, double latitude, double longitude) {
        Store store = create(userId, storeName, latitude, longitude);
        store.delete();
        return store;
    }

    public static Store createDeletedWithDefaultMenu(Long userId, String storeName, double latitude, double longitude) {
        Store store = create(userId, storeName, latitude, longitude);
        store.addMenus(List.of(MenuCreator.create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)));
        store.delete();
        return store;
    }

}
