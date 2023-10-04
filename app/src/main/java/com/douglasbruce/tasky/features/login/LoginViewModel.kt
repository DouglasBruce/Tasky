package com.douglasbruce.tasky.features.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.domain.repository.AuthRepository
import com.douglasbruce.tasky.core.domain.validation.EmailValidator
import com.douglasbruce.tasky.core.domain.validation.ErrorType
import com.douglasbruce.tasky.features.login.form.LoginFormEvent
import com.douglasbruce.tasky.features.login.form.LoginFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val emailValidator: EmailValidator,
) : ViewModel() {

    var state by savedStateHandle.saveable {
        mutableStateOf(LoginFormState())
    }
        private set

    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EmailValueChanged -> {
                setAndValidateEmail(event.email)
            }

            is LoginFormEvent.PasswordValueChanged -> {
                setAndValidatePassword(event.password)
            }

            is LoginFormEvent.TogglePasswordVisibility -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }

            is LoginFormEvent.Submit -> {
                if (state.isEmailValid && state.isPasswordValid) {
                    login(state.email, state.password)
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password)
        }
    }

    private fun setAndValidateEmail(email: String) {
        val result = emailValidator.validate(email)
        state = state.copy(
            email = email,
            emailErrorType = result.errorType,
            isEmailValid = result.successful
        )
    }

    private fun setAndValidatePassword(password: String) {
        val isPasswordNotBlank = password.isNotBlank()
        state = state.copy(
            password = password,
            passwordErrorType = if (isPasswordNotBlank) ErrorType.NONE else ErrorType.EMPTY,
            isPasswordValid = isPasswordNotBlank
        )
    }
}