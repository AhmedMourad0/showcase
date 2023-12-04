package dev.ahmedmourad.showcase.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.ahmedmourad.showcase.common.initializers.appCtx
import dev.ahmedmourad.showcase.common.screens.themeselector.AndroidSchemes
import dev.ahmedmourad.showcase.common.screens.themeselector.ThemeSchemes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

actual class PreferenceManager {

    actual suspend fun toggleIsInDarkMode() {
        appCtx.dataStore.edit { settings ->
            settings[IsInDarkModeKey] = settings[IsInDarkModeKey]?.not() ?: false
        }
    }

    actual suspend fun setIsInDarkMode(value: Boolean) {
        appCtx.dataStore.edit { settings ->
            settings[IsInDarkModeKey] = value
        }
    }

    actual fun isInDarkMode(otherwise: () -> Boolean): Flow<Boolean> {
        return appCtx.dataStore.data.map { settings -> settings[IsInDarkModeKey] ?: otherwise() }
    }

    actual suspend fun selectTheme(value: ThemeSchemes) {
        appCtx.dataStore.edit { settings ->
            settings[SelectedThemeKey] = value.id
        }
    }

    actual fun selectedTheme(otherwise: () -> ThemeSchemes): Flow<ThemeSchemes> {
        return appCtx.dataStore.data.map { settings ->
            AndroidSchemes.firstOrNull {
                it.id == settings[SelectedThemeKey]
            } ?: otherwise()
        }
    }

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        val IsInDarkModeKey = booleanPreferencesKey("is_in_dark_mode")
        val SelectedThemeKey = stringPreferencesKey("selected_theme")
    }
}
