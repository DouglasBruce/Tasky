package com.douglasbruce.tasky.core.model

import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.utils.UiText

enum class VisitorFilterType(val text: UiText) {
    ALL(UiText.StringResource(R.string.all)),
    GOING(UiText.StringResource(R.string.going)),
    NOT_GOING(UiText.StringResource(R.string.not_going));
}