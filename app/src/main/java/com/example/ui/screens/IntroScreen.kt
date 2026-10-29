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
import androidx.compose.material.icons.filled.Explore
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TextHighWhite
import com.example.ui.theme.TextSoftZinc
import com.example.ui.theme.TextMutedZinc

/**
 * Launch / splash screen — the branded Continue gate.
 *
 * Design: a single centered emblem, a wordmark, a quiet subtitle, credits, and one clear CTA.
 * No ambient orbs, orbit rings, shimmer gradients, or infinite loops. One gentle entrance
 * fade, then stillness. [motionReduction] (from Settings) skips even that.
 */
@Composable
fun IntroScreen(
    onBeginExperience: () -> Unit,
    modifier: Modifier = Modifier,
    motionReduction: Boolean = false
) {
    val palette = LocalAppPalette.current

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
            .background(Color(0xFF060608))
            .testTag("intro_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Subtle brand-tinted vertical wash — no orbs, no star grid.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF060608),
                            palette.accentSurface.copy(alpha = 0.35f),
                            Color(0xFF060608)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .alpha(alpha)
                .graphicsLayer { translationY = riseOffset },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // ── Emblem ──────────────────────────────────────────────
            // Clean emblem on a white tile with a hairline ring. No glass pedestal, no glow.
            Box(
                modifier = Modifier
                    .size(152.dp)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(36.dp), ambientColor = Color.Black, spotColor = Color.Black)
                    .clip(RoundedCornerShape(36.dp))
                    .background(Color.White)
                    .border(1.dp, palette.accentBorder, RoundedCornerShape(36.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splash_trishul),
                    contentDescription = "Naihati Nights emblem",
                    modifier = Modifier.size(112.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Edition badge ───────────────────────────────────────
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0x0AFFFFFF))
                    .border(1.dp, Color(0x1AFFFFFF), CircleShape)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(palette.accent)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "OFFICIAL 2026 EDITION",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.8.sp,
                    color = TextSoftZinc
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "\u2022",
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.sp,
                    color = TextMutedZinc
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Naihati Kali Puja",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    color = TextMutedZinc
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ── Wordmark ────────────────────────────────────────────
            Text(
                text = "NAIHATI NIGHTS",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp,
                lineHeight = 36.sp,
                letterSpacing = (-0.5).sp,
                color = TextHighWhite,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Accent rule ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(2.dp)
                    .clip(CircleShape)
                    .background(palette.accent)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ── Subtitle ────────────────────────────────────────────
            Text(
                text = "Naihati Kali Puja 2026 \u2022 Official Parikrama & Guide",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp,
                color = TextSoftZinc,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 300.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // ── Credits ─────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buildAnnotatedString {
                        append("Architected by ")
                        withStyle(
                            SpanStyle(
                                color = TextHighWhite,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Videsh Karmakar")
                        }
                    },
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    letterSpacing = (-0.2).sp,
                    color = TextMutedZinc,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verified",
                    tint = palette.accentBright,
                    modifier = Modifier.size(14.dp)
                )
            }

            Text(
                text = "Jay Maa Boro Maa \u2022 Naihati Kali Puja 2026",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Medium,
                fontSize = 9.5.sp,
                lineHeight = 14.sp,
                letterSpacing = 0.5.sp,
                color = TextMutedZinc,
                textAlign = TextAlign.Center
            )

            SectionCreditFooter(
                topPadding = 2.dp,
                bottomPadding = 0.dp,
                color = TextMutedZinc
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── CTA ─────────────────────────────────────────────────
            ContinueButton(onClick = onBeginExperience)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * The single primary action: a surface-filled pill with a leading explore chip, the label,
 * and a trailing accent arrow chip — press-scale feedback only, no sweep or glow.
 */
@Composable
private fun ContinueButton(onClick: () -> Unit) {
    val palette = LocalAppPalette.current
    val shape = RoundedCornerShape(16.dp)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(64.dp)
            .graphicsLayer {
                val s = if (pressed) 0.985f else 1f
                scaleX = s
                scaleY = s
            }
            .shadow(elevation = 6.dp, shape = shape, ambientColor = Color.Black, spotColor = Color.Black)
            .clip(shape)
            .background(palette.accentSurface)
            .border(1.dp, palette.accentBorder, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClickLabel = "Continue to Naihati Nights",
                onClick = onClick
            )
            .testTag("begin_parikrama_button")
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading explore chip.
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(palette.accentSoft)
                .border(1.dp, palette.accentBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = null,
                tint = palette.accentBright,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "CONTINUE",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            letterSpacing = 1.sp,
            color = TextHighWhite
        )

        Spacer(modifier = Modifier.weight(1f))

        // Trailing arrow chip.
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(palette.accent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = palette.onAccent,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
