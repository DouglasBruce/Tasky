package com.douglasbruce.tasky.core.domain.validation

import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

internal class PasswordValidatorTests {
    private lateinit var passwordValidator: PasswordValidator

    @Before
    fun setUp() {
        passwordValidator = PasswordValidator()
    }

    @Test
    fun `Invalid password, empty returns false with empty error`() {
        val name = ""
        val result = passwordValidator.validate(name)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.EMPTY)
    }

    @Test
    fun `Invalid password, blank returns false with empty error`() {
        val name = "    "
        val result = passwordValidator.validate(name)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.EMPTY)
    }

    @Test
    fun `Invalid password, too short returns false with length error`() {
        val password = "abc"
        val result = passwordValidator.validate(password)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.LENGTH)
    }

    @Test
    fun `Invalid password, no digits returns false with length error`() {
        val password = "abcABCabcABC"
        val result = passwordValidator.validate(password)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.FORMAT)
    }

    @Test
    fun `Invalid password, no uppercase letters returns false with length error`() {
        val password = "abc123abc123"
        val result = passwordValidator.validate(password)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.FORMAT)
    }

    @Test
    fun `Invalid password, no lowercase letters returns false with length error`() {
        val password = "ABC123ABC123"
        val result = passwordValidator.validate(password)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.FORMAT)
    }

    @Test
    fun `Valid password, returns true with no error`() {
        val password = "abcABC123"
        val result = passwordValidator.validate(password)
        Truth.assertThat(result.successful).isTrue()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.NONE)
    }
}