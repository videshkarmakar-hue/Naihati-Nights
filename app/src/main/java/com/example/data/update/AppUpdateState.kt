package com.example.data.update

import java.io.File

/**
 * Everything the UI needs to describe an in-app update.
 *
 * The flow is: [Idle] -> [Checking] -> [Available] -> [Downloading] -> [Ready]. [Failed] keeps the
 * release it was working on so the user can retry without running the network check again.
 */
sealed interface AppUpdateState {
    data object Idle : AppUpdateState

    data object Checking : AppUpdateState

    data class UpToDate(
        val latestVersion: String,
        val checkedAtMillis: Long
    ) : AppUpdateState

    data class Available(val info: ReleaseInfo) : AppUpdateState

    data class Downloading(
        val info: ReleaseInfo,
        val progress: Float,
        val downloadedBytes: Long,
        val totalBytes: Long
    ) : AppUpdateState

    data class Ready(val info: ReleaseInfo, val apkFile: File) : AppUpdateState

    data class Failed(
        val message: String,
        val info: ReleaseInfo? = null
    ) : AppUpdateState
}

/** The release behind the current state, if there is one. */
val AppUpdateState.releaseInfo: ReleaseInfo?
    get() = when (this) {
        is AppUpdateState.Available -> info
        is AppUpdateState.Downloading -> info
        is AppUpdateState.Ready -> info
        is AppUpdateState.Failed -> info
        else -> null
    }
