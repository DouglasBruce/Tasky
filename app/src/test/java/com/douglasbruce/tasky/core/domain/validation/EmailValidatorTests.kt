package com.douglasbruce.tasky.core.domain.validation

import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

internal class EmailValidatorTests {
    private lateinit var emailValidator: EmailValidator
    private lateinit var emailMatcher: EmailMatcher

    @Before
    fun setUp() {
        emailMatcher = object : EmailMatcher {
            override fun matches(email: String): Boolean {
                return true
            }
        }
        emailValidator = EmailValidator(emailMatcher)
    }

    @Test
    fun `Invalid email, empty returns false with empty error`() {
        val email = ""
        val result = emailValidator.validate(email)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.EMPTY)
    }

    @Test
    fun `Invalid email, blank returns false with empty error`() {
        val email = "    "
        val result = emailValidator.validate(email)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.EMPTY)
    }

    @Test
    fun `Valid email, returns true with no error`() {
        val email = "test@tester.com"
        val result = emailValidator.validate(email)
        Truth.assertThat(result.successful).isTrue()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.NONE)
    }
}