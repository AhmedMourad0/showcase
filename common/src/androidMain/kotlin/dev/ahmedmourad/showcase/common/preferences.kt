package dev.ahmedmourad.showcase.common

import androidx.datastore.preferences.core.edit
import dev.ahmedmourad.showcase.common.datastore.IsInDarkModeKey
import dev.ahmedmourad.showcase.common.datastore.dataStore
import dev.ahmedmourad.showcase.common.initializers.appCtx
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
}
