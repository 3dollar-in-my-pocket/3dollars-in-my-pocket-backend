package com.depromeet.threedollar.api.userservice.service.store.dto.response;

import com.depromeet.threedollar.api.core.service.common.dto.response.AuditingTimeResponse;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Menu;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuResponse extends AuditingTimeResponse {

    private Long menuId;
    private UserMenuCategoryType category;
    private String name;
    private String price;

    @Builder(access = AccessLevel.PRIVATE)
    private MenuResponse(Long menuId, UserMenuCategoryType category, String name, String price) {
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
