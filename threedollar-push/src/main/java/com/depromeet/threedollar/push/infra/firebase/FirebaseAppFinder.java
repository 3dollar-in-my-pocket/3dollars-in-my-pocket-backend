package com.depromeet.threedollar.push.infra.firebase;

import org.jetbrains.annotations.NotNull;

import com.google.firebase.FirebaseApp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class FirebaseAppFinder {

    @NotNull
    static FirebaseApp find(@NotNull FirebaseAppType firebaseAppType) {
        return FirebaseApp.getInstance(firebaseAppType.name());
    }

}
