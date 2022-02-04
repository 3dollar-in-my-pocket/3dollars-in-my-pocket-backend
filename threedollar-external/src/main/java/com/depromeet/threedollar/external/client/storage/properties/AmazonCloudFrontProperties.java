package com.depromeet.threedollar.external.client.storage.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ConstructorBinding
@ConfigurationProperties("cloud.aws.cloudfront")
public class AmazonCloudFrontProperties {

    private final String url;

    public String getFullPathFileUrl(String fileName) {
        return this.url.concat(fileName);
    }

}
