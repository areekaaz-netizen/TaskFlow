package com.example.taskflow.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.taskflow.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.themeDataStore by preferencesDataStore(
    name = "theme_preferences"
)

class ThemePreferences(
    private val context: Context
) {

    private object PreferenceKeys {
        val THEME_MODE: Preferences.Key<String> =
            stringPreferencesKey("theme_mode")
    }

    val themeMode: Flow<ThemeMode> =
        context.themeDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(
                        androidx.datastore.preferences.core.emptyPreferences()
                    )
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                when (
                    preferences[PreferenceKeys.THEME_MODE]
                ) {
                    ThemeMode.LIGHT.name -> ThemeMode.LIGHT
                    ThemeMode.DARK.name -> ThemeMode.DARK
                    else -> ThemeMode.SYSTEM
                }
            }

    suspend fun saveThemeMode(
        themeMode: ThemeMode
    ) {
        context.themeDataStore.edit { preferences ->
            preferences[PreferenceKeys.THEME_MODE] =
                themeMode.name
        }
    }
}