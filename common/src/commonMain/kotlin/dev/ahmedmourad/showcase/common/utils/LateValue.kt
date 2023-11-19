package dev.ahmedmourad.showcase.common.utils

import androidx.compose.runtime.Immutable
import dev.ahmedmourad.showcase.common.*

typealias LocalValue<T> = LateValue<LocalReadWriteException, T>
typealias LocalList<T> = LateList<LocalReadWriteException, T>

@Immutable
sealed interface LateValue<out E : Parcelable?, out T : Parcelable?> : Parcelable {
    @Parcelize
    @Immutable
    data class Success<out T : Parcelable?>(val v: T) : LateValue<Nothing, T>
}

@Immutable
sealed interface LateList<out E : Parcelable?, out T : Parcelable?> : Parcelable {
    @Parcelize
    @Immutable
    data class Success<out T : Parcelable?>(val v: List<T>) : LateList<Nothing, T>
}

@Parcelize
@Immutable
object Loading : LateValue<Nothing, Nothing>, LateList<Nothing, Nothing>

@Parcelize
@Immutable
data class Error<out E : Parcelable?>(val e: E) : LateValue<E, Nothing>, LateList<E, Nothing>

fun <T : Parcelable?> LateList<*, T>.successOrNull(): List<T>? = when (this) {
    is LateList.Success -> this.v
    Loading -> null
    is Error -> null
}

fun <T : Parcelable?> LateValue<*, T>.successOrNull(): T? = when (this) {
    is LateValue.Success -> this.v
    Loading -> null
    is Error -> null
}

fun <T : Parcelable?> List<T>.success() = LateList.Success(this)
fun <T : Parcelable?> T.success() = LateValue.Success(this)

fun <E : Parcelable?> E.error() = Error(this)
fun loading() = Loading

fun LateValue<*, *>.isSuccess() = when (this) {
    is LateValue.Success -> true
    Loading, is Error -> false
}

fun LateList<*, *>.isSuccess() = when (this) {
    is LateList.Success -> true
    Loading, is Error -> false
}

fun LateList<*, *>.isSuccessAndEmpty() = when (this) {
    is LateList.Success -> this.v.isEmpty()
    Loading, is Error -> false
}

fun <E : Parcelable, From : Parcelable, To : Parcelable> LateValue<E, From>.mapSuccess(
    transform: (From) -> To
): LateValue<E, To> = when (this) {
    is LateValue.Success -> transform(this.v).success()
    Loading -> Loading
    is Error -> this
}

fun <E : Parcelable, From : Parcelable, To : Parcelable> LateList<E, From>.mapSuccess(
    transform: (List<From>) -> List<To>
): LateList<E, To> = when (this) {
    is LateList.Success -> transform(this.v).success()
    Loading -> Loading
    is Error -> this
}
