package dev.ahmedmourad.showcase.common

import dev.ahmedmourad.showcase.common.pickers.time.TimePickerType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.mapLatest
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSUserDefaultsDidChangeNotification

private const val IsInDarkModeKey = "is_in_dark_mode"
private const val PreferredTimePickerKey = "preferred_time_picker"

@OptIn(ExperimentalCoroutinesApi::class)
actual class PreferenceManager {

    private val defaults = NSUserDefaults.standardUserDefaults

    actual suspend fun toggleIsInDarkMode() {
        val new = defaults.getOrDefault(
            key = IsInDarkModeKey,
            otherwise = ::defaultIsInDarkMode,
            get = defaults::boolForKey
        ).not()
        defaults.setBool(value = new, forKey = IsInDarkModeKey)
    }

    actual suspend fun setIsInDarkMode(value: Boolean) {
        defaults.setBool(value = value, forKey = IsInDarkModeKey)
    }

    actual fun isInDarkMode(otherwise: () -> Boolean): Flow<Boolean> {
        return defaults.asFlow().mapLatest {
            defaults.getOrDefault(
                key = IsInDarkModeKey,
                otherwise = otherwise,
                get = defaults::boolForKey
            )
        }
    }

    actual suspend fun setPreferredTimePicker(value: TimePickerType) {
        defaults.setInteger(value.ordinal.toLong(), forKey = PreferredTimePickerKey)
    }

    actual fun preferredTimePicker(otherwise: () -> TimePickerType): Flow<TimePickerType> {
        return defaults.asFlow().mapLatest {
            defaults.getOrDefault(
                key = PreferredTimePickerKey,
                otherwise = otherwise
            ) {
                TimePickerType.values()
                    .getOrNull(defaults.integerForKey(it).toInt())
                    ?: otherwise()
            }
        }
    }
}

private fun NSUserDefaults.asFlow(): Flow<Unit> {
    return callbackFlow {
        this.trySend(Unit)
        val observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = NSUserDefaultsDidChangeNotification,
            `object` = this@asFlow,
            queue = null
        ) {
            this.trySend(Unit)
        }
        awaitClose {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }
}

private fun <T : Any> NSUserDefaults.getOrDefault(
    key: String,
    otherwise: () -> T,
    get: (key: String) -> T
): T = if (this.objectForKey(key) != null) {
    get(key)
} else {
    otherwise()
}
