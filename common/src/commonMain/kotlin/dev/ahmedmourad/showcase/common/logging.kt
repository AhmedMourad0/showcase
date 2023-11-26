package dev.ahmedmourad.showcase.common

import dev.ahmedmourad.showcase.common.logging.LogType

expect fun log(message: String?)
expect fun log(type: LogType, message: String?)
expect fun log(type: LogType, message: String?, throwable: Throwable?)
