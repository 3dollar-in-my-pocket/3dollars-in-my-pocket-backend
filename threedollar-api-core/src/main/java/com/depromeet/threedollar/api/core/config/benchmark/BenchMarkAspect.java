package com.depromeet.threedollar.api.core.config.benchmark;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class BenchMarkAspect {

    @Around("@annotation(com.depromeet.threedollar.api.core.config.benchmark.BenchMark)")
    public Object calculatePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.nanoTime();
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            long endTime = System.nanoTime();
            log.info("Performance: {}s", (TimeUnit.NANOSECONDS.toMillis(endTime - startTime) / 1000.0));
        }
    }

}
