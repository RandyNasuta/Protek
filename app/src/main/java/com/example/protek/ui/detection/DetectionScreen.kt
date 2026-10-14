package com.example.protek.ui.detection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.protek.utils.DetectionCategory

@Composable
fun DetectionScreen(
    modifier: Modifier = Modifier,
    detectionType: DetectionCategory,
    viewModel: DetectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val inputText by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        Button(onClick = { viewModel.detectPhishingText(inputText) }) {
            Text("Deteksi Sekarang")
        }

        when(val state = uiState) {
            is UiState.Idle -> {

            }
            is UiState.Loading -> {
                CircularProgressIndicator()
            }
            is UiState.Success -> {
                val data = state.data
                Text(text = "Hasil: ${data.severityLevel}")
                Text(text = "Rekomendasi: ${data.recommendation}")
            }
            is UiState.Error -> {
                Text(text = state.message, color = Color.Red)
            }
        }
    }
}