package com.depromeet.threedollar.api.user.service.store.dto.request;

import com.depromeet.threedollar.domain.rds.user.domain.store.Menu;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    private MenuCategoryType category;

    public static MenuRequest of(String name, String price, MenuCategoryType category) {
        return new MenuRequest(name, price, category);
    }

    public Menu toEntity(Store store) {
        return Menu.of(store, name, price, category);
    }

}
