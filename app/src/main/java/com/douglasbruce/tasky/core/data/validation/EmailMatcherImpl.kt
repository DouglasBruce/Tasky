package com.douglasbruce.tasky.core.data.validation

import android.util.Patterns
import com.douglasbruce.tasky.core.domain.validation.EmailMatcher
import javax.inject.Inject

class EmailMatcherImpl @Inject constructor(): EmailMatcher {
    override fun matches(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}