package com.douglasbruce.tasky.core.domain.formatter

import javax.inject.Inject

class NameFormatter @Inject constructor() {

    fun getInitials(fullName: String): String {
        // Split the full name into first and last name.
        val (firstName, lastName) = fullName.split(' ')

        // If the full name consists of one word, return the first 2 characters.
        if (lastName.isEmpty()) {
            return firstName.take(2)
        }

        // Otherwise, return the first letter of the first and last name.
        return firstName.take(1) + lastName.take(1)
    }
}