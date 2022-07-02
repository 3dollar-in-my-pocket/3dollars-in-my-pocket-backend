package com.depromeet.threedollar.api.core.config.benchmark;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class BenchMarkAspect {

    @Around("@annotation(com.depromeet.threedollar.api.core.config.benchmark.BenchMark)")
    public Object calculatePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            stopWatch.stop();
            log.info("Performance: {}s", (stopWatch.getTotalTimeMillis()));
        }
    }

}
