package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans

private val CreditsNoir = Color(0xFF050505)
private val CreditsSurface = Color(0xFF0E0E14)
private val CreditsCard = Color(0xFF14141C)
private val CreditsRaised = Color(0xFF1B1B26)
private val CreditsViolet: Color @Composable get() = LocalAppPalette.current.accentDeep
private val CreditsVioletHover: Color @Composable get() = LocalAppPalette.current.accentStrong
private val CreditsMutedViolet: Color @Composable get() = LocalAppPalette.current.accentSurface
private val CreditsLightViolet: Color @Composable get() = LocalAppPalette.current.accentBright
private val CreditsAccentViolet: Color @Composable get() = LocalAppPalette.current.accent
private val CreditsBorder = Color(0x12FFFFFF)
private val CreditsVioletBorder: Color @Composable get() = LocalAppPalette.current.accentBorderStrong
private val CreditsText = Color(0xFFD4D4D8)
private val CreditsTextHigh = Color(0xFFF4F4F5)
private val CreditsTextMuted = Color(0xFFA1A1AA)
private val CreditsSectionHeading = Color(0xFFE4E4E7)
private val CreditsEmerald = Color(0xFF10B981)
private val CreditsEmeraldLight = Color(0xFF34D399)

/** Avatar supplied by the developer for the credits page. */
private const val DEVELOPER_AVATAR_URL =
    "https://cdn.phototourl.com/member/2026-09-24-d3b8ff6f-f0d4-43e0-9082-9f39cc851774.png"

private data class DeveloperLink(
    val title: String,
    val subtitle: String,
    val url: String,
    val testTag: String
)

private val developerLinks = listOf(
    DeveloperLink(
        title = "Official Website",
        subtitle = "videshkarmakar.carrd.co",
        url = "https://videshkarmakar.carrd.co/",
        testTag = "dev_credits_link_website"
    ),
    DeveloperLink(
        title = "Spotify",
        subtitle = "Videsh Karmakar — Artist Profile",
        url = "https://open.spotify.com/artist/2eJEzi0mzJB60zVJf0OjWP",
        testTag = "dev_credits_link_spotify"
    ),
    DeveloperLink(
        title = "Instagram",
        subtitle = "@code.red_videsh",
        url = "https://www.instagram.com/code.red_videsh/",
        testTag = "dev_credits_link_instagram"
    ),
    DeveloperLink(
        title = "YouTube",
        subtitle = "@videshkarmakar2000",
        url = "https://www.youtube.com/@videshkarmakar2000",
        testTag = "dev_credits_link_youtube"
    ),
    DeveloperLink(
        title = "Facebook",
        subtitle = "gfx.videsh",
        url = "https://www.facebook.com/gfx.videsh",
        testTag = "dev_credits_link_facebook"
    )
)

private data class DesignFact(
    val label: String,
    val value: String,
    val highlight: Boolean = false
)

private val designFacts = listOf(
    DesignFact("Product", "Naihati Kali Puja 2026"),
    DesignFact("Developed & Designed", "Videsh Karmakar"),
    DesignFact("Design & Prototyping", "Figma · Stitch"),
    DesignFact("Platform", "Android · iOS Soon", highlight = true)
)

@Composable
fun DeveloperCreditsScreen(
    onBack: () -> Unit,
    onOpenFeedback: () -> Unit,
    onOpenSidebar: () -> Unit = {},
    onOpenDeveloperCredits: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun openLink(url: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Exception) {
            Toast.makeText(context, "Opening $url", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CreditsNoir)
            .statusBarsPadding()
            .testTag("dev_credits_screen"),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 0.dp,
            bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // ============ TOP BAR ============
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                AppBrandHeader(
                    onOpenSidebar = onOpenSidebar,
                    onOpenDeveloperCredits = onOpenDeveloperCredits,
                    navigation = {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(CreditsCard)
                                .border(1.dp, CreditsBorder, RoundedCornerShape(12.dp))
                                .clickable(onClick = onBack)
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                                .testTag("dev_credits_back_button"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = CreditsLightViolet,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Back",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = CreditsText
                            )
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0x0DFFFFFF))
                )
            }
        }

        // ============ HERO PROFILE ============
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CreditsSurface)
                    .border(1.dp, CreditsBorder, RoundedCornerShape(20.dp))
                    .padding(20.dp)
                    .testTag("dev_credits_hero")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DeveloperAvatar()

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .background(CreditsMutedViolet)
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "PRODUCT LEAD & ARCHITECT",
                                fontFamily = PlusJakartaSans,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.9.sp,
                                color = CreditsLightViolet,
                                maxLines = 1
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Videsh Karmakar",
                                fontFamily = PlusJakartaSans,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 22.sp,
                                letterSpacing = (-0.2).sp,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified creator",
                                tint = CreditsAccentViolet,
                                modifier = Modifier.size(15.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = "Founder · Developer · UI/UX Designer",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = CreditsLightViolet
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0x0DFFFFFF))
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Naihati Nights is proudly conceptualized, designed, and developed by Videsh Karmakar — bringing together software engineering, product design, and a devotion to Naihati's Kali Puja to create a refined and immersive festival experience.",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    lineHeight = 19.sp,
                    color = CreditsText
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "From the interface and visual identity to the underlying product experience, every detail of Naihati Nights is thoughtfully designed with a focus on simplicity, performance, and usability.",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    lineHeight = 19.sp,
                    color = CreditsTextMuted
                )
            }
        }

        // ============ CONNECT WITH THE DEVELOPER ============
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                SectionHeading(
                    title = "CONNECT WITH THE DEVELOPER",
                    subtitle = "Stay connected and explore more of the developer's work, music, and creative projects."
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    developerLinks.forEach { link ->
                        DeveloperLinkRow(
                            link = link,
                            onClick = { openLink(link.url) }
                        )
                    }
                }
            }
        }

        // ============ DEVELOPMENT & DESIGN ============
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                SectionHeading(title = "DEVELOPMENT & DESIGN")

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    designFacts.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { fact ->
                                DesignFactCard(
                                    fact = fact,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // ============ ACKNOWLEDGEMENT ============
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CreditsSurface)
                    .border(1.dp, CreditsBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "ACKNOWLEDGEMENT",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = CreditsLightViolet
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Built independently with a commitment to thoughtful design, modern technology, and a better way to experience Naihati's festival nights. Thank you for being part of Naihati Nights.",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    lineHeight = 19.sp,
                    color = CreditsText
                )
            }
        }

        // ============ FOOTER CTA & SIGNATURE ============
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CreditsViolet)
                        .border(1.dp, LocalAppPalette.current.accentBorder, RoundedCornerShape(14.dp))
                        .clickable(onClick = onOpenFeedback)
                        .padding(vertical = 14.dp)
                        .testTag("dev_credits_feedback_button"),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubble,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Send Feedback or Suggestions",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CreditsSurface)
                        .border(1.dp, CreditsBorder, RoundedCornerShape(12.dp))
                        .padding(vertical = 12.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Crafted with devotion by VIDESH KARMAKAR",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = CreditsTextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("dev_credits_signature")
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "© 2026 Videsh Karmakar. All Rights Reserved.",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        color = CreditsTextMuted,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "• JAY BORO MAA •",
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.sp,
                    letterSpacing = 1.2.sp,
                    color = CreditsTextMuted,
                    textAlign = TextAlign.Center
                )

                SectionCreditFooter()
            }
        }
    }
}

@Composable
private fun SectionHeading(title: String, subtitle: String? = null) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp)) {
        Text(
            text = title,
            fontFamily = PlusJakartaSans,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            color = CreditsSectionHeading
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtitle,
                fontFamily = PlusJakartaSans,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                color = CreditsTextMuted
            )
        }
    }
}

@Composable
private fun DeveloperAvatar() {
    Box {
        Box(
            modifier = Modifier
                .size(112.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(LocalAppPalette.current.accentSurface, LocalAppPalette.current.accentStrong)
                    )
                )
                .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
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
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .testTag("dev_credits_avatar")
            )
        }

        OnlineBeacon(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 2.dp, bottom = 2.dp)
        )
    }
}

@Composable
private fun AvatarFallback() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(LocalAppPalette.current.accentDeep, LocalAppPalette.current.accent)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "VK",
            fontFamily = PlusJakartaSans,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun OnlineBeacon(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "onlineBeacon")
    val pulseAlpha by transition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400),
            repeatMode = RepeatMode.Restart
        ),
        label = "onlineBeaconAlpha"
    )

    Box(modifier = modifier.size(16.dp), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(pulseAlpha)
                .clip(CircleShape)
                .background(CreditsEmeraldLight)
        )
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(CreditsEmerald)
                .border(2.dp, CreditsSurface, CircleShape)
        )
    }
}

@Composable
private fun DeveloperLinkRow(
    link: DeveloperLink,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CreditsSurface)
            .border(1.dp, CreditsBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .testTag(link.testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = link.title,
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = CreditsTextHigh
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = link.subtitle,
                fontFamily = PlusJakartaSans,
                fontSize = 10.sp,
                color = CreditsTextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "→",
            fontFamily = PlusJakartaSans,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = CreditsTextMuted
        )
    }
}

@Composable
private fun DesignFactCard(
    fact: DesignFact,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CreditsSurface)
            .border(1.dp, CreditsBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = fact.label.uppercase(),
            fontFamily = PlusJakartaSans,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.7.sp,
            color = CreditsTextMuted,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = fact.value,
            fontFamily = PlusJakartaSans,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (fact.highlight) CreditsLightViolet else CreditsTextHigh,
            maxLines = 2
        )
    }
}
