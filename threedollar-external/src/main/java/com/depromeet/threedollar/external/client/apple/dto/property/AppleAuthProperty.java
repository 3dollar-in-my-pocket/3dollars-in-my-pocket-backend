package com.depromeet.threedollar.external.client.apple.dto.property;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ConstructorBinding
@ConfigurationProperties("apple")
public class AppleAuthProperty {

    private final String issuer;

    private final String clientId;

}
