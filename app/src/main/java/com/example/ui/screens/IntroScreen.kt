package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
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
import com.example.ui.theme.PlusJakartaSans

private val OuterCanvas = Color(0xFF050608)
private val InnerCanvas = Color(0xFF090A0D)
private val WidgetTile = Color(0xFF11141B)
private val WidgetInner = Color(0xFF0C0E14)
private val WidgetBorder = Color(0xFF232836)
private val CtaSurface = Color(0xFF141720)
private val CtaBorder = Color(0xFF242938)
private val IntroAccent = Color(0xFF4FDBC8)
private val TextPrimary = Color(0xFFF5F5F5)
private val TextSoft = Color(0xFFD4D4D4)
private val TextMuted = Color(0xFFA3A3A3)

/**
 * Launch / splash screen — the branded Continue gate.
 *
 * Reev Dark / Finesta KWGT lockup: a matte obsidian canvas with two barely-visible ambient
 * vignettes, a tactile emblem widget, a live status chip, the wordmark and one high-contrast
 * "Continue your journey" call to action. One gentle entrance fade, then stillness.
 * [motionReduction] (from Settings) skips even that.
 */
@Composable
fun IntroScreen(
    onBeginExperience: () -> Unit,
    modifier: Modifier = Modifier,
    motionReduction: Boolean = false
) {
    // One-shot entrance: fade + rise, 600ms, then done. Respects reduced motion.
    val entrance = remember { Animatable(if (motionReduction) 1f else 0f) }
    LaunchedEffect(motionReduction) {
        if (motionReduction) {
            entrance.snapTo(1f)
        } else {
            entrance.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
            )
        }
    }
    val alpha = entrance.value
    val riseOffset = with(LocalDensity.current) { (1f - entrance.value) * 12.dp.toPx() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OuterCanvas)
            .testTag("intro_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxSize()
                .drawBehind {
                    // Ambient matte depth: a faint white bloom top-right and a teal bloom
                    // low on the left, both barely above the canvas.
                    drawRect(
                        Brush.verticalGradient(
                            colors = listOf(InnerCanvas, Color(0xFF08090D), InnerCanvas)
                        )
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x08FFFFFF), Color.Transparent)
                        ),
                        radius = size.width * 0.85f,
                        center = Offset(size.width, size.height * 0.04f)
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x0D4FDBC8), Color.Transparent)
                        ),
                        radius = size.width * 0.72f,
                        center = Offset(0f, size.height * 0.72f)
                    )
                }
                .padding(horizontal = 24.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
                .alpha(alpha)
                .graphicsLayer { translationY = riseOffset },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // ── Emblem widget ───────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black,
                        spotColor = Color.Black
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(WidgetTile)
                    .border(1.dp, WidgetBorder, RoundedCornerShape(16.dp))
                    .padding(1.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(15.dp))
                        .background(WidgetInner),
                    contentAlignment = Alignment.Center
                ) {
                    // Subtle top hairline edge on the tile.
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color(0x33FFFFFF),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.splash_trishul),
                        contentDescription = "Naihati Nights emblem",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Live status chip ────────────────────────────────────
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(WidgetTile)
                    .border(1.dp, WidgetBorder, CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(IntroAccent)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Videsh Karmakar Project",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    letterSpacing = 0.2.sp,
                    color = TextSoft
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Wordmark ────────────────────────────────────────────
            Text(
                text = "NAIHATI NIGHTS",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                lineHeight = 33.sp,
                letterSpacing = 2.8.sp,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )

            Spacer(modifier = Modifier.height(6.dp))

            // ── Subtitle ────────────────────────────────────────────
            Text(
                text = "Best Guidance App For Naihati Kali Puja Pandals Hopping Guides.",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.5.sp,
                lineHeight = 20.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 290.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Primary call to action ──────────────────────────────
            BeginJourneyButton(onClick = onBeginExperience)

            Spacer(modifier = Modifier.weight(1f))

            // ── Devotion footer ─────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buildAnnotatedString {
                        append("Architected by ")
                        withStyle(
                            SpanStyle(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.3.sp
                            )
                        ) {
                            append("Videsh Karmakar")
                        }
                    },
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verified creator",
                    tint = IntroAccent,
                    modifier = Modifier.size(13.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "JAY BORO MAA \u2022 NAIHATI 2026",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 9.sp,
                lineHeight = 14.sp,
                letterSpacing = 1.4.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Native-style home indicator.
            Box(
                modifier = Modifier
                    .width(112.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
            )

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

/**
 * The single primary action: a matte card with the journey label and a teal arrow chip.
 * Press-scale feedback only, no sweep or glow.
 */
@Composable
private fun BeginJourneyButton(onClick: () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer {
                val scale = if (pressed) 0.99f else 1f
                scaleX = scale
                scaleY = scale
            }
            .clip(shape)
            .background(CtaSurface)
            .border(1.dp, CtaBorder, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClickLabel = "Continue to Naihati Nights",
                onClick = onClick
            )
            .testTag("begin_parikrama_button")
            .padding(start = 14.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "CONTINUE YOUR JOURNEY",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            letterSpacing = 1.3.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(IntroAccent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = InnerCanvas,
                modifier = Modifier.size(19.dp)
            )
        }
    }
}
