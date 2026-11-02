package com.example.ui.screens

import android.os.Build
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.update.AppUpdateState
import com.example.data.update.ReleaseInfo
import com.example.data.update.formatBytesCompact
import com.example.data.update.releaseInfo
import com.example.data.update.releaseNoteLines
import com.example.ui.components.AppBrandHeader
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val OtaCanvasTop = Color(0xFF090A0D)
private val OtaCanvasMid = Color(0xFF0D0E13)
private val OtaCanvasLow = Color(0xFF2A2C35)
private val OtaCanvasSilver = Color(0xFFD8DADE)
private val OtaCanvasMist = Color(0xFFE2E4E8)

private val OtaCard = Color(0xE60F1015)
private val OtaTile = Color(0xCC161720)
private val OtaInset = Color(0xE6111218)
private val OtaHairline = Color(0x14FFFFFF)
private val OtaSoftHairline = Color(0x0AFFFFFF)

private val OtaTextHigh = Color(0xFFF4F4F5)
private val OtaTextBody = Color(0xFFD4D4D8)
private val OtaTextMuted = Color(0xFFA1A1AA)
private val OtaTextFaint = Color(0xFF71717A)
private val OtaTextOnSilver = Color(0xFF52525B)

private val OtaEmerald = Color(0xFF34D399)
private val OtaRose = Color(0xFFFB7185)
private val OtaAmber = Color(0xFFFBBF24)
private val OtaMono = FontFamily.Monospace

/**
 * Dedicated "Check Updates" destination, reachable from the sidebar.
 *
 * It repeats the release-channel state in full (installed build, newest release, notes, download
 * progress) and owns the one prominent Update button, so the user never has to hunt for it inside
 * Settings. Opening the screen refreshes the GitHub lookup once.
 *
 * The layout follows the supplied OTA mockup: a silver-mist canvas, a matte obsidian core card with
 * the app emblem, a live status pill, primary and secondary actions, a last-verified row and a
 * two-by-two telemetry grid that reports the real package, runtime, signing identity and update
 * policy of this build.
 */
@Composable
fun CheckUpdatesScreen(
    installedVersion: String,
    installedBuild: Int,
    state: AppUpdateState,
    notice: String?,
    onScreenOpen: () -> Unit,
    onCheck: () -> Unit,
    onUpdate: () -> Unit,
    onOpenRelease: () -> Unit,
    onBack: () -> Unit,
    onOpenSidebar: () -> Unit = {},
    onOpenDeveloperCredits: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val palette = LocalAppPalette.current
    val context = LocalContext.current
    val info = state.releaseInfo
    val isChecking = state is AppUpdateState.Checking
    val downloading = state as? AppUpdateState.Downloading
    val isReady = state is AppUpdateState.Ready
    val isAvailable = state is AppUpdateState.Available
    val failure = (state as? AppUpdateState.Failed)?.message

    LaunchedEffect(Unit) { onScreenOpen() }

    val statusTone = when (state) {
        is AppUpdateState.UpToDate, is AppUpdateState.Ready -> OtaEmerald
        is AppUpdateState.Available, is AppUpdateState.Downloading -> palette.accentBright
        is AppUpdateState.Failed -> OtaRose
        else -> OtaAmber
    }

    val statusPill = when (state) {
        AppUpdateState.Idle -> "SYSTEM READY • CHECK FOR UPDATES"
        AppUpdateState.Checking -> "CONTACTING SERVER • CHECKING"
        is AppUpdateState.UpToDate -> "SYSTEM SECURE • UP TO DATE"
        is AppUpdateState.Available -> "UPDATE AVAILABLE • v${state.info.versionName}"
        is AppUpdateState.Downloading -> "DOWNLOADING • ${(state.progress * 100f).toInt()}%"
        is AppUpdateState.Ready -> "DOWNLOAD COMPLETE • READY TO INSTALL"
        is AppUpdateState.Failed -> "CHECK FAILED • VERIFY CONNECTION"
    }

    val statusTitle = when (state) {
        AppUpdateState.Idle -> "Ready to check"
        AppUpdateState.Checking -> "Contacting the update server…"
        is AppUpdateState.UpToDate -> "You're up to date"
        is AppUpdateState.Available -> "v${state.info.versionName} is available"
        is AppUpdateState.Downloading -> "Downloading the update…"
        is AppUpdateState.Ready -> "Ready to install"
        is AppUpdateState.Failed -> "Couldn't check for updates"
    }

    val statusBody = when (state) {
        AppUpdateState.Idle ->
            "Naihati Nights checks the official release feed for newer builds."

        AppUpdateState.Checking -> "Looking for the newest APK published for this build."
        is AppUpdateState.UpToDate ->
            "v${state.latestVersion} is the newest release. Nothing to install."

        is AppUpdateState.Available ->
            if (state.info.hasApk) {
                "A newer build is available • ${formatBytesCompact(state.info.apkSizeBytes)}."
            } else {
                "A newer release exists but has no APK attached. Open it instead."
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
        isAvailable -> "OPEN RELEASE"
        isChecking -> "CHECKING…"
        else -> "CHECK FOR UPDATES"
    }

    val primaryEnabled = !isChecking && downloading == null
    val primaryAction: () -> Unit = when {
        isReady || isAvailable -> if (info?.hasApk == true) onUpdate else onOpenRelease
        else -> onCheck
    }

    val verified = formatVerifiedAt((state as? AppUpdateState.UpToDate)?.checkedAtMillis ?: 0L)
    val runtimeEngine = buildString {
        append(Build.SUPPORTED_ABIS.firstOrNull()?.lowercase(Locale.US) ?: "unknown")
        append(" • Android ")
        append(Build.VERSION.RELEASE)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        0f to OtaCanvasTop,
                        0.45f to OtaCanvasMid,
                        0.70f to OtaCanvasLow,
                        0.94f to OtaCanvasSilver,
                        1f to OtaCanvasMist
                    )
                )
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x70FFFFFF),
                            Color(0x1FC8CDD7),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.5f, size.height * 0.9f),
                        radius = size.width * 1.05f
                    )
                )
            },
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 448.dp)
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .testTag("check_updates_screen"),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 128.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "updates_header") {
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
                                .border(1.dp, OtaHairline, RoundedCornerShape(12.dp))
                                .testTag("check_updates_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = OtaTextHigh,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )
            }

            item(key = "updates_title") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Terminal,
                        contentDescription = null,
                        tint = palette.accentBright,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Check Updates",
                        fontFamily = PlusJakartaSans,
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.4).sp,
                        color = OtaTextHigh,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "OTA",
                        fontFamily = OtaMono,
                        fontSize = 9.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        color = palette.accentBright,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(palette.accentSoft)
                            .border(1.dp, palette.accentBorder, CircleShape)
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    )
                }
            }

            item(key = "updates_core") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(OtaCard)
                        .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(22.dp))
                        .padding(horizontal = 18.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OtaEmblem()

                    Spacer(modifier = Modifier.height(12.dp))

                    StatusPill(label = statusPill, tone = statusTone)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = OtaTextHigh,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 23.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.5).sp
                                )
                            ) {
                                append("Naihati Nights  ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = palette.accentBright,
                                    fontFamily = OtaMono,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = (-0.3).sp
                                )
                            ) {
                                append("v$installedVersion")
                            }
                        },
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("check_updates_version")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    BuildSpecPill(
                        installedBuild = installedBuild,
                        apkSizeBytes = info?.apkSizeBytes ?: 0L
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = statusTitle,
                        fontFamily = PlusJakartaSans,
                        fontSize = 14.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (failure != null) OtaRose else OtaTextHigh,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = statusBody,
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp,
                        color = OtaTextMuted,
                        textAlign = TextAlign.Center,
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
                            color = OtaAmber,
                            textAlign = TextAlign.Center,
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
                                if (primaryEnabled) palette.accentBorderStrong else OtaHairline,
                                RoundedCornerShape(14.dp)
                            )
                            .clickable(enabled = primaryEnabled, onClick = primaryAction)
                            .padding(vertical = 14.dp)
                            .testTag("check_updates_button"),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isReady) Icons.Filled.Verified else Icons.Filled.Sync,
                            contentDescription = null,
                            tint = if (primaryEnabled) palette.onAccent else OtaTextFaint,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = primaryLabel,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            color = if (primaryEnabled) palette.onAccent else OtaTextFaint,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xCC1B1C26))
                            .border(1.dp, Color(0x0FFFFFFF), RoundedCornerShape(14.dp))
                            .clickable(onClick = onOpenRelease)
                            .padding(vertical = 11.dp)
                            .testTag("check_updates_release_notes"),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Article,
                            contentDescription = null,
                            tint = palette.accentBright,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "View Detailed Release Notes",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.1.sp,
                            color = OtaTextBody,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = null,
                            tint = OtaTextFaint,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = verified ?: "Mandatory check runs on every app launch",
                            fontFamily = OtaMono,
                            fontSize = 9.5.sp,
                            lineHeight = 13.sp,
                            color = OtaTextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.testTag("check_updates_verified")
                        )
                    }
                }
            }

            item(key = "updates_telemetry") {
                TelemetrySection(
                    packageName = context.packageName,
                    runtimeEngine = runtimeEngine
                )
            }

            if (info != null) {
                item(key = "updates_whats_new") {
                    NotesCard(info = info, accent = palette.accentBright)
                }
            }

            item(key = "updates_footnote") {
                Text(
                    text = "Updates are delivered through the in-app updater. " +
                        "Android asks once for permission to install apps from this source.",
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.sp,
                    lineHeight = 15.sp,
                    color = OtaTextOnSilver,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            item(key = "updates_footer") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 8.dp)
                        .testTag("check_updates_footer"),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0x990A0B0E))
                            .border(1.dp, Color(0x12FFFFFF), CircleShape)
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ENGINEERED & ARCHITECTED BY",
                            fontFamily = OtaMono,
                            fontSize = 8.sp,
                            lineHeight = 11.sp,
                            letterSpacing = 1.sp,
                            color = OtaTextFaint
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "VIDESH KARMAKAR",
                            fontFamily = OtaMono,
                            fontSize = 8.sp,
                            lineHeight = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp,
                            color = OtaTextBody
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OtaEmblem() {
    val palette = LocalAppPalette.current
    val ping = rememberInfiniteTransition(label = "otaPing")
    val pingScale by ping.animateFloat(
        initialValue = 1f,
        targetValue = 1.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800),
            repeatMode = RepeatMode.Restart
        ),
        label = "pingScale"
    )
    val pingAlpha by ping.animateFloat(
        initialValue = 0.35f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800),
            repeatMode = RepeatMode.Restart
        ),
        label = "pingAlpha"
    )

    Box(modifier = Modifier.size(66.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            palette.accentDeep.copy(alpha = 0.55f),
                            Color(0xFF12131C)
                        )
                    )
                )
                .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(18.dp))
                .drawBehind { drawCornerBraces(Color(0x66FFFFFF)) }
        ) {
            Image(
                painter = painterResource(R.drawable.app_brand_logo),
                contentDescription = "Naihati Nights emblem",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(7.dp)
                    .clip(RoundedCornerShape(13.dp))
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 4.dp, y = 4.dp)
                .size(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size((14 * pingScale).dp)
                    .clip(CircleShape)
                    .background(OtaEmerald.copy(alpha = pingAlpha * 0.6f))
            )
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0A0B0E))
                    .border(1.dp, OtaEmerald.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(OtaEmerald)
                )
            }
        }
    }
}

@Composable
private fun StatusPill(label: String, tone: Color) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(tone.copy(alpha = 0.10f))
            .border(1.dp, tone.copy(alpha = 0.28f), CircleShape)
            .padding(horizontal = 11.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(tone)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontFamily = OtaMono,
            fontSize = 9.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.9.sp,
            color = tone,
            maxLines = 1
        )
    }
}

@Composable
private fun BuildSpecPill(installedBuild: Int, apkSizeBytes: Long) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0x08FFFFFF))
            .border(1.dp, OtaSoftHairline, CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "BUILD",
            fontFamily = OtaMono,
            fontSize = 8.sp,
            lineHeight = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.8.sp,
            color = OtaTextFaint
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = installedBuild.toString(),
            fontFamily = OtaMono,
            fontSize = 10.sp,
            lineHeight = 13.sp,
            fontWeight = FontWeight.Medium,
            color = OtaTextBody
        )
        if (apkSizeBytes > 0L) {
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "•",
                fontFamily = OtaMono,
                fontSize = 9.sp,
                color = OtaTextFaint
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = formatBytesCompact(apkSizeBytes),
                fontFamily = OtaMono,
                fontSize = 10.sp,
                lineHeight = 13.sp,
                color = OtaTextMuted
            )
        }
    }
}

@Composable
private fun TelemetrySection(packageName: String, runtimeEngine: String) {
    val palette = LocalAppPalette.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Memory,
                contentDescription = null,
                tint = palette.accentBright,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "SYSTEM ARCHITECTURE & TELEMETRY",
                fontFamily = OtaMono,
                fontSize = 9.5.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.9.sp,
                color = OtaTextMuted
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xD90E0F14))
                .border(1.dp, OtaHairline, RoundedCornerShape(18.dp))
                .padding(12.dp)
                .testTag("check_updates_telemetry")
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TelemetryTile(
                    label = "App Package",
                    value = packageName,
                    modifier = Modifier.weight(1f)
                )
                TelemetryTile(
                    label = "Runtime Engine",
                    value = runtimeEngine,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TelemetryTile(
                    label = "Security Cert",
                    value = "Signed by Videsh K.",
                    valueColor = OtaEmerald,
                    modifier = Modifier.weight(1f)
                )
                TelemetryTile(
                    label = "Update Policy",
                    value = "Required • Automatic",
                    valueColor = palette.accentBright,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("check_updates_auto_toggle")
                )
            }
        }
    }
}

@Composable
private fun TelemetryTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = OtaTextHigh
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(OtaTile)
            .border(1.dp, OtaSoftHairline, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 9.dp)
    ) {
        Text(
            text = label.uppercase(Locale.US),
            fontFamily = OtaMono,
            fontSize = 9.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.6.sp,
            color = OtaTextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = value,
            fontFamily = OtaMono,
            fontSize = 10.sp,
            lineHeight = 13.sp,
            fontWeight = FontWeight.Medium,
            color = valueColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun NotesCard(info: ReleaseInfo, accent: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(OtaInset)
            .border(1.dp, OtaHairline, RoundedCornerShape(18.dp))
            .padding(16.dp)
            .testTag("check_updates_notes")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "WHAT'S NEW IN ${info.tagName.uppercase(Locale.US)}",
                fontFamily = OtaMono,
                fontSize = 9.5.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.9.sp,
                color = OtaTextFaint,
                modifier = Modifier.weight(1f)
            )
            if (info.apkSizeBytes > 0L) {
                Text(
                    text = formatBytesCompact(info.apkSizeBytes),
                    fontFamily = OtaMono,
                    fontSize = 9.5.sp,
                    lineHeight = 13.sp,
                    color = OtaTextFaint
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
                color = OtaTextBody
            )
        } else {
            notes.forEach { line ->
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "•",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = accent
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = line,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = OtaTextBody
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
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

private fun DrawScope.drawCornerBraces(color: Color) {
    val inset = 5.dp.toPx()
    val length = 7.dp.toPx()
    val stroke = 1.dp.toPx()
    val right = size.width - inset
    val bottom = size.height - inset

    drawLine(color, Offset(inset, inset), Offset(inset + length, inset), stroke, StrokeCap.Round)
    drawLine(color, Offset(inset, inset), Offset(inset, inset + length), stroke, StrokeCap.Round)
    drawLine(color, Offset(right, inset), Offset(right - length, inset), stroke, StrokeCap.Round)
    drawLine(color, Offset(right, inset), Offset(right, inset + length), stroke, StrokeCap.Round)
    drawLine(color, Offset(inset, bottom), Offset(inset + length, bottom), stroke, StrokeCap.Round)
    drawLine(color, Offset(inset, bottom), Offset(inset, bottom - length), stroke, StrokeCap.Round)
    drawLine(color, Offset(right, bottom), Offset(right - length, bottom), stroke, StrokeCap.Round)
    drawLine(color, Offset(right, bottom), Offset(right, bottom - length), stroke, StrokeCap.Round)
}

/**
 * Formats [millis] as an IST timestamp ("Last verified today at 07:15 AM IST"). Returns null for a
 * missing timestamp so the caller can fall back to the mandatory-launch copy.
 */
private fun formatVerifiedAt(millis: Long): String? {
    if (millis <= 0L) return null
    val zone = TimeZone.getTimeZone("Asia/Kolkata")
    val now = Calendar.getInstance(zone)
    val then = Calendar.getInstance(zone).apply { timeInMillis = millis }
    val sameDay = now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
        now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)
    val time = SimpleDateFormat("hh:mm a", Locale.US).apply { timeZone = zone }.format(Date(millis))
    if (sameDay) return "Last verified today at $time IST"
    val date = SimpleDateFormat("dd MMM", Locale.US).apply { timeZone = zone }.format(Date(millis))
    return "Last verified $date at $time IST"
}
