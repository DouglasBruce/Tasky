package com.douglasbruce.tasky.core.common.auth

import com.douglasbruce.tasky.core.common.utils.UiText
import com.douglasbruce.tasky.core.domain.utils.JsonSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

fun HttpException.isUnauthorized(): Boolean {
    return code() == 401
}

suspend fun <T> HttpException.asAuthResult(serializer: JsonSerializer): AuthResult<T> {
    if (isUnauthorized()) {
        return AuthResult.Unauthorized()
    }
    return withContext(Dispatchers.IO) {
        val message = getErrorMessage(serializer)?.message
        AuthResult.Error(
            message = if (message != null) {
                UiText.DynamicString(message)
            } else UiText.UnknownError
        )
    }
}

suspend fun HttpException.getErrorMessage(serializer: JsonSerializer): ErrorMessage? {
    return withContext(Dispatchers.IO) {
        response()?.errorBody()?.bytes()?.decodeToString()?.let { body ->
            serializer.fromJson(body, ErrorMessage::class.java)
        }
    }
}