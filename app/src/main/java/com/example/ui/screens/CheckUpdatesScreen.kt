package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.update.AppUpdateState
import com.example.data.update.formatBytesCompact
import com.example.data.update.releaseInfo
import com.example.data.update.releaseNoteLines
import com.example.ui.components.AppBrandHeader
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans

private val UpdatesPage = Color(0xFF050505)
private val UpdatesCard = Color(0xFF101018)
private val UpdatesInset = Color(0xFF15151D)
private val UpdatesText = Color(0xFFF4F4F5)
private val UpdatesBody = Color(0xFFD4D4D8)
private val UpdatesMuted = Color(0xFFA1A1AA)
private val UpdatesFaint = Color(0xFF71717A)
private val UpdatesHairline = Color(0x14FFFFFF)
private val UpdatesRose = Color(0xFFFDA4AF)

/**
 * Dedicated "Check Updates" destination, reachable from the sidebar.
 *
 * It repeats the release-channel state in full (installed build, newest release, notes, download
 * progress) and owns the one prominent Update button, so the user never has to hunt for it inside
 * Settings. Opening the screen refreshes the GitHub lookup once.
 */
@Composable
fun CheckUpdatesScreen(
    installedVersion: String,
    installedBuild: Int,
    state: AppUpdateState,
    notice: String?,
    autoCheckEnabled: Boolean,
    onScreenOpen: () -> Unit,
    onCheck: () -> Unit,
    onUpdate: () -> Unit,
    onOpenRelease: () -> Unit,
    onAutoCheckChange: (Boolean) -> Unit,
    onBack: () -> Unit,
    onOpenSidebar: () -> Unit = {},
    onOpenDeveloperCredits: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val palette = LocalAppPalette.current
    val info = state.releaseInfo
    val isChecking = state is AppUpdateState.Checking
    val downloading = state as? AppUpdateState.Downloading
    val isReady = state is AppUpdateState.Ready
    val isAvailable = state is AppUpdateState.Available
    val failure = (state as? AppUpdateState.Failed)?.message

    LaunchedEffect(Unit) { onScreenOpen() }

    val badge = when (state) {
        AppUpdateState.Idle -> "GITHUB"
        AppUpdateState.Checking -> "CHECKING"
        is AppUpdateState.UpToDate -> "LATEST"
        is AppUpdateState.Available, is AppUpdateState.Downloading, is AppUpdateState.Ready -> "UPDATE"
        is AppUpdateState.Failed -> "OFFLINE"
    }

    val statusTitle = when (state) {
        AppUpdateState.Idle -> "Ready to check"
        AppUpdateState.Checking -> "Contacting GitHub…"
        is AppUpdateState.UpToDate -> "You're up to date"
        is AppUpdateState.Available -> "v${state.info.versionName} is available"
        is AppUpdateState.Downloading -> "Downloading the update…"
        is AppUpdateState.Ready -> "Ready to install"
        is AppUpdateState.Failed -> "Couldn't check for updates"
    }

    val statusBody = when (state) {
        AppUpdateState.Idle ->
            "Naihati Nights checks the official GitHub Releases feed. No Play Store needed."

        AppUpdateState.Checking -> "Looking for the newest APK published on GitHub."
        is AppUpdateState.UpToDate ->
            "v${state.latestVersion} is the newest release. Nothing to install."

        is AppUpdateState.Available ->
            if (state.info.hasApk) {
                "A newer build is on GitHub • ${formatBytesCompact(state.info.apkSizeBytes)}."
            } else {
                "A newer release exists but has no APK attached. Open it on GitHub instead."
            }

        is AppUpdateState.Downloading -> buildString {
            append("${(state.progress * 100f).toInt()}% downloaded")
            if (state.totalBytes > 0L) {
                append(" • ${formatBytesCompact(state.downloadedBytes)} of ")
                append(formatBytesCompact(state.totalBytes))
            }
        }

        is AppUpdateState.Ready -> "Downloaded to this device. Tap Install update to finish."
        is AppUpdateState.Failed -> state.message
    }

    val primaryLabel = when {
        downloading != null -> "DOWNLOADING ${(downloading.progress * 100f).toInt()}%"
        isReady -> "INSTALL UPDATE"
        isAvailable && info?.hasApk == true -> "UPDATE NOW"
        isAvailable -> "OPEN GITHUB RELEASE"
        isChecking -> "CHECKING…"
        else -> "CHECK FOR UPDATES"
    }

    val primaryEnabled = !isChecking && downloading == null
    val primaryAction: () -> Unit = when {
        isReady || isAvailable -> if (info?.hasApk == true) onUpdate else onOpenRelease
        else -> onCheck
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(UpdatesPage),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 448.dp)
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .testTag("check_updates_screen"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 128.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "updates_header") {
                Column {
                    AppBrandHeader(
                        onOpenSidebar = onOpenSidebar,
                        onOpenDeveloperCredits = onOpenDeveloperCredits,
                        navigation = {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x0AFFFFFF))
                                    .border(1.dp, UpdatesHairline, RoundedCornerShape(12.dp))
                                    .testTag("check_updates_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = UpdatesText,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            item(key = "updates_hero") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(palette.accentSurfaceStrong, UpdatesCard)
                            )
                        )
                        .border(1.dp, palette.accentBorder, RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(palette.accentSoft)
                            .border(1.dp, palette.accentBorderStrong, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SystemUpdate,
                            contentDescription = null,
                            tint = palette.accentBright,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CHECK UPDATES",
                            fontFamily = PlusJakartaSans,
                            fontSize = 10.sp,
                            lineHeight = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.1.sp,
                            color = palette.accentBright
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Naihati Nights",
                            fontFamily = PlusJakartaSans,
                            fontSize = 18.sp,
                            lineHeight = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = UpdatesText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Installed v$installedVersion  •  Build $installedBuild",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = UpdatesMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.testTag("check_updates_version")
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = badge,
                        fontFamily = PlusJakartaSans,
                        fontSize = 9.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        color = palette.accentBright,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(palette.accentSoft)
                            .border(1.dp, palette.accentBorder, CircleShape)
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                    )
                }
            }

            item(key = "updates_status") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(UpdatesCard)
                        .border(1.dp, UpdatesHairline, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when {
                                isReady || state is AppUpdateState.UpToDate -> Icons.Filled.CheckCircle
                                failure != null -> Icons.Filled.CloudOff
                                else -> Icons.Filled.Download
                            },
                            contentDescription = null,
                            tint = when {
                                failure != null -> UpdatesRose
                                isReady || state is AppUpdateState.UpToDate -> palette.accentBright
                                else -> palette.accentBright
                            },
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = statusTitle,
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (failure != null) UpdatesRose else UpdatesText
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = statusBody,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = UpdatesMuted,
                        modifier = Modifier.testTag("check_updates_status")
                    )

                    downloading?.let { progress ->
                        Spacer(modifier = Modifier.height(14.dp))
                        UpdateProgressBar(progress = progress.progress)
                    }

                    notice?.let { message ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = message,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = Color(0xFFFBBF24),
                            modifier = Modifier.testTag("check_updates_notice")
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (primaryEnabled) {
                                    Brush.horizontalGradient(
                                        listOf(
                                            palette.accentDeep,
                                            palette.accentStrong,
                                            palette.accentDeep
                                        )
                                    )
                                } else {
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF1A1A24), Color(0xFF1A1A24))
                                    )
                                }
                            )
                            .border(
                                1.dp,
                                if (primaryEnabled) palette.accentBorderStrong else UpdatesHairline,
                                RoundedCornerShape(14.dp)
                            )
                            .clickable(enabled = primaryEnabled, onClick = primaryAction)
                            .padding(vertical = 14.dp)
                            .testTag("check_updates_button"),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (downloading == null) {
                            Icon(
                                imageVector = if (isReady) Icons.Filled.Verified else Icons.Filled.SystemUpdate,
                                contentDescription = null,
                                tint = if (primaryEnabled) palette.onAccent else UpdatesFaint,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = primaryLabel,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            color = if (primaryEnabled) palette.onAccent else UpdatesFaint,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(onClick = onOpenRelease)
                            .padding(horizontal = 6.dp, vertical = 8.dp)
                            .testTag("check_updates_open_release"),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.OpenInNew,
                            contentDescription = null,
                            tint = palette.accentBright,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "View release on GitHub",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = palette.accentBright
                        )
                    }
                }
            }

            if (info != null) {
                item(key = "updates_whats_new") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(UpdatesInset)
                            .border(1.dp, UpdatesHairline, RoundedCornerShape(20.dp))
                            .padding(16.dp)
                            .testTag("check_updates_notes")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "WHAT'S NEW IN ${info.tagName.uppercase()}",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.sp,
                                lineHeight = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.9.sp,
                                color = UpdatesFaint,
                                modifier = Modifier.weight(1f)
                            )
                            if (info.apkSizeBytes > 0L) {
                                Text(
                                    text = formatBytesCompact(info.apkSizeBytes),
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 10.sp,
                                    lineHeight = 13.sp,
                                    color = UpdatesFaint
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        val notes = releaseNoteLines(info.notes, limit = 6)
                        if (notes.isEmpty()) {
                            Text(
                                text = "Bug fixes and polish for the Parikrama guide.",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = UpdatesBody
                            )
                        } else {
                            notes.forEach { line ->
                                Row(verticalAlignment = Alignment.Top) {
                                    Text(
                                        text = "•",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp,
                                        color = palette.accentBright
                                    )
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(
                                        text = line,
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp,
                                        color = UpdatesBody
                                    )
                                }
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }
                    }
                }
            }

            item(key = "updates_auto_check") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(UpdatesCard)
                        .border(1.dp, UpdatesHairline, RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Auto-check on launch",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = UpdatesText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Show a popup when a new version is published.",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = UpdatesFaint
                        )
                    }
                    Switch(
                        checked = autoCheckEnabled,
                        onCheckedChange = onAutoCheckChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = palette.onAccent,
                            checkedTrackColor = palette.accent,
                            checkedBorderColor = palette.accentBorderStrong,
                            uncheckedThumbColor = UpdatesMuted,
                            uncheckedTrackColor = Color(0xFF1E1E28),
                            uncheckedBorderColor = UpdatesHairline
                        ),
                        modifier = Modifier.testTag("check_updates_auto_toggle")
                    )
                }
            }

            item(key = "updates_footnote") {
                Text(
                    text = "Updates are delivered from GitHub Releases, not the Play Store. " +
                        "Android asks once for permission to install apps from this source.",
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.sp,
                    lineHeight = 15.sp,
                    color = UpdatesFaint,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun UpdateProgressBar(progress: Float) {
    val palette = LocalAppPalette.current
    val fraction = progress.coerceIn(0f, 1f).coerceAtLeast(0.02f)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(CircleShape)
            .background(Color(0x24FFFFFF))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(listOf(palette.accentDeep, palette.accentBright))
                )
        )
    }
}
