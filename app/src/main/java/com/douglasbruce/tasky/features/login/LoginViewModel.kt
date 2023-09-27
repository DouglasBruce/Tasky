package com.douglasbruce.tasky.features.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.douglasbruce.tasky.core.domain.validation.EmailValidator
import com.douglasbruce.tasky.features.login.form.LoginFormEvent
import com.douglasbruce.tasky.features.login.form.LoginFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val loginStateKey = "loginState"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val emailValidator: EmailValidator,
) : ViewModel() {

    var state by mutableStateOf(
        savedStateHandle[loginStateKey] ?: LoginFormState()
    )
        private set

    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EmailValueChanged -> {
                val result = emailValidator(event.email)
                updateState(
                    state.copy(
                        email = event.email, isEmailValid = result.successful
                    )
                )
            }

            is LoginFormEvent.PasswordValueChanged -> {
                updateState(state.copy(password = event.password))
            }

            is LoginFormEvent.PasswordVisibilityChanged -> {
                updateState(state.copy(isPasswordVisible = event.isPasswordVisible))
            }
        }
    }

    private fun updateState(newState: LoginFormState) {
        state = newState
        savedStateHandle[loginStateKey] = newState
    }
}