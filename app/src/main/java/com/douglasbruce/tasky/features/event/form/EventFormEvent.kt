package com.douglasbruce.tasky.features.event.form

sealed class EventFormEvent {
    data object ToggleEditMode : EventFormEvent()
    data class OnEditorSave(val title: String, val description: String) : EventFormEvent()
}