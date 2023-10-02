package com.douglasbruce.tasky.core.domain.validation

import javax.inject.Inject

class PasswordValidator @Inject constructor() {

    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorType = ErrorType.EMPTY,
            )
        }

        if (password.length < 9) {
            return ValidationResult(
                successful = false,
                errorType = ErrorType.LENGTH,
            )
        }

        val hasUppercaseChar = password.any { it.isUpperCase() }
        val hasLowercaseChar = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        if (!hasUppercaseChar || !hasLowercaseChar || !hasDigit) {
            return ValidationResult(
                successful = false,
                errorType = ErrorType.FORMAT,
            )
        }

        return ValidationResult(
            successful = true,
        )
    }
}