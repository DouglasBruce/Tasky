package com.douglasbruce.tasky.features.editor.form

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditorState(
    val key: String,
    val value: String,
    val isTitleEditor: Boolean,
) : Parcelable