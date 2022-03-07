package com.depromeet.threedollar.external.client.storage.property;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ConstructorBinding
@ConfigurationProperties("cloud.aws.s3")
public class AmazonS3Property {

    private final String bucket;

}
