package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PlusJakartaSans
import android.content.Intent
import android.net.Uri

/**
 * Launch / intro screen. Visual treatment follows the Finesta / Reev Dark reference:
 * a cosmic night aurora backdrop, a glassmorphic emblem with cinematic entrance,
 * a tracked display wordmark, a white pill CONTINUE button with ambient sheen,
 * a 3D-flip social icon bar, and a verified attribution line.
 * Visual-only; all test anchors (tags, copy) are preserved.
 */
@Composable
fun IntroScreen(
    onBeginExperience: () -> Unit,
    motionReduction: Boolean = false
) {
    val context = LocalContext.current

    // Cinematic fade-in of the whole splash.
    val splashAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        splashAlpha.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
    }

    // Ambient loops.
    val infinite = rememberInfiniteTransition(label = "ambient")
    val auraAlpha by infinite.animateFloat(
        initialValue = 0.4f, targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(3000), RepeatMode.Reverse),
        label = "auraAlpha"
    )
    val auraScale by infinite.animateFloat(
        initialValue = 0.96f, targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(3000), RepeatMode.Reverse),
        label = "auraScale"
    )
    val emblemHalo by infinite.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2900), RepeatMode.Reverse),
        label = "emblemHalo"
    )
    val buttonGlow by infinite.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2250), RepeatMode.Reverse),
        label = "buttonGlow"
    )
    val sheenX by infinite.animateFloat(
        initialValue = -1.5f, targetValue = 2.5f,
        animationSpec = infiniteRepeatable(tween(4800), RepeatMode.Restart),
        label = "sheenX"
    )
    val beamX by infinite.animateFloat(
        initialValue = -1f, targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(3500), RepeatMode.Restart),
        label = "beamX"
    )

    // Spring press for the CONTINUE button.
    // Cinematic exit.
    var exiting by remember { mutableStateOf(false) }
    val exitScale by animateFloatAsState(
        targetValue = if (exiting) 1.04f else 1f,
        animationSpec = tween(1100, easing = FastOutSlowInEasing),
        label = "exitScale"
    )
    val exitAlpha by animateFloatAsState(
        targetValue = if (exiting) 0f else 1f,
        animationSpec = tween(1100, easing = FastOutSlowInEasing),
        label = "exitAlpha"
    )
    val curtainAlpha by animateFloatAsState(
        targetValue = if (exiting) 1f else 0f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "curtainAlpha"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = 500f, dampingRatio = 0.95f),
        label = "pressScale"
    )

    val fastOutSlowIn = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(splashAlpha.value)
            .background(Color(0xFF02030A))
            .testTag("intro_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Cosmic aurora backdrop.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF0A1A2E), Color(0xFF050810), Color(0xFF02030A)),
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
                        colors = listOf(Color(0x551A3A5C), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(0.3f, 0.6f),
                        radius = 900f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x442A1A4A), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(0.7f, 0.7f),
                        radius = 800f
                    )
                )
        )
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

        // Ambient aura behind emblem.
        Box(
            modifier = Modifier
                .size(288.dp)
                .graphicsLayer { scaleX = auraScale; scaleY = auraScale; alpha = auraAlpha }
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x33FFFFFF), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .scale(exitScale)
                .alpha(exitAlpha)
                .blur(if (exiting) 10.dp else 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Emblem with cinematic entrance + halo breathe.
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(1400, delayMillis = 200, easing = fastOutSlowIn)) +
                        scaleIn(tween(1400, delayMillis = 200, easing = fastOutSlowIn), initialScale = 0.92f) +
                        slideInVertically(tween(1400, delayMillis = 200, easing = fastOutSlowIn)) { it / 8 }
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .graphicsLayer {
                            // halo breathe via border alpha (approximated by scale of inner glow)
                        }
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xCC121215))
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.12f + 0.20f * emblemHalo),
                            shape = RoundedCornerShape(24.dp)
                        )
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title reveal.
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(1200, delayMillis = 500, easing = fastOutSlowIn)) +
                        slideInVertically(tween(1200, delayMillis = 500, easing = fastOutSlowIn)) { it / 6 }
            ) {
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
            }

            // Caption.
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(1200, delayMillis = 750, easing = fastOutSlowIn)) +
                        slideInVertically(tween(1200, delayMillis = 750, easing = fastOutSlowIn)) { it / 8 }
            ) {
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
            }

            // Tagline dissolve.
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(1200, delayMillis = 750, easing = fastOutSlowIn))
            ) {
                Text(
                    text = "Just One Tap To Explore Whole Naihati Kali Puja Pandals With Map.",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.5.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CONTINUE button with ambient sheen + glow breathe.
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(1000, delayMillis = 1000, easing = fastOutSlowIn)) +
                        slideInVertically(tween(1000, delayMillis = 1000, easing = fastOutSlowIn)) { it / 4 }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(250.dp)
                        .scale(pressScale)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.White)
                        .drawBehind {
                            // ambient sheen sweep
                            val sheenWidth = size.width * 0.33f
                            val x = sheenX * size.width
                            drawRect(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White.copy(alpha = 0.7f),
                                        Color.Transparent
                                    ),
                                    start = androidx.compose.ui.geometry.Offset(x, 0f),
                                    end = androidx.compose.ui.geometry.Offset(x + sheenWidth, 0f)
                                ),
                                size = size
                            )
                        }
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { exiting = true; onBeginExperience() }
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Attribution fade.
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(1200, delayMillis = 1350, easing = FastOutSlowInEasing))
            ) {
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
        }

        // Social flip bar with beam borders.
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 72.dp)
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(1000, delayMillis = 1150, easing = fastOutSlowIn)) +
                        scaleIn(tween(1000, delayMillis = 1150, easing = fastOutSlowIn), initialScale = 0.96f) +
                        slideInVertically(tween(1000, delayMillis = 1150, easing = fastOutSlowIn)) { it / 10 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.width(12.dp).height(1.dp).background(Color.White.copy(alpha = 0.2f)))
                        Text(
                            text = "Connect to Developer",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp,
                            letterSpacing = 2.sp,
                            color = Color.White.copy(alpha = 0.4f)
                        )
                        Box(modifier = Modifier.width(12.dp).height(1.dp).background(Color.White.copy(alpha = 0.2f)))
                    }
                    Row(
                        modifier = Modifier
                            .background(Color(0x66000000))
                            .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(999.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SocialFlipCard(letter = "W", url = "https://videshkarmakar.carrd.co/", backColor = Color(0xFF191B21))
                        SocialFlipCard(letter = "S", url = "https://open.spotify.com/artist/2eJEzi0mzJB60zVJf0OjWP", backColor = Color(0xFF0E1F14), iconTint = Color(0xFF1DB954))
                        SocialFlipCard(letter = "I", url = "https://www.instagram.com/code.red_videsh/", backColor = Color(0xFF23121D), iconTint = Color(0xFFE1306C))
                        SocialFlipCard(letter = "Y", url = "https://www.youtube.com/@videshkarmakar2000", backColor = Color(0xFF241010), iconTint = Color(0xFFFF0000))
                        SocialFlipCard(letter = "F", url = "https://www.facebook.com/gfx.videsh", backColor = Color(0xFF0D182B), iconTint = Color(0xFF1877F2))
                    }
                }
            }
        }

        // Cinematic exit curtain.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(curtainAlpha)
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.00f to Color.Black,
                            0.50f to Color(0xFF06070A),
                            1.00f to Color(0xFF040406)
                        )
                    )
                )
        )

        // Bottom home indicator.
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

@Composable
private fun SocialFlipCard(
    letter: String,
    url: String,
    backColor: Color,
    iconTint: Color = Color.White
) {
    val context = LocalContext.current
    var flipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(600),
        label = "flip"
    )

    Box(
        modifier = Modifier
            .size(24.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8f * density
            }
            .clickable { flipped = !flipped; context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
    ) {
        // Front face.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { if (rotation > 90f) alpha = 0f }
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xCC121215))
                .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
        // Back face.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { if (rotation <= 90f) alpha = 0f; rotationY = 180f }
                .clip(RoundedCornerShape(8.dp))
                .background(backColor)
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = iconTint
            )
        }
    }
}
