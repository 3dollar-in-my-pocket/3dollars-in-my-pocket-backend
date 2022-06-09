package com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.core.constants.ColumnDefinition;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AdvertisementDetail {

    @Column(length = 50)
    private String title;

    @Column(length = 100)
    private String subTitle;

    @Column(nullable = false, length = 2048)
    private String imageUrl;

    @Column(length = 2048)
    private String linkUrl;

    @Column(length = 7, columnDefinition = ColumnDefinition.CHAR)
    private String bgColor;

    @Column(length = 7, columnDefinition = ColumnDefinition.CHAR)
    private String fontColor;

    @Builder(access = AccessLevel.PACKAGE)
    private AdvertisementDetail(
        @Nullable String title,
        @Nullable String subTitle,
        @NotNull String imageUrl,
        @Nullable String linkUrl,
        @Nullable String bgColor,
        @Nullable String fontColor
    ) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
    }

    public static AdvertisementDetail of(
        @Nullable String title,
        @Nullable String subTitle,
        @NotNull String imageUrl,
        @Nullable String linkUrl,
        @Nullable String bgColor,
        @Nullable String fontColor
    ) {
        return AdvertisementDetail.builder()
            .title(title)
            .subTitle(subTitle)
            .imageUrl(imageUrl)
            .linkUrl(linkUrl)
            .bgColor(bgColor)
            .fontColor(fontColor)
            .build();
    }

}
