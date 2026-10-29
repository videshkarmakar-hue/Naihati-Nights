package com.example.data.remote

import com.example.model.AppContent
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Optional remote API for the Naihati Nights local-first content layer.
 *
 * The app never depends on these endpoints being available: bundled content and the Room
 * cache are always used as the source of truth when the network is unreachable.
 */
interface NaihatiApi {
    @GET("content")
    suspend fun getContent(): Response<AppContent>

    @GET("routes")
    suspend fun getRoutes(): Response<AppContent>

    @POST("feedback")
    suspend fun submitFeedback(@Body body: FeedbackSubmission): Response<FeedbackAck>
}

data class FeedbackSubmission(
    val inquiryType: String,
    val zone: String,
    val relatedLandmark: String,
    val reporterName: String,
    val contactNumber: String,
    val incidentDetails: String,
    val urgencyLevel: String,
    val hasAttachedPhoto: Boolean,
    val createdAt: Long
)

data class FeedbackAck(
    val reference: String? = null,
    val status: String? = null
)
