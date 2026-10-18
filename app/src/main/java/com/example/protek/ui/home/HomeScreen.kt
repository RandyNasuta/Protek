package com.example.protek.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.protek.ui.theme.ProtekTheme
import com.example.protek.utils.DetectionCategory


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (DetectionCategory) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeContent(
        modifier = modifier.fillMaxSize(),
        onNavigate = onNavigate
    )
}

@Composable
fun HomeContent(
    modifier: Modifier,
    onNavigate: (DetectionCategory) -> Unit
) {
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
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CardMenu(
                    title = "Deteksi Pesan Penipuan",
                    description = "Analisis pesan SMS atau chat mencurigakan.",
                    icon = Icons.Default.Email,
                    iconTint = Color(0xFF4FACFE),
                    onClick = {
                        onNavigate(DetectionCategory.SCAM_MESSAGE)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                CardMenu(
                    title = "Deteksi Berita Palsu",
                    description = "Analisis Berita atau Artikel yang mengandung Berita Palsu.",
                    icon = Icons.Default.Newspaper,
                    iconTint = Color(0xFF4FFE58),
                    onClick = {
                        onNavigate(DetectionCategory.FAKE_NEWS)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                CardMenu(
                    title = "Deteksi Deepfake",
                    description = "Analisis Gambar atau Video yang mengandung Deepfake.",
                    icon = Icons.Default.Image,
                    iconTint = Color(0xFFFE4F4F),
                    onClick = {
                        onNavigate(DetectionCategory.DEEPFAKE)
                    }
                )


            }
        }
    }
}

@Composable
fun CardMenu(
    modifier: Modifier = Modifier.fillMaxWidth(),
    title: String,
    description: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .padding(vertical = 8.dp)
            .heightIn(min = 96.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF232342).copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        color = iconTint.copy(alpha = 0.15f), shape = RoundedCornerShape(14.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    HomeContent(
        onNavigate = {},
        modifier = Modifier
    )
}