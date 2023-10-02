package com.douglasbruce.tasky.features.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.domain.validation.EmailValidator
import com.douglasbruce.tasky.core.domain.validation.NameValidator
import com.douglasbruce.tasky.core.domain.validation.PasswordValidator
import com.douglasbruce.tasky.features.register.form.RegistrationFormEvent
import com.douglasbruce.tasky.features.register.form.RegistrationFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class RegisterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val nameValidator: NameValidator,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) : ViewModel() {

    var state by savedStateHandle.saveable {
        mutableStateOf(RegistrationFormState())
    }
        private set

    fun onEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.NameValueChanged -> {
                val result = nameValidator(event.name)
                state = state.copy(
                    name = event.name,
                    nameErrorType = result.errorType,
                    isNameValid = result.successful,
                )
            }

            is RegistrationFormEvent.EmailValueChanged -> {
                val result = emailValidator(event.email)
                state = state.copy(
                    email = event.email,
                    emailErrorType = result.errorType,
                    isEmailValid = result.successful
                )
            }

            is RegistrationFormEvent.PasswordValueChanged -> {
                val result = passwordValidator(event.password)
                state = state.copy(
                    password = event.password,
                    passwordErrorType = result.errorType,
                    isPasswordValid = result.successful
                )
            }

            is RegistrationFormEvent.TogglePasswordVisibility -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }

            is RegistrationFormEvent.Submit -> {

            }
        }
    }
}