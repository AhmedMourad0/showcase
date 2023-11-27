package dev.ahmedmourad.showcase.common

import kotlinx.coroutines.flow.Flow

expect class PreferenceManager() {
    suspend fun toggleIsInDarkMode()
    suspend fun setIsInDarkMode(value: Boolean)
    fun isInDarkMode(otherwise: () -> Boolean): Flow<Boolean>
}

fun defaultIsInDarkMode() = true
