package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.user.domain.store.Menu;
import com.depromeet.threedollar.domain.user.domain.store.MenuCategoryType;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuResponse extends AuditingTimeResponse {

    private Long menuId;
    private MenuCategoryType category;
    private String name;
    private String price;

    @Builder(access = AccessLevel.PRIVATE)
    private MenuResponse(Long menuId, MenuCategoryType category, String name, String price) {
        this.menuId = menuId;
        this.category = category;
        this.name = name;
        this.price = price;
    }

    public static MenuResponse of(@NotNull Menu menu) {
        MenuResponse response = MenuResponse.builder()
            .menuId(menu.getId())
            .category(menu.getCategory())
            .name(menu.getName())
            .price(menu.getPrice())
            .build();
        response.setBaseTime(menu);
        return response;
    }

}
