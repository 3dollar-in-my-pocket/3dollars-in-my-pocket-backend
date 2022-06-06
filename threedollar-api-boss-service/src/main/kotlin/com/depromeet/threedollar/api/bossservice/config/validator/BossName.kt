package com.depromeet.threedollar.api.bossservice.config.validator

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [BossNameValidator::class])
@Target(AnnotationTarget.FIELD)
annotation class BossName(
    val message: String = "{account.name.format}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
