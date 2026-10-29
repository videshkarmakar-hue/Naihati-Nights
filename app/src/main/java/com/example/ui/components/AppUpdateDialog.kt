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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.update.AppUpdateState
import com.example.data.update.formatBytesCompact
import com.example.data.update.releaseInfo
import com.example.data.update.releaseNoteLines
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans

private val DialogSurface = Color(0xFF101018)
private val DialogText = Color(0xFFF4F4F5)
private val DialogMuted = Color(0xFFA1A1AA)
private val DialogFaint = Color(0xFF71717A)
private val DialogRose = Color(0xFFFDA4AF)
private val DialogAmber = Color(0xFFFBBF24)

/**
 * "A new update is available" prompt.
 *
 * Shown on launch when GitHub has a newer release, and from Settings when the user checks
 * manually. The same dialog walks through download progress, the system installer hand-off and
 * failures, so the whole update is one surface with one set of actions.
 */
@Composable
fun AppUpdateDialog(
    visible: Boolean,
    state: AppUpdateState,
    notice: String?,
    onUpdateNow: () -> Unit,
    onLater: () -> Unit,
    onOpenRelease: () -> Unit
) {
    if (!visible) return
    val info = state.releaseInfo ?: return

    val palette = LocalAppPalette.current
    val isDownloading = state is AppUpdateState.Downloading
    val isReady = state is AppUpdateState.Ready
    val failure = (state as? AppUpdateState.Failed)?.message
    val notes = releaseNoteLines(info.notes)

    Dialog(
        onDismissRequest = onLater,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(DialogSurface)
                .border(1.dp, palette.accentBorderStrong, RoundedCornerShape(24.dp))
                .padding(20.dp)
                .testTag("app_update_dialog")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(palette.accentSoft)
                        .border(1.dp, palette.accentBorder, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isReady) Icons.Default.CheckCircle else Icons.Default.Download,
                        contentDescription = null,
                        tint = palette.accentBright,
                        modifier = Modifier.size(19.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isReady) "READY TO INSTALL" else "UPDATE AVAILABLE",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        lineHeight = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.1.sp,
                        color = palette.accentBright,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Naihati Nights v${info.versionName}",
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
                        lineHeight = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = DialogText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = buildString {
                    append("Installed v${com.example.BuildConfig.VERSION_NAME}")
                    if (info.apkSizeBytes > 0L) {
                        append("  •  ${formatBytesCompact(info.apkSizeBytes)}")
                    }
                    if (!info.hasApk) append("  •  no APK attached")
                },
                fontFamily = PlusJakartaSans,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                color = DialogMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF15151D))
                        .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "WHAT'S NEW",
                        fontFamily = PlusJakartaSans,
                        fontSize = 9.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.9.sp,
                        color = DialogFaint
                    )
                    notes.forEach { line ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = "•",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                color = palette.accentBright
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = line,
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                color = Color(0xFFE4E4E7)
                            )
                        }
                    }
                }
            }

            if (isDownloading) {
                val downloading = state as AppUpdateState.Downloading
                Spacer(modifier = Modifier.height(16.dp))
                UpdateProgressBar(progress = downloading.progress)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        append("Downloading ${(downloading.progress * 100f).toInt()}%")
                        if (downloading.totalBytes > 0L) {
                            append("  •  ${formatBytesCompact(downloading.downloadedBytes)}")
                            append(" of ${formatBytesCompact(downloading.totalBytes)}")
                        }
                    },
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = DialogMuted,
                    modifier = Modifier.testTag("app_update_progress")
                )
            }

            failure?.let { message ->
                Spacer(modifier = Modifier.height(14.dp))
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
                        imageVector = Icons.Default.Warning,
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
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = message,
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = DialogAmber,
                    modifier = Modifier.testTag("app_update_notice")
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            val primaryLabel = when {
                isDownloading -> "DOWNLOADING…"
                isReady -> "INSTALL UPDATE"
                !info.hasApk -> "OPEN GITHUB RELEASE"
                failure != null -> "TRY AGAIN"
                else -> "UPDATE NOW"
            }
            UpdatePrimaryButton(
                label = primaryLabel,
                enabled = !isDownloading,
                onClick = if (!info.hasApk) onOpenRelease else onUpdateNow
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Later",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = DialogMuted,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onLater)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("app_update_later")
                )
                Text(
                    text = "View release on GitHub",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = palette.accentBright,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onOpenRelease)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("app_update_release_link")
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

@Composable
private fun UpdatePrimaryButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val palette = LocalAppPalette.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (enabled) {
                    Brush.horizontalGradient(
                        listOf(palette.accentDeep, palette.accentStrong, palette.accentDeep)
                    )
                } else {
                    Brush.horizontalGradient(listOf(Color(0xFF1A1A24), Color(0xFF1A1A24)))
                }
            )
            .border(
                1.dp,
                if (enabled) palette.accentBorderStrong else Color(0x1AFFFFFF),
                RoundedCornerShape(14.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 14.dp)
            .testTag(if (label.contains("INSTALL")) "app_update_install" else "app_update_now"),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = PlusJakartaSans,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            color = if (enabled) Color(0xFFF8FAFC) else DialogFaint,
            maxLines = 1
        )
    }
}
