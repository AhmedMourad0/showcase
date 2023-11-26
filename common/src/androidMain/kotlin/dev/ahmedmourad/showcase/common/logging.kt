package dev.ahmedmourad.showcase.common

import android.util.Log
import dev.ahmedmourad.showcase.common.logging.D
import dev.ahmedmourad.showcase.common.logging.E
import dev.ahmedmourad.showcase.common.logging.LogType

actual fun log(message: String?) {
    log(type = E, message = message, throwable = null)
}

actual fun log(type: LogType, message: String?) {
    log(type = type, message = message, throwable = null)
}

actual fun log(type: LogType, message: String?, throwable: Throwable?) {
    val stackTrace = Thread.currentThread().stackTrace[2]
    val tag = "${stackTrace.fileName}:${stackTrace.className}:${stackTrace.methodName}"
    when (type) {
        D -> Log.d(tag, message, throwable)
        E -> Log.e(tag, message, throwable)
    }
}
