package com.example.protek.data.remote

import com.example.protek.data.remote.dto.BaseResponse
import com.example.protek.data.remote.dto.DetectionData
import com.example.protek.data.remote.dto.TextDetectionRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ProtekApi {
    @POST("/api/detect/phishing/text")
    suspend fun detectionPhisingText(
        @Body request: TextDetectionRequest
    ): BaseResponse<DetectionData>
}