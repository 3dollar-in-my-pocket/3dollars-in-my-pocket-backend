package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uni_user_medal_1", columnNames = {"userId", "medalType"})
    }
)
public class UserMedal extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private UserMedalType medalType;

    private UserMedal(Long userId, UserMedalType medalType) {
        this.userId = userId;
        this.medalType = medalType;
    }

    public static UserMedal of(Long userId, UserMedalType medalType) {
        return new UserMedal(userId, medalType);
    }

}
