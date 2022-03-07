package com.depromeet.threedollar.api.user.config.resolver;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapCoordinate {

    boolean required() default true;

}
