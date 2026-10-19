package com.example.protek.data.remote

import com.example.protek.data.remote.dto.BaseResponse
import com.example.protek.data.remote.dto.DetectionData
import com.example.protek.data.remote.dto.TextDetectionRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProtekApi {
    @POST("/api/detect/phishing/text")
    suspend fun detectionPhisingText(
        @Body request: TextDetectionRequest
    ): BaseResponse<DetectionData>

    @Multipart
    @POST("/api/detect/phishing/image")
    suspend fun detectionPhisingImage(
        @Part file: MultipartBody.Part
    ): BaseResponse<DetectionData>
}