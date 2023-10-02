package com.douglasbruce.tasky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasbruce.tasky.MainActivityUiState.Loading
import com.douglasbruce.tasky.MainActivityUiState.Success
import com.douglasbruce.tasky.core.domain.repository.UserDataRepository
import com.douglasbruce.tasky.core.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData.map {
        Success(it, false)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserData, val isUserLoggedIn: Boolean) : MainActivityUiState
}