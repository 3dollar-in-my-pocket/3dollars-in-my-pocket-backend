package com.depromeet.threedollar.domain.collection.medal;

import com.depromeet.threedollar.domain.domain.medal.Medal;
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionType;
import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.domain.user.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MedalUserObtainCollection {

    private final List<Medal> medalsUserNotHeld;
    private final MedalAcquisitionConditionType conditionType;

    public static MedalUserObtainCollection of(List<Medal> medals, MedalAcquisitionConditionType conditionType, @NotNull User user) {
        List<Medal> userNotHeldMedals = filterMedalsUserNotHeld(medals, user);
        return new MedalUserObtainCollection(userNotHeldMedals, conditionType);
    }

    private static List<Medal> filterMedalsUserNotHeld(List<Medal> medals, @NotNull User user) {
        List<Long> medalsUserObtains = user.getUserMedals().stream()
            .map(UserMedal::getMedalId)
            .collect(Collectors.toList());
        return medals.stream()
            .filter(medal -> !medalsUserObtains.contains(medal.getId()))
            .collect(Collectors.toList());
    }

    public boolean hasNoMoreMedalsCanBeObtained() {
        return this.medalsUserNotHeld.isEmpty();
    }

    public boolean hasMoreMedalsCanBeObtained() {
        return !hasNoMoreMedalsCanBeObtained();
    }

    public List<Medal> getSatisfyMedalsCanBeObtainedByDefault() {
        return getSatisfyMedalsCanBeObtained(0L);
    }

    public List<Medal> getSatisfyMedalsCanBeObtained(long counts) {
        return this.medalsUserNotHeld.stream()
            .filter(medal -> medal.canObtain(conditionType, counts))
            .collect(Collectors.toList());
    }

}
