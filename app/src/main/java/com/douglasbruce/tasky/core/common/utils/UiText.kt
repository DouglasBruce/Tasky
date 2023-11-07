package com.douglasbruce.tasky.core.common.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.douglasbruce.tasky.R

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data object UnknownError : UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
            is UnknownError -> stringResource(R.string.something_went_wrong)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
            is UnknownError -> context.getString(R.string.something_went_wrong)
        }
    }
}