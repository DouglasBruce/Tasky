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
                return email.contains("@") && email.contains(".")
            }
        }
        emailValidator = EmailValidator(emailMatcher)
    }

    @Test
    fun `Invalid email missing @ and dot, returns false with format error`() {
        val email = "test"
        val result = emailValidator(email)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.FORMAT)
    }

    @Test
    fun `Invalid email missing @, returns false with format error`() {
        val email = "test.com"
        val result = emailValidator(email)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.FORMAT)
    }

    @Test
    fun `Invalid email missing dot, returns false with format error`() {
        val email = "test@tester"
        val result = emailValidator(email)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.FORMAT)
    }

    @Test
    fun `Valid email, returns true with no error`() {
        val email = "test@tester.com"
        val result = emailValidator(email)
        Truth.assertThat(result.successful).isTrue()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.NONE)
    }
}