package com.depromee.threedollar.domain.mongo

import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import com.depromeet.threedollar.common.constants.PackageConstants.BASE_PACKAGE
import com.depromeet.threedollar.domain.mongo.ThreeDollarDomainMongoRoot

@ConfigurationPropertiesScan(basePackages = [BASE_PACKAGE])
@SpringBootApplication(scanBasePackages = [BASE_PACKAGE])
internal class ThreeDollarDomainMongoRootTest : ThreeDollarDomainMongoRoot() {

    @Test
    fun contextsLoad() {

    }

}
