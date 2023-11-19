package dev.ahmedmourad.showcase.common.logging

import dev.ahmedmourad.showcase.common.*

fun log(type: LogType, throwable: Throwable) {
    log(type, "", throwable)
}

sealed interface LogType
@Suppress("ClassName")
data object E : LogType
@Suppress("ClassName")
data object D : LogType
