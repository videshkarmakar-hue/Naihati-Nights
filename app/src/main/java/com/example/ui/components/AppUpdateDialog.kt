package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.update.AppUpdateState
import com.example.data.update.formatBytesCompact
import com.example.data.update.releaseInfo
import com.example.data.update.releaseNoteLines
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans

private val DialogCard = Color(0xF2111216)
private val DialogCardBorder = Color(0xFF26272E)
private val DialogIconSurface = Color(0xFF17181D)
private val DialogIconBorder = Color(0xFF2A2B33)
private val DialogInset = Color(0x8017171D)
private val DialogInsetBorder = Color(0xFF232429)
private val DialogText = Color(0xFFF4F4F5)
private val DialogBody = Color(0xFFD4D4D8)
private val DialogMuted = Color(0xFFA1A1AA)
private val DialogFaint = Color(0xFF71717A)
private val DialogRose = Color(0xFFFDA4AF)
private val DialogAmber = Color(0xFFFBBF24)
private val DialogTrack = Color(0xFF2A2B31)
private val DialogMono = FontFamily.Monospace

/**
 * Mandatory "update available" prompt, styled to the Finesta minimalist smoke mockup.
 *
 * Shown on launch when GitHub has a newer release, and from Settings when the user checks
 * manually. The same dialog walks through download progress, the system installer hand-off and
 * failures, so the whole update is one surface with one set of actions.
 *
 * The update cannot be dismissed: there is no cancel, outside-tap or back-press escape. The
 * secondary action opens the release notes instead of cancelling, and the white primary action is
 * always the next step of the update.
 */
@Composable
fun AppUpdateDialog(
    visible: Boolean,
    state: AppUpdateState,
    notice: String?,
    onUpdateNow: () -> Unit,
    onOpenRelease: () -> Unit
) {
    if (!visible) return
    val info = state.releaseInfo ?: return

    val palette = LocalAppPalette.current
    val isDownloading = state is AppUpdateState.Downloading
    val isReady = state is AppUpdateState.Ready
    val failure = (state as? AppUpdateState.Failed)?.message
    val notes = releaseNoteLines(info.notes, limit = 5)

    val title = when {
        isReady -> "Ready to Install"
        failure != null -> "Update Failed"
        isDownloading -> "Downloading Update"
        else -> "Update Available"
    }

    val headerIcon = when {
        isReady -> Icons.Filled.Check
        failure != null -> Icons.Filled.Warning
        else -> Icons.Filled.Download
    }

    val progress = when (state) {
        is AppUpdateState.Downloading -> state.progress
        is AppUpdateState.Ready -> 1f
        else -> 0f
    }.coerceIn(0f, 1f)

    val totalBytes = when (state) {
        is AppUpdateState.Downloading -> state.totalBytes
        is AppUpdateState.Ready ->
            state.apkFile.length().takeIf { it > 0L } ?: info.apkSizeBytes
        else -> info.apkSizeBytes
    }

    val downloadedBytes = when (state) {
        is AppUpdateState.Downloading -> state.downloadedBytes
        is AppUpdateState.Ready -> totalBytes
        else -> 0L
    }

    val statusLabel = when {
        isReady -> "Ready to Install"
        isDownloading -> "Downloading"
        failure != null -> "Download Failed"
        else -> "Preparing Download"
    }

    val percentLabel = when {
        isReady -> "100%"
        progress > 0f -> "${(progress * 100f).toInt()}%"
        else -> "0%"
    }

    val detailLabel = when {
        isReady -> "Download Complete"
        isDownloading -> "In progress"
        failure != null -> "Paused"
        else -> "Starts automatically"
    }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(
                        brush = Brush.radialGradient(
                            0f to Color(0xFFE2E4E9),
                            0.18f to Color(0xFFA4A9B3),
                            0.42f to Color(0xFF363A45),
                            0.70f to Color(0xFF14161A),
                            1f to Color(0xFF08090B),
                            center = Offset(size.width * 0.5f, size.height * 1.2f),
                            radius = size.height * 1.25f
                        )
                    )
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to Color(0xFF0A0B0D),
                            0.5f to Color(0xBF0A0B0D),
                            1f to Color(0x000A0B0D)
                        ),
                        size = Size(size.width, size.height * 0.45f)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 412.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 24.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = Color.Black,
                            spotColor = Color.Black
                        )
                        .clip(RoundedCornerShape(24.dp))
                        .background(DialogCard)
                        .border(1.dp, DialogCardBorder, RoundedCornerShape(24.dp))
                        .padding(20.dp)
                        .testTag("app_update_dialog")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(DialogIconSurface)
                                .border(1.dp, DialogIconBorder, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = headerIcon,
                                contentDescription = null,
                                tint = DialogText,
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                fontFamily = PlusJakartaSans,
                                fontSize = 16.sp,
                                lineHeight = 21.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = (-0.2).sp,
                                color = DialogText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Naihati Nights",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                color = DialogMuted,
                                maxLines = 1
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "v${info.versionName}",
                            fontFamily = DialogMono,
                            fontSize = 10.sp,
                            lineHeight = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.2.sp,
                            color = palette.accentBright,
                            maxLines = 1,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(palette.accentSoft)
                                .border(1.dp, palette.accentBorder, CircleShape)
                                .padding(horizontal = 9.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DialogInset)
                            .border(1.dp, DialogInsetBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 11.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            tint = palette.accentBright,
                            modifier = Modifier
                                .padding(top = 1.dp)
                                .size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "This update is required to keep using Naihati Nights. " +
                                "It downloads automatically and Android asks you to confirm the install.",
                            fontFamily = PlusJakartaSans,
                            fontSize = 10.5.sp,
                            lineHeight = 15.sp,
                            color = DialogMuted,
                            modifier = Modifier.testTag("app_update_required_note")
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "WHAT'S NEW",
                        fontFamily = DialogMono,
                        fontSize = 9.5.sp,
                        lineHeight = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp,
                        color = DialogMuted
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(DialogInset)
                            .border(1.dp, DialogInsetBorder, RoundedCornerShape(14.dp))
                            .padding(horizontal = 12.dp, vertical = 11.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val bullets = notes.ifEmpty {
                            listOf("Bug fixes and polish for the Parikrama guide.")
                        }
                        bullets.forEach { line ->
                            Row(verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 6.dp)
                                        .size(5.dp)
                                        .clip(CircleShape)
                                        .background(DialogMuted)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = line,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    color = DialogBody
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(DialogInset)
                            .border(1.dp, DialogInsetBorder, RoundedCornerShape(14.dp))
                            .padding(12.dp)
                            .testTag("app_update_progress_card")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = statusLabel,
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (failure != null) DialogRose else DialogText,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = percentLabel,
                                fontFamily = DialogMono,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DialogBody
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(DialogTrack)
                        ) {
                            if (progress > 0f) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(progress.coerceAtLeast(0.02f))
                                        .fillMaxHeight()
                                        .clip(CircleShape)
                                        .background(if (failure != null) DialogRose else DialogText)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (totalBytes > 0L) {
                                    "${formatBytesCompact(downloadedBytes)} / " +
                                        formatBytesCompact(totalBytes)
                                } else {
                                    "Waiting for release asset"
                                },
                                fontFamily = DialogMono,
                                fontSize = 9.5.sp,
                                lineHeight = 13.sp,
                                color = DialogMuted,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("app_update_progress")
                            )
                            Text(
                                text = detailLabel,
                                fontFamily = DialogMono,
                                fontSize = 9.5.sp,
                                lineHeight = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = DialogBody,
                                maxLines = 1
                            )
                        }
                    }

                    failure?.let { message ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x1FF43F5E))
                                .border(1.dp, Color(0x33F43F5E), RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = null,
                                tint = DialogRose,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = message,
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                color = DialogRose
                            )
                        }
                    }

                    notice?.let { message ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = message,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = DialogAmber,
                            modifier = Modifier.testTag("app_update_notice")
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val primaryLabel = when {
                        isDownloading -> "DOWNLOADING…"
                        isReady -> "INSTALL UPDATE"
                        !info.hasApk -> "OPEN RELEASE"
                        failure != null -> "RETRY UPDATE"
                        notice != null -> "CONTINUE UPDATE"
                        else -> "UPDATE NOW"
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UpdatePrimaryButton(
                            label = primaryLabel,
                            enabled = !isDownloading,
                            ready = isReady,
                            onClick = if (!info.hasApk) onOpenRelease else onUpdateNow,
                            modifier = Modifier.weight(1f)
                        )
                        UpdateSecondaryButton(
                            label = "RELEASE NOTES",
                            onClick = onOpenRelease
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UpdatePrimaryButton(
    label: String,
    enabled: Boolean,
    ready: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (enabled) Color(0xFFF8FAFC) else Color(0xFF1A1B21))
            .border(
                1.dp,
                if (enabled) Color(0xFFF8FAFC) else Color(0x1AFFFFFF),
                RoundedCornerShape(12.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 13.dp, horizontal = 12.dp)
            .testTag(if (label.contains("INSTALL")) "app_update_install" else "app_update_now"),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (ready) Icons.Filled.Check else Icons.Filled.Download,
            contentDescription = null,
            tint = if (enabled) Color(0xFF0A0B0D) else DialogFaint,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = label,
            fontFamily = PlusJakartaSans,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.7.sp,
            color = if (enabled) Color(0xFF0A0B0D) else DialogFaint,
            maxLines = 1
        )
    }
}

@Composable
private fun UpdateSecondaryButton(
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DialogIconSurface)
            .border(1.dp, DialogCardBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp, horizontal = 14.dp)
            .testTag("app_update_notes"),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = PlusJakartaSans,
            fontSize = 10.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.6.sp,
            color = DialogMuted,
            maxLines = 1
        )
    }
}
