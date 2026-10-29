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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TempleHindu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ThemePandel
import com.example.model.ThemePandelData
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.ObsidianNoir
import com.example.ui.theme.PlusJakartaSans

private val SurfaceLow = Color(0xFF0E0E14)
private val SurfaceCard = Color(0xFF13131B)
private val SurfaceHigh = Color(0xFF181824)
private val CardBorder = Color(0x14FFFFFF)
private val TextZinc100 = Color(0xFFF4F4F5)
private val TextZinc300 = Color(0xFFD4D4D8)
private val TextZinc400 = Color(0xFFA1A1AA)
private val TextZinc500 = Color(0xFF71717A)
private val Amber300 = Color(0xFFFCD34D)
private val Amber400 = Color(0xFFFBBF24)

@Composable
fun ThemePandelScreen(
    onOpenSidebar: () -> Unit = {},
    onOpenDeveloperCredits: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    val query = searchQuery.trim()
    val allItems = ThemePandelData.items
    val visibleItems = remember(query) {
        if (query.isBlank()) {
            allItems
        } else {
            allItems.filter { it.matchesQuery(query) }
        }
    }

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

        item(key = "theme_pandel_hero") {
            ThemePandelHero(totalPandels = allItems.size)
        }

        item(key = "theme_pandel_search") {
            ThemePandelSearch(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )
        }

        if (visibleItems.isEmpty()) {
            item(key = "theme_pandel_empty") {
                ThemePandelEmpty(
                    hasQuery = query.isNotBlank(),
                    onClearSearch = { searchQuery = "" }
                )
            }
        } else {
            itemsIndexed(
                items = visibleItems,
                key = { _, item -> item.id.ifBlank { item.committee } }
            ) { index, item ->
                ThemePandelCard(
                    item = item,
                    displayNumber = allItems.indexOf(item) + 1,
                    context = context,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = if (index == visibleItems.lastIndex) 0.dp else 14.dp
                    )
                )
            }
        }

        item(key = "theme_pandel_footer") {
            ThemePandelFooter()
        }
    }
}

@Composable
private fun ThemePandelHero(totalPandels: Int) {
    val glowTint = LocalAppPalette.current.accentBorder
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceLow)
            .drawBehind {
                val glowRadius = 118.dp.toPx()
                val glowCenter = Offset(size.width - 12.dp.toPx(), 18.dp.toPx())
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(glowTint, Color.Transparent),
                        center = glowCenter,
                        radius = glowRadius
                    ),
                    radius = glowRadius,
                    center = glowCenter
                )
            }
            .border(1.dp, CardBorder, shape)
            .padding(horizontal = 18.dp, vertical = 16.dp)
            .testTag("theme_pandel_hero_card")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LocalAppPalette.current.accentDeep)
                        .border(
                            1.dp,
                            LocalAppPalette.current.accentBorderStrong,
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TempleHindu,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "NAIHATI KALI PUJA 2026",
                        fontFamily = PlusJakartaSans,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp,
                        color = LocalAppPalette.current.accentBright,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "The Theme Pandel Trail",
                        fontFamily = PlusJakartaSans,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp,
                        color = TextZinc100
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "The 12 most talked-about pandal themes of the season — committee by committee, theme by theme.",
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp,
                color = TextZinc400
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ThemeStatChip(label = "$totalPandels Pandels", icon = Icons.Default.Palette)
                ThemeStatChip(label = "12 Committees")
                ThemeStatChip(label = "2026 Edition")
            }
        }
    }
}

@Composable
private fun ThemeStatChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    val shape = CircleShape
    Row(
        modifier = Modifier
            .clip(shape)
            .background(LocalAppPalette.current.accentSoft)
            .border(1.dp, LocalAppPalette.current.accentBorder, shape)
            .padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LocalAppPalette.current.accentBright,
                modifier = Modifier.size(11.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        Text(
            text = label,
            fontFamily = PlusJakartaSans,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = LocalAppPalette.current.accentBright,
            maxLines = 1
        )
    }
}

@Composable
private fun ThemePandelSearch(
    query: String,
    onQueryChange: (String) -> Unit
) {
    var focused by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(14.dp)
    val borderColor = if (focused || query.isNotEmpty()) {
        LocalAppPalette.current.accentBorderStrong
    } else {
        CardBorder
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(SurfaceCard)
                .border(1.dp, borderColor, shape)
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .testTag("theme_pandel_search_box"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = LocalAppPalette.current.accentBright,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focused = it.isFocused }
                    .testTag("theme_pandel_search_field"),
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                ),
                cursorBrush = SolidColor(LocalAppPalette.current.accentBright),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = "Search committee, theme or locality...",
                                fontFamily = PlusJakartaSans,
                                color = TextZinc500,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }
                }
            )

            if (query.isNotEmpty()) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .clickable { onQueryChange("") }
                        .testTag("theme_pandel_search_clear"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        tint = TextZinc400,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemePandelCard(
    item: ThemePandel,
    displayNumber: Int,
    context: Context,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    val accent = LocalAppPalette.current.accentBright

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceLow)
            .border(1.dp, CardBorder, shape)
            .testTag("theme_pandel_card_${item.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                LocalAppPalette.current.accentDeep,
                                LocalAppPalette.current.accentStrong
                            )
                        )
                    )
                    .border(
                        1.dp,
                        LocalAppPalette.current.accentBorderStrong,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayNumber.toString().padStart(2, '0'),
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "THEME ${displayNumber.toString().padStart(2, '0')}",
                        fontFamily = PlusJakartaSans,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.9.sp,
                        color = accent,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    EditionPill(text = item.edition)
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = item.committeeBengali.ifBlank { item.committee },
                    fontFamily = FontFamily.Default,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp,
                    color = Color.White
                )

                if (item.committeeBengali.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.committee,
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 14.sp,
                        color = TextZinc400
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x0DFFFFFF))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 13.dp, bottom = 16.dp)
        ) {
            Text(
                text = "PANDAL THEME",
                fontFamily = PlusJakartaSans,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = accent
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = item.pandalBengali.ifBlank { item.pandal },
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp,
                color = Amber300
            )

            if (item.pandalBengali.isNotBlank() && item.pandal.isNotBlank()) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = item.pandal,
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 15.sp,
                    color = TextZinc300
                )
            }

            if (item.locationNote.isNotBlank() || item.locationNoteBengali.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Amber400,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = listOf(item.locationNoteBengali, item.locationNote)
                            .filter { it.isNotBlank() }
                            .joinToString(" • "),
                        fontFamily = FontFamily.Default,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 14.sp,
                        color = TextZinc400
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${item.edition} • Naihati",
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextZinc500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                MapChip(
                    onClick = {
                        openGoogleMaps(
                            context = context,
                            query = "${item.committee} Naihati Kali Puja"
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun EditionPill(text: String) {
    val shape = CircleShape
    Box(
        modifier = Modifier
            .clip(shape)
            .background(LocalAppPalette.current.accentSoft)
            .border(1.dp, LocalAppPalette.current.accentBorder, shape)
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontFamily = PlusJakartaSans,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = LocalAppPalette.current.accentBright,
            maxLines = 1
        )
    }
}

@Composable
private fun MapChip(onClick: () -> Unit) {
    val shape = RoundedCornerShape(10.dp)
    Row(
        modifier = Modifier
            .clip(shape)
            .background(LocalAppPalette.current.accentDeep)
            .border(1.dp, LocalAppPalette.current.accentBorderStrong, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 11.dp, vertical = 6.dp)
            .testTag("theme_pandel_map_chip"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "Map",
            fontFamily = PlusJakartaSans,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
private fun ThemePandelEmpty(
    hasQuery: Boolean,
    onClearSearch: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceLow)
            .border(1.dp, CardBorder, shape)
            .padding(24.dp)
            .testTag("theme_pandel_empty"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = TextZinc500,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (hasQuery) "No theme pandals match your search" else "Theme pandals are being updated",
            fontFamily = PlusJakartaSans,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (hasQuery) {
                "Try another committee, theme or locality."
            } else {
                "Pull the latest guide from Settings and try again."
            },
            fontFamily = PlusJakartaSans,
            color = TextZinc400,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        if (hasQuery) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Clear search",
                fontFamily = PlusJakartaSans,
                color = LocalAppPalette.current.accentBright,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClearSearch() }
                    .border(1.dp, LocalAppPalette.current.accentBorderStrong, CircleShape)
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            )
        }
    }
}

@Composable
private fun ThemePandelFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(LocalAppPalette.current.accentDeep, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "• Jay Maa Boro Maa •",
                fontFamily = PlusJakartaSans,
                color = TextZinc300,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.4.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(LocalAppPalette.current.accentDeep, CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        SectionCreditFooter(
            topPadding = 0.dp,
            bottomPadding = 0.dp
        )
    }
}

private fun ThemePandel.matchesQuery(rawQuery: String): Boolean {
    val query = rawQuery.trim()
    if (query.isEmpty()) return true

    return listOf(
        committee,
        committeeBengali,
        pandal,
        pandalBengali,
        edition,
        locationNote,
        locationNoteBengali
    ).any { value -> value.contains(query, ignoreCase = true) }
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
