package com.example.protek.ui.detection


import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.protek.data.remote.dto.DetectionData
import com.example.protek.data.repository.DetectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val data: DetectionData) : UiState()
    data class Error(val message: String) : UiState()
}

class DetectionViewModel @Inject constructor(
    private val repository: DetectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun detectPhishingText(text: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                val result = repository.detectPhishingText(text)
                _uiState.value = UiState.Success(result)
            } catch (e: HttpException) {
                _uiState.value = UiState.Error("Terjadi kesalahan server: ${e.message()}")
            } catch (e: IOException) {
                _uiState.value = UiState.Error("Tidak ada koneksi internet. Periksa jaringan Anda.")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Terjadi kesalahan yang tidak diketahui")
            }
        }
    }
}