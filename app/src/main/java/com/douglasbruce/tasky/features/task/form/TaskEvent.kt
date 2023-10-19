package com.douglasbruce.tasky.features.task.form

sealed class TaskEvent {
    data object ToggleEditMode : TaskEvent()
    data class OnEditorSave(val title: String, val description: String) : TaskEvent()
}