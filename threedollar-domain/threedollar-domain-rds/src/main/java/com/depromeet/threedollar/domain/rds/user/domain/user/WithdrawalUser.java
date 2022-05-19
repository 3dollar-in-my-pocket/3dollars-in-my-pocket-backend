package com.depromeet.threedollar.domain.rds.user.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class WithdrawalUser extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String name;

    @Embedded
    private UserSocialInfo socialInfo;

    private LocalDateTime userCreatedAt;

    @Builder(access = AccessLevel.PACKAGE)
    private WithdrawalUser(@NotNull Long userId, @NotNull String name, @NotNull UserSocialInfo socialInfo, @Nullable LocalDateTime userCreatedAt) {
        this.userId = userId;
        this.name = name;
        this.socialInfo = socialInfo;
        this.userCreatedAt = userCreatedAt;
    }

    public static WithdrawalUser newInstance(@NotNull User signOutUser) {
        return WithdrawalUser.builder()
            .userId(signOutUser.getId())
            .name(signOutUser.getName())
            .socialInfo(UserSocialInfo.of(signOutUser.getSocialId(), signOutUser.getSocialType()))
            .userCreatedAt(signOutUser.getCreatedAt())
            .build();
    }

}
