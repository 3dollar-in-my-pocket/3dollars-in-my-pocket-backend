package com.depromee.threedollar.domain.mongo

import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import com.depromeet.threedollar.domain.mongo.ThreeDollarDomainMongoRoot

@ConfigurationPropertiesScan(basePackages = ["com.depromeet.threedollar"])
@SpringBootApplication(scanBasePackages = ["com.depromeet.threedollar"])
internal class ThreeDollarDomainMongoRootTest : ThreeDollarDomainMongoRoot() {

    @Test
    fun contextsLoad() {

    }

}
