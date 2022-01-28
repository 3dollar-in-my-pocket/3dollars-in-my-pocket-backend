package com.depromeet.threedollar.domain.user.domain.popup;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PopupDetail {

    @Column(length = 50)
    private String title;

    @Column(length = 100)
    private String subTitle;

    @Column(nullable = false, length = 2048)
    private String imageUrl;

    @Column(length = 2048)
    private String linkUrl;

    @Column(length = 7)
    private String bgColor;

    @Column(length = 7)
    private String fontColor;

    @Builder(access = AccessLevel.PRIVATE)
    private PopupDetail(String title, String subTitle, String imageUrl, String linkUrl, String bgColor, String fontColor) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
    }

    public static PopupDetail of(@Nullable String title, @Nullable String subTitle, String imageUrl, @Nullable String linkUrl, @Nullable String bgColor, @Nullable String fontColor) {
        return PopupDetail.builder()
            .title(title)
            .subTitle(subTitle)
            .imageUrl(imageUrl)
            .linkUrl(linkUrl)
            .bgColor(bgColor)
            .fontColor(fontColor)
            .build();
    }

}
