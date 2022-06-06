package com.depromeet.threedollar.domain.rds.domain.userservice.medal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.common.model.AuditingTimeEntity;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private UserMedalStatus status;

    @Builder(access = AccessLevel.PACKAGE)
    private UserMedal(@NotNull Medal medal, @NotNull User user, @NotNull UserMedalStatus status) {
        this.medal = medal;
        this.user = user;
        this.status = status;
    }

    public static UserMedal of(@NotNull Medal medal, @NotNull User user) {
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

    @NotNull
    public Long getMedalId() {
        return this.medal.getId();
    }

}
