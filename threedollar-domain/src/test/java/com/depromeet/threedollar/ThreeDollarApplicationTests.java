package com.depromeet.threedollar;

import com.depromeet.threedollar.domain.ThreeDollarDomainRoot;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = ThreeDollarDomainRoot.class)
class ThreeDollarApplicationTests extends ThreeDollarDomainRoot {

    @Test
    void contextLoads() {

    }

}
