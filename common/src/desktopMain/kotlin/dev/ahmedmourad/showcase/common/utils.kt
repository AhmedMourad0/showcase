package dev.ahmedmourad.showcase.common

import java.util.UUID

actual fun randomUUID(): String = UUID.randomUUID().toString()

actual fun isRtl(): Boolean = false
