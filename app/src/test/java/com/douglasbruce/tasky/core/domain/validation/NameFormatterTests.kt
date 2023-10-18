package com.douglasbruce.tasky.core.domain.validation

import com.douglasbruce.tasky.core.domain.formatter.NameFormatter
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

internal class NameFormatterTests {
    private lateinit var nameFormatter: NameFormatter

    @Before
    fun setUp() {
        nameFormatter = NameFormatter()
    }

    @Test
    fun `Empty name returns empty string`() {
        val name = ""
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("")
    }

    @Test
    fun `Blank name returns empty string`() {
        val name = " "
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("")
    }

    @Test
    fun `First name with trailing whitespace returns first two letters`() {
        val name = "First "
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("Fi")
    }

    @Test
    fun `First name with trailing tab returns first two letters`() {
        val name = "First\t"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("Fi")
    }

    @Test
    fun `First name with trailing newline returns first two letters`() {
        val name = "First\n"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("Fi")
    }

    @Test
    fun `First name with leading whitespace returns first two letters`() {
        val name = " First"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("Fi")
    }

    @Test
    fun `First name with leading tab returns first two letters`() {
        val name = "\tFirst"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("Fi")
    }

    @Test
    fun `First name with leading newline returns first two letters`() {
        val name = "\nFirst"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("Fi")
    }

    @Test
    fun `Valid first name returns first two letters`() {
        val name = "First"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("Fi")
    }

    @Test
    fun `Valid first and last name returns first letter of each name`() {
        val name = "First Last"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("FL")
    }

    @Test
    fun `Last name with trailing whitespace returns first letter of each name`() {
        val name = "First Last "
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("FL")
    }

    @Test
    fun `Last name with trailing tab returns first letter of each name`() {
        val name = "First Last\t"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("FL")
    }

    @Test
    fun `Last name with trailing newline returns first letter of each name`() {
        val name = "First Last\n"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("FL")
    }

    @Test
    fun `Multiple whitespaces between first and last names returns first letter of each name`() {
        val name = "First   Last"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("FL")
    }

    @Test
    fun `Tab between first and last names returns first letter of each name`() {
        val name = "First\tLast"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("FL")
    }

    @Test
    fun `Newline between first and last names returns first letter of each name`() {
        val name = "First\nLast"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("FL")
    }

    @Test
    fun `Valid first, middle and last name returns first letter of first and last name`() {
        val name = "First Middle Last"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("FL")
    }

    @Test
    fun `First, middle and last name with multiple whitespaces returns first letter of first and last name`() {
        val name = "First   Middle   Last"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("FL")
    }

    @Test
    fun `First, middle and last name with multiple tabs returns first letter of first and last name`() {
        val name = "First\tMiddle\tLast"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("FL")
    }

    @Test
    fun `First, middle and last name with multiple newlines returns first letter of first and last name`() {
        val name = "First\nMiddle\nLast"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("FL")
    }

    @Test
    fun `Multiple middles names returns first letter of first and last name`() {
        val name = "First Second Third Last"
        val result = nameFormatter.getInitials(name)
        Truth.assertThat(result).isEqualTo("FL")
    }
}