package dev.ahmedmourad.showcase.common.utils

import dev.ahmedmourad.showcase.common.*

sealed interface Message {
    val v: String
}

data class Required(override val v: String) : Message
data class MustHave(override val v: String) : Message
data class CannotHave(override val v: String) : Message
data class IllegalName(override val v: String) : Message
data class Raw(override val v: String) : Message
data class Fallback(override val v: String) : Message

sealed interface MessageStrategy {
    data class Required(val m: String) : MessageStrategy
    data class Composite(
        val mustHave: List<String>,
        val cannotHave: List<String>,
        val illegalNames: List<String>,
        val rangeIntersections: List<String>,
        val raw: List<String>
    ) : MessageStrategy
    data class Fallback(val m: List<String>) : MessageStrategy
    data object None : MessageStrategy
}

fun List<Message>.asStrategy(): MessageStrategy {

    data class Accumulator(
        val composite: MessageStrategy.Composite = MessageStrategy.Composite(emptyList(), emptyList(), emptyList(), emptyList(), emptyList()),
        val fallback: MessageStrategy.Fallback = MessageStrategy.Fallback(emptyList())
    )

    fun Accumulator.withComposite(transform: (MessageStrategy.Composite) -> MessageStrategy.Composite): Accumulator {
        return this.copy(composite = transform(this.composite))
    }

    fun Accumulator.withFallback(transform: (MessageStrategy.Fallback) -> MessageStrategy.Fallback): Accumulator {
        return this.copy(fallback = transform(this.fallback))
    }

    val acc = this.fold(Accumulator()) { acc, message ->
        when (message) {
            is Required -> return MessageStrategy.Required(message.v)
            is CannotHave -> acc.withComposite { it.copy(cannotHave = it.cannotHave + message.v) }
            is MustHave -> acc.withComposite { it.copy(mustHave = it.mustHave + message.v) }
            is IllegalName -> acc.withComposite { it.copy(illegalNames = it.illegalNames + message.v) }
            is Raw -> acc.withComposite { it.copy(raw = it.raw + message.v) }
            is Fallback -> acc.withFallback { it.copy(m = it.m + message.v) }
        }
    }

    return when {
        acc.composite.mustHave.isNotEmpty() ||
            acc.composite.cannotHave.isNotEmpty() ||
            acc.composite.raw.isNotEmpty() ||
            acc.composite.illegalNames.isNotEmpty() ||
            acc.composite.rangeIntersections.isNotEmpty() -> acc.composite
        acc.fallback.m.isNotEmpty() -> acc.fallback
        else -> MessageStrategy.None
    }
}

fun MessageStrategy.showsAsError(): Boolean {
    return when (this) {
        is MessageStrategy.Required -> false
        is MessageStrategy.Composite -> true
        is MessageStrategy.Fallback -> true
        MessageStrategy.None -> false
    }
}

fun somethingWentWrong() = Raw(RR.strings.something_went_wrong.desc().get())
