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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
     * Mandatory update check, called on every launch/resume from the UI.
     *
     * The first call in a process always probes the release feed; later calls are throttled to one
     * probe every [AUTO_CHECK_INTERVAL_MS] so the app does not hammer the GitHub API. The user's
     * "auto-check" preference is intentionally ignored: checking for a newer build is required.
     */
    fun autoCheckForAppUpdate() {
        if (isUiTestRun) return
        viewModelScope.launch {
            // If a newer build is already known, drive it forward on resume (e.g. after the user
            // granted the install permission) instead of spending a network probe on it.
            if (continuePendingUpdate()) return@launch

            val firstCheckThisProcess = !hasCheckedThisProcess
            if (!firstCheckThisProcess) {
                val lastCheck = settingsDataStore.lastUpdateCheckAt()
                if (System.currentTimeMillis() - lastCheck < AUTO_CHECK_INTERVAL_MS) return@launch
            }
            hasCheckedThisProcess = true
            runUpdateCheck(manual = false)
        }
    }

    /**
     * Advances an update that is already in hand: starts the download when it is available and
     * hands a finished APK to the installer. Returns true when it took over the resume.
     */
    private fun continuePendingUpdate(): Boolean {
        val state = _appUpdate.value
        return when (state) {
            is AppUpdateState.Available -> {
                _updatePromptVisible.value = true
                if (state.info.hasApk) startAutoUpdate()
                true
            }

            is AppUpdateState.Ready -> {
                _updatePromptVisible.value = true
                // Re-open the system installer if the previous hand-off was interrupted.
                downloadAndInstallUpdate()
                true
            }

            is AppUpdateState.Downloading -> {
                _updatePromptVisible.value = true
                true
            }

            else -> false
        }
    }

    /** Explicit check from the Settings card: always shows its result, even if dismissed before. */
    fun checkForAppUpdate() {
        viewModelScope.launch { runUpdateCheck(manual = true) }
    }

    /** Entry point of the Check Updates screen: refresh quietly, then auto-start the update. */
    fun refreshAppUpdate() {
        if (isUiTestRun) return
        viewModelScope.launch { runUpdateCheck(manual = true) }
    }

    private suspend fun runUpdateCheck(manual: Boolean) {
        val current = _appUpdate.value
        // Never let a background check interrupt an in-flight download or an install hand-off.
        if (current is AppUpdateState.Downloading || current is AppUpdateState.Ready) return
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
        when (state) {
            is AppUpdateState.Available -> {
                // Mandatory flow: surface the gate and start the download immediately. There is no
                // dismiss path, so a user who opened the app always sees the update through.
                _updatePromptVisible.value = true
                if (state.info.hasApk) {
                    startAutoUpdate()
                } else {
                    _updateNotice.value =
                        "This release has no APK attached. Open the release to install manually."
                }
            }

            is AppUpdateState.Failed -> {
                // A failed attempt must keep the mandatory retry surface alive on screen.
                _updatePromptVisible.value = true
            }

            else -> Unit
        }

        if (manual) {
            _updateNotice.value = when (state) {
                is AppUpdateState.UpToDate ->
                    "You are on the newest build (v${appUpdater.installedVersionName})."

                is AppUpdateState.Failed -> state.message
                else -> _updateNotice.value
            }
        }
    }

    /** True after the first mandatory release probe of this process. */
    private var hasCheckedThisProcess = false

    /** Only auto-open the OS "install unknown apps" screen once per process. */
    private var installPermissionPrompted = false

    private var updateWatchdogJob: Job? = null

    /**
     * Foreground watchdog: while the app is open it periodically asks the release feed for a newer
     * build, so a release published mid-session surfaces without any user action.
     */
    fun startUpdateWatchdog(intervalMs: Long = UPDATE_WATCH_INTERVAL_MS) {
        stopUpdateWatchdog()
        updateWatchdogJob = viewModelScope.launch {
            while (true) {
                delay(intervalMs)
                if (continuePendingUpdate()) continue
                // Same throttle as the launch/resume check: at most one release probe every
                // [AUTO_CHECK_INTERVAL_MS] instead of hammering the GitHub API.
                val lastCheck = settingsDataStore.lastUpdateCheckAt()
                if (System.currentTimeMillis() - lastCheck < AUTO_CHECK_INTERVAL_MS) continue
                runUpdateCheck(manual = false)
            }
        }
    }

    fun stopUpdateWatchdog() {
        updateWatchdogJob?.cancel()
        updateWatchdogJob = null
    }

    /**
     * Keeps the mandatory update popup on screen on every resume, whichever screen the user is on.
     * There is no dismissed state: a pending update can only be cleared by installing it.
     */
    fun reassertUpdatePrompt() {
        val state = _appUpdate.value
        val pending = state is AppUpdateState.Available ||
            state is AppUpdateState.Downloading ||
            state is AppUpdateState.Ready ||
            (state is AppUpdateState.Failed && state.info != null)
        if (pending) _updatePromptVisible.value = true
    }

    /**
     * Auto-drive entry point: starts the mandatory download, but only deep-links into the OS
     * "install unknown apps" screen once per process so a denied permission does not trap the user
     * in a Settings loop. A manual tap on the dialog always re-opens the toggle.
     */
    private fun startAutoUpdate() {
        val state = _appUpdate.value
        if (state is AppUpdateState.Available && !appUpdater.canInstallPackages()) {
            _updateNotice.value =
                "Allow \"Install unknown apps\" for Naihati Nights to finish this required update."
            if (!installPermissionPrompted) {
                installPermissionPrompted = true
                appUpdater.openInstallPermissionSettings()
            }
            return
        }
        downloadAndInstallUpdate()
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
                    "Could not open the installer. Open the release to install manually."
            }
            return
        }

        val info = state.releaseInfo ?: return
        if (!info.hasApk) {
            _updateNotice.value =
                "This release has no APK attached. Opening it instead."
            appUpdater.openReleasesPage(info.releaseUrl)
            return
        }

        if (!appUpdater.canInstallPackages()) {
            _updateNotice.value =
                "Allow \"Install unknown apps\" for Naihati Nights to finish this required update."
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
        /** Mandatory probe cadence while the app is used: fast enough to catch a fresh release. */
        const val AUTO_CHECK_INTERVAL_MS = 5L * 60L * 1000L

        /** Foreground poll while the app is open: how often to ask the release feed for updates. */
        const val UPDATE_WATCH_INTERVAL_MS = 5L * 60L * 1000L
    }
}
