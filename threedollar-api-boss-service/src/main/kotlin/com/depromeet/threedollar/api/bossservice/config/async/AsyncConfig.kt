package com.depromeet.threedollar.api.bossservice.config.async

import mu.KotlinLogging
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.AsyncConfigurerSupport
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.lang.reflect.Method
import java.util.concurrent.Executor

private const val MAX_POOL_SIZE = 50

@Profile("!integration-test")
@EnableAsync
@Configuration
class AsyncConfig : AsyncConfigurerSupport() {

    @Bean
    override fun getAsyncExecutor(): Executor {
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.maxPoolSize = MAX_POOL_SIZE
        taskExecutor.initialize()
        return taskExecutor
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler {
        return AsyncTaskExceptionHandler()
    }

}


class AsyncTaskExceptionHandler : SimpleAsyncUncaughtExceptionHandler() {

    private val log = KotlinLogging.logger {}

    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any) {
        log.error("비동기 처리 중 에러가 발생하였습니다  method: (${method}) params: ($params) exception: ($ex)")
    }

}
