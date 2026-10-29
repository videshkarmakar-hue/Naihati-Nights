package com.example.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.BuildConfig
import com.example.R
import com.example.model.AppContent
import com.example.model.AppSettings
import com.example.model.ThemeStyle
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.AvailablePalettes
import com.example.ui.theme.EmergencyRose
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.ObsidianNoir
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TextHighWhite
import com.example.ui.theme.TextMutedZinc
import com.example.ui.theme.TextSoftZinc
import com.example.ui.viewmodel.UpdateCheckState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Palette mirrored from the "Settings & Preferences" reference design.
private val SettingsPage = Color(0xFF08080C)
private val SettingsSurface = Color(0xFF0F0F15)
private val SettingsOutline = Color(0x12FFFFFF)
private val SettingsHairline = Color(0x0DFFFFFF)
private val SettingsText = Color(0xFFF4F4F5)
private val SettingsMuted = Color(0xFFA1A1AA)
private val SettingsMutedDeep = Color(0xFF71717A)
private val SettingsAccent: Color @Composable get() = LocalAppPalette.current.accentBright
private val SettingsLavender: Color @Composable get() = LocalAppPalette.current.accentBright
private val SettingsIconViolet: Color @Composable get() = LocalAppPalette.current.accentBorder
private val SettingsIconBorder: Color @Composable get() = LocalAppPalette.current.accentBorder
private val SettingsSwitchOff = Color(0xFF282834)
private val SettingsEmerald = Color(0xFF34D399)
private val SettingsFuchsia = Color(0xFFE879F9)
private val SettingsRose = Color(0xFFFDA4AF)

private const val DEVELOPER_AVATAR_URL =
    "https://cdn.phototourl.com/member/2026-09-24-d3b8ff6f-f0d4-43e0-9082-9f39cc851774.png"

private const val PORTFOLIO_URL = "https://videshkarmakar.carrd.co"
private const val SPOTIFY_URL = "https://open.spotify.com"
private const val INSTAGRAM_URL = "https://instagram.com"

/**
 * Settings & Preferences, rebuilt from the supplied reference screen.
 *
 * The preference toggles are bound to [AppSettings] and persisted by the view model, so the
 * visual refresh does not change saved state behaviour.
 */
@Composable
fun SettingsScreen(
    appSettings: AppSettings,
    content: AppContent,
    onUpdateSettings: (AppSettings) -> Unit,
    onResetSettings: () -> Unit = { onUpdateSettings(AppSettings()) },
    onCheckForUpdates: () -> Unit = {},
    updateCheck: UpdateCheckState = UpdateCheckState.Idle,
    onBack: () -> Unit,
    onOpenDeveloperProfile: () -> Unit,
    onOpenSidebar: () -> Unit = {},
    onOpenDeveloperCredits: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isResetting by remember { mutableStateOf(false) }
    var resetTurns by remember { mutableIntStateOf(0) }

    val resetRotation by animateFloatAsState(
        targetValue = resetTurns * 360f,
        animationSpec = tween(durationMillis = 200),
        label = "resetRotation"
    )

    fun openLink(url: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "No app found to open this link", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SettingsPage)
            .statusBarsPadding()
            .testTag("settings_list"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 124.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item(key = "settings_header") {
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
                                .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(12.dp))
                                .testTag("settings_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = SettingsText,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    },
                    trailing = {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x0AFFFFFF))
                                .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(8.dp))
                                .clickable {
                                    isResetting = true
                                    resetTurns += 1
                                    onResetSettings()
                                    onUpdateSettings(AppSettings())
                                    coroutineScope.launch {
                                        delay(1200)
                                        isResetting = false
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("reset_settings_button"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = SettingsAccent,
                                modifier = Modifier
                                    .size(14.dp)
                                    .rotate(resetRotation)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isResetting) "Reset!" else "Reset",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isResetting) SettingsAccent else SettingsText
                            )
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(SettingsHairline)
                )
            }
        }

        item(key = "settings_navigation") {
            SettingsGroupCard(
                title = "Parikrama & Navigation",
                icon = Icons.Default.Navigation,
                badge = "Active"
            ) {
                SettingsToggleRow(
                    label = "Live Pandal Queue Alerts",
                    checked = appSettings.queueEstimator,
                    onCheckedChange = { onUpdateSettings(appSettings.copy(queueEstimator = it)) },
                    testTag = "settings_toggle_queue_alerts"
                )
                SettingsHairlineLine()
                SettingsToggleRow(
                    label = "Offline Map Cache",
                    checked = appSettings.offlineCorridors,
                    onCheckedChange = { onUpdateSettings(appSettings.copy(offlineCorridors = it)) },
                    testTag = "settings_toggle_offline_map_cache"
                )
                SettingsHairlineLine()
                SettingsToggleRow(
                    label = "GPS High Precision Mode",
                    checked = appSettings.gpsHighPrecision,
                    onCheckedChange = { onUpdateSettings(appSettings.copy(gpsHighPrecision = it)) },
                    testTag = "settings_toggle_gps_high_precision"
                )
            }
        }

        item(key = "settings_display") {
            SettingsGroupCard(
                title = "Display & Experience",
                icon = Icons.Default.WbSunny
            ) {
                ThemeColorPicker(
                    selected = appSettings.themeStyle,
                    onSelect = { onUpdateSettings(appSettings.copy(themeStyle = it)) }
                )
                SettingsHairlineLine()
                SettingsToggleRow(
                    label = "OLED True Black Mode",
                    checked = appSettings.oledTrueBlack,
                    onCheckedChange = { onUpdateSettings(appSettings.copy(oledTrueBlack = it)) },
                    testTag = "settings_toggle_oled_true_black"
                )
                SettingsHairlineLine()
                SettingsToggleRow(
                    label = "Motion Reduction",
                    checked = appSettings.motionReduction,
                    onCheckedChange = { onUpdateSettings(appSettings.copy(motionReduction = it)) },
                    testTag = "settings_toggle_motion_reduction"
                )
                SettingsHairlineLine()
                SettingsToggleRow(
                    label = "Haptic Feedback",
                    checked = appSettings.hapticFeedback,
                    onCheckedChange = { onUpdateSettings(appSettings.copy(hapticFeedback = it)) },
                    testTag = "settings_toggle_haptic_feedback"
                )
            }
        }

        item(key = "settings_safety") {
            SettingsGroupCard(
                title = "Safety & Helpline",
                icon = Icons.Default.Shield,
                tint = SettingsRose,
                iconBackground = Color(0x33F43F5E),
                iconBorder = Color(0x4DF43F5E),
                badge = "24/7 Monitored",
                badgeTextColor = SettingsRose,
                badgeBackground = Color(0x1AF43F5E),
                badgeBorder = Color(0x33F43F5E)
            ) {
                SettingsToggleRow(
                    label = "Emergency Quick-Dial Trigger",
                    checked = appSettings.emergencySosBeacon,
                    onCheckedChange = { onUpdateSettings(appSettings.copy(emergencySosBeacon = it)) },
                    testTag = "settings_toggle_emergency_quick_dial"
                )
                SettingsHairlineLine()
                SettingsToggleRow(
                    label = "Community Reporting Sync",
                    checked = appSettings.communityReportingSync,
                    onCheckedChange = { onUpdateSettings(appSettings.copy(communityReportingSync = it)) },
                    testTag = "settings_toggle_community_reporting_sync"
                )
            }
        }

        item(key = "settings_developer_credits") {
            DeveloperCreditsCard(
                onOpenProfile = onOpenDeveloperProfile,
                onOpenLink = ::openLink
            )
        }

        item(key = "settings_system_core") {
            SystemCoreCard(
                updateCheck = updateCheck,
                onCheckForUpdates = onCheckForUpdates
            )
        }

        item(key = "settings_footer") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp)
                    .testTag("settings_devotional_footer"),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    LocalAppPalette.current.accentBorderStrong,
                                    Color.Transparent
                                )
                            )
                        )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "JAY BORO MAA",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.75.sp,
                    color = SettingsLavender,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Crafted with Devotion in Naihati",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = SettingsMutedDeep,
                    textAlign = TextAlign.Center
                )
                SectionCreditFooter(
                    topPadding = 8.dp,
                    bottomPadding = 0.dp
                )
            }
        }
    }
}

@Composable
private fun SettingsGroupCard(
    title: String,
    icon: ImageVector,
    badge: String? = null,
    tint: Color = SettingsAccent,
    iconBackground: Color = SettingsIconViolet,
    iconBorder: Color = SettingsIconBorder,
    badgeTextColor: Color = Color(0xB3CBBEFF),
    badgeBackground: Color = LocalAppPalette.current.accentSoft,
    badgeBorder: Color = LocalAppPalette.current.accentBorder,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SettingsSurface)
            .border(1.dp, SettingsOutline, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingsIconBox(
                    icon = icon,
                    tint = tint,
                    background = iconBackground,
                    border = iconBorder
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title.uppercase(),
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.6.sp,
                    color = Color(0xFFD4D4D8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (badge != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBackground)
                        .border(1.dp, badgeBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badge.uppercase(),
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp,
                        color = badgeTextColor,
                        maxLines = 1
                    )
                }
            }
        }

        SettingsHairlineLine()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsIconBox(
    icon: ImageVector,
    tint: Color,
    background: Color,
    border: Color
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .border(1.dp, border, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun SettingsToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            fontFamily = PlusJakartaSans,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            color = SettingsText
        )
        SettingsSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            testTag = testTag
        )
    }
}

@Composable
private fun SettingsSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    val thumbOffset by animateFloatAsState(
        targetValue = if (checked) 20f else 0f,
        animationSpec = tween(durationMillis = 120),
        label = "switchThumb"
    )
    val thumbColor by animateColorAsState(
        targetValue = if (checked) Color.White else Color(0xFFA1A1AA),
        animationSpec = tween(durationMillis = 120),
        label = "switchThumbColor"
    )

    Box(
        modifier = Modifier
            .size(width = 44.dp, height = 24.dp)
            .clip(CircleShape)
            .background(
                if (checked) {
                    Brush.horizontalGradient(
                        listOf(LocalAppPalette.current.accentDeep, LocalAppPalette.current.accentStrong)
                    )
                } else {
                    SolidColor(SettingsSwitchOff)
                }
            )
            .border(
                width = 1.dp,
                color = if (checked) LocalAppPalette.current.accentBorderStrong else Color(0x1AFFFFFF),
                shape = CircleShape
            )
            .clickable { onCheckedChange(!checked) }
            .padding(2.dp)
            .testTag(testTag),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(start = thumbOffset.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}

@Composable
private fun SettingsHairlineLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0x0DFFFFFF))
    )
}

@Composable
private fun DeveloperCreditsCard(
    onOpenProfile: () -> Unit,
    onOpenLink: (String) -> Unit
) {
    val profileShape = RoundedCornerShape(12.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SettingsSurface)
            .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingsIconBox(
                    icon = Icons.Default.Person,
                    tint = SettingsAccent,
                    background = SettingsIconViolet,
                    border = SettingsIconBorder
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "DEVELOPER CREDITS",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.6.sp,
                    color = Color(0xFFD4D4D8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(LocalAppPalette.current.accentSoft)
                    .border(1.dp, LocalAppPalette.current.accentBorder, CircleShape)
                    .padding(horizontal = 10.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SettingsAccent,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Verified Creator",
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SettingsLavender,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SettingsHairline)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { onOpenProfile() }
                .padding(vertical = 2.dp)
                .testTag("settings_view_profile_button"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(profileShape)
                    .background(Color(0xFF161622))
                    .border(1.dp, Color(0x1AFFFFFF), profileShape)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(DEVELOPER_AVATAR_URL)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Videsh Karmakar",
                    contentScale = ContentScale.Crop,
                    loading = { AvatarFallback() },
                    error = { AvatarFallback() },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Videsh Karmakar",
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.2).sp,
                    color = SettingsText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Founder • Lead Architect & UI Designer",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = SettingsMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Kolkata, WB",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = SettingsMutedDeep,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DeveloperQuickLink(
                label = "Portfolio",
                icon = Icons.Default.Language,
                tint = SettingsAccent,
                testTag = "settings_link_portfolio",
                onClick = { onOpenLink(PORTFOLIO_URL) },
                modifier = Modifier.weight(1f)
            )
            DeveloperQuickLink(
                label = "Spotify",
                icon = Icons.Default.MusicNote,
                tint = SettingsEmerald,
                testTag = "settings_link_spotify",
                onClick = { onOpenLink(SPOTIFY_URL) },
                modifier = Modifier.weight(1f)
            )
            DeveloperQuickLink(
                label = "Instagram",
                icon = Icons.Default.PhotoCamera,
                tint = SettingsFuchsia,
                testTag = "settings_link_instagram",
                onClick = { onOpenLink(INSTAGRAM_URL) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AvatarFallback() {
    Image(
        painter = painterResource(id = R.drawable.developer_avatar),
        contentDescription = "Videsh Karmakar",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun DeveloperQuickLink(
    label: String,
    icon: ImageVector,
    tint: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x0AFFFFFF))
            .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .testTag(testTag),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontFamily = PlusJakartaSans,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFE4E4E7),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/** Version pill shown at the top right of the App Architecture card. */
@Composable
private fun UpdateVersionBadge() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0x1A34D399))
            .border(1.dp, Color(0x4D34D399), RoundedCornerShape(999.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(SettingsEmerald)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "v${BuildConfig.VERSION_NAME} • STABLE",
            fontFamily = PlusJakartaSans,
            fontSize = 10.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = SettingsEmerald,
            maxLines = 1
        )
    }
}

/** Live status of the last update check. */
@Composable
private fun UpdateStatusChip(updateCheck: UpdateCheckState) {
    val label: String
    val tone: Color
    val chipBackground: Color
    val chipBorder: Color
    when (updateCheck) {
        is UpdateCheckState.Idle -> {
            label = "Ready"
            tone = SettingsMuted
            chipBackground = Color(0x14FFFFFF)
            chipBorder = Color(0x1AFFFFFF)
        }
        is UpdateCheckState.Checking -> {
            label = "Checking…"
            tone = SettingsAccent
            chipBackground = LocalAppPalette.current.accentSoft
            chipBorder = LocalAppPalette.current.accentBorder
        }
        is UpdateCheckState.Synced -> {
            label = "✓ Up to date"
            tone = SettingsEmerald
            chipBackground = Color(0x1A34D399)
            chipBorder = Color(0x4D34D399)
        }
        is UpdateCheckState.Offline -> {
            label = "⚠ Offline copy"
            tone = SettingsRose
            chipBackground = Color(0x1AF43F5E)
            chipBorder = Color(0x33F43F5E)
        }
    }

    Text(
        text = label,
        fontFamily = PlusJakartaSans,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = tone,
        maxLines = 1,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(chipBackground)
            .border(1.dp, chipBorder, RoundedCornerShape(999.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
private fun ThemeColorPicker(
    selected: ThemeStyle,
    onSelect: (ThemeStyle) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Theme Colour",
            fontFamily = PlusJakartaSans,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = SettingsText
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AvailablePalettes.forEach { style ->
                ThemeSwatch(
                    style = style,
                    selected = style == selected,
                    onClick = { onSelect(style) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Text(
            text = "Each theme keeps the same high-contrast text ramp, so every label stays " +
                "readable on its surfaces.",
            fontFamily = PlusJakartaSans,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            color = SettingsMutedDeep
        )
    }
}

@Composable
private fun ThemeSwatch(
    style: ThemeStyle,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)

    Column(
        modifier = modifier
            .clip(shape)
            .background(if (selected) LocalAppPalette.current.accentSoft else Color(0x0AFFFFFF))
            .border(
                width = 1.dp,
                color = if (selected) LocalAppPalette.current.accentBorderStrong else SettingsOutline,
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .testTag("settings_theme_${style.name.lowercase()}"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color(style.swatch))
                    .border(1.dp, Color(style.swatchContainer), CircleShape)
            )
            if (selected) {
                // Light disc + dark tick so the badge stays legible both on the coloured swatch
                // and on the dark card behind it, whatever the active theme is.
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color(0xFF050505),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(15.dp)
                        .background(SettingsText, CircleShape)
                        .border(1.dp, SettingsOutline, CircleShape)
                        .padding(2.dp)
                )
            }
        }
        Text(
            text = style.label,
            fontFamily = PlusJakartaSans,
            fontSize = 10.sp,
            lineHeight = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) SettingsText else SettingsMuted,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SystemCoreCard(
    updateCheck: UpdateCheckState,
    onCheckForUpdates: () -> Unit
) {
    val isChecking = updateCheck is UpdateCheckState.Checking
    val infiniteTransition = rememberInfiniteTransition(label = "updateCheckSpin")
    val spin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isChecking) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "updateCheckSpinAngle"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SettingsSurface)
            .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsIconBox(
                icon = Icons.Default.Computer,
                tint = SettingsAccent,
                background = SettingsIconViolet,
                border = SettingsIconBorder
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "APP ARCHITECTURE",
                modifier = Modifier.weight(1f),
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                color = Color(0xFFE4E4E7),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            UpdateVersionBadge()
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SettingsHairline)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF12121A))
                .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Parikrama Radar Core",
                    modifier = Modifier.weight(1f),
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SettingsText
                )
                UpdateStatusChip(updateCheck)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Channel: Stable / Production • Build ${BuildConfig.VERSION_CODE}",
                fontFamily = PlusJakartaSans,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                color = SettingsMutedDeep,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                LocalAppPalette.current.accentSurface,
                                LocalAppPalette.current.accentSurfaceStrong,
                                LocalAppPalette.current.accentSurfaceStrong
                            )
                        )
                    )
                    .border(1.dp, LocalAppPalette.current.accentBorder, RoundedCornerShape(12.dp))
                    .clickable(enabled = !isChecking) { onCheckForUpdates() }
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .testTag("check_for_updates_button"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = SettingsAccent,
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(spin)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isChecking) "Checking for updates" else "CHECK FOR UPDATES",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.3.sp,
                        color = Color(0xFFF4F4F5),
                        maxLines = 1
                    )
                }
                Text(
                    text = "• Now",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isChecking) SettingsAccent else SettingsMuted,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x4D000000))
                .border(1.dp, Color(0x0DFFFFFF), RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = SettingsAccent,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "v${BuildConfig.VERSION_NAME} Patch Notes",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp,
                    color = SettingsMuted
                )
            }

            PatchNoteBullet("Optimized low-light contrast & OLED battery efficiency")
            PatchNoteBullet("Real-time crowd radar offline fallback sync")
            PatchNoteBullet("Updated 24/7 emergency medical & municipal helplines")
        }
    }
}

@Composable
private fun PatchNoteBullet(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "•",
            fontFamily = PlusJakartaSans,
            fontSize = 12.sp,
            lineHeight = 19.5.sp,
            fontWeight = FontWeight.Bold,
            color = SettingsAccent
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontFamily = PlusJakartaSans,
            fontSize = 12.sp,
            lineHeight = 19.5.sp,
            color = Color(0xFFD4D4D8)
        )
    }
}
