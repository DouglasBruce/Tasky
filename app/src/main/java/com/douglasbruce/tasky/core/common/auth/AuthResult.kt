package com.douglasbruce.tasky.core.common.auth

import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.utils.UiText

sealed class AuthResult<T>(
    val isAuthorized: Boolean,
    val data: T? = null,
    val message: UiText? = null
) {

    class Success<T>(data: T, message: UiText? = null): AuthResult<T>(
        isAuthorized = true,
        data = data,
        message = message
    )

    class Unauthorized<T>: AuthResult<T>(
        isAuthorized = false,
        message = UiText.StringResource(R.string.unauthorized)
    )

    class Error<T>(
        message: UiText,
        data: T? = null
    ): AuthResult<T>(
        isAuthorized = true,
        data = data,
        message = message
    )
}