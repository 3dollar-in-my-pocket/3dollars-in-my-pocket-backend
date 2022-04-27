package com.depromeet.threedollar.external.client.google.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleProfileInfoResponse {

    private String id;

    private String email;

    private String name;

    @Builder(access = AccessLevel.PRIVATE)
    private GoogleProfileInfoResponse(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static GoogleProfileInfoResponse testInstance(String id, String email, String name) {
        return GoogleProfileInfoResponse.builder()
            .id(id)
            .email(email)
            .name(name)
            .build();
    }

    public static GoogleProfileInfoResponse testInstance(String id) {
        return GoogleProfileInfoResponse.builder()
            .id(id)
            .email("test@gmail.com")
            .name("테스트")
            .build();
    }

}
