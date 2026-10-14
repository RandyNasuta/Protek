package com.example.protek.data.repository

import com.example.protek.data.remote.ProtekApi
import com.example.protek.data.remote.dto.DetectionData
import com.example.protek.data.remote.dto.TextDetectionRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class DetectionRepository @Inject constructor(
    private val apiService: ProtekApi
) {
    suspend fun detectPhishingText(text: String): DetectionData {
        val request = TextDetectionRequest(text)
        val response = apiService.detectionPhisingText(request)

        if (response.code == 200 && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message)
        }
    }
}