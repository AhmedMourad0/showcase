package dev.ahmedmourad.showcase.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("ComposableNaming")
@Composable
fun <T> Flow<T>.collectAsEffect(
    block: suspend (T) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        onEach(block).launchIn(this)
    }
}
