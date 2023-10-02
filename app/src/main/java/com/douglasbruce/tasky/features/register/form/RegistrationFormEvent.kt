package com.douglasbruce.tasky.features.register.form

sealed class RegistrationFormEvent {
    data class NameValueChanged(val name: String) : RegistrationFormEvent()
    data class EmailValueChanged(val email: String) : RegistrationFormEvent()
    data class PasswordValueChanged(val password: String) : RegistrationFormEvent()
    data object TogglePasswordVisibility : RegistrationFormEvent()
    data object Submit : RegistrationFormEvent()
}