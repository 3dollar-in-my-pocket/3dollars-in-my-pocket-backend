package com.depromeet.threedollar.domain.domain.user;

import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import com.depromeet.threedollar.domain.domain.medal.Medal;
import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.depromeet.threedollar.common.exception.ErrorCode.NOT_FOUND_MEDAL_EXCEPTION;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<UserMedal> userMedals = new ArrayList<>();

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

    public void addMedals(List<Medal> medals) {
        for (Medal medal : medals) {
            addMedal(medal);
        }
    }

    private void addMedal(Medal medal) {
        this.userMedals.add(UserMedal.of(medal, this));
    }

    public void updateActiveMedal(@Nullable Long medalId) {
        inActiveCurrentMedal();
        if (medalId == null) {
            return;
        }
        activeNewMedal(medalId);
    }

    private void inActiveCurrentMedal() {
        this.userMedals.stream()
            .filter(UserMedal::isActive)
            .forEach(UserMedal::inActive);
    }

    private void activeNewMedal(@NotNull Long medalId) {
        UserMedal findUserMedal = this.userMedals.stream()
            .filter(userMedal -> userMedal.getMedal().getId().equals(medalId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("해당 유저(%s)는 해당하는 유저 메달(%s)을 보유하고 있지 않습니다", this.id, medalId), NOT_FOUND_MEDAL_EXCEPTION));
        findUserMedal.active();
    }

    public String getSocialId() {
        return this.socialInfo.getSocialId();
    }

    public UserSocialType getSocialType() {
        return this.socialInfo.getSocialType();
    }

    public UserMedal getActiveMedal() {
        return userMedals.stream()
            .filter(UserMedal::isActive)
            .findFirst()
            .orElse(null);
    }

}
