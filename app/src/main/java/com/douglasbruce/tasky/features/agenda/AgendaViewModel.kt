package com.douglasbruce.tasky.features.agenda

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.domain.datastore.UserDataPreferences
import com.douglasbruce.tasky.core.domain.formatter.NameFormatter
import com.douglasbruce.tasky.features.agenda.form.AgendaEvent
import com.douglasbruce.tasky.features.agenda.form.AgendaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class AgendaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userDataPreferences: UserDataPreferences,
    private val nameFormatter: NameFormatter,
) : ViewModel() {

    var state by savedStateHandle.saveable {
        mutableStateOf(AgendaState())
    }
        private set

    init {
        val fullName = runBlocking {
            userDataPreferences.userData.map { it.fullName }.first()
        }

        state = state.copy(initials = nameFormatter.getInitials(fullName))
    }

    fun onEvent(event: AgendaEvent) {
        when (event) {
            is AgendaEvent.ToggleShowCreateAgendaOptions -> {
                state = state.copy(showCreateAgendaOptions = !state.showCreateAgendaOptions)
            }
            is AgendaEvent.ToggleShowSelectedAgendaOptions -> {
                state = state.copy(showSelectedAgendaOptions = !state.showSelectedAgendaOptions)
            }
            is AgendaEvent.ToggleShowAccountOptions -> {
                state = state.copy(showAccountOptions = !state.showAccountOptions)
            }
        }
    }
}