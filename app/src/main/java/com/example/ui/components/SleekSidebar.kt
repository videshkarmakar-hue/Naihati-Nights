package com.example.ui.components

import kotlin.collections.List

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
import androidx.compose.material.icons.automirrored.outlined.AltRoute
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans
import com.example.model.ThemePandel
import com.example.model.ThemePandelData

/** Brand mark shown at the top of the sidebar header. */
private const val SIDEBAR_BRAND_LOGO_URL =
    "https://cdn.phototourl.com/member/2026-10-07-3560ff11-c4ca-4914-ada1-bb03390ea789.png"

enum class SidebarNavItem(
    val label: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val testTag: String
) {
    ROUTE("Route", Icons.AutoMirrored.Filled.AltRoute, Icons.AutoMirrored.Outlined.AltRoute, "sidebar_item_route"),
    RESTAURANT("Restaurant", Icons.Filled.Restaurant, Icons.Outlined.Restaurant, "sidebar_item_restaurant"),
    HELPLINE("Helpline", Icons.Filled.Call, Icons.Outlined.Call, "sidebar_item_helpline"),
    THEME_PANDEL("Theme Pandel", Icons.Filled.Palette, Icons.Outlined.Palette, "sidebar_item_theme_pandel"),
    BORMA_PUJO_SCHEDULE("বড়মা পুজোর নির্ঘণ্ট", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth, "sidebar_item_borma_pujo_schedule"),
    APP_INFO_CREDIT("App Info & Developer Credit", Icons.Filled.Info, Icons.Outlined.Info, "sidebar_item_app_info_credit"),
    CHECK_UPDATES("Check Updates", Icons.Filled.SystemUpdate, Icons.Outlined.SystemUpdate, "sidebar_item_check_updates"),
    DEVELOPER_CREDITS("Developer Credits", Icons.Filled.Verified, Icons.Outlined.Verified, "sidebar_item_developer_credits"),
    SETTINGS("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, "sidebar_item_settings"),
    REPORT_FEEDBACK("Report & Feedback", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline, "sidebar_item_report_feedback");

    companion object {
        val primaryItems = listOf(ROUTE, RESTAURANT, HELPLINE)
        val themeItems = listOf(THEME_PANDEL, BORMA_PUJO_SCHEDULE)
        val utilityItems = listOf(APP_INFO_CREDIT, CHECK_UPDATES, SETTINGS, REPORT_FEEDBACK)
        val developerItems = listOf(DEVELOPER_CREDITS)
    }
}

@Composable
fun SleekSidebar(
    isExpanded: Boolean,
    isDarkMode: Boolean,
    selectedItem: SidebarNavItem?,
    onSelectItem: (SidebarNavItem) -> Unit,
    onToggleExpand: () -> Unit,
    hasUpdate: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Exact palette extracted from reference:
    // Light mode:
    val lightBg = Color(0xFFECECEC)
    val lightUnselected = Color(0xFF1E1E1E)
    val lightActivePill = Color(0xFF1E1E1E)
    val lightActiveContent = Color(0xFFFFFFFF)
    val lightHandle = Color(0xFF707070)

    // Dark mode:
    val darkBg = Color(0xFF1B1B1B)
    val darkUnselected = Color(0xFFD4D4D4)
    val darkActivePill = Color(0xFFECECEC)
    val darkActiveContent = Color(0xFF1B1B1B)
    val darkHandle = Color(0xFF888888)

    val currentBg = if (isDarkMode) darkBg else lightBg
    val unselectedColor = if (isDarkMode) darkUnselected else lightUnselected
    val activePillBg = if (isDarkMode) darkActivePill else lightActivePill
    val activeContentColor = if (isDarkMode) darkActiveContent else lightActiveContent
    val handleColor = if (isDarkMode) darkHandle else lightHandle

    val animatedWidth by animateDpAsState(
        targetValue = if (isExpanded) 220.dp else 68.dp,
        animationSpec = tween(durationMillis = 160),
        label = "sidebarWidth"
    )

    val newestTheme = ThemePandelData.newest
    val newThemeNumber = newestTheme?.let { ThemePandelData.displayNumber(it) }

    Box(
        modifier = modifier
            .width(animatedWidth)
            .clip(RoundedCornerShape(26.dp))
            .background(currentBg)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = Color.Black.copy(alpha = 0.4f),
                spotColor = Color.Black.copy(alpha = 0.6f)
            )
            .padding(horizontal = 10.dp, vertical = 18.dp)
            .testTag("sleek_sidebar_container")
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = if (isExpanded) Alignment.Start else Alignment.CenterHorizontally
        ) {
            // Brand Header: app logo + app name (tap anywhere to expand/collapse)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onToggleExpand() }
                    .padding(horizontal = if (isExpanded) 8.dp else 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(SIDEBAR_BRAND_LOGO_URL)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Naihati Nights",
                    contentScale = ContentScale.Crop,
                    loading = { BrandLogoFallback() },
                    error = { BrandLogoFallback() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, unselectedColor.copy(alpha = 0.8f), CircleShape)
                        .testTag("sidebar_brand_logo")
                )

                if (isExpanded) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Naihati Nights",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = unselectedColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Primary destinations
            SidebarItemGroup(
                items = SidebarNavItem.primaryItems,
                isExpanded = isExpanded,
                selectedItem = selectedItem,
                activePillBg = activePillBg,
                activeContentColor = activeContentColor,
                unselectedColor = unselectedColor,
                onSelectItem = onSelectItem
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Theme Pandel — festival edition picker
            SidebarItemGroup(
                items = SidebarNavItem.themeItems,
                isExpanded = isExpanded,
                selectedItem = selectedItem,
                activePillBg = activePillBg,
                activeContentColor = activeContentColor,
                unselectedColor = unselectedColor,
                onSelectItem = onSelectItem,
                newThemeNumber = newThemeNumber
            )

            if (isExpanded && newestTheme != null && newThemeNumber != null) {
                Spacer(modifier = Modifier.height(8.dp))
                ThemePandelNewHighlight(
                    panel = newestTheme,
                    number = newThemeNumber,
                    onClick = { onSelectItem(SidebarNavItem.THEME_PANDEL) }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Divider between primary and utility
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (isExpanded) 8.dp else 12.dp)
                    .height(1.dp)
                    .background(unselectedColor.copy(alpha = 0.18f))
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Utilities
            SidebarItemGroup(
                items = SidebarNavItem.utilityItems,
                isExpanded = isExpanded,
                selectedItem = selectedItem,
                activePillBg = activePillBg,
                activeContentColor = activeContentColor,
                unselectedColor = unselectedColor,
                hasUpdate = hasUpdate,
                onSelectItem = onSelectItem
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Divider between utilities and the dedicated developer credits section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (isExpanded) 8.dp else 12.dp)
                    .height(1.dp)
                    .background(unselectedColor.copy(alpha = 0.18f))
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Developer credits
            SidebarItemGroup(
                items = SidebarNavItem.developerItems,
                isExpanded = isExpanded,
                selectedItem = selectedItem,
                activePillBg = activePillBg,
                activeContentColor = activeContentColor,
                unselectedColor = unselectedColor,
                onSelectItem = onSelectItem
            )

            Spacer(modifier = Modifier.weight(1f))

            // Sidebar footer signature credit
            if (isExpanded) {
                Text(
                    text = "©Videsh Karmakar Project",
                    fontFamily = PlusJakartaSans,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = handleColor,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 2.dp)
                        .testTag("sidebar_footer_credit")
                )
            }
        }
    }
}

/**
 * Compact card under the Theme Pandel slot that names the newest committee and its pandal theme,
 * so the addition is readable straight from the sidebar.
 */
@Composable
private fun ThemePandelNewHighlight(
    panel: ThemePandel,
    number: Int,
    onClick: () -> Unit
) {
    val palette = LocalAppPalette.current
    val shape = RoundedCornerShape(12.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(palette.accentSurface)
            .border(1.dp, palette.accentBorder, shape)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .testTag("sidebar_theme_new_card"),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = "NEW • THEME $number",
            fontFamily = PlusJakartaSans,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.6.sp,
            color = palette.accentBright,
            maxLines = 1
        )
        Text(
            text = panel.committeeBengali.ifBlank { panel.committee },
            fontFamily = FontFamily.Default,
            fontSize = 10.5.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = palette.text,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = panel.pandalBengali.ifBlank { panel.pandal },
            fontFamily = FontFamily.Default,
            fontSize = 10.sp,
            lineHeight = 13.sp,
            fontWeight = FontWeight.Bold,
            color = palette.accentBright,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Festival edition card shown inside the Theme Pandel slot.
 */
@Composable
private fun ThemePandelCard(
    panel: ThemePandel,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LocalAppPalette.current.accentSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, LocalAppPalette.current.accentBorder),
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = panel.committee.uppercase(),
                fontFamily = PlusJakartaSans,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.4.sp,
                color = LocalAppPalette.current.accentBright,
                maxLines = 1
            )
            Text(
                text = panel.edition,
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = LocalAppPalette.current.text,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Pandal: ${panel.pandal} • ${panel.deity}",
                fontFamily = PlusJakartaSans,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = LocalAppPalette.current.textMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** Shown in place of the brand mark while the remote logo is loading or unavailable. */
@Composable
private fun BrandLogoFallback() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppPalette.current.accentDeep),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "N",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = LocalAppPalette.current.accentBright
        )
    }
}

@Composable
private fun SidebarItemGroup(
    items: List<SidebarNavItem>,
    isExpanded: Boolean,
    selectedItem: SidebarNavItem?,
    activePillBg: Color,
    activeContentColor: Color,
    unselectedColor: Color,
    onSelectItem: (SidebarNavItem) -> Unit,
    hasUpdate: Boolean = false,
    newThemeNumber: Int? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = if (isExpanded) Alignment.Start else Alignment.CenterHorizontally
    ) {
        items.forEach { item ->
            SidebarButton(
                item = item,
                isSelected = selectedItem == item,
                isExpanded = isExpanded,
                activePillBg = activePillBg,
                activeContentColor = activeContentColor,
                unselectedColor = unselectedColor,
                showUpdateDot = hasUpdate && item == SidebarNavItem.CHECK_UPDATES,
                newBadgeText = if (item == SidebarNavItem.THEME_PANDEL && newThemeNumber != null) {
                    "NEW $newThemeNumber"
                } else {
                    null
                },
                onClick = { onSelectItem(item) }
            )
        }
    }
}

@Composable
private fun SidebarButton(
    item: SidebarNavItem,
    isSelected: Boolean,
    isExpanded: Boolean,
    activePillBg: Color,
    activeContentColor: Color,
    unselectedColor: Color,
    onClick: () -> Unit,
    showUpdateDot: Boolean = false,
    newBadgeText: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    val contentColor = if (isSelected) activeContentColor else unselectedColor

    val icon = if (isSelected) item.filledIcon else item.outlinedIcon

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) activePillBg else Color.Transparent)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
            .padding(horizontal = if (isExpanded) 12.dp else 0.dp, vertical = 9.dp)
            .testTag(item.testTag),
        contentAlignment = if (isExpanded) Alignment.CenterStart else Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = item.label,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )

            if (showUpdateDot) {
                Box(
                    modifier = Modifier
                        .padding(start = 3.dp)
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(LocalAppPalette.current.accentBright)
                        .testTag("sidebar_update_dot")
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = item.label,
                    fontFamily = PlusJakartaSans,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    fontSize = 13.sp,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (newBadgeText != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .height(15.dp)
                            .widthIn(min = 26.dp)
                            .clip(CircleShape)
                            .background(LocalAppPalette.current.accentBright)
                            .testTag("sidebar_theme_new_badge"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = newBadgeText,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.sp,
                            letterSpacing = 0.4.sp,
                            color = Color(0xFF0B0B10),
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                }
                if (showUpdateDot) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "NEW",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.sp,
                        letterSpacing = 0.6.sp,
                        color = LocalAppPalette.current.accentBright,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(LocalAppPalette.current.accentSoft)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
