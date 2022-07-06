package com.depromeet.threedollar.push.infra.firebase;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FirebaseMessagingFinder {

    @NotNull
    public static FirebaseMessaging find(@NotNull ApplicationType applicationType) {
        return FirebaseMessaging.getInstance(FirebaseAppFinder.find(applicationType));
    }

}
