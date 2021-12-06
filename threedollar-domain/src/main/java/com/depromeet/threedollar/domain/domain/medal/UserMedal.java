package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import com.depromeet.threedollar.domain.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uni_user_medal_1", columnNames = {"medal_id", "user_id"})
    },
    indexes = {
        @Index(name = "idx_user_medal_1", columnList = "user_id"),
    }
)
public class UserMedal extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medal_id", nullable = false)
    private Medal medal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserMedalStatus status;

    UserMedal(Medal medal, User user, UserMedalStatus status) {
        this.medal = medal;
        this.user = user;
        this.status = status;
    }

    public static UserMedal of(Medal medal, User user) {
        return new UserMedal(medal, user, UserMedalStatus.IN_ACTIVE);
    }

    public void updateToActive() {
        this.status = UserMedalStatus.ACTIVE;
    }

    public void updateToInActive() {
        this.status = UserMedalStatus.IN_ACTIVE;
    }

    public boolean isActiveStatus() {
        return this.status == UserMedalStatus.ACTIVE;
    }

    public boolean hasSameMedalId(Long medalId) {
        return this.getMedalId().equals(medalId);
    }

    public Long getMedalId() {
        return this.medal.getId();
    }

}
