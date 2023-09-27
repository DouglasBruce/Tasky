package com.douglasbruce.tasky.core.domain.validation

interface EmailMatcher {
    fun matches(email: String): Boolean
}