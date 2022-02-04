package com.depromeet.threedollar.api.service.store.dto.request;

import com.depromeet.threedollar.domain.user.domain.store.Menu;
import com.depromeet.threedollar.domain.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.user.domain.store.Store;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuRequest {

    @Length(max = 50, message = "{menu.name.length}")
    @NotNull(message = "{menu.name.notNull}")
    private String name;

    @Length(max = 100, message = "{menu.price.length}")
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
