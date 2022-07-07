package com.depromeet.threedollar.domain.rds.core.support;

import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuerydslSupport {

    public static Predicate predicate(boolean condition, Supplier<Predicate> supplier) {
        if (!condition) {
            return null;
        }
        return supplier.get();
    }

}
