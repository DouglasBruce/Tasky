package com.douglasbruce.tasky.features.task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.features.task.form.TaskState
import com.douglasbruce.tasky.features.task.navigation.TaskArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val taskArgs: TaskArgs = TaskArgs(savedStateHandle)

    var state by savedStateHandle.saveable {
        mutableStateOf(TaskState(id = if(taskArgs.taskId == "-1") null else taskArgs.taskId))
    }
        private set
}