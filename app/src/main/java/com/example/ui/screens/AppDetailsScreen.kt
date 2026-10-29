package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.R
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans

private val DetailsNoir = Color(0xFF050505)
private val DetailsSurface = Color(0xFF0E0E14)
private val DetailsCard = Color(0xFF14141C)
private val DetailsViolet: Color @Composable get() = LocalAppPalette.current.accentDeep
private val DetailsMutedViolet: Color @Composable get() = LocalAppPalette.current.accentSurface
private val DetailsLightViolet: Color @Composable get() = LocalAppPalette.current.accentBright
private val DetailsAccentViolet: Color @Composable get() = LocalAppPalette.current.accent
private val DetailsBorder = Color(0x12FFFFFF)
private val DetailsVioletBorder: Color @Composable get() = LocalAppPalette.current.accentBorderStrong
private val DetailsText = Color(0xFFD4D4D8)
private val DetailsTextHigh = Color(0xFFF4F4F5)
private val DetailsTextMuted = Color(0xFFA1A1AA)
private val DetailsSectionHeading = Color(0xFFE4E4E7)

private data class LegalInfo(
    val title: String,
    val body: String
)

private val PrivacyPolicy = LegalInfo(
    title = "Privacy Policy",
    body = """
        Naihati Nights is a local-first festival guide. You do not need an account to use it.

        The app does not sell personal data, show targeted advertising, or collect precise location in the background.

        When you submit a report through the Feedback screen, the details you enter — inquiry type, zone, landmark, name, contact number, report details, urgency and an optional photo — are stored on this device and may be sent to the developer only to review and resolve that report. Photos are never uploaded automatically.

        You can remove locally stored reports by clearing the app's data in Android settings. Map links open your installed maps application under that app's own privacy policy.
    """.trimIndent()
)

private val TermsAndConditions = LegalInfo(
    title = "Terms & Conditions",
    body = """
        Naihati Nights is a free, unofficial devotional and navigation guide for Naihati Kali Puja 2026. It is not affiliated with any puja committee, emergency service, or government body.

        Pandal, route, and timing information is compiled with care but can change during the festival. Always follow local authorities, volunteers, and puja committee instructions. Do not rely on this app for emergency response; use the Helpline screen or call public emergency services.

        The app, its original interface, copy, and design system are the intellectual property of Videsh Karmakar. Public festival names and locations remain the property of their respective committees and communities.
    """.trimIndent()
)

private val DataAndLocation = LegalInfo(
    title = "Data & Location Information",
    body = """
        Location features are optional. When you tap a map or route action, Naihati Nights hands the selected destination to your installed maps application. It does not track your location in the background or build a location history.

        Guide data is cached on this device so routes, helplines, and restaurant information remain available when connectivity is poor. No location history is sold or shared with advertisers.
    """.trimIndent()
)

private val ThirdPartyLicenses = LegalInfo(
    title = "Third-Party Licenses",
    body = """
        Naihati Nights is built with Kotlin, Jetpack Compose, AndroidX, Room, Retrofit, Coil, and Google Material Icons. These components are used under the Apache License 2.0 and MIT licenses.

        The Boro Maa emblem is used with respect for the festival tradition. Naihati Nights is not affiliated with Google, any puja committee, or any emergency service.
    """.trimIndent()
)

private val CopyrightInformation = LegalInfo(
    title = "Copyright Information",
    body = """
        © 2026 Videsh Karmakar. All rights reserved.

        Naihati Nights, its original interface, copy, and design system may not be copied, redistributed, or resold without written permission.

        Puja names, locations, and public festival information remain the property of their respective committees and communities.
    """.trimIndent()
)

@Composable
fun AppDetailsScreen(
    onBack: () -> Unit,
    onOpenFeedback: () -> Unit,
    onOpenHome: () -> Unit = {},
    onOpenSidebar: () -> Unit = {},
    onOpenDeveloperCredits: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var legalInfo by remember { mutableStateOf<LegalInfo?>(null) }
    var showLocationInfo by remember { mutableStateOf(false) }

    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(fontFamily = PlusJakartaSans)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(DetailsNoir),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 448.dp)
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .testTag("app_details_list"),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 20.dp,
                    bottom = 128.dp
                )
            ) {
                item {
                    AppDetailsHeader(
                        onBack = onBack,
                        onShare = { shareAppDetails(context) },
                        onOpenSidebar = onOpenSidebar,
                        onOpenDeveloperCredits = onOpenDeveloperCredits
                    )
                }
                item { Spacer(modifier = Modifier.height(12.dp)) }
                item { AppDetailsHeroCard() }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item { AppInformationSection() }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item { AboutNaihatiNightsSection() }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    DiscoverNaihatiSection(
                        onOpenHome = onOpenHome,
                        onOpenMap = { openMaps(context, "Naihati Kali Puja Pandals") },
                        onOpenLocation = { showLocationInfo = true }
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    LegalAndSupportSection(
                        onOpenLegal = { legalInfo = it },
                        onOpenFeedback = onOpenFeedback,
                        onOpenContact = {
                            openExternalUrl(
                                context = context,
                                url = "https://videshkarmakar.carrd.co/#contact",
                                failureMessage = "No browser available to open contact support."
                            )
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item { AcknowledgementSection() }
                item { AppDetailsFooter(onOpenFeedback = onOpenFeedback) }
            }

            legalInfo?.let { info ->
                LegalInfoDialog(
                    info = info,
                    onDismiss = { legalInfo = null }
                )
            }

            if (showLocationInfo) {
                LocationInfoDialog(
                    onDismiss = { showLocationInfo = false },
                    onOpenMaps = {
                        showLocationInfo = false
                        openMaps(context, "Naihati Kali Puja Pandals")
                    }
                )
            }
        }
    }
}

@Composable
private fun AppDetailsHeader(
    onBack: () -> Unit,
    onShare: () -> Unit,
    onOpenSidebar: () -> Unit,
    onOpenDeveloperCredits: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AppBrandHeader(
            onOpenSidebar = onOpenSidebar,
            onOpenDeveloperCredits = onOpenDeveloperCredits,
            navigation = {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(DetailsCard)
                        .border(1.dp, DetailsBorder, RoundedCornerShape(12.dp))
                        .clickable(onClick = onBack)
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .testTag("app_details_back_button"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BackChevron()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Back",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = DetailsText
                    )
                }
            },
            trailing = {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DetailsCard)
                        .border(1.dp, DetailsBorder, RoundedCornerShape(12.dp))
                        .clickable(onClick = onShare)
                        .testTag("app_details_share_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share details",
                        tint = DetailsLightViolet,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x0DFFFFFF))
        )
    }
}

@Composable
private fun BackChevron() {
    // Resolved outside the draw scope: Canvas lambdas are not composable.
    val chevronTint = LocalAppPalette.current.accentBright
    Canvas(modifier = Modifier.size(16.dp)) {
        // The supplied design uses a 24x24 chevron viewBox inside a 16dp icon.
        val scale = size.width / 24f
        val stroke = 2.2f * scale
        val tip = Offset(8f * scale, 12f * scale)
        drawLine(
            color = chevronTint,
            start = Offset(15f * scale, 19f * scale),
            end = tip,
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = chevronTint,
            start = tip,
            end = Offset(15f * scale, 5f * scale),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun AppDetailsHeroCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DetailsSurface)
            .border(1.dp, DetailsBorder, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black,
                        spotColor = Color.Black
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(DetailsCard)
                    .border(1.dp, DetailsVioletBorder, RoundedCornerShape(16.dp))
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splash_trishul),
                    contentDescription = "Naihati Nights App Icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(DetailsMutedViolet)
                        .border(1.dp, LocalAppPalette.current.accentBorder, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "OFFICIAL GUIDE",
                        fontSize = 9.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DetailsLightViolet,
                        letterSpacing = 0.9.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "NAIHATI  NIGHTS",
                    fontSize = 20.sp,
                    lineHeight = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Discover the Night. Experience Naihati.",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = DetailsLightViolet
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x0DFFFFFF))
        )
        Spacer(modifier = Modifier.height(14.dp))

        HeroParagraph(
            "Naihati Nights is a modern digital guide created to help you discover, explore, navigate, and experience the spectacular world of Naihati Kali Puja."
        )
        Spacer(modifier = Modifier.height(10.dp))
        HeroParagraph(
            "From the legendary Boro Maa to the city’s most renowned Kali Puja pandals, Naihati Nights brings the festive landscape of Naihati together in one convenient place."
        )
        Spacer(modifier = Modifier.height(10.dp))
        HeroParagraph(
            "Built around discovery, navigation, accessibility, and celebration, the app makes it easier to find famous pandals, explore new destinations, understand locations, and plan your festive journey."
        )
        Spacer(modifier = Modifier.height(10.dp))
        HeroParagraph(
            "Every part of Naihati Nights is designed to make your Kali Puja experience simpler, smarter, and more memorable."
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Discover. Navigate. Experience.",
            fontSize = 12.sp,
            lineHeight = 19.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = DetailsLightViolet,
            letterSpacing = 0.3.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun HeroParagraph(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        lineHeight = 19.5.sp,
        color = DetailsText
    )
}

@Composable
private fun AppInformationSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "APP INFORMATION",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.6.sp,
            color = DetailsSectionHeading,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoCard(
                    label = "APPLICATION",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    SimpleInfoValue(
                        text = "Naihati Nights",
                        color = Color.White
                    )
                }
                InfoCard(
                    label = "APP CATEGORY",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    SimpleInfoValue("Festival Guide & Pandal Discovery")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoCard(
                    label = "PLATFORM",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    PlatformValue()
                }
                InfoCard(
                    label = "VERSION & BUILD",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    SimpleInfoValue("${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoCard(
                    label = "RELEASE DATE",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    SimpleInfoValue("7 October 2026")
                }
                InfoCard(
                    label = "DEVELOPED & DESIGNED BY",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    SimpleInfoValue("Videsh Karmakar")
                }
            }

            InfoCard(
                label = "UI/UX & PRODUCT DESIGN",
                modifier = Modifier.fillMaxWidth()
            ) {
                SimpleInfoValue(
                    text = "Figma & Stitch",
                    color = DetailsLightViolet
                )
            }
        }
    }
}

@Composable
private fun InfoCard(
    label: String,
    modifier: Modifier = Modifier,
    value: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DetailsSurface)
            .border(1.dp, DetailsBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            lineHeight = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DetailsTextMuted,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        value()
    }
}

@Composable
private fun SimpleInfoValue(
    text: String,
    color: Color = DetailsTextHigh
) {
    Text(
        text = text,
        fontSize = 12.sp,
        lineHeight = 15.sp,
        fontWeight = FontWeight.Bold,
        color = color
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PlatformValue() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = "Android",
            fontSize = 12.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Bold,
            color = DetailsLightViolet
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(DetailsMutedViolet)
                .border(1.dp, LocalAppPalette.current.accentBorder, RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "iOS Soon",
                fontSize = 9.sp,
                lineHeight = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = DetailsAccentViolet
            )
        }
    }
}

@Composable
private fun AboutNaihatiNightsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DetailsSurface)
            .border(1.dp, DetailsBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "ABOUT NAIHATI NIGHTS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.6.sp,
            color = DetailsLightViolet
        )
        Spacer(modifier = Modifier.height(10.dp))

        AboutParagraph(
            "Naihati Nights is built to create a complete digital experience for Naihati Kali Puja."
        )
        Spacer(modifier = Modifier.height(8.dp))
        AboutParagraph(
            "The app combines pandal discovery, location information, mapping, navigation, and essential festival details into a single intuitive experience."
        )
        Spacer(modifier = Modifier.height(8.dp))
        AboutParagraph(
            "Whether you're a lifelong resident of Naihati, returning home for Kali Puja, or visiting the city for the first time, Naihati Nights helps you discover the celebrations that matter most."
        )
        Spacer(modifier = Modifier.height(8.dp))
        AboutParagraph(
            "From iconic destinations to new places waiting to be explored, the goal is simple:"
        )
        Spacer(modifier = Modifier.height(8.dp))
        AboutParagraph(
            text = "Make every night easier to discover and every celebration easier to experience.",
            color = DetailsTextHigh,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.padding(top = 4.dp)) {
            Text(
                text = "Simple. Informative. Connected. Immersive.",
                fontSize = 12.sp,
                lineHeight = 19.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = DetailsLightViolet
            )
            Text(
                text = "That philosophy guides every part of Naihati Nights.",
                fontSize = 11.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Normal,
                color = DetailsTextMuted
            )
        }
    }
}

@Composable
private fun AboutParagraph(
    text: String,
    color: Color = DetailsText,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        fontSize = 12.sp,
        lineHeight = 19.5.sp,
        fontWeight = fontWeight,
        color = color
    )
}

@Composable
private fun DiscoverNaihatiSection(
    onOpenHome: () -> Unit,
    onOpenMap: () -> Unit,
    onOpenLocation: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = 4.dp)) {
            Text(
                text = "DISCOVER NAIHATI",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.6.sp,
                color = DetailsSectionHeading
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Explore some of Naihati's most celebrated Kali Puja destinations.",
                fontSize = 11.sp,
                lineHeight = 18.sp,
                color = DetailsTextMuted
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DiscoverItem(
                title = "Famous Pandals",
                description = "Discover renowned pandals and iconic puja celebrations.",
                testTag = "app_details_discover_pandals",
                onClick = onOpenHome
            )
            DiscoverItem(
                title = "Boro Maa",
                description = "Explore information about the legendary Boro Maa and her celebrated Kali Puja tradition.",
                testTag = "app_details_discover_boro_maa",
                onClick = onOpenHome
            )
            DiscoverItem(
                title = "Pandal Map",
                description = "Find pandals and festive destinations through an organized map experience.",
                testTag = "app_details_discover_map",
                onClick = onOpenMap
            )
            DiscoverItem(
                title = "Location Information",
                description = "Access important location and destination details in one place.",
                testTag = "app_details_discover_location",
                onClick = onOpenLocation
            )
            DiscoverItem(
                title = "Explore More",
                description = "Discover celebrations beyond the places you already know.",
                testTag = "app_details_discover_explore",
                onClick = onOpenHome
            )
            DiscoverItem(
                title = "Plan Your Night",
                description = "Organize your pandal-hopping experience and make the most of your time.",
                testTag = "app_details_discover_plan",
                onClick = onOpenHome
            )
        }
    }
}

@Composable
private fun DiscoverItem(
    title: String,
    description: String,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DetailsSurface)
            .border(1.dp, DetailsBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
            .padding(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = DetailsTextHigh
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = description,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            color = DetailsTextMuted
        )
    }
}

@Composable
private fun LegalAndSupportSection(
    onOpenLegal: (LegalInfo) -> Unit,
    onOpenFeedback: () -> Unit,
    onOpenContact: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = 4.dp)) {
            Text(
                text = "LEGAL & SUPPORT",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.6.sp,
                color = DetailsSectionHeading
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Naihati Nights is committed to providing a transparent, reliable, and responsible digital experience. The following resources are available for information, assistance, and accountability:",
                fontSize = 11.sp,
                lineHeight = 18.sp,
                color = DetailsTextMuted
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            LegalItem(
                title = "Privacy Policy",
                description = "Learn how Naihati Nights collects, uses, and protects user information.",
                testTag = "app_details_legal_privacy",
                onClick = { onOpenLegal(PrivacyPolicy) }
            )
            LegalItem(
                title = "Terms & Conditions",
                description = "Review the terms governing the use of the Naihati Nights application and services.",
                testTag = "app_details_legal_terms",
                onClick = { onOpenLegal(TermsAndConditions) }
            )
            LegalItem(
                title = "Data & Location Information",
                description = "Learn how location-related features and information are used within the application.",
                testTag = "app_details_legal_data",
                onClick = { onOpenLegal(DataAndLocation) }
            )
            LegalItem(
                title = "Third-Party Licenses",
                description = "Review acknowledgements and licenses related to third-party technologies and services.",
                testTag = "app_details_legal_licenses",
                onClick = { onOpenLegal(ThirdPartyLicenses) }
            )
            LegalItem(
                title = "Contact Support",
                description = "Get in touch with the Naihati Nights support team for assistance.",
                testTag = "app_details_legal_contact",
                onClick = onOpenContact
            )
            LegalItem(
                title = "Report a Problem",
                description = "Report bugs, incorrect information, technical issues, or unexpected behavior.",
                testTag = "app_details_legal_report",
                onClick = onOpenFeedback
            )
            LegalItem(
                title = "Copyright Information",
                description = "View copyright notices and intellectual-property information associated with Naihati Nights.",
                testTag = "app_details_legal_copyright",
                onClick = { onOpenLegal(CopyrightInformation) }
            )
        }
    }
}

@Composable
private fun LegalItem(
    title: String,
    description: String,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DetailsSurface)
            .border(1.dp, DetailsBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DetailsTextHigh
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                color = DetailsTextMuted
            )
        }
        Text(
            text = "→",
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DetailsTextMuted,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
        )
    }
}

@Composable
private fun AcknowledgementSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DetailsSurface)
            .border(1.dp, DetailsBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "ACKNOWLEDGEMENT",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.6.sp,
            color = DetailsLightViolet
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Naihati Nights is created with love and respect for the culture, traditions, devotion, and vibrant spirit of Naihati Kali Puja.",
            fontSize = 12.sp,
            lineHeight = 19.5.sp,
            color = DetailsText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We acknowledge the dedication of puja committees, volunteers, artists, organizers, and the people of Naihati who make these celebrations extraordinary.",
            fontSize = 12.sp,
            lineHeight = 19.5.sp,
            color = DetailsText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "With gratitude to everyone who keeps the spirit of Naihati alive.",
            fontSize = 12.sp,
            lineHeight = 19.5.sp,
            fontWeight = FontWeight.Medium,
            color = DetailsLightViolet
        )
    }
}

@Composable
private fun AppDetailsFooter(onOpenFeedback: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FeedbackButton(onClick = onOpenFeedback)
        VersionCard()
        SectionCreditFooter(
            topPadding = 0.dp,
            bottomPadding = 0.dp
        )
        Text(
            text = "• JAY MAA BORO MAA •",
            fontSize = 10.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = DetailsLightViolet,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FeedbackButton(onClick: () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.99f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "appDetailsFeedbackScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = LocalAppPalette.current.accentSoft,
                spotColor = LocalAppPalette.current.accentSoft
            )
            .clip(shape)
            .background(DetailsViolet)
            .border(1.dp, LocalAppPalette.current.accentBorder, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .testTag("app_details_feedback_button")
            .padding(horizontal = 16.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Sms,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Send Feedback or Suggestions",
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun VersionCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DetailsSurface)
            .border(1.dp, DetailsBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x0DFFFFFF))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "© 2026 Videsh Karmakar. All rights reserved.",
            fontSize = 11.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium,
            color = DetailsTextMuted,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "NAIHATI NIGHTS • Discover the Night. Experience Naihati.",
            fontSize = 10.sp,
            lineHeight = 14.sp,
            color = DetailsTextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LegalInfoDialog(
    info: LegalInfo,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("app_details_info_dialog"),
        title = {
            Text(
                text = info.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DetailsTextHigh
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 360.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = info.body,
                    fontSize = 12.sp,
                    lineHeight = 19.sp,
                    color = DetailsText
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Close",
                    color = DetailsLightViolet,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = DetailsSurface,
        titleContentColor = DetailsTextHigh,
        textContentColor = DetailsText,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun LocationInfoDialog(
    onDismiss: () -> Unit,
    onOpenMaps: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("app_details_location_dialog"),
        title = {
            Text(
                text = "Location Information",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DetailsTextHigh
            )
        },
        text = {
            Text(
                text = "Naihati is in North 24 Parganas, West Bengal. The guide covers Boro Maa, Naihati Station, and the seven parikrama corridors. Open Maps for directions and live festival locations.",
                fontSize = 12.sp,
                lineHeight = 19.sp,
                color = DetailsText
            )
        },
        confirmButton = {
            TextButton(onClick = onOpenMaps) {
                Text(
                    text = "Open in Maps",
                    color = DetailsLightViolet,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Close",
                    color = DetailsTextMuted
                )
            }
        },
        containerColor = DetailsSurface,
        titleContentColor = DetailsTextHigh,
        textContentColor = DetailsText,
        shape = RoundedCornerShape(16.dp)
    )
}

private fun shareAppDetails(context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "Naihati Nights — Official Naihati Kali Puja 2026 Guide\n" +
                "Discover. Navigate. Experience.\n" +
                "https://videshkarmakar.carrd.co/"
        )
    }
    try {
        context.startActivity(Intent.createChooser(shareIntent, "Share Naihati Nights"))
    } catch (error: Exception) {
        Toast.makeText(context, "No app available to share details.", Toast.LENGTH_SHORT).show()
    }
}

private fun openExternalUrl(
    context: Context,
    url: String,
    failureMessage: String
) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (error: Exception) {
        Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
    }
}

private fun openMaps(context: Context, query: String) {
    openExternalUrl(
        context = context,
        url = "https://www.google.com/maps/search/?api=1&query=" + Uri.encode(query),
        failureMessage = "No maps app available."
    )
}
