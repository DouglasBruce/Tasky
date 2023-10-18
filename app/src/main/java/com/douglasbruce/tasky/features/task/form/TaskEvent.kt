package com.douglasbruce.tasky.features.task.form

sealed class TaskEvent {
    data object ToggleEditMode : TaskEvent()
}