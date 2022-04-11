package com.depromeet.threedollar.domain.rds.user.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.jetbrains.annotations.NotNull;

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

    private Long userId;

    private String name;

    @Embedded
    private SocialInfo socialInfo;

    private LocalDateTime userCreatedAt;

    @Builder(access = AccessLevel.PACKAGE)
    private WithdrawalUser(Long userId, String name, SocialInfo socialInfo, LocalDateTime userCreatedAt) {
        this.userId = userId;
        this.name = name;
        this.socialInfo = socialInfo;
        this.userCreatedAt = userCreatedAt;
    }

    public static WithdrawalUser newInstance(@NotNull User signOutUser) {
        return WithdrawalUser.builder()
            .userId(signOutUser.getId())
            .name(signOutUser.getName())
            .socialInfo(SocialInfo.of(signOutUser.getSocialId(), signOutUser.getSocialType()))
            .userCreatedAt(signOutUser.getCreatedAt())
            .build();
    }

}
