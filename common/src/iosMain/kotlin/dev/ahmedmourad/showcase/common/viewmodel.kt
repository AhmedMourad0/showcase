package dev.ahmedmourad.showcase.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

actual class Handle {
    actual fun <T : Any, M : MutableState<T>> saveable(
        init: () -> M
    ): PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, T>> {
        return PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, T>> { _, _ ->
            val mutableState = init.invoke()
            // Create a property that delegates to the mutableState
            object : ReadWriteProperty<Any?, T> {
                override fun getValue(thisRef: Any?, property: KProperty<*>): T =
                    mutableState.getValue(thisRef, property)

                override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
                    mutableState.setValue(thisRef, property, value)
            }
        }
    }
    actual operator fun <T> set(key: String, value: T?) {

    }
    actual operator fun <T> get(key: String): T? {
        return null
    }
    companion object {
        val Default = Handle()
    }
}

actual abstract class ViewModel actual constructor(handle: Handle) {
    actual val viewModelScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    //TODO: call with disposable effect? maybe create a delegate that creates the vm and registers the effect
    private val onClearedDispatcher: MutableList<() -> Unit> = mutableListOf()
    actual fun doOnDispose(action: () -> Unit) {
        onClearedDispatcher.add(action)
    }
    fun clear() {
        onClearedDispatcher.forEach { it.invoke() }
        viewModelScope.coroutineContext.cancelChildren()
    }
}
