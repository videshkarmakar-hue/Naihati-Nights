package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.ObsidianHighlight
import com.example.ui.theme.ObsidianRaised
import com.example.ui.theme.SacredGold
import com.example.ui.theme.SacredGoldLight

@Composable
fun TrishulInsignia(
    modifier: Modifier = Modifier,
    containerSize: Dp = 80.dp,
    iconSize: Dp = 44.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "tridentGlow")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    Box(
        modifier = modifier
            .size(containerSize)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = LocalAppPalette.current.accentDeep,
                spotColor = Color(0xFFE5A93B)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ObsidianHighlight,
                        ObsidianRaised,
                        Color(0xFF0C0C12)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color(0x3DFBB524),
                shape = RoundedCornerShape(22.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Inner Trishul Canvas
        Canvas(modifier = Modifier.size(iconSize * glowScale)) {
            val w = size.width
            val h = size.height

            val goldBrush = Brush.verticalGradient(
                colors = listOf(
                    Color.White,
                    SacredGoldLight,
                    SacredGold,
                    Color(0xFFB45309)
                )
            )

            // Center vertical staff
            drawLine(
                brush = goldBrush,
                start = Offset(w * 0.5f, h * 0.12f),
                end = Offset(w * 0.5f, h * 0.85f),
                strokeWidth = w * 0.055f,
                cap = StrokeCap.Round
            )

            // Center spear flame head
            val spearPath = Path().apply {
                moveTo(w * 0.5f, h * 0.08f)
                lineTo(w * 0.58f, h * 0.22f)
                lineTo(w * 0.54f, h * 0.38f)
                lineTo(w * 0.46f, h * 0.38f)
                lineTo(w * 0.42f, h * 0.22f)
                close()
            }
            drawPath(
                path = spearPath,
                brush = goldBrush
            )

            // Left prong curve
            val leftProng = Path().apply {
                moveTo(w * 0.5f, h * 0.60f)
                cubicTo(
                    w * 0.40f, h * 0.60f,
                    w * 0.22f, h * 0.50f,
                    w * 0.22f, h * 0.26f
                )
                cubicTo(
                    w * 0.22f, h * 0.22f,
                    w * 0.25f, h * 0.18f,
                    w * 0.28f, h * 0.24f
                )
                cubicTo(
                    w * 0.30f, h * 0.38f,
                    w * 0.38f, h * 0.50f,
                    w * 0.50f, h * 0.52f
                )
                close()
            }
            drawPath(
                path = leftProng,
                brush = goldBrush
            )

            // Right prong curve
            val rightProng = Path().apply {
                moveTo(w * 0.5f, h * 0.60f)
                cubicTo(
                    w * 0.60f, h * 0.60f,
                    w * 0.78f, h * 0.50f,
                    w * 0.78f, h * 0.26f
                )
                cubicTo(
                    w * 0.78f, h * 0.22f,
                    w * 0.75f, h * 0.18f,
                    w * 0.72f, h * 0.24f
                )
                cubicTo(
                    w * 0.70f, h * 0.38f,
                    w * 0.62f, h * 0.50f,
                    w * 0.50f, h * 0.52f
                )
                close()
            }
            drawPath(
                path = rightProng,
                brush = goldBrush
            )

            // Crescent Moon Base
            val crescent = Path().apply {
                moveTo(w * 0.34f, h * 0.68f)
                cubicTo(
                    w * 0.40f, h * 0.75f,
                    w * 0.60f, h * 0.75f,
                    w * 0.66f, h * 0.68f
                )
                cubicTo(
                    w * 0.60f, h * 0.71f,
                    w * 0.40f, h * 0.71f,
                    w * 0.34f, h * 0.68f
                )
                close()
            }
            drawPath(
                path = crescent,
                brush = goldBrush,
                style = Stroke(width = w * 0.05f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Sacred Bindu (Third Eye)
            drawCircle(
                color = Color.White,
                radius = w * 0.045f,
                center = Offset(w * 0.5f, h * 0.34f)
            )
            drawCircle(
                color = Color(0xFFEF4444),
                radius = w * 0.025f,
                center = Offset(w * 0.5f, h * 0.34f)
            )

            // Base horizontal bar
            drawLine(
                brush = goldBrush,
                start = Offset(w * 0.36f, h * 0.88f),
                end = Offset(w * 0.64f, h * 0.88f),
                strokeWidth = w * 0.06f,
                cap = StrokeCap.Round
            )
        }
    }
}
