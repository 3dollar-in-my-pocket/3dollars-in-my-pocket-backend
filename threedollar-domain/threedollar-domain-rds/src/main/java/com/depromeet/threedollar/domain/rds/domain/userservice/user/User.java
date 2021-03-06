package com.depromeet.threedollar.domain.rds.domain.userservice.user;

import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.core.model.AuditingTimeEntity;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.E404_NOT_EXISTS_USER_MEDAL;

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
    private UserSocialInfo socialInfo;

    @Column(length = 50, nullable = false)
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<UserMedal> userMedals = new ArrayList<>();

    @Builder(access = AccessLevel.PACKAGE)
    private User(@NotNull String socialId, @NotNull UserSocialType socialType, @NotNull String name) {
        this.socialInfo = UserSocialInfo.of(socialId, socialType);
        this.name = name;
    }

    public static User newInstance(@NotNull String socialId, @NotNull UserSocialType socialType, @NotNull String name) {
        return User.builder()
            .socialId(socialId)
            .socialType(socialType)
            .name(name)
            .build();
    }

    public void updateName(@NotNull String name) {
        this.name = name;
    }

    public void addMedals(List<Medal> medals) {
        for (Medal medal : medals) {
            addMedal(medal);
        }
    }

    private void addMedal(@NotNull Medal medal) {
        this.userMedals.add(UserMedal.of(medal, this));
    }

    public void updateActivatedMedal(@NotNull Long medalId) {
        inactivatedAllUserMedals();
        UserMedal userMedal = findUserMedal(medalId);
        userMedal.updateToActive();
    }

    private void inactivatedAllUserMedals() {
        for (UserMedal userMedal : this.userMedals) {
            userMedal.updateToInActive();
        }
    }

    @NotNull
    private UserMedal findUserMedal(@NotNull Long medalId) {
        return this.userMedals.stream()
            .filter(userMedal -> userMedal.hasSameMedalId(medalId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("?????? ??????(%s)??? ???????????? ?????? ??????(%s)??? ???????????? ?????? ????????????", this.id, medalId), E404_NOT_EXISTS_USER_MEDAL));
    }

    @NotNull
    public String getSocialId() {
        return this.socialInfo.getSocialId();
    }

    @NotNull
    public UserSocialType getSocialType() {
        return this.socialInfo.getSocialType();
    }

    @Nullable
    public UserMedal getActivatedMedal() {
        return this.userMedals.stream()
            .filter(UserMedal::isActiveStatus)
            .findFirst()
            .orElse(null);
    }

}
