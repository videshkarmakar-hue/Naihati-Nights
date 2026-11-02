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

private val OuterCanvas = Color(0xFF121318)
private val InnerCanvas = Color(0xFF1A1C23)
private val NeuBase = Color(0xFF1B1D24)
private val NeuSunken = Color(0xFF16181F)
private val NeuSunkenPill = Color(0xFF14161C)
private val NeuGradientStart = Color(0xFF20232C)
private val NeuGradientEnd = Color(0xFF181921)
private val NeuEdgeLight = Color(0xFF2A2E38)
private val NeuEdgeDark = Color(0xFF14161C)
private val EmeraldActive = Color(0xFF34D399)
private val TextPrimary = Color(0xFFF5F5F5)
private val TextSoft = Color(0xFFD4D4D4)
private val TextMuted = Color(0xFFA3A3A3)
private val TextDim = Color(0xFF71717A)

/**
 * Launch / splash screen — the branded Continue gate.
 *
 * Dark neumorphic lockup: a raised emblem squircle, a sunken status pill, the wordmark and a
 * capsule "CONTINUE" action with a recessed arrow well. One gentle entrance fade, then stillness.
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
                .widthIn(max = 412.dp)
                .fillMaxSize()
                .drawBehind {
                    // Soft ambient lighting: a faint white bloom top-left, a deeper shade
                    // bottom-right, both barely above the canvas.
                    drawRect(InnerCanvas)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x0AFFFFFF), Color.Transparent)
                        ),
                        radius = size.width * 0.62f,
                        center = Offset(0f, 0f)
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x66000000), Color.Transparent)
                        ),
                        radius = size.width * 0.78f,
                        center = Offset(size.width, size.height)
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
                    .size(64.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black,
                        spotColor = Color.Black
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(NeuBase)
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(listOf(NeuEdgeLight, NeuEdgeDark)),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeuSunken)
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(listOf(NeuEdgeDark, NeuEdgeLight)),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splash_trishul),
                        contentDescription = "Naihati Nights emblem",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Live status chip ────────────────────────────────────
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(NeuSunkenPill)
                    .border(1.dp, Color(0xFF21232C), CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(EmeraldActive)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Videsh Karmakar Projects",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.2.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Wordmark ────────────────────────────────────────────
            Text(
                text = "NAIHATI NIGHTS",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                letterSpacing = 3.6.sp,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ── Subtitle ────────────────────────────────────────────
            Text(
                text = "Best Guide App For Hopping Kali Puja on Naihati Area.",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 11.5.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.2.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 270.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // ── Primary call to action ──────────────────────────────
            BeginJourneyButton(onClick = onBeginExperience)

            Spacer(modifier = Modifier.height(16.dp))

            // ── Devotion footer ─────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buildAnnotatedString {
                        append("Architected by ")
                        withStyle(
                            SpanStyle(
                                color = TextSoft,
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
                    tint = TextMuted,
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
                color = TextDim,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Native-style home indicator.
            Box(
                modifier = Modifier
                    .width(112.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0x4D525252))
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

/**
 * The single primary action: a raised capsule with the continue label and a recessed arrow well.
 * Press-scale feedback only, no sweep or glow.
 */
@Composable
private fun BeginJourneyButton(onClick: () -> Unit) {
    val shape = CircleShape
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .graphicsLayer {
                val scale = if (pressed) 0.98f else 1f
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (pressed) 2.dp else 10.dp,
                shape = shape,
                ambientColor = Color.Black,
                spotColor = Color.Black
            )
            .clip(shape)
            .background(Brush.linearGradient(listOf(NeuGradientStart, NeuGradientEnd)))
            .border(1.dp, Color(0x99272A35), shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClickLabel = "Continue to Naihati Nights",
                onClick = onClick
            )
            .testTag("begin_parikrama_button")
            .padding(start = 24.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "CONTINUE",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            letterSpacing = 1.56.sp,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(NeuSunken)
                .border(1.dp, Color(0xFF23262F), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = TextSoft,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}
