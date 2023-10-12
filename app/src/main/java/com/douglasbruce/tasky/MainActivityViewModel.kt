package com.douglasbruce.tasky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.domain.auth.SessionManager
import com.douglasbruce.tasky.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoading.value = true
            when (authRepository.authenticate()) {
                is AuthResult.Success -> {
                    _isAuthenticated.value = true
                }

                is AuthResult.Error -> {
                    _isAuthenticated.value = true
                }

                is AuthResult.Unauthorized -> {
                    _isAuthenticated.value = false
                }
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.logout()
        }
    }
}