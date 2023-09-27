package com.douglasbruce.tasky.features.login.form

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginFormState(
    val email: String = "",
    val isEmailValid: Boolean = false,
    val password: String = "",
    val isPasswordVisible: Boolean = false,
): Parcelable