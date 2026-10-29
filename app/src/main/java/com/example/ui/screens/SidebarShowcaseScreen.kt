package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ViewSidebar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SectionCreditFooter
import com.example.ui.components.SidebarNavItem
import com.example.ui.components.SleekSidebar
import com.example.ui.theme.KumartuliTeal
import com.example.model.ThemePandel
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.ObsidianBase
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianNoir
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.SacredGold
import com.example.ui.theme.TextHighWhite
import com.example.ui.theme.TextSoftZinc
import com.example.ui.theme.LocalAppPalette

enum class ShowcaseMode {
    INTERACTIVE_LIVE,
    REFERENCE_CANVAS
}

@Composable
fun SidebarShowcaseScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showcaseMode by remember { mutableStateOf(ShowcaseMode.INTERACTIVE_LIVE) }
    var isExpanded by remember { mutableStateOf(true) }
    var isDarkMode by remember { mutableStateOf(true) }
    var selectedItem by remember { mutableStateOf(SidebarNavItem.ROUTE) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(if (showcaseMode == ShowcaseMode.REFERENCE_CANVAS) Color(0xFF1D5B6E) else ObsidianNoir)
            .statusBarsPadding()
            .testTag("sidebar_showcase_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x1FFFFFFF))
                        .border(1.dp, Color(0x33FFFFFF), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextHighWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Sleek Minimal Sidebar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextHighWhite
                    )
                    Text(
                        text = "Light & Dark Mode • Collapsible Rail",
                        fontSize = 11.sp,
                        color = TextSoftZinc
                    )
                }
            }

            // Segmented mode switcher
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0x26000000))
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
                    .padding(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (showcaseMode == ShowcaseMode.INTERACTIVE_LIVE) LocalAppPalette.current.accentDeep else Color.Transparent)
                        .clickable { showcaseMode = ShowcaseMode.INTERACTIVE_LIVE }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "Live",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (showcaseMode == ShowcaseMode.REFERENCE_CANVAS) LocalAppPalette.current.accentDeep else Color.Transparent)
                        .clickable { showcaseMode = ShowcaseMode.REFERENCE_CANVAS }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "Showcase",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }

        if (showcaseMode == ShowcaseMode.INTERACTIVE_LIVE) {
            // Live Interactive Playground
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // Controls Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Expand/Collapse Toggle
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ObsidianBase)
                            .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
                            .clickable { isExpanded = !isExpanded }
                            .padding(vertical = 10.dp, horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ViewSidebar,
                                contentDescription = null,
                                tint = LocalAppPalette.current.accentBright,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isExpanded) "Expanded" else "Collapsed",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextHighWhite
                            )
                        }
                    }

                    // Light/Dark Mode Toggle
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ObsidianBase)
                            .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
                            .clickable { isDarkMode = !isDarkMode }
                            .padding(vertical = 10.dp, horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                tint = if (isDarkMode) LocalAppPalette.current.accentBright else SacredGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isDarkMode) "Dark Mode" else "Light Mode",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextHighWhite
                            )
                        }
                    }
                }

                // Main Interactive Workspace: Sidebar on left, Active view on right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Sleek Sidebar
                    SleekSidebar(
                        isExpanded = isExpanded,
                        isDarkMode = isDarkMode,
                        selectedItem = selectedItem,
                        onSelectItem = { item -> selectedItem = item },
                        onToggleExpand = { isExpanded = !isExpanded }
                    )

                    // Active Section Content Preview Card
                    Card(
                        shape = RoundedCornerShape(26.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkMode) Color(0xFF121218) else Color(0xFFF7F7F9)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .shadow(10.dp, RoundedCornerShape(26.dp))
                    ) {
                        SidebarContentDisplay(
                            selectedItem = selectedItem,
                            isDarkMode = isDarkMode
                        )
                    }
                }
            }
        } else {
            // Reference Canvas Mode
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "FOUR-STATE COMPONENT REFERENCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.9f),
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Every destination in every rail state",
                    color = Color(0xCCECECEC),
                    fontSize = 11.5.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Scrollable container with all 4 sidebars side by side
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // 1. Light Mode Collapsed
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Light • Collapsed",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        SleekSidebar(
                            isExpanded = false,
                            isDarkMode = false,
                            selectedItem = SidebarNavItem.ROUTE,
                            onSelectItem = {},
                            onToggleExpand = {}
                        )
                    }

                    // 2. Light Mode Expanded
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Light • Expanded",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        SleekSidebar(
                            isExpanded = true,
                            isDarkMode = false,
                            selectedItem = SidebarNavItem.APP_INFO_CREDIT,
                            onSelectItem = {},
                            onToggleExpand = {}
                        )
                    }

                    // 3. Dark Mode Collapsed
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Dark • Collapsed",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        SleekSidebar(
                            isExpanded = false,
                            isDarkMode = true,
                            selectedItem = SidebarNavItem.REPORT_FEEDBACK,
                            onSelectItem = {},
                            onToggleExpand = {}
                        )
                    }

                    // 4. Dark Mode Expanded
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Dark • Expanded",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        SleekSidebar(
                            isExpanded = true,
                            isDarkMode = true,
                            selectedItem = SidebarNavItem.REPORT_FEEDBACK,
                            onSelectItem = {},
                            onToggleExpand = {}
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Watermark signature from reference
                Text(
                    text = "@theveberr",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 42.sp,
                    color = Color(0x330A2B36),
                    letterSpacing = (-0.02).sp
                )
            }
        }
    }
}

@Composable
fun SidebarContentDisplay(
    selectedItem: SidebarNavItem,
    isDarkMode: Boolean
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = selectedItem.label,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) TextHighWhite else Color(0xFF16161D)
                )
                Text(
                    text = "Selected via Sleek Sidebar",
                    fontSize = 11.sp,
                    color = TextSoftZinc
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(LocalAppPalette.current.accentDeep)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = selectedItem.filledIcon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x1FFFFFFF))
        )

        when (selectedItem) {
            SidebarNavItem.ROUTE -> {
                Text(
                    text = "Route",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalAppPalette.current.accentBright
                )
                Text(
                    text = "The route guide landing view with live corridors, pandal stops, and night navigation.",
                    fontSize = 11.5.sp,
                    color = TextSoftZinc,
                    lineHeight = 16.sp
                )
            }
            SidebarNavItem.THEME_PANDEL -> {
                Text(
                    text = "Theme Pandel",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = KumartuliTeal
                )
                Text(
                    text = "Naihati Kali Puja 2026 – 13 Pandels from Naihati Swadhinata Sanadikaron Samity, Loharghat Park Association, Indira Nagar Yuva Samaj Club, 7 No. Bijoynagar Kathgola, Deolpara Amra Sabai Cluber, Deulpara Sukanta Soronijogya, Narkel Bagan Kali Puja Committee, Abhijatri Club, Gopal Smriti Sangha, Parar Sabai Klub, Katlopara, Kali Pujo Somaj Kalyan Samiti, and Gouripur Mirabagan Math.",
                    fontSize = 11.5.sp,
                    color = TextSoftZinc,
                    lineHeight = 16.sp
                )
            }
            SidebarNavItem.BORMA_PUJO_SCHEDULE -> {
                Text(
                    text = "বড়মা পুজোর নির্ঘণ্ট",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = KumartuliTeal
                )
                Text(
                    text = "অমাবস্যা তিথি শুরু, তিথি শেষ এবং নিশিত কালের প্রধান পুজোর সম্পূর্ণ সময়সূচি — শুধুমাত্র সাইডবার থেকে দেখা যায়।",
                    fontSize = 11.5.sp,
                    color = TextSoftZinc,
                    lineHeight = 16.sp
                )
            }
            SidebarNavItem.RESTAURANT -> {
                Text(
                    text = "Restaurant",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SacredGold
                )
                Text(
                    text = "Bhog kitchens, biryani houses, and late-night eateries along the Naihati parikrama corridors.",
                    fontSize = 11.5.sp,
                    color = TextSoftZinc,
                    lineHeight = 16.sp
                )
            }
            SidebarNavItem.HELPLINE -> {
                Text(
                    text = "Helpline",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = KumartuliTeal
                )
                Text(
                    text = "Police, medical, lost & found, and volunteer booth contacts for every zone of the route.",
                    fontSize = 11.5.sp,
                    color = TextSoftZinc,
                    lineHeight = 16.sp
                )
            }
            SidebarNavItem.APP_INFO_CREDIT -> {
                Text(
                    text = "App Info & Developer Credit",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SacredGold
                )
                Text(
                    text = "Version, build channel, permissions, and the full credit sheet for Videsh Karmakar, the product lead, architect, and designer behind Naihati Nights.",
                    fontSize = 11.5.sp,
                    color = TextSoftZinc,
                    lineHeight = 16.sp
                )
            }
            SidebarNavItem.DEVELOPER_CREDITS -> {
                Text(
                    text = "Developer Credits",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalAppPalette.current.accentBright
                )
                Text(
                    text = "The dedicated credit sheet for Videsh Karmakar: product lead, architect, and designer, with links to the developer's work and social profiles.",
                    fontSize = 11.5.sp,
                    color = TextSoftZinc,
                    lineHeight = 16.sp
                )
            }
            SidebarNavItem.SETTINGS -> {
                Text(
                    text = "Settings",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalAppPalette.current.accentBright
                )
                Text(
                    text = "Tune route alerts, offline corridors, queue estimates, and accessibility preferences for your parikrama.",
                    fontSize = 11.5.sp,
                    color = TextSoftZinc,
                    lineHeight = 16.sp
                )
            }
            SidebarNavItem.CHECK_UPDATES -> {
                Text(
                    text = "Check Updates",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalAppPalette.current.accentBright
                )
                Text(
                    text = "Ask GitHub Releases for a newer Naihati Nights build, read the release notes, and install the update without leaving the app.",
                    fontSize = 11.5.sp,
                    color = TextSoftZinc,
                    lineHeight = 16.sp
                )
            }
            SidebarNavItem.REPORT_FEEDBACK -> {
                Text(
                    text = "Report & Feedback",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalAppPalette.current.accentBright
                )
                Text(
                    text = "Raise route issues, pandal updates, lost & found reports, or suggestions for the Naihati Nights guide.",
                    fontSize = 11.5.sp,
                    color = TextSoftZinc,
                    lineHeight = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        SectionCreditFooter(
            topPadding = 0.dp,
            bottomPadding = 8.dp
        )
    }
}

@Composable
internal fun DevotionalNoticeSlot(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            fontFamily = FontFamily.Default,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextHighWhite,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = subtitle,
            fontFamily = FontFamily.Default,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextSoftZinc,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }
}
