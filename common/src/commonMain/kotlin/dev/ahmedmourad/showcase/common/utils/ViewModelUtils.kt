package dev.ahmedmourad.showcase.common.utils

import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> ViewModel.SaveableMutableStateFlow(initial: T, handle: Handle): MutableStateFlow<T> {
    val flow = MutableStateFlow(handle["state"] ?: initial)
    flow.onEach { handle["state"] = it }.flowOn(Dispatchers.Default).launchIn(viewModelScope)
    return flow
}
