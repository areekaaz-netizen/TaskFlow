package com.example.taskflow.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.preferences.ThemePreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val themePreferences =
        ThemePreferences(application.applicationContext)

    val themeMode: StateFlow<ThemeMode> =
        themePreferences.themeMode.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5_000
            ),
            initialValue = ThemeMode.SYSTEM
        )

    fun updateThemeMode(
        themeMode: ThemeMode
    ) {
        viewModelScope.launch {
            themePreferences.saveThemeMode(themeMode)
        }
    }
}