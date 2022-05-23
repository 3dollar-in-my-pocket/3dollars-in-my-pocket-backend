package com.depromeet.threedollar.domain.rds.vendor.domain.store;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Menu extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String price;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private UserMenuCategoryType category;

    @Builder(access = AccessLevel.PACKAGE)
    private Menu(@NotNull Store store, @Nullable String name, @Nullable String price, @NotNull UserMenuCategoryType category) {
        this.store = store;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public static Menu of(@NotNull Store store, @Nullable String name, @Nullable String price, @NotNull UserMenuCategoryType category) {
        return Menu.builder()
            .store(store)
            .name(name)
            .price(price)
            .category(category)
            .build();
    }

    boolean isCategory(@NotNull UserMenuCategoryType category) {
        return this.category.equals(category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(store.getId(), menu.store.getId())
            && Objects.equals(name, menu.name)
            && Objects.equals(price, menu.price) && category == menu.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(store.getId(), name, price, category);
    }

}
