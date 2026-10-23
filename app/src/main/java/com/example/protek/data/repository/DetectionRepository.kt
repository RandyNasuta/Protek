package com.example.protek.data.repository

import com.example.protek.data.remote.ProtekApi
import com.example.protek.data.remote.dto.DetectionData
import com.example.protek.data.remote.dto.FakeNewsDetectionData
import com.example.protek.data.remote.dto.TextDetectionRequest
import com.example.protek.data.remote.dto.UrlDetectionRequest
import okhttp3.MultipartBody
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

    suspend fun detectFakeNewsUrl(url: String, deepCheck: Boolean): FakeNewsDetectionData {
        val request = UrlDetectionRequest(url, deepCheck)
        val response = apiService.detectionFakeNewsUrl(request)

        if (response.code == 200 && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message)
        }
    }

    suspend fun detectFakeNewsImage(imagePart: MultipartBody.Part, deepCheck: Boolean): FakeNewsDetectionData {
        val response = apiService.detectionFakeNewsImage(imagePart, deepCheck)

        if (response.code == 200 && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message)
        }
    }
}