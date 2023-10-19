package com.douglasbruce.tasky.features.editor

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.features.editor.form.EditorEvent
import com.douglasbruce.tasky.features.editor.form.EditorState
import com.douglasbruce.tasky.features.editor.navigation.EditorArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val editorArgs: EditorArgs = EditorArgs(savedStateHandle)

    var state by savedStateHandle.saveable {
        mutableStateOf(
            EditorState(
                key = editorArgs.editorKey,
                value = editorArgs.editorValue,
                isTitleEditor = editorArgs.editorType,
            )
        )
    }
        private set

    fun onEvent(event: EditorEvent) {
        when (event) {
            is EditorEvent.OnTextValueChanged -> {
                state = state.copy(value = event.value)
            }
        }
    }
}