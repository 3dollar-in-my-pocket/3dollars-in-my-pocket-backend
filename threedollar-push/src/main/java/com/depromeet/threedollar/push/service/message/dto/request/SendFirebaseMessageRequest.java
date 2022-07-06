package com.depromeet.threedollar.push.service.message.dto.request;

import javax.validation.constraints.NotBlank;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendFirebaseMessageRequest {

    @NotBlank
    private String token;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    public Message toMessage() {
        return Message.builder()
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .setToken(token)
            .build();
    }

}
