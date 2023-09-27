package com.douglasbruce.tasky.features.login.form

sealed class LoginFormEvent {
    data class EmailValueChanged(val email: String): LoginFormEvent()
    data class PasswordValueChanged(val password: String): LoginFormEvent()
    data class PasswordVisibilityChanged(val isPasswordVisible: Boolean): LoginFormEvent()
}