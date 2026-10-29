package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A single, shared attribution line rendered at the bottom of every section.
 * Kept intentionally small and muted so it never competes with section content.
 */
@Composable
fun SectionCreditFooter(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF71717A),
    topPadding: Dp = 12.dp,
    bottomPadding: Dp = 4.dp
) {
    Text(
        text = "©Crafted by VIDESH KARMAKAR",
        modifier = modifier
            .fillMaxWidth()
            .padding(top = topPadding, bottom = bottomPadding),
        color = color,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.15.sp,
        textAlign = TextAlign.Center
    )
}
