package com.depromeet.threedollar.api.config.vadlidator

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [NickNameValidator::class])
@Target(AnnotationTarget.FIELD)
annotation class NickName(
    val message: String = "{user.name.format}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
