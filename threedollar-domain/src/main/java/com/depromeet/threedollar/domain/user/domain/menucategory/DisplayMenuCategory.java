package com.depromeet.threedollar.domain.user.domain.menucategory;

import com.depromeet.threedollar.domain.common.domain.AuditingTimeEntity;
import com.depromeet.threedollar.domain.user.domain.store.MenuCategoryType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uni_display_menu_category_1", columnNames = {"categoryType"})
    }
)
public class DisplayMenuCategory extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private MenuCategoryType categoryType;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(nullable = false, length = 300)
    private String iconUrl;

    @Column(nullable = false)
    private boolean isNew;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private DisplayMenuCategoryStatusType status;

    @Column(nullable = false)
    private int displayOrder;

    @Builder(builderMethodName = "testBuilder")
    private DisplayMenuCategory(MenuCategoryType categoryType, String name, String description, String iconUrl, boolean isNew, DisplayMenuCategoryStatusType status, int displayOrder) {
        this.categoryType = categoryType;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
        this.isNew = isNew;
        this.status = status;
        this.displayOrder = displayOrder;
    }

    public boolean isVisible() {
        return this.status.isVisible();
    }

}
