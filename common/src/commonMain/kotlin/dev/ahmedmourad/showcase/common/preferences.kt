package dev.ahmedmourad.showcase.common

import dev.ahmedmourad.showcase.common.pickers.time.TimePickerType
import kotlinx.coroutines.flow.Flow

expect class PreferenceManager() {
    suspend fun toggleIsInDarkMode()
    suspend fun setIsInDarkMode(value: Boolean)
    fun isInDarkMode(otherwise: () -> Boolean): Flow<Boolean>
    suspend fun setPreferredTimePicker(value: TimePickerType)
    fun preferredTimePicker(otherwise: () -> TimePickerType): Flow<TimePickerType>
}

fun defaultIsInDarkMode() = true
fun defaultTimePickerType() = TimePickerType.Wheel
