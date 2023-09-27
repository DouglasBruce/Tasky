package com.douglasbruce.tasky.core.domain.validation

import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

internal class NameValidatorTests {
    private lateinit var nameValidator: NameValidator

    @Before
    fun setUp() {
        nameValidator = NameValidator()
    }

    @Test
    fun `Invalid name, too short returns false with length error`() {
        val name = "abc"
        val result = nameValidator(name)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.LENGTH)
    }

    @Test
    fun `Invalid name, too long returns false with length error`() {
        val name = "abc".repeat(17)
        val result = nameValidator(name)
        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.LENGTH)
    }

    @Test
    fun `Valid name, max length returns true with no error`() {
        val name = "ab".repeat(25)
        val result = nameValidator(name)
        Truth.assertThat(result.successful).isTrue()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.NONE)
    }

    @Test
    fun `Valid name, min length returns true with no error`() {
        val name = "abcd"
        val result = nameValidator(name)
        Truth.assertThat(result.successful).isTrue()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.NONE)
    }

    @Test
    fun `Valid name, returns true with no error`() {
        val name = "abcde".repeat(5)
        val result = nameValidator(name)
        Truth.assertThat(result.successful).isTrue()
        Truth.assertThat(result.errorType).isEquivalentAccordingToCompareTo(ErrorType.NONE)
    }
}