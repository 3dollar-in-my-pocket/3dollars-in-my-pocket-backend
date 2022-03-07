package com.depromeet.threedollar.api.user.config.resolver;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface GeoCoordinate {

    boolean required() default true;

}
