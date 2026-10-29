package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feedback_reports")
data class FeedbackEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val inquiryType: String,
    val zone: String,
    val relatedLandmark: String,
    val reporterName: String,
    val contactNumber: String,
    val incidentDetails: String,
    val urgencyLevel: String,
    val hasAttachedPhoto: Boolean,
    val createdAt: Long,
    val synced: Boolean = false
)
