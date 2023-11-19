package dev.ahmedmourad.showcase.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.saveable.autoSaver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadWriteProperty
import androidx.lifecycle.viewModelScope as androidViewModelScope

actual class Handle(private val handle: SavedStateHandle) {
    @OptIn(SavedStateHandleSaveableApi::class)
    actual fun <T : Any, M : MutableState<T>> saveable(
        init: () -> M
    ): PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, T>> {
        return handle.saveable(stateSaver = autoSaver(), init = init)
    }
    actual operator fun <T> set(key: String, value: T?) {
        handle[key] = value
    }
    actual operator fun <T> get(key: String): T? {
        return handle[key]
    }
}

actual abstract class ViewModel actual constructor(
    handle: Handle
) : androidx.lifecycle.ViewModel() {
    actual val viewModelScope: CoroutineScope = androidViewModelScope
    private val onClearedDispatcher: MutableList<() -> Unit> = mutableListOf()
    actual fun doOnDispose(action: () -> Unit) {
        onClearedDispatcher.add(action)
    }
    override fun onCleared() {
        onClearedDispatcher.forEach { it.invoke() }
        super.onCleared()
    }
}
