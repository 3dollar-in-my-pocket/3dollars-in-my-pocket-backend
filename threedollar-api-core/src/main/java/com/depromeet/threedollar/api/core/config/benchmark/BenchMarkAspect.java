package com.depromeet.threedollar.api.core.config.benchmark;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class BenchMarkAspect {

    @Around("@annotation(com.depromeet.threedollar.api.core.config.benchmark.BenchMark)")
    public Object calculatePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Performance: {}", endTime - startTime);
        }
    }

}
