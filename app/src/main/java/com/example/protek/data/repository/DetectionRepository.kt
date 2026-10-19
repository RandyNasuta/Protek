package com.example.protek.data.repository

import android.net.Uri
import com.example.protek.data.remote.ProtekApi
import com.example.protek.data.remote.dto.DetectionData
import com.example.protek.data.remote.dto.TextDetectionRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
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

    suspend fun detectPhishingImage(imagePart: MultipartBody.Part): DetectionData {
        val response = apiService.detectionPhisingImage(imagePart)

        if (response.code == 200 && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message)
        }
    }
}