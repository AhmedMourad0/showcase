package dev.ahmedmourad.showcase.common

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadWriteProperty

expect class Handle {
    fun <T : Any, M : MutableState<T>> saveable(
        init: () -> M
    ): PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, T>>
    operator fun <T> set(key: String, value: T?)
    operator fun <T> get(key: String): T?
}

expect abstract class ViewModel(handle: Handle) {
    val viewModelScope: CoroutineScope
    fun doOnDispose(action: () -> Unit)
}
