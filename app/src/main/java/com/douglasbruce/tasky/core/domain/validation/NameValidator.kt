package com.douglasbruce.tasky.core.domain.validation

import javax.inject.Inject

class NameValidator @Inject constructor() {

    operator fun invoke(name: String): ValidationResult {
        if (name.length in 4..50) {
            return ValidationResult(
                successful = true
            )
        }

        return ValidationResult(
            successful = false,
            errorType = ErrorType.LENGTH
        )
    }
}