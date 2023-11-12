package com.douglasbruce.tasky.core.domain.validation

enum class ErrorType { NONE, LENGTH, FORMAT, EMPTY, DOES_NOT_EXIST }

data class ValidationResult(
    val successful: Boolean,
    val errorType: ErrorType = ErrorType.NONE,
)