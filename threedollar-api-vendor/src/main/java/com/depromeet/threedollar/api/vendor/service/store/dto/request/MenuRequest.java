package com.depromeet.threedollar.api.vendor.service.store.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.Menu;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.Store;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuRequest {

    @Size(max = 50, message = "{menu.name.size}")
    @NotNull(message = "{menu.name.notNull}")
    private String name;

    @Size(max = 100, message = "{menu.price.size}")
    @NotNull(message = "{menu.price.notNull}")
    private String price;

    @NotNull(message = "{menu.category.notNull}")
    private UserMenuCategoryType category;

    public static MenuRequest of(String name, String price, UserMenuCategoryType category) {
        return new MenuRequest(name, price, category);
    }

    public Menu toEntity(Store store) {
        return Menu.of(store, name, price, category);
    }

}
