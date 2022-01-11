package com.depromeet.threedollar.application.config.validator

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
