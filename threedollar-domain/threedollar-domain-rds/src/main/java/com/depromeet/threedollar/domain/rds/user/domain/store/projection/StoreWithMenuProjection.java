package com.depromeet.threedollar.domain.rds.user.domain.store.projection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.depromeet.threedollar.common.type.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class StoreWithMenuProjection {

    private static final String DELETE_STORE_NAME = "삭제된 가게입니다";

    private final Long id;
    private final Long userId;
    private final double latitude;
    private final double longitude;
    private final String name;
    private final double rating;
    private final StoreStatus status;
    private final List<MenuProjection> menus;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @QueryProjection
    public StoreWithMenuProjection(Long id, Long userId, double latitude, double longitude, String name, double rating, StoreStatus status, List<MenuProjection> menus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.rating = rating;
        this.status = status;
        this.menus = menus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return this.status.isDeleted();
    }

    public List<MenuCategoryType> getMenuCategories() {
        return this.menus.stream()
            .map(MenuProjection::getCategory)
            .collect(Collectors.toList());
    }

    public String getName() {
        if (isDeleted()) {
            return DELETE_STORE_NAME;
        }
        return this.name;
    }

    @ToString
    @Getter
    public static class MenuProjection {

        private final String name;
        private final String price;
        private final MenuCategoryType category;

        @QueryProjection
        public MenuProjection(String name, String price, MenuCategoryType category) {
            this.name = name;
            this.price = price;
            this.category = category;
        }

    }

}
