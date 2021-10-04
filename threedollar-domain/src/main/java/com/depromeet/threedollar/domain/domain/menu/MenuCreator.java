package com.depromeet.threedollar.domain.domain.menu;

import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MenuCreator {

    public static Menu create(Store store, String name, String price, MenuCategoryType category) {
        return Menu.of(store, name, price, category);
    }

}
