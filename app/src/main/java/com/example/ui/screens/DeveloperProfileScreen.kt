package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter

import com.example.ui.theme.KumartuliTeal

import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.ObsidianBase
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianNoir
import com.example.ui.theme.TextHighWhite
import com.example.ui.theme.TextMutedZinc
import com.example.ui.theme.TextSoftZinc


@Composable
fun DeveloperProfileScreen(
    onBack: () -> Unit,
    onOpenFeedback: () -> Unit,
    onOpenAppDetails: () -> Unit,
    onOpenSidebar: () -> Unit = {},
    onOpenDeveloperCredits: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Opening $url", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianNoir)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            AppBrandHeader(
                modifier = Modifier.padding(vertical = 8.dp),
                onOpenSidebar = onOpenSidebar,
                onOpenDeveloperCredits = onOpenDeveloperCredits,
                navigation = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x14FFFFFF))
                            .border(1.dp, Color(0x26FFFFFF), CircleShape)
                            .testTag("dev_profile_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextHighWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                trailing = {
                    IconButton(
                        onClick = onOpenAppDetails,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0x14FFFFFF))
                            .testTag("dev_profile_app_details_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "App Details",
                            tint = TextHighWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            )
        }

        // Profile Hero Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianBase),
                border = androidx.compose.foundation.BorderStroke(1.dp, LocalAppPalette.current.accentBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar with status pip
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(LocalAppPalette.current.accentSurface, LocalAppPalette.current.accentStrong)
                                    )
                                )
                                .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(22.dp))
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.developer_avatar),
                                contentDescription = "Videsh Karmakar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(20.dp))
                        )
                    }

                        // Green Active Status Pip
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(KumartuliTeal)
                                .border(2.dp, ObsidianBase, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Role Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(LocalAppPalette.current.accentSoft)
                            .border(1.dp, LocalAppPalette.current.accentBorderStrong, RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "PRODUCT LEAD & ARCHITECT",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalAppPalette.current.accentBright,
                            letterSpacing = 0.06.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Videsh Karmakar",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextHighWhite
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = LocalAppPalette.current.accent,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Founder • Developer • UI/UX Designer",
                        fontSize = 12.sp,
                        color = TextSoftZinc
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Naihati Nights is proudly conceptualized, designed, and developed by Videsh Karmakar — bringing together software engineering, product design, and cultural devotion to create a refined and immersive festival experience.\n\nFrom the interface and visual identity to the underlying product experience, every detail is thoughtfully designed with a focus on simplicity, performance, and usability.",
                        fontSize = 12.sp,
                        color = TextSoftZinc,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // CONNECT WITH THE DEVELOPER
        item {
            Column {
                Text(
                    text = "CONNECT WITH THE DEVELOPER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppPalette.current.accentBright,
                    letterSpacing = 0.08.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Stay connected and explore more of the developer's work, music, and creative projects.",
                    fontSize = 11.5.sp,
                    color = TextSoftZinc
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DeveloperLinkItem(
                        title = "Official Website",
                        subtitle = "videshkarmakar.carrd.co",
                        onClick = { openLink("https://videshkarmakar.carrd.co") }
                    )
                    DeveloperLinkItem(
                        title = "Spotify",
                        subtitle = "Videsh Karmakar — Artist Profile",
                        onClick = { openLink("https://open.spotify.com") }
                    )
                    DeveloperLinkItem(
                        title = "Instagram",
                        subtitle = "@code.red_videsh",
                        onClick = { openLink("https://instagram.com/code.red_videsh") }
                    )
                    DeveloperLinkItem(
                        title = "YouTube",
                        subtitle = "@videshkarmakar2000",
                        onClick = { openLink("https://youtube.com/@videshkarmakar2000") }
                    )
                    DeveloperLinkItem(
                        title = "Facebook",
                        subtitle = "gfx.videsh",
                        onClick = { openLink("https://facebook.com/gfx.videsh") }
                    )
                }
            }
        }

        // DEVELOPMENT & DESIGN GRID
        item {
            Column {
                Text(
                    text = "DEVELOPMENT & DESIGN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppPalette.current.accentBright,
                    letterSpacing = 0.08.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DevInfoCard(
                            label = "PRODUCT",
                            value = "Naihati Kali Puja 2026",
                            modifier = Modifier.weight(1f)
                        )
                        DevInfoCard(
                            label = "DEVELOPED & DESIGNED",
                            value = "Videsh Karmakar",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DevInfoCard(
                            label = "DESIGN & PROTOTYPING",
                            value = "Figma • Stitch",
                            modifier = Modifier.weight(1f)
                        )
                        DevInfoCard(
                            label = "PLATFORM",
                            value = "Android • iOS Soon",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // ACKNOWLEDGEMENT
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianBase),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "ACKNOWLEDGEMENT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppPalette.current.accentBright,
                        letterSpacing = 0.05.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Built independently with a commitment to thoughtful design, modern technology, and a better way to experience devotional journeys. Thank you for being part of Naihati Nights.",
                        fontSize = 11.5.sp,
                        color = TextSoftZinc,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Action Button: Send Feedback or Suggestions
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(LocalAppPalette.current.accentDeep)
                    .clickable { onOpenFeedback() }
                    .padding(vertical = 14.dp)
                    .testTag("dev_profile_feedback_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ChatBubble,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Send Feedback or Suggestions",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Devotional Signature & Copyright
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SectionCreditFooter(
                    topPadding = 0.dp,
                    bottomPadding = 0.dp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "© 2026 Videsh Karmakar. All Rights Reserved.",
                    color = TextMutedZinc,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• JAY MAA BORO MAA •",
                    color = LocalAppPalette.current.accentBright,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.15.sp
                )
            }
        }
    }
}

@Composable
fun DeveloperLinkItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianBase),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextHighWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextSoftZinc
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = TextMutedZinc,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
fun DevInfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianBase),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextMutedZinc,
                letterSpacing = 0.05.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextHighWhite
            )
        }
    }
}