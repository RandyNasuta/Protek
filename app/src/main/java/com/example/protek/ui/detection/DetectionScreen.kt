package com.example.protek.ui.detection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.protek.utils.DetectionCategory

@Composable
fun DetectionScreen(
    modifier: Modifier = Modifier,
    viewModel: DetectionViewModel = hiltViewModel(),
    detectionType: DetectionCategory
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetectionContent(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        detectionType = detectionType,
        onDetectClick = { inputText ->
            viewModel.detectPhishingText(inputText)
        }
    )
}

@Composable
fun DetectionContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    detectionType: DetectionCategory,
    onDetectClick: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var menuDropdown by remember { mutableStateOf("Text") }
    var isInputText by remember { mutableStateOf(true) }
    var userInputText by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0D0D1E), Color(0xFF1A1A3A), Color(0xFF2A2A4A)
                        )
                    )
                ),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    OutlinedTextField(
                        value = menuDropdown,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFA29BFE).copy(alpha = 0.05f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.03f),
                            focusedBorderColor = Color(0xFFA29BFE),
                            unfocusedBorderColor = Color(0xFF4A4A6A),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(alpha = 0.7f),
                            focusedTrailingIconColor = Color(0xFFA29BFE),
                            unfocusedTrailingIconColor = Color(0xFF8F8FAD)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = true
                            )
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color(0xFF1C2541))
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Text",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            },
                            onClick = {
                                menuDropdown = "Text"
                                isInputText = true
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Gambar",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            },
                            onClick = {
                                menuDropdown = "Gambar"
                                isInputText = false
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isInputText) {
                    OutlinedTextField(
                        value = userInputText,
                        onValueChange = {
                            userInputText = it
                        },
                        label = {
                            Text(
                                "Masukkan teks yang ingin dideteksi",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFA29BFE).copy(alpha = 0.05f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.03f),
                            focusedBorderColor = Color(0xFFA29BFE),
                            unfocusedBorderColor = Color(0xFF4A4A6A),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(alpha = 0.7f),
                            focusedTrailingIconColor = Color(0xFFA29BFE),
                            unfocusedTrailingIconColor = Color(0xFF8F8FAD)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onDetectClick(userInputText) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Deteksi Sekarang")
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (val state = uiState) {
                    is UiState.Idle -> {

                    }

                    is UiState.Loading -> {
                        CircularProgressIndicator(color = Color(0xFFA29BFE))
                    }

                    is UiState.Success -> {
                        val data = state.data

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFA29BFE).copy(alpha = 0.15f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color(0xFFA29BFE).copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Hasil Analisis",
                                    color = Color(0xFFA29BFE),
                                    fontSize = 18.sp
                                )

                                val statusPalsu = if (data.isPhishing) "Iya" else "Tidak"
                                Text(text = "Indikasi Palsu: $statusPalsu", color = Color.White)

                                val akurasiFormat =
                                    String.format(java.util.Locale.US, "%.2f", data.confidence)
                                Text(text = "Tingkat Akurasi: $akurasiFormat%", color = Color.White)

                                Text(text = "Pesan: ${data.message}", color = Color.White)

                                Text(
                                    text = "Rekomendasi: ${data.recommendation}",
                                    color = Color.White
                                )
                            }
                        }
                    }

                    is UiState.Error -> {
                        Text(
                            text = "Error: ${state.message}",
                            color = Color(0xFFFF6B6B),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetectiContentPreview() {
    DetectionContent(
        modifier = Modifier.fillMaxSize(),
        uiState = UiState.Idle,
        detectionType = DetectionCategory.SCAM_MESSAGE,
        onDetectClick = {}
    )
}