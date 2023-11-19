package dev.ahmedmourad.showcase.common.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val IsInDarkModeKey = booleanPreferencesKey("is_in_dark_mode")
val DefaultRingtoneNameKey = stringPreferencesKey("default_ringtone_name")
val DefaultRingtoneSourceKey = stringPreferencesKey("default_ringtone_source")
val DefaultPreferredTimePickerKey = intPreferencesKey("default_preferred_time_picker")
