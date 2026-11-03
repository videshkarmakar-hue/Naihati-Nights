package com.example.ui.screens

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans

/**
 * Launch / intro screen. Visual treatment follows the Finesta / Reev Dark reference:
 * an atmospheric fog landscape, a glassmorphic emblem, a tracked display wordmark,
 * a liquid-metal shimmer CONTINUE button and a verified attribution line.
 * Visual-only; all test anchors (tags, copy) are preserved.
 */
@Composable
fun IntroScreen(
    onBeginExperience: () -> Unit,
    motionReduction: Boolean = false
) {
    val palette = LocalAppPalette.current

    // Liquid-metal shimmer for the CONTINUE button.
    val transition = rememberInfiniteTransition(label = "liquidMetal")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400),
            repeatMode = RepeatMode.Restart
        ),
        label = "metalPhase"
    )
    val metalA = lerp(Color(0xFF5A5A5E), Color(0xFFB8B8BC), phase)
    val metalB = lerp(Color(0xFFD8D8DC), Color(0xFFFFFFFF), phase)
    val metalC = lerp(Color(0xFF6E6E72), Color(0xFF9A9A9E), phase)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.00f to Color(0xFF090A0C),
                        0.52f to Color(0xFF0D0E12),
                        0.70f to Color(0xFF22252C),
                        0.82f to Color(0xFF9097A3),
                        0.93f to Color(0xFFD2D7DF),
                        1.00f to Color(0xFFCFD4DC)
                    )
                )
            )
            .testTag("intro_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Atmospheric fog overlays.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x73D7DBE2), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(0.5f, 0.84f),
                        radius = 2000f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x73F0F3F8), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(0.70f, 0.76f),
                        radius = 1200f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x66C8CED8), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(0.25f, 0.82f),
                        radius = 800f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Glassmorphic emblem
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xCC121215))
                    .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(24.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x66000000))
                        .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_brand_logo),
                        contentDescription = "Naihati Nights Emblem",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
                // Status dot
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-4).dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, Color(0xFF0A0A0C), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "NAIHATI NIGHTS",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                letterSpacing = 5.72.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 1.dp)
            )

            Text(
                text = "Videsh Karmakar Projects",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                color = Color.White.copy(alpha = 0.45f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )

            Text(
                text = "Just One Tap To Explore Whole Naihati Kali Puja Pandals With Map.",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.5.sp,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Liquid-metal CONTINUE button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(260.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Brush.linearGradient(colors = listOf(metalA, metalB, metalC, metalB, metalA)))
                    .clickable { onBeginExperience() }
                    .padding(vertical = 12.dp, horizontal = 24.dp)
                    .testTag("begin_parikrama_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CONTINUE",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    letterSpacing = 2.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Crafted by Videsh Karmakar",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal,
                    fontSize = 8.5.sp,
                    letterSpacing = 1.5.sp,
                    color = palette.accentBright,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(palette.accentBright)
                )
            }
        }

        // Bottom home indicator
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .width(128.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.Black.copy(alpha = 0.75f))
        )
    }
}
