package com.depromeet.threedollar.infrastructure.external.feign;

import static com.depromeet.threedollar.common.constants.PackageConstants.BASE_PACKAGE;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = BASE_PACKAGE)
@Configuration
public class FeignClientConfig {

}
