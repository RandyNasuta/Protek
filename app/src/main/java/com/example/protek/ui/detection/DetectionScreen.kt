package com.example.protek.ui.detection

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.protek.data.remote.dto.DetectionData
import com.example.protek.data.remote.dto.FakeNewsDetectionData
import com.example.protek.utils.DetectionCategory

@Composable
fun DetectionScreen(
    modifier: Modifier = Modifier,
    viewModel: DetectionViewModel = hiltViewModel(),
    detectionType: DetectionCategory
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    when (detectionType) {
        DetectionCategory.SCAM_MESSAGE -> {
            DetectionScamMessageContent(
                modifier = modifier.fillMaxSize(),
                uiState = uiState,
                onDetectClickText = { inputText ->
                    viewModel.detectPhishingText(inputText)
                },
                onDetectClickImage = { selectedFileUri ->
                    selectedFileUri.let { uri ->
                        viewModel.detectPhishingImage(context, uri)
                    }
                }
            )
        }

        DetectionCategory.FAKE_NEWS -> {
            DetectionFakeNewsContent(
                modifier = modifier.fillMaxSize(),
                uiState = uiState,
                onDetectClickText = { inputText, isDeepCheck ->
                    viewModel.detectFakeNewsUrl(inputText, isDeepCheck)
                },
                onDetectClickImage = { selectedFileUri, isDeepCheck ->
                    viewModel.detectFakeNewsImage(context, selectedFileUri, isDeepCheck)
                }
            )
        }

        DetectionCategory.DEEPFAKE -> {

        }

        else -> {
            Text(text = "Unsupported Detection Type")
        }
    }
}

@Composable
fun DetectionScamMessageContent(
    modifier: Modifier = Modifier,
    uiState: UiState<Any>,
    onDetectClickText: (String) -> Unit = {},
    onDetectClickImage: (Uri) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var menuDropdown by remember { mutableStateOf("Text") }
    var isInputText by remember { mutableStateOf(true) }
    var userInputText by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var isError by remember { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedFileUri = uri
            if (uri != null) {
                isError = false
            }
        }
    )

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
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
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
                        isError = isError,
                        supportingText = {
                            if (isError) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Field tidak boleh kosong",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
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
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 160.dp)
                            .clickable {
                                filePickerLauncher.launch("image/*")
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFA29BFE).copy(alpha = 0.15f)
                        ),
                        border = BorderStroke(
                            width = if (isError && selectedFileUri == null) 2.dp else 1.dp,
                            color = if (isError && selectedFileUri == null) {
                                Color(0xFFFF6B6B)
                            } else {
                                Color(0xFFA29BFE).copy(alpha = 0.5f)
                            }
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedFileUri != null) {
                                AsyncImage(
                                    model = selectedFileUri,
                                    contentDescription = "Preview Gambar Pilihan",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Column(
                                    modifier = Modifier.padding(all = 16.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Icon(
                                        contentDescription = "Upload Image",
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        tint = Color(0xFFA29BFE),
                                        modifier = Modifier.size(48.dp)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Upload Gambar",
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Cari file gambar di perangkat",
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    if (isError && selectedFileUri == null) {
                        Text(
                            text = "Gambar tidak boleh kosong",
                            color = Color(0xFFFF6B6B),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp, start = 16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        isError = false

                        if (isInputText && userInputText.isEmpty()) {
                            isError = true
                        } else if (!isInputText && selectedFileUri == null) {
                            isError = true
                        }

                        if (!isError) {
                            if (isInputText) {
                                onDetectClickText(userInputText)
                            } else {
                                onDetectClickImage(selectedFileUri!!)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Deteksi")
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (val state = uiState) {
                    is UiState.Idle -> {

                    }

                    is UiState.Loading -> {

                    }

                    is UiState.Success -> {
                        val data = state.data as DetectionData

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

                                val statusPalsu = if (data.isFraud) "Iya" else "Tidak"
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

            if (uiState is UiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .pointerInput(Unit) {
                            detectTapGestures { }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1C2541)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularWavyProgressIndicator(
                                color = Color(0xFFFFB703),
                                trackColor = Color.White.copy(alpha = 0.1f),
                                modifier = Modifier.size(48.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Sedang Menganalisis",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Mohon tunggu sebentar...",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetectionFakeNewsContent(
    modifier: Modifier = Modifier,
    uiState: UiState<Any>,
    onDetectClickText: (String, Boolean) -> Unit = { _, _ -> },
    onDetectClickImage: (Uri, Boolean) -> Unit = { _, _ -> }
) {
    var expanded by remember { mutableStateOf(false) }
    var menuDropdown by remember { mutableStateOf("Text") }
    var isInputText by remember { mutableStateOf(true) }
    var userInputText by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var isDeepCheckChecked by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedFileUri = uri
            if (uri != null) {
                isError = false
            }
        }
    )

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
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
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
                        isError = isError,
                        supportingText = {
                            if (isError) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Field tidak boleh kosong",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        label = {
                            Text(
                                "Masukkan url informasi yang ingin dideteksi",
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
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 160.dp)
                            .clickable {
                                filePickerLauncher.launch("image/*")
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFA29BFE).copy(alpha = 0.15f)
                        ),
                        border = BorderStroke(
                            width = if (isError && selectedFileUri == null) 2.dp else 1.dp,
                            color = if (isError && selectedFileUri == null) {
                                Color(0xFFFF6B6B)
                            } else {
                                Color(0xFFA29BFE).copy(alpha = 0.5f)
                            }
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedFileUri != null) {
                                AsyncImage(
                                    model = selectedFileUri,
                                    contentDescription = "Preview Gambar Pilihan",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Column(
                                    modifier = Modifier.padding(all = 16.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Icon(
                                        contentDescription = "Upload Image",
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        tint = Color(0xFFA29BFE),
                                        modifier = Modifier.size(48.dp)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Upload Gambar",
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Cari file gambar di perangkat",
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    if (isError && selectedFileUri == null) {
                        Text(
                            text = "Gambar tidak boleh kosong",
                            color = Color(0xFFFF6B6B),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp, start = 16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Deep Check",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Switch(
                        checked = isDeepCheckChecked, onCheckedChange = {
                            isDeepCheckChecked = !isDeepCheckChecked
                        }, colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF0B132B),
                            checkedTrackColor = Color(0xFFFFB703),

                            uncheckedThumbColor = Color(0xFFFFFFFF),
                            uncheckedTrackColor = Color(0xFFE0E0E0),
                            uncheckedBorderColor = Color(0xFFBDBDBD)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        isError = false

                        if (isInputText && userInputText.isEmpty()) {
                            isError = true
                        } else if (!isInputText && selectedFileUri == null) {
                            isError = true
                        }

                        if (!isError) {
                            if (isInputText) {
                                onDetectClickText(userInputText, isDeepCheckChecked)
                            } else {
                                onDetectClickImage(selectedFileUri!!, isDeepCheckChecked)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Deteksi")
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (val state = uiState) {
                    is UiState.Idle -> {

                    }

                    is UiState.Loading -> {
                    }

                    is UiState.Success -> {
                        val data = state.data as FakeNewsDetectionData

                        val (statusColor, statusText, statusIcon) = when (data.isFraud) {
                            true -> Triple(
                                Color(0xFFFF6B6B),
                                "Indikasi Hoaks / Salah",
                                Icons.Default.Warning
                            )

                            false -> Triple(
                                Color(0xFF4CAF50),
                                "Terkonfirmasi Fakta",
                                Icons.Default.CheckCircle
                            )

                            null -> Triple(
                                Color(0xFFFFB703),
                                "Perlu Verifikasi Tambahan",
                                Icons.Default.Info
                            )
                        }

                        val decisionBasisId = when (data.decisionBasis) {
                            "external_fact_check" -> "Pengecekan Fakta Eksternal (Deep Check)"
                            "external_inconclusive" -> "Pengecekan Eksternal (Belum Konklusif)"
                            "model_low_confidence" -> "Prediksi AI Model (Tingkat Keyakinan Rendah)"
                            "model" -> "Prediksi AI Model"
                            else -> data.decisionBasis
                        }

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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = statusIcon,
                                        contentDescription = "Status Ikon",
                                        tint = statusColor,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = statusText,
                                        color = statusColor,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "Keputusan berdasarkan: ${
                                        decisionBasisId.replace(
                                            "_",
                                            " "
                                        ).uppercase()
                                    }",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )

                                val akurasiFormat =
                                    String.format(java.util.Locale.US, "%.2f", data.confidence)
                                Text(
                                    text = "Tingkat Akurasi Model: $akurasiFormat%",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )

                                Text(text = data.message, color = Color.White, fontSize = 14.sp)
                                Text(
                                    text = "Rekomendasi: ${data.recommendation}",
                                    color = Color(0xFF90CAF9),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                val external = data.externalVerification

                                if (external.enabled && external.factcheckDecision?.evidence?.isNotEmpty() == true) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(
                                                0xFF1A1A3A
                                            )
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(
                                            1.dp,
                                            Color(0xFFFFB703).copy(alpha = 0.3f)
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = "Bukti Pengecekan Fakta Eksternal:",
                                                color = Color(0xFFFFB703),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))

                                            val topEvidence =
                                                external.factcheckDecision.evidence.first()
                                            Text(
                                                text = "Sumber: ${topEvidence.publisher ?: "Tidak diketahui"}",
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = topEvidence.title ?: topEvidence.claim ?: "",
                                                color = Color.White,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }

                                val newsRelated = external.publicSources?.sources?.newsComparisons
                                if (external.enabled && !newsRelated.isNullOrEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Berita Terkait (Pembanding):",
                                        color = Color(0xFFA29BFE),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    newsRelated.take(2).forEach { news ->
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(
                                                    0xFF1C2541
                                                )
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp)
                                                .clickable {
                                                    news.url?.let { urlString ->
                                                        try {
                                                            uriHandler.openUri(urlString)
                                                        } catch (e: Exception) {
                                                            Log.e(
                                                                "DetectionScreen",
                                                                "Gagal membuka URL berita: $urlString",
                                                                e
                                                            )
                                                            Toast.makeText(
                                                                context,
                                                                "Gagal membuka URL berita: $urlString",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                                }
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text(
                                                    text = (news.publisher
                                                        ?: "Media Berita").uppercase(),
                                                    color = Color.White.copy(alpha = 0.6f),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = news.title ?: "Tanpa Judul",
                                                    color = Color.White,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }
                                }
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
            if (uiState is UiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .pointerInput(Unit) {
                            detectTapGestures { }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1C2541)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularWavyProgressIndicator(
                                color = Color(0xFFFFB703),
                                trackColor = Color.White.copy(alpha = 0.1f),
                                modifier = Modifier.size(48.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Sedang Menganalisis",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Mohon tunggu sebentar...",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetectiContentPreview() {
    DetectionFakeNewsContent(
        modifier = Modifier.fillMaxSize(),
        uiState = UiState.Idle,
    )
}