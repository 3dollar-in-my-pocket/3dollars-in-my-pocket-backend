package com.depromeet.threedollar.external.client.slack.dto.request;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSlackMessageRequest {

    private String text;

    public static PostSlackMessageRequest of(String text) {
        return new PostSlackMessageRequest(text);
    }

}
