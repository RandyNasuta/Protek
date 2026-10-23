package com.example.protek.ui.detection


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.protek.data.remote.dto.DetectionData
import com.example.protek.data.repository.DetectionRepository
import com.example.protek.utils.prepareImageFilePart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
@HiltViewModel
class DetectionViewModel @Inject constructor(
    private val repository: DetectionRepository
) : ViewModel() {
    private val TAG: String = "DetectionViewModel"
    private val _uiState = MutableStateFlow<UiState<Any>>(UiState.Idle)
    val uiState: StateFlow<UiState<Any>> = _uiState.asStateFlow()

    fun detectPhishingText(text: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                val result = repository.detectPhishingText(text)
                Log.i(TAG, "detectPhishingText: Hasil scan adalah: ${result.message}")
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

    fun detectPhishingImage(context: Context, selectedFileUri: Uri) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                val imagePart = prepareImageFilePart(context, selectedFileUri)

                if (imagePart != null) {
                    val result = repository.detectPhishingImage(imagePart)
                    _uiState.value = UiState.Success(result)
                } else {
                    _uiState.value = UiState.Error("Gagal membaca atau memproses file gambar dari perangkat.")
                }
            } catch (e: HttpException) {
                _uiState.value = UiState.Error("Terjadi kesalahan server: ${e.message()}")
            } catch (e: IOException) {
                _uiState.value = UiState.Error("Tidak ada koneksi internet. Periksa jaringan Anda. [${e.message}]")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Terjadi kesalahan saat mengunggah gambar")
            }
        }
    }

    fun detectFakeNewsUrl(url: String, deepCheck: Boolean) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                val result = repository.detectFakeNewsUrl(url, deepCheck)
                _uiState.value = UiState.Success(result)
            } catch (e: HttpException) {
                _uiState.value = UiState.Error("Terjadi kesalahan server: ${e.message()}")
                Log.e(TAG, "detectFakeNewsUrl: ${e.message()}", e)
            } catch (e: IOException) {
                _uiState.value = UiState.Error("Tidak ada koneksi internet. Periksa jaringan Anda.")
                Log.e(TAG, "detectFakeNewsUrl: ${e.message}", e)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Terjadi kesalahan yang tidak diketahui")
                Log.e(TAG, "detectFakeNewsUrl: ${e.message}", e)
            }
        }
    }

    fun detectFakeNewsImage(context: Context, selectedFileUri: Uri, deepCheck: Boolean) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                val imagePart = prepareImageFilePart(context, selectedFileUri)

                if (imagePart != null) {
                    val result = repository.detectFakeNewsImage(imagePart, deepCheck)
                    _uiState.value = UiState.Success(result)
                } else {
                    _uiState.value = UiState.Error("Gagal membaca atau memproses file gambar dari perangkat.")
                }
            } catch (e: HttpException) {
                _uiState.value = UiState.Error("Terjadi kesalahan server: ${e.message()}")
                Log.e(TAG, "detectFakeNewsImage: ${e.message()}", e)
            } catch (e: IOException) {
                _uiState.value = UiState.Error("Tidak ada koneksi internet. Periksa jaringan Anda.")
                Log.e(TAG, "detectFakeNewsImage: ${e.message}", e)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Terjadi kesalahan saat mengunggah gambar")
                Log.e(TAG, "detectFakeNewsImage: ${e.message}", e)
            }
        }
    }
}