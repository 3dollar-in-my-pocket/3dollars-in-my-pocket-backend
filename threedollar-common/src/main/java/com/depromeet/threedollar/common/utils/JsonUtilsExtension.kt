package com.depromeet.threedollar.common.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

inline fun <reified T : Any> ObjectMapper.decode(content: String): T = readValue(content)
