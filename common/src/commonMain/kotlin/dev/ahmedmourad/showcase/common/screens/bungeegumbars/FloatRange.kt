package dev.ahmedmourad.showcase.common.screens.bungeegumbars

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlin.jvm.JvmInline

@Stable
fun FloatRange(start: Float, end: Float) = FloatRange(packFloats(start, end))

@Immutable
@JvmInline
value class FloatRange(private val packedValue: Long) {

    @Stable
    val start: Float get() = unpackFloat1(packedValue)

    @Stable
    val end: Float get() = unpackFloat2(packedValue)

    @Stable
    operator fun component1(): Float = start

    @Stable
    operator fun component2(): Float = end

    fun copy(start: Float = this.start, end: Float = this.end) = FloatRange(start, end)

    override fun toString(): String {
        return "FloatRange(start=$start,end=$end)"
    }
}

/**
 * Packs two Float values into one Long value for use in inline classes.
 */
inline fun packFloats(val1: Float, val2: Float): Long {
    val v1 = val1.toBits().toLong()
    val v2 = val2.toBits().toLong()
    return v1.shl(32) or (v2 and 0xFFFFFFFF)
}

/**
 * Unpacks the first Float value in [packFloats] from its returned Long.
 */
inline fun unpackFloat1(value: Long): Float {
    return Float.fromBits(value.shr(32).toInt())
}

/**
 * Unpacks the second Float value in [packFloats] from its returned Long.
 */
inline fun unpackFloat2(value: Long): Float {
    return Float.fromBits(value.and(0xFFFFFFFF).toInt())
}
