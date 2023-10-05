package com.douglasbruce.tasky.features.login.form

sealed class LoginFormEvent {
    data class EmailValueChanged(val email: String) : LoginFormEvent()
    data class PasswordValueChanged(val password: String) : LoginFormEvent()
    data object TogglePasswordVisibility : LoginFormEvent()
    data class Submit(val onLoginClick: () -> Unit) : LoginFormEvent()
}