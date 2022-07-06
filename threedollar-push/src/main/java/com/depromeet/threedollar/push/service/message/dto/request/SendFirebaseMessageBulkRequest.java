package com.depromeet.threedollar.push.service.message.dto.request;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendFirebaseMessageBulkRequest {

    @NotEmpty
    @Size(min = 1, max = 500)
    @JsonProperty("tokens")
    private Set<String> tokens;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    public List<Message> toMessages() {
        return tokens.stream()
            .map(this::toMessage)
            .collect(Collectors.toList());
    }

    private Message toMessage(String token) {
        return Message.builder()
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .setToken(token)
            .build();
    }

}
