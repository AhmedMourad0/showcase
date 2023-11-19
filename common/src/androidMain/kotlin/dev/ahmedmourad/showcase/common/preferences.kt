package dev.ahmedmourad.showcase.common

import androidx.datastore.preferences.core.edit
import dev.ahmedmourad.showcase.common.datastore.DefaultPreferredTimePickerKey
import dev.ahmedmourad.showcase.common.datastore.IsInDarkModeKey
import dev.ahmedmourad.showcase.common.datastore.dataStore
import dev.ahmedmourad.showcase.common.initializers.appCtx
import dev.ahmedmourad.showcase.common.pickers.time.TimePickerType
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

    actual suspend fun setPreferredTimePicker(value: TimePickerType) {
        appCtx.dataStore.edit { settings ->
            settings[DefaultPreferredTimePickerKey] = value.ordinal
        }
    }

    actual fun preferredTimePicker(otherwise: () -> TimePickerType): Flow<TimePickerType> {
        return appCtx.dataStore.data.map { settings ->
            TimePickerType.values().getOrNull(
                settings[DefaultPreferredTimePickerKey] ?: return@map otherwise()
            ) ?: return@map otherwise()
        }
    }
}
