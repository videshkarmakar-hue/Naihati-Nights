package com.example.ui.viewmodel

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.FeedbackEntity
import com.example.data.local.NaihatiDatabase
import com.example.data.local.SettingsDataStore
import com.example.data.repository.NaihatiRepository
import com.example.data.update.AppUpdateState
import com.example.data.update.AppUpdater
import com.example.data.update.AppVersion
import com.example.data.update.ReleaseLookup
import com.example.data.update.releaseInfo
import com.example.di.ServiceLocator
import com.example.model.AppContent
import com.example.model.AppSettings
import kotlinx.coroutines.CancellationException
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
    private val appUpdater = AppUpdater(application)

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

    /** GitHub release channel state (app binary, not guide content). */
    private val _appUpdate = MutableStateFlow<AppUpdateState>(AppUpdateState.Idle)
    val appUpdate: StateFlow<AppUpdateState> = _appUpdate.asStateFlow()

    /** True while the "a new update is available" dialog should be on screen. */
    private val _updatePromptVisible = MutableStateFlow(false)
    val updatePromptVisible: StateFlow<Boolean> = _updatePromptVisible.asStateFlow()

    /** One-line hint shown inside the update dialog / update card (permission, install errors). */
    private val _updateNotice = MutableStateFlow<String?>(null)
    val updateNotice: StateFlow<String?> = _updateNotice.asStateFlow()

    val installedVersionName: String get() = appUpdater.installedVersionName
    val releasesPageUrl: String get() = appUpdater.releasesPageUrl

    /**
     * Robolectric runs the real [com.example.MainActivity] in unit tests; a live release check
     * there would pop the update dialog over the screen under test.
     */
    private val isUiTestRun: Boolean =
        Build.FINGERPRINT.contains("robolectric", ignoreCase = true)

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
     *
     * The same button now also asks GitHub whether a newer APK has been published.
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
        viewModelScope.launch { runUpdateCheck(manual = true) }
    }

    /**
     * Called once per launch from the UI. Throttled so the app talks to GitHub at most every
     * [AUTO_CHECK_INTERVAL_MS] instead of on every resume, and skipped when the user turned the
     * "Auto-check for updates" switch off.
     */
    fun autoCheckForAppUpdate() {
        if (isUiTestRun) return
        viewModelScope.launch {
            if (!settingsDataStore.isAutoUpdateCheckEnabled()) return@launch
            val lastCheck = settingsDataStore.lastUpdateCheckAt()
            if (System.currentTimeMillis() - lastCheck < AUTO_CHECK_INTERVAL_MS) return@launch
            runUpdateCheck(manual = false)
        }
    }

    /** Explicit check from the Settings card: always shows its result, even if dismissed before. */
    fun checkForAppUpdate() {
        viewModelScope.launch { runUpdateCheck(manual = true) }
    }

    /**
     * Entry point of the Check Updates screen. It refreshes quietly: the screen already renders the
     * full state and the Update button, so a remembered "Later" must not re-open the popup.
     */
    fun refreshAppUpdate() {
        if (isUiTestRun) return
        viewModelScope.launch { runUpdateCheck(manual = true, forcePrompt = false) }
    }

    private suspend fun runUpdateCheck(manual: Boolean, forcePrompt: Boolean = manual) {
        if (_appUpdate.value is AppUpdateState.Checking) return
        _updatePromptVisible.value = false
        _updateNotice.value = null
        _appUpdate.value = AppUpdateState.Checking

        val lookup = appUpdater.latestRelease()
        val checkedAt = System.currentTimeMillis()
        settingsDataStore.setLastUpdateCheckAt(checkedAt)

        _appUpdate.value = when (lookup) {
            is ReleaseLookup.Found -> {
                if (AppVersion.isNewer(lookup.info.versionName, appUpdater.installedVersionName)) {
                    AppUpdateState.Available(lookup.info)
                } else {
                    AppUpdateState.UpToDate(lookup.info.versionName, checkedAt)
                }
            }

            ReleaseLookup.NoRelease -> AppUpdateState.UpToDate(appUpdater.installedVersionName, checkedAt)
            is ReleaseLookup.Failed -> AppUpdateState.Failed(lookup.message)
        }

        val state = _appUpdate.value
        if (state is AppUpdateState.Available) {
            val dismissed = settingsDataStore.dismissedUpdateVersion()
            if (forcePrompt || !dismissed.equals(state.info.versionName, ignoreCase = true)) {
                _updatePromptVisible.value = true
            }
        } else if (manual) {
            _updateNotice.value = when (state) {
                is AppUpdateState.UpToDate ->
                    "You are on the newest build (v${appUpdater.installedVersionName})."

                is AppUpdateState.Failed -> state.message
                else -> null
            }
        }
    }

    /** "Later": hide the prompt and remember the version so launch checks stay quiet for it. */
    fun dismissUpdatePrompt() {
        _updatePromptVisible.value = false
        val version = _appUpdate.value.releaseInfo?.versionName ?: return
        viewModelScope.launch { settingsDataStore.setDismissedUpdateVersion(version) }
    }

    /** Clears a remembered "Later" so a manual check can prompt again. */
    fun forgetDismissedUpdate() {
        viewModelScope.launch { settingsDataStore.setDismissedUpdateVersion(null) }
    }

    /**
     * Downloads the release APK (progress is published to [appUpdate]) and hands it to the
     * system installer. Tapping again after a cancel resumes from a fresh download.
     */
    fun downloadAndInstallUpdate() {
        val state = _appUpdate.value
        if (state is AppUpdateState.Downloading) return

        if (state is AppUpdateState.Ready) {
            if (!appUpdater.installApk(state.apkFile)) {
                _updateNotice.value =
                    "Could not open the installer. Open the GitHub release to install manually."
            }
            return
        }

        val info = state.releaseInfo ?: return
        if (!info.hasApk) {
            _updateNotice.value =
                "This release has no APK attached. Opening the GitHub release page instead."
            appUpdater.openReleasesPage(info.releaseUrl)
            return
        }

        if (!appUpdater.canInstallPackages()) {
            _updateNotice.value =
                "Allow \"Install unknown apps\" for Naihati Nights, then tap Update now again."
            appUpdater.openInstallPermissionSettings()
            return
        }

        viewModelScope.launch {
            _updateNotice.value = null
            _appUpdate.value = AppUpdateState.Downloading(info, 0f, 0L, info.apkSizeBytes)
            try {
                var lastPercent = -1
                val apkFile = appUpdater.downloadApk(info) { downloaded, total ->
                    val progress = if (total > 0L) {
                        (downloaded.toDouble() / total.toDouble()).toFloat().coerceIn(0f, 1f)
                    } else {
                        0f
                    }
                    val percent = (progress * 100f).toInt()
                    if (percent != lastPercent) {
                        lastPercent = percent
                        _appUpdate.value =
                            AppUpdateState.Downloading(info, progress, downloaded, total)
                    }
                }
                _appUpdate.value = AppUpdateState.Ready(info, apkFile)
                if (!appUpdater.installApk(apkFile)) {
                    _updateNotice.value = "Download finished. Tap \"INSTALL UPDATE\" to continue."
                }
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (t: Throwable) {
                _appUpdate.value =
                    AppUpdateState.Failed(t.message ?: "The update download failed", info)
            }
        }
    }

    fun openReleasesPage() {
        val url = _appUpdate.value.releaseInfo?.releaseUrl ?: appUpdater.releasesPageUrl
        appUpdater.openReleasesPage(url)
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

    private companion object {
        /** Six hours: frequent enough to catch a release the same day, cheap against the API limit. */
        const val AUTO_CHECK_INTERVAL_MS = 6L * 60L * 60L * 1000L
    }
}
