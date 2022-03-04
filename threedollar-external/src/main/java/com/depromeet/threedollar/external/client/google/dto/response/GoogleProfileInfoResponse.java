package com.depromeet.threedollar.external.client.google.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleProfileInfoResponse {

    private String id;

    private String email;

    private String name;

    public static GoogleProfileInfoResponse testInstance(String id, String email, String name) {
        return new GoogleProfileInfoResponse(id, email, name);
    }

    public static GoogleProfileInfoResponse testInstance(String id) {
        return testInstance(id, "test@gmail.com", "테스트");
    }

}
