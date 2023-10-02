package com.douglasbruce.tasky.core.domain.validation

import javax.inject.Inject

class EmailValidator @Inject constructor(
    private val matcher: EmailMatcher
) {

    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorType = ErrorType.EMPTY
            )
        }

        if (!matcher.matches(email)) {
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