package com.depromeet.threedollar.infrastructure.firebase.provider;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FirebaseMessagingFinder {

    @NotNull
    public static FirebaseMessaging find(@NotNull ApplicationType applicationType) {
        return FirebaseMessaging.getInstance(FirebaseAppFinder.find(applicationType));
    }

}
