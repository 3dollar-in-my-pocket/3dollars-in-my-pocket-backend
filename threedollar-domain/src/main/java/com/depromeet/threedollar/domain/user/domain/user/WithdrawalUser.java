package com.depromeet.threedollar.domain.user.domain.user;

import com.depromeet.threedollar.domain.common.domain.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

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
