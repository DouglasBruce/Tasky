package com.douglasbruce.tasky.core.domain.validation

import javax.inject.Inject

class NameValidator @Inject constructor() {

    operator fun invoke(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(
                successful = false,
                errorType = ErrorType.EMPTY,
            )
        }

        if (name.length !in 4..50) {
            return ValidationResult(
                successful = false,
                errorType = ErrorType.LENGTH,
            )
        }

        return ValidationResult(
            successful = true,
        )
    }
}