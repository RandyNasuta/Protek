package com.example.protek.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FakeNewsDetectionData(
    @SerializedName("type") val type: String,
    @SerializedName("is_fraud") val isFraud: Boolean?,
    @SerializedName("confidence") val confidence: Double,
    @SerializedName("severity_level") val severityLevel: String,
    @SerializedName("message") val message: String,
    @SerializedName("recommendation") val recommendation: String,

    @SerializedName("decision_basis") val decisionBasis: String,
    @SerializedName("extracted_text_preview") val extractedTextPreview: String,
    @SerializedName("external_verification") val externalVerification: ExternalVerification
)

data class ExternalVerification(
    @SerializedName("enabled") val enabled: Boolean,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("factcheck_decision") val factcheckDecision: FactCheckDecision?,
    @SerializedName("public_sources") val publicSources: PublicSources?
)

data class PublicSources(
    @SerializedName("sources") val sources: SourcesCategory?
)

data class SourcesCategory(
    @SerializedName("news_comparisons") val newsComparisons: List<NewsItem>?
)

data class NewsItem(
    @SerializedName("title") val title: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("publisher") val publisher: String?
)

data class FactCheckDecision(
    @SerializedName("decision") val decision: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("evidence") val evidence: List<EvidenceItem>?
)

data class EvidenceItem(
    @SerializedName("url") val url: String?,
    @SerializedName("publisher") val publisher: String?,
    @SerializedName("claim") val claim: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("rating") val rating: String?
)