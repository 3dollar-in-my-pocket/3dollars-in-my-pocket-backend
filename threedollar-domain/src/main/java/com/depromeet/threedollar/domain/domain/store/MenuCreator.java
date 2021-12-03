package com.depromeet.threedollar.domain.domain.store;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MenuCreator {

    public static Menu create(Store store, String name, String price, MenuCategoryType category) {
        return Menu.of(store, name, price, category);
    }

}
