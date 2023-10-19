package com.douglasbruce.tasky.features.editor.form

sealed class EditorEvent {
    data class OnTextValueChanged(val value: String) : EditorEvent()
}