package com.example.protek.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class UiState(
    val title: String
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _uiState = MutableStateFlow(UiState(title = "Home Screen"))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(title = "Home Screen Init")
        }
    }
}