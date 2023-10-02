package com.douglasbruce.tasky.features.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.domain.validation.EmailValidator
import com.douglasbruce.tasky.features.login.form.LoginFormEvent
import com.douglasbruce.tasky.features.login.form.LoginFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val emailValidator: EmailValidator,
) : ViewModel() {

    var state by savedStateHandle.saveable {
        mutableStateOf(LoginFormState())
    }
        private set

    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EmailValueChanged -> {
                val result = emailValidator(event.email)
                state = state.copy(
                    email = event.email,
                    isEmailValid = result.successful
                )
            }

            is LoginFormEvent.PasswordValueChanged -> {
                state = state.copy(password = event.password)
            }

            is LoginFormEvent.TogglePasswordVisibility -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }

            is LoginFormEvent.Submit -> {

            }
        }
    }
}