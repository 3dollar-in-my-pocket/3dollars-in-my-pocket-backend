package com.depromeet.threedollar.infrastructure.firebase;

import com.depromeet.threedollar.common.exception.model.ServiceUnAvailableException;
import com.depromeet.threedollar.common.type.ApplicationType;
import com.google.firebase.FirebaseApp;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class FirebaseAppFinder {

    @NotNull
    static FirebaseApp find(@NotNull ApplicationType applicationType) {
        try {
            return FirebaseApp.getInstance(applicationType.name());
        } catch (IllegalStateException e) {
            throw new ServiceUnAvailableException(String.format("해당하는 애플리케이션(%s)은 파이어베이스 등록이 되어 있지 않습니다", applicationType));
        }
    }

}
