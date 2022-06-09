package com.depromeet.threedollar.domain.rds.core.support;

import java.util.function.Supplier;

import com.querydsl.core.types.Predicate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuerydslSupport {

    public static Predicate predicate(boolean condition, Supplier<Predicate> supplier) {
        if (!condition) {
            return null;
        }
        return supplier.get();
    }

}
