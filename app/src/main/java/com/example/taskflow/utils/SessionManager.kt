package com.example.taskflow.utils

import android.content.Context

class SessionManager(
    context: Context
) {
    private val preferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )
    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ) {
        preferences.edit()
            .putString(
                KEY_FULL_NAME,
                fullName.trim()
            )
            .putString(
                KEY_EMAIL,
                email.trim().lowercase()
            )
            .putString(
                KEY_PASSWORD,
                password
            )
            .putBoolean(
                KEY_LOGGED_IN,
                false
            )
            .apply()
    }

    fun isAccountRegistered(): Boolean {
        val email = preferences.getString(
            KEY_EMAIL,
            null
        )

        val password = preferences.getString(
            KEY_PASSWORD,
            null
        )

        return !email.isNullOrBlank() &&
                !password.isNullOrBlank()
    }

    fun validateCredentials(
        email: String,
        password: String
    ): Boolean {
        val registeredEmail = preferences.getString(
            KEY_EMAIL,
            null
        )

        val registeredPassword = preferences.getString(
            KEY_PASSWORD,
            null
        )

        return email.trim().lowercase() ==
                registeredEmail &&
                password == registeredPassword
    }

    fun isRegisteredEmail(
        email: String
    ): Boolean {
        val registeredEmail = preferences.getString(
            KEY_EMAIL,
            null
        )

        return email.trim().lowercase() ==
                registeredEmail
    }

    fun updatePassword(
        newPassword: String
    ) {
        preferences.edit()
            .putString(
                KEY_PASSWORD,
                newPassword
            )
            .putBoolean(
                KEY_LOGGED_IN,
                false
            )
            .apply()
    }

    fun setLoggedIn(
        loggedIn: Boolean
    ) {
        preferences.edit()
            .putBoolean(
                KEY_LOGGED_IN,
                loggedIn
            )
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return preferences.getBoolean(
            KEY_LOGGED_IN,
            false
        )
    }

    fun setOnboardingCompleted(
        completed: Boolean
    ) {
        preferences.edit()
            .putBoolean(
                KEY_ONBOARDING_COMPLETED,
                completed
            )
            .apply()
    }

    fun isOnboardingCompleted(): Boolean {
        return preferences.getBoolean(
            KEY_ONBOARDING_COMPLETED,
            false
        )
    }

    fun getRegisteredEmail(): String {
        return preferences.getString(
            KEY_EMAIL,
            ""
        ).orEmpty()
    }

    fun getFullName(): String {
        return preferences.getString(
            KEY_FULL_NAME,
            ""
        ).orEmpty()
    }

    fun logout() {
        setLoggedIn(false)
    }

    fun clearAllSessionData() {
        preferences.edit()
            .clear()
            .apply()
    }

    companion object {
        private const val PREFERENCES_NAME =
            "taskflow_session_preferences"

        private const val KEY_FULL_NAME =
            "registered_full_name"

        private const val KEY_EMAIL =
            "registered_email"

        private const val KEY_PASSWORD =
            "registered_password"

        private const val KEY_LOGGED_IN =
            "is_logged_in"

        private const val KEY_ONBOARDING_COMPLETED =
            "onboarding_completed"
    }
}