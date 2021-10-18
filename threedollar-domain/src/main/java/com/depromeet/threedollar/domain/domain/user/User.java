package com.depromeet.threedollar.domain.domain.user;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uni_user_1", columnNames = {"socialId", "socialType"})
    },
    indexes = {
        @Index(name = "idx_user_1", columnList = "name")
    }
)
public class User extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private SocialInfo socialInfo;

    @Column(length = 50, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private UserMedalType medalType;

    private User(String socialId, UserSocialType socialType, String name) {
        this.socialInfo = SocialInfo.of(socialId, socialType);
        this.name = name;
    }

    public static User newInstance(String socialId, UserSocialType socialType, String name) {
        return new User(socialId, socialType, name);
    }

	public void updateName(String name) {
        this.name = name;
    }

    public void updateMedal(UserMedalType medalType) {
        this.medalType = medalType;
    }

    public String getSocialId() {
        return this.socialInfo.getSocialId();
    }

    public UserSocialType getSocialType() {
        return this.socialInfo.getSocialType();
    }

}
