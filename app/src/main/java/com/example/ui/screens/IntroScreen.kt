package com.example.ui.screens

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PlusJakartaSans
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring

/**
 * Launch / intro screen. Visual treatment follows the Finesta / Reev Dark reference:
 * a cosmic night aurora backdrop, a glassmorphic emblem, a tracked display wordmark,
 * a clean white pill CONTINUE button and a verified attribution line.
 * Visual-only; all test anchors (tags, copy) are preserved.
 */
@Composable
fun IntroScreen(
    onBeginExperience: () -> Unit,
    motionReduction: Boolean = false
) {
    // Subtle aurora phase for the cosmic backdrop.
    val transition = rememberInfiniteTransition(label = "aurora")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000),
            repeatMode = androidx.compose.animation.core.RepeatMode.Restart
        ),
        label = "auroraPhase"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = 500f, dampingRatio = 0.95f),
        label = "pressScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF02030A))
            .testTag("intro_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Cosmic night aurora backdrop (animated).
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF0A1A2E),
                            Color(0xFF050810),
                            Color(0xFF02030A)
                        ),
                        center = androidx.compose.ui.geometry.Offset(0.5f, 0.4f),
                        radius = 1400f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0x551A3A5C),
                            Color.Transparent
                        ),
                        center = androidx.compose.ui.geometry.Offset(0.3f + phase * 0.4f, 0.6f),
                        radius = 900f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0x442A1A4A),
                            Color.Transparent
                        ),
                        center = androidx.compose.ui.geometry.Offset(0.7f - phase * 0.3f, 0.7f),
                        radius = 800f
                    )
                )
        )
        // Subtle vertical light streaks.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.00f to Color(0x00000000),
                            0.50f to Color(0x11FFFFFF),
                            1.00f to Color(0x00000000)
                        )
                    )
                )
        )
        // Vignette.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.Transparent, Color(0xCC02030A)),
                        center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
                        radius = 1200f
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

            // CONTINUE pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(250.dp)
                    .scale(pressScale)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { onBeginExperience() }
                    .padding(vertical = 10.dp, horizontal = 24.dp)
                    .testTag("begin_parikrama_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CONTINUE",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 2.64.sp,
                    color = Color(0xFF171717)
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
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 8.5.sp,
                    letterSpacing = 1.98.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.White)
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
                .background(Color.White.copy(alpha = 0.4f))
        )
    }
}
