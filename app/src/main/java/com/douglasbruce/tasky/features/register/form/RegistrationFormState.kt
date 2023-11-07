package com.douglasbruce.tasky.features.register.form

import android.os.Parcelable
import com.douglasbruce.tasky.core.domain.validation.ErrorType
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegistrationFormState(
    val name: String = "",
    val nameErrorType: ErrorType = ErrorType.NONE,
    val isNameValid: Boolean = false,
    val email: String = "",
    val emailErrorType: ErrorType = ErrorType.NONE,
    val isEmailValid: Boolean = false,
    val password: String = "",
    val passwordErrorType: ErrorType = ErrorType.NONE,
    val isPasswordValid: Boolean = false,
    val isPasswordVisible: Boolean = false,
) : Parcelable