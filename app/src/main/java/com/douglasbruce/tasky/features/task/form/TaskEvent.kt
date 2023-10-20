package com.douglasbruce.tasky.features.task.form

sealed class TaskEvent {
    data object ToggleEditMode : TaskEvent()
    data class OnEditorSave(val title: String, val description: String) : TaskEvent()
    data class OnTimePickerClick(val show: Boolean) : TaskEvent()
    data class OnTimeSelected(val hour: Int, val minute: Int) : TaskEvent()
    data class OnDatePickerClick(val show: Boolean) : TaskEvent()
    data class OnDateSelected(val dateMillis: Long) : TaskEvent()
}