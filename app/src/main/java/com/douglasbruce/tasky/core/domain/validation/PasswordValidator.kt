package com.douglasbruce.tasky.core.domain.validation

import javax.inject.Inject

class PasswordValidator @Inject constructor() {

    private val passwordRegex = Regex("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*\$")
    operator fun invoke(password: String): ValidationResult {
        if(password.length < 9) {
            return ValidationResult(
                successful = false,
                errorType = ErrorType.LENGTH
            )
        }

        if(!password.matches(passwordRegex)) {
            return ValidationResult(
                successful = false,
                errorType = ErrorType.FORMAT
            )
        }

        return ValidationResult(
            successful = true,
        )
    }
}