package com.depromeet.threedollar.api.user.service.store.dto.response;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.rds.user.domain.store.Menu;
import com.depromeet.threedollar.common.type.MenuCategoryType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
        response.setAuditingTimeByEntity(menu);
        return response;
    }

}
