package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ThemePandel
import com.example.model.ThemePandelData
import com.example.ui.components.AppBrandHeader
import com.example.ui.theme.ObsidianNoir
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TextSoftZinc

private val CardSurface = Color(0xFF111217)
private val CardBorder = Color(0x14FFFFFF)
private val DividerColor = Color(0x0FFFFFFF)
private val TextZinc100 = Color(0xFFF4F4F5)
private val TextZinc200 = Color(0xFFE4E4E7)
private val TextZinc300 = Color(0xFFD4D4D8)
private val TextZinc500 = Color(0xFF71717A)
private val Amber200 = Color(0xFFFDE68A)
private val Amber300 = Color(0xFFFCD34D)
private val SecondaryLavender = Color(0xFFD0BCFF)

@Composable
fun ThemePandelScreen(
    onOpenSidebar: () -> Unit = {},
    onOpenDeveloperCredits: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val items = ThemePandelData.items

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianNoir)
            .statusBarsPadding()
            .testTag("theme_pandel_screen"),
        contentPadding = PaddingValues(bottom = 108.dp)
    ) {
        item(key = "theme_pandel_header") {
            AppBrandHeader(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                onOpenSidebar = onOpenSidebar,
                onOpenDeveloperCredits = onOpenDeveloperCredits
            )
        }

        item(key = "theme_pandel_list_top") {
            Spacer(modifier = Modifier.height(12.dp))
        }

        itemsIndexed(
            items = items,
            key = { _, item -> item.id.ifBlank { item.committee } }
        ) { index, item ->
            ThemePandelCard(
                item = item,
                displayNumber = index + 1,
                context = context,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = if (index == items.lastIndex) 0.dp else 14.dp
                )
            )
        }

        item(key = "theme_pandel_footer") {
            ThemePandelFooter()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ThemePandelCard(
    item: ThemePandel,
    displayNumber: Int,
    context: Context,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    val accent = item.cardAccent

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(CardSurface)
            .border(1.dp, accentBorder(accent), shape)
            .testTag("theme_pandel_card_${item.id}")
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            NumberBadge(
                number = displayNumber,
                accent = accent
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = item.committeeBengali.ifBlank { item.committee },
                        fontFamily = FontFamily.Default,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp,
                        color = TextZinc100
                    )
                    EditionPill(text = item.edition, accent = accent)
                    if (item.isNew) {
                        NewEntryChip()
                    }
                }

                if (item.committeeBengali.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.committee,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 16.sp,
                        color = TextSoftZinc
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            MapChip(
                onClick = {
                    openGoogleMaps(
                        context = context,
                        query = "${item.committee} Naihati Kali Puja"
                    )
                }
            )
        }

        if (item.locationNote.isNotBlank() || item.locationNoteBengali.isNotBlank()) {
            LocationRow(
                item = item,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        if (item.specialAttraction.isNotBlank() &&
            item.specialAttractionStyle == "amber" &&
            !item.specialAttractionBelowTheme
        ) {
            SpecialAttractionChip(
                item = item,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(1.dp)
                .background(DividerColor)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                text = "PANDAL THEME",
                fontFamily = PlusJakartaSans,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
                color = Color(0xE6FDE68A)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "“${item.pandalBengali.ifBlank { item.pandal }}”",
                fontFamily = FontFamily.Default,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 23.sp,
                color = Color(0xFFF2F3F7)
            )

            if (item.pandal.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.pandal,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 17.sp,
                    color = TextSoftZinc
                )
            }

            if (item.specialAttraction.isNotBlank() &&
                item.specialAttractionStyle == "amber" &&
                item.specialAttractionBelowTheme
            ) {
                SpecialAttractionChip(
                    item = item,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun NumberBadge(number: Int, accent: String) {
    val shape = RoundedCornerShape(8.dp)
    val (background, border, textColor) = when (accent) {
        "amber" -> Triple(Color(0x1AFBBF24), Color(0x4DFCD34D), Amber200)
        "purple" -> Triple(Color(0x1AA855F7), Color(0x4DD8B4FE), SecondaryLavender)
        else -> Triple(Color(0x0AFFFFFF), Color(0x1AFFFFFF), TextZinc300)
    }

    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(shape)
            .background(background)
            .border(1.dp, border, shape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString().padStart(2, '0'),
            fontFamily = PlusJakartaSans,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
private fun EditionPill(text: String, accent: String) {
    val shape = CircleShape
    val (background, border, textColor) = when (accent) {
        "amber" -> Triple(Color(0x1AFBBF24), Color(0x4DFCD34D), Amber200)
        "purple" -> Triple(Color(0x1AA855F7), Color(0x4DD8B4FE), SecondaryLavender)
        else -> Triple(Color(0x08FFFFFF), Color(0x1AFFFFFF), TextZinc300)
    }

    Box(
        modifier = Modifier
            .clip(shape)
            .background(background)
            .border(1.dp, border, shape)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontFamily = PlusJakartaSans,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.4.sp,
            color = textColor,
            maxLines = 1
        )
    }
}

@Composable
private fun NewEntryChip() {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Amber300)
            .padding(horizontal = 7.dp, vertical = 2.dp)
            .testTag("theme_pandel_new_badge"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "NEW",
            fontFamily = PlusJakartaSans,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = Color(0xFF0B0B10),
            maxLines = 1
        )
    }
}

@Composable
private fun LocationRow(item: ThemePandel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("theme_pandel_location_${item.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = TextSoftZinc,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        if (item.locationNoteBengali.isNotBlank()) {
            Text(
                text = item.locationNoteBengali,
                fontFamily = FontFamily.Default,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                color = TextZinc300
            )
        }
        if (item.locationNote.isNotBlank()) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "(${item.locationNote})",
                fontFamily = PlusJakartaSans,
                fontSize = 10.sp,
                color = TextZinc500
            )
        }
    }
}

@Composable
private fun SpecialAttractionChip(item: ThemePandel, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(6.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0x1AFBBF24))
            .border(1.dp, Color(0x33FCD34D), shape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .testTag("theme_pandel_special_${item.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.specialAttraction,
            fontFamily = FontFamily.Default,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 15.sp,
            color = Color(0xE6FDE68A)
        )
    }
}

@Composable
private fun MapChip(onClick: () -> Unit) {
    val shape = RoundedCornerShape(6.dp)
    Row(
        modifier = Modifier
            .clip(shape)
            .background(Color(0x0FFFFFFF))
            .border(1.dp, Color(0x26FFFFFF), shape)
            .clickable(onClick = onClick)
            .height(28.dp)
            .padding(horizontal = 10.dp)
            .testTag("theme_pandel_map_chip"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.CallMade,
            contentDescription = null,
            tint = TextZinc200,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Map",
            fontFamily = PlusJakartaSans,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextZinc200
        )
    }
}

@Composable
private fun ThemePandelFooter() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x0FFFFFFF))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "© Crafted BY VIDESH KARMAKAR",
            modifier = Modifier
                .fillMaxWidth()
                .testTag("theme_pandel_footer"),
            fontFamily = PlusJakartaSans,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.1.sp,
            color = TextSoftZinc,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun accentBorder(accent: String): Color = when (accent) {
    "amber" -> Color(0x33FCD34D)
    "purple" -> Color(0x33D8B4FE)
    else -> CardBorder
}

private fun openGoogleMaps(context: Context, query: String) {
    val uri = Uri.parse(
        "https://www.google.com/maps/search/?api=1&query=" + Uri.encode(query)
    )
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    } catch (_: Exception) {
        Toast.makeText(context, "No maps app available", Toast.LENGTH_SHORT).show()
    }
}
