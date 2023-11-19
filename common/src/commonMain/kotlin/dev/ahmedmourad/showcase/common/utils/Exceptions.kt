package dev.ahmedmourad.showcase.common.utils

import dev.ahmedmourad.showcase.common.Parcelable
import dev.ahmedmourad.showcase.common.Parcelize
import dev.ahmedmourad.showcase.common.log
import dev.ahmedmourad.showcase.common.logging.E

sealed interface LocalReadWriteException : Parcelable

@Parcelize
data class UnknownException(val origin: Throwable) : LocalReadWriteException {
    init {
        log(E, origin.message, origin)
    }
}
