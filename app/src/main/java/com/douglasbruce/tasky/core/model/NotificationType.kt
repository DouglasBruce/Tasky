package com.douglasbruce.tasky.core.model

import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.utils.UiText

enum class NotificationType(val text: UiText) {
    TEN_MINUTES(UiText.StringResource(R.string.ten_minutes_before)),
    THIRTY_MINUTES(UiText.StringResource(R.string.thirty_minutes_before)),
    ONE_HOUR(UiText.StringResource(R.string.one_hour_before)),
    SIX_HOURS(UiText.StringResource(R.string.six_hours_before)),
    ONE_DAY(UiText.StringResource(R.string.one_day_before))
}