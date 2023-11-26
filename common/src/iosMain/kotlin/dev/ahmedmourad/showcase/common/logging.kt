package dev.ahmedmourad.showcase.common

import dev.ahmedmourad.showcase.common.logging.D
import dev.ahmedmourad.showcase.common.logging.E
import dev.ahmedmourad.showcase.common.logging.LogType
import platform.Foundation.NSThread

actual fun log(message: String?) {
    log(type = E, message = message, throwable = null)
}

actual fun log(type: LogType, message: String?) {
    log(type = type, message = message, throwable = null)
}

actual fun log(type: LogType, message: String?, throwable: Throwable?) {
    when (type) {
        D, E -> {
            println(NSThread.callStackSymbols)
            throwable?.printStackTrace()
        }
    }
}
