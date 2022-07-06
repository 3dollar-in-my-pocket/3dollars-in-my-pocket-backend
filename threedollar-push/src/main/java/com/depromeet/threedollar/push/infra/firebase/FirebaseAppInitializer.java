package com.depromeet.threedollar.push.infra.firebase;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.common.type.ApplicationType;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Component
public class FirebaseAppInitializer {

    private static final String USER_FIREBASE_CREDENTIAL_PATH = "firebase/firebase-user.json";
    private static final String BOSS_FIREBASE_CREDENTIAL_PATH = "firebase/firebase-boss.json";

    @PostConstruct
    public void initializeFirebaseApps() {
        initializeFirebaseApp(ApplicationType.USER_API, USER_FIREBASE_CREDENTIAL_PATH);
        initializeFirebaseApp(ApplicationType.BOSS_API, BOSS_FIREBASE_CREDENTIAL_PATH);
    }

    private void initializeFirebaseApp(@NotNull ApplicationType applicationType, @NotNull String firebaseCredentialPath) {
        try {
            if (notInitializeFirebaseApp(applicationType)) {
                FirebaseApp.initializeApp(FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseCredentialPath).getInputStream())).build(), applicationType.name());
            }
        } catch (IOException e) {
            throw new InternalServerException(String.format("Firebase 앱을 초기화 하는 중 에러가 발생하였습니다 message: (%s)", e.getMessage()));
        }
    }

    private boolean notInitializeFirebaseApp(@NotNull ApplicationType applicationType) {
        return FirebaseApp.getApps().stream()
            .noneMatch(app -> applicationType.name().equals(app.getName()));
    }

}
