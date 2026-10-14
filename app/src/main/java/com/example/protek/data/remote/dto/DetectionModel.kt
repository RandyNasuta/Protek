package com.example.protek.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TextDetectionRequest(
    @SerializedName("text") val text: String
)

data class BaseResponse<T>(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T?
)

data class DetectionData(
    @SerializedName("type") val type: String,
    @SerializedName("is_phishing") val isPhishing: Boolean,
    @SerializedName("confidence") val confidence: Double,
    @SerializedName("severity_level") val severityLevel: String,
    @SerializedName("message") val message: String,
    @SerializedName("recommendation") val recommendation: String
)