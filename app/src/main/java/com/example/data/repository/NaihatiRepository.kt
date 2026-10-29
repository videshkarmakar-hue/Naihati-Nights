package com.example.data.repository

import android.content.Context
import com.example.data.NaihatiDataProvider
import com.example.data.local.Converters
import com.example.data.local.FeedbackEntity
import com.example.data.local.NaihatiDao
import com.example.data.local.toEntity
import com.example.data.local.toModel
import com.example.data.remote.FeedbackSubmission
import com.example.data.remote.NaihatiApi
import com.example.model.AppContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NaihatiRepository(
    private val dao: NaihatiDao,
    private val api: NaihatiApi?,
    private val context: Context
) {
    private val converters = Converters()

    val content: Flow<AppContent> = dao.observeContent()
        .map { entity ->
            (entity?.toModel(converters) ?: NaihatiDataProvider.content()).withBundledSections()
        }
        .catch {
            emit(NaihatiDataProvider.content().withBundledSections())
        }
        .flowOn(Dispatchers.IO)

    suspend fun syncContent(forceRefresh: Boolean = false): AppContent = withContext(Dispatchers.IO) {
        val cached = dao.getContent()?.toModel(converters)
        if (!forceRefresh && cached != null) {
            return@withContext cached.copy(source = "cached")
        }

        if (api != null) {
            try {
                val response = api.getContent()
                if (response.isSuccessful) {
                    val remote = response.body()
                    if (remote != null) {
                        val withMetadata = remote.copy(
                            updatedAt = System.currentTimeMillis(),
                            source = "remote"
                        ).withBundledSections()
                        dao.upsertContent(withMetadata.toEntity())
                        return@withContext withMetadata
                    }
                }
            } catch (_: Exception) {
                // Fall through to cached/bundled
            }
        }

        val fallback = cached ?: NaihatiDataProvider.content()
        if (cached == null) {
            dao.upsertContent(fallback.toEntity())
        }
        return@withContext fallback.copy(source = if (cached == null) "bundled" else "cached")
            .withBundledSections()
    }

    suspend fun ensureSeeded() = withContext(Dispatchers.IO) {
        val cached = dao.getContent()
        if (cached == null) {
            dao.upsertContent(NaihatiDataProvider.content().toEntity())
        }
    }

    /**
     * Persists a community report locally first, then attempts a best-effort remote sync.
     * A failed upload never loses the report; it is retried by [syncPendingFeedback].
     */
    suspend fun submitFeedback(entity: FeedbackEntity): Long = withContext(Dispatchers.IO) {
        val id = dao.insertFeedback(entity)
        val stored = entity.copy(id = id)
        if (stored.id > 0) {
            tryRemoteSubmit(stored)
        }
        id
    }

    suspend fun syncPendingFeedback() = withContext(Dispatchers.IO) {
        if (api == null) return@withContext
        dao.getUnsyncedFeedback().forEach { report ->
            tryRemoteSubmit(report)
        }
    }

    private suspend fun tryRemoteSubmit(entity: FeedbackEntity): Boolean {
        val remote = api ?: return false
        return try {
            val response = remote.submitFeedback(
                FeedbackSubmission(
                    inquiryType = entity.inquiryType,
                    zone = entity.zone,
                    relatedLandmark = entity.relatedLandmark,
                    reporterName = entity.reporterName,
                    contactNumber = entity.contactNumber,
                    incidentDetails = entity.incidentDetails,
                    urgencyLevel = entity.urgencyLevel,
                    hasAttachedPhoto = entity.hasAttachedPhoto,
                    createdAt = entity.createdAt
                )
            )
            if (response.isSuccessful) {
                dao.markFeedbackSynced(entity.id)
                true
            } else {
                false
            }
        } catch (_: Exception) {
            false
        }
    }
}

/**
 * Keeps newly added bundled sections available even when an older remote payload or
 * a pre-existing Room cache predates them.
 */
private fun AppContent.withBundledSections(): AppContent {
    val hydratedRoutes = when {
        routes.isEmpty() -> NaihatiDataProvider.routes
        else -> routes.map { cached ->
            if (cached.stopGroups.isEmpty()) {
                NaihatiDataProvider.routes.firstOrNull { it.id == cached.id } ?: cached
            } else {
                cached
            }
        }
    }
    val hydratedRestaurants =
        if (restaurants.isEmpty()) NaihatiDataProvider.restaurants else restaurants

    val bundledHelplinesById = NaihatiDataProvider.helplines.associateBy { it.id }
    val hydratedHelplines = if (helplines.isEmpty()) {
        NaihatiDataProvider.helplines
    } else {
        val bundledOrder = NaihatiDataProvider.helplines.map { bundled ->
            helplines.firstOrNull { it.id == bundled.id } ?: bundled
        }
        val extras = helplines.filterNot { it.id in bundledHelplinesById }
        bundledOrder + extras
    }
    val hydratedBooths =
        if (policeBooths.isEmpty()) NaihatiDataProvider.policeBooths else policeBooths
    val hydratedGuidelines =
        if (safetyGuidelines.isEmpty()) NaihatiDataProvider.safetyGuidelines else safetyGuidelines

    return copy(
        routes = hydratedRoutes,
        restaurants = hydratedRestaurants,
        helplines = hydratedHelplines,
        policeBooths = hydratedBooths,
        safetyGuidelines = hydratedGuidelines
    )
}
