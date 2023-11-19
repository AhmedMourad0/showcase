package dev.ahmedmourad.showcase.common.milliontimes

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.ahmedmourad.showcase.common.Handle
import dev.ahmedmourad.showcase.common.InstantParceler
import dev.ahmedmourad.showcase.common.Parcelable
import dev.ahmedmourad.showcase.common.Parcelize
import dev.ahmedmourad.showcase.common.TypeParceler
import dev.ahmedmourad.showcase.common.ViewModel
import dev.ahmedmourad.showcase.common.utils.SaveableMutableStateFlow
import dev.ahmedmourad.showcase.common.utils.tickerFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

@Stable
open class MillionTimesViewModel(handle: Handle) : ViewModel(handle) {
    val state = SaveableMutableStateFlow(MillionTimesState(), handle)
    init {
        tickerFlow(
            period = 1.seconds,
            initialDelay = 3.seconds
        ).map {
            state.update {
                it.copy(matrix = createTimeMatrix(Clock.System.now().minus(it.start)))
            }
        }.flowOn(Dispatchers.Default).launchIn(viewModelScope)
    }
}

@Parcelize
@TypeParceler<Instant, InstantParceler>
@Immutable
data class MillionTimesState(
    val start: Instant = Clock.System.now(),
    val matrix: Matrix = createTimeMatrix(null)
) : Parcelable
