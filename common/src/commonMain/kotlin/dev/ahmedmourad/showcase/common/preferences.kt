package dev.ahmedmourad.showcase.common

import dev.ahmedmourad.showcase.common.screens.themeselector.ThemeSchemes
import dev.ahmedmourad.showcase.common.screens.themeselector.schemes.DarkBlueSchemes
import kotlinx.coroutines.flow.Flow

expect class PreferenceManager() {
    suspend fun toggleIsInDarkMode()
    suspend fun setIsInDarkMode(value: Boolean)
    fun isInDarkMode(otherwise: () -> Boolean): Flow<Boolean>
    suspend fun selectTheme(value: ThemeSchemes)
    fun selectedTheme(otherwise: () -> ThemeSchemes): Flow<ThemeSchemes>
}

fun defaultIsInDarkMode() = true
fun defaultSelectedTheme() = DarkBlueSchemes
