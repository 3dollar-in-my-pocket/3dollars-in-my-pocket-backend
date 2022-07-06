package com.depromeet.threedollar.push.infra.firebase;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Component
public class FirebaseAppInitializer {

    private static final String USER_FIREBASE_CREDENTIAL_PATH = "firebase/firebase-user.json";
    private static final String BOSS_FIREBASE_CREDENTIAL_PATH = "firebase/firebase-boss.json";

    @PostConstruct
    public void initializeFirebaseApps() {
        initializeFirebaseApp(FirebaseAppType.USER, USER_FIREBASE_CREDENTIAL_PATH);
        initializeFirebaseApp(FirebaseAppType.BOSS, BOSS_FIREBASE_CREDENTIAL_PATH);
    }

    private void initializeFirebaseApp(@NotNull FirebaseAppType firebaseAppType, @NotNull String firebaseCredentialPath) {
        try {
            if (notInitializeFirebaseApp(firebaseAppType)) {
                FirebaseApp.initializeApp(FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseCredentialPath).getInputStream())).build(), firebaseAppType.getAppName());
            }
        } catch (IOException e) {
            throw new InternalServerException(String.format("Firebase 앱을 초기화 하는 중 에러가 발생하였습니다 message: (%s)", e.getMessage()));
        }
    }

    private boolean notInitializeFirebaseApp(@NotNull FirebaseAppType appType) {
        return FirebaseApp.getApps().stream()
            .noneMatch(app -> appType.getAppName().equals(app.getName()));
    }

}
