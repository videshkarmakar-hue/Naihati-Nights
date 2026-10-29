package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.FeedbackEntity
import com.example.data.local.NaihatiDatabase
import com.example.data.local.SettingsDataStore
import com.example.data.repository.NaihatiRepository
import com.example.di.ServiceLocator
import com.example.model.AppContent
import com.example.model.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Outcome of a manual "Check for Updates" request from the Settings screen.
 *
 * The guide is local-first, so the honest result is whether the live endpoint answered or
 * whether the app is still serving the cached/bundled copy.
 */
sealed interface UpdateCheckState {
    data object Idle : UpdateCheckState
    data object Checking : UpdateCheckState
    data class Synced(val checkedAtMillis: Long) : UpdateCheckState
    data class Offline(val checkedAtMillis: Long) : UpdateCheckState
}

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NaihatiRepository = ServiceLocator.getRepository(application)
    private val settingsDataStore: SettingsDataStore = ServiceLocator.getSettingsDataStore(application)
    private val dao = NaihatiDatabase.getInstance(application).dao()

    val content: StateFlow<AppContent> = repository.content
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = com.example.data.NaihatiDataProvider.content()
        )

    val settings: StateFlow<AppSettings> = settingsDataStore.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    val onboardingComplete: StateFlow<Boolean> = settingsDataStore.onboardingCompleteFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val feedbackHistory = dao.observeFeedback()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _updateCheck = MutableStateFlow<UpdateCheckState>(UpdateCheckState.Idle)
    val updateCheck: StateFlow<UpdateCheckState> = _updateCheck.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            repository.syncContent(forceRefresh = false)
            repository.syncPendingFeedback()
        }
    }

    /**
     * Refreshes the live guide and publishes the outcome so Settings can show a truthful
     * status (synced vs. offline) instead of a canned confirmation.
     */
    fun checkForUpdates() {
        if (_updateCheck.value is UpdateCheckState.Checking) return
        viewModelScope.launch {
            _updateCheck.value = UpdateCheckState.Checking
            val checkedAt = System.currentTimeMillis()
            _updateCheck.value = try {
                val result = repository.syncContent(forceRefresh = true)
                if (result.source == "remote") {
                    UpdateCheckState.Synced(checkedAt)
                } else {
                    UpdateCheckState.Offline(checkedAt)
                }
            } catch (_: Exception) {
                UpdateCheckState.Offline(checkedAt)
            }
        }
    }

    fun updateSettings(settings: AppSettings) {
        viewModelScope.launch {
            settingsDataStore.updateSettings(settings)
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            settingsDataStore.resetSettings()
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            settingsDataStore.setOnboardingComplete(true)
        }
    }

    fun submitFeedback(
        inquiryType: String,
        zone: String,
        relatedLandmark: String,
        reporterName: String,
        contactNumber: String,
        incidentDetails: String,
        urgencyLevel: String,
        hasAttachedPhoto: Boolean
    ) {
        viewModelScope.launch {
            val entity = FeedbackEntity(
                inquiryType = inquiryType,
                zone = zone,
                relatedLandmark = relatedLandmark,
                reporterName = reporterName,
                contactNumber = contactNumber,
                incidentDetails = incidentDetails,
                urgencyLevel = urgencyLevel,
                hasAttachedPhoto = hasAttachedPhoto,
                createdAt = System.currentTimeMillis(),
                synced = false
            )
            repository.submitFeedback(entity)
        }
    }
}
