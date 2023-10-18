package com.douglasbruce.tasky.core.domain.formatter

import javax.inject.Inject

class NameFormatter @Inject constructor() {

    fun getInitials(fullName: String): String {
        // Check if the full name is blank.
        if (fullName.isBlank()) {
            return ""
        }

        // Trim any leading and trailing whitespace characters from the full name.
        val trimmedFullName = fullName.trim()

        // Split the full name using a regular expression to split on any whitespace characters, including trailing whitespace characters.
        val splitNames = trimmedFullName.split("\\s+".toRegex())

        // If the full name consists of one word, return the first 2 characters.
        if (splitNames.size == 1) {
            return trimmedFullName.take(2)
        }

        // Otherwise, return the first letter of the first and last name.
        return splitNames.first().take(1) + splitNames.last().take(1)
    }
}