package com.depromeet.threedollar.domain.mongo.config.mongo

import org.springframework.transaction.annotation.Transactional

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Transactional(transactionManager = MONGO_TRANSACTION_MANAGER)
annotation class MongoTransactional
