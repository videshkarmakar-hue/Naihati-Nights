package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TempleHindu
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Water
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppContent
import com.example.model.CorridorFacility
import com.example.model.PandalRoute
import com.example.model.PandalStop
import com.example.model.RouteFacilities
import com.example.model.RouteNote
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.ObsidianNoir

private val SurfaceLow = Color(0xFF0E0E14)
private val SurfaceCard = Color(0xFF13131B)
private val SurfaceHigh = Color(0xFF181824)
private val RegalViolet: Color @Composable get() = LocalAppPalette.current.accentDeep
private val RegalVioletBorder: Color @Composable get() = LocalAppPalette.current.accentStrong
private val VioletBorderSoft: Color @Composable get() = LocalAppPalette.current.accentBorder
private val PaleLavender: Color @Composable get() = LocalAppPalette.current.accentBright
private val TextZinc100 = Color(0xFFF4F4F5)
private val TextZinc200 = Color(0xFFE4E4E7)
private val TextZinc300 = Color(0xFFD4D4D8)
private val TextZinc400 = Color(0xFFA1A1AA)
private val TextZinc500 = Color(0xFF71717A)
private val CardBorder = Color(0x14FFFFFF)
private val StatusGreen = Color(0xFF34D399)
private val Rose300 = Color(0xFFFDA4AF)
private val Rose400 = Color(0xFFFB7185)
private val RoseCard = Color(0x4D4C0519)
private val RoseBorder = Color(0x4DF43F5E)
private val Emerald400 = Color(0xFF34D399)
private val Blue400 = Color(0xFF60A5FA)
private val Amber300 = Color(0xFFFCD34D)
private val Amber400 = Color(0xFFFBBF24)
private val Amber500 = Color(0xFFF59E0B)
private val AmberCardStart = Color(0x6678350F)
private val AmberBorder = Color(0x80F59E0B)

@Composable
fun HomeScreen(
    content: AppContent = AppContent(),
    onOpenSidebar: () -> Unit = {},
    onOpenDeveloperCredits: () -> Unit = {},
    onOpenDining: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var expandedRouteIds by remember { mutableStateOf(emptySet<String>()) }

    val routes = content.routes
    val query = searchQuery.trim()
    val visibleRoutes = remember(routes, query) {
        if (query.isBlank()) routes else routes.filter { it.matchesQuery(query) }
    }
    val searchAutoExpands = query.length > 1

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianNoir)
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 104.dp)
    ) {
        item(key = "home_top_bar") {
            HomeTopBar(
                onOpenSidebar = onOpenSidebar,
                onOpenDeveloperCredits = onOpenDeveloperCredits
            )
        }

        item(key = "home_hero") {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)) {
                RoutesHero(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
            }
        }

        item(key = "home_hero_spacer") {
            Spacer(modifier = Modifier.height(14.dp))
        }

        if (visibleRoutes.isEmpty()) {
            item(key = "home_empty") {
                EmptyRoutes(
                    hasQuery = query.isNotBlank(),
                    onClearSearch = { searchQuery = "" }
                )
            }
        } else {
            items(visibleRoutes, key = { it.id }) { route ->
                RouteAccordionCard(
                    route = route,
                    expanded = searchAutoExpands || route.id in expandedRouteIds,
                    onToggle = {
                        expandedRouteIds = if (route.id in expandedRouteIds) {
                            expandedRouteIds - route.id
                        } else {
                            expandedRouteIds + route.id
                        }
                    },
                    context = context,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
                )
            }
        }

        item(key = "home_footer") {
            HomeFooter()
        }
    }
}

@Composable
private fun HomeTopBar(
    onOpenSidebar: () -> Unit,
    onOpenDeveloperCredits: () -> Unit
) {
    AppBrandHeader(
        modifier = Modifier.padding(horizontal = 16.dp),
        onOpenSidebar = onOpenSidebar,
        onOpenDeveloperCredits = onOpenDeveloperCredits
    )
}

@Composable
private fun RoutesHero(
    query: String,
    onQueryChange: (String) -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    // Read outside drawBehind: the draw scope is not composable.
    val glowTint = LocalAppPalette.current.accentBorder
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceLow)
            .drawBehind {
                // Decorative glow anchored to the top-right corner. Drawn behind the content so it
                // never inflates the card height (which previously left a large blank band under
                // the search field).
                val glowRadius = 72.dp.toPx()
                val glowCenter = Offset(size.width - 26.dp.toPx(), 26.dp.toPx())
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
            .testTag("home_hero_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            HomeSearchBox(
                query = query,
                onQueryChange = onQueryChange
            )
        }
    }
}

@Composable
private fun HomeSearchBox(
    query: String,
    onQueryChange: (String) -> Unit
) {
    var focused by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)
    val borderColor = if (focused || query.isNotEmpty()) LocalAppPalette.current.accentBorderStrong else CardBorder

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceCard)
            .border(1.dp, borderColor, shape)
            .testTag("home_search_box")
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = PaleLavender,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focused = it.isFocused }
                .testTag("home_search_field"),
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp
            ),
            cursorBrush = SolidColor(PaleLavender),
            decorationBox = { innerTextField ->
                Box {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search pandal or club (e.g., Borokali, Bankim)...",
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
                    .testTag("home_search_clear"),
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

@Composable
private fun RouteAccordionCard(
    route: PandalRoute,
    expanded: Boolean,
    onToggle: () -> Unit,
    context: Context,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 120),
        label = "route_chevron"
    )
    val displayNumber = route.displayNumber()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceLow)
            .border(1.dp, CardBorder, shape)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(16.dp)
                .testTag("home_route_card_${route.id}"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(RegalViolet)
                    .border(1.dp, RegalVioletBorder, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = route.routeNumber,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "ROUTE $displayNumber",
                        color = PaleLavender,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.7.sp,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    RouteCategoryPill(route = route)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = route.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = route.subtitle,
                    color = TextZinc400,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse route" else "Expand route",
                    tint = TextZinc400,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(chevronRotation)
                )
            }
        }

        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                RouteMapButton(
                    label = "View Route $displayNumber on Google Maps",
                    query = route.routeMapQuery.ifBlank { route.title },
                    context = context,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                route.stopGroups.forEachIndexed { groupIndex, group ->
                    if (group.title.isNotBlank()) {
                        if (groupIndex > 0) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        Text(
                            text = group.title,
                            color = PaleLavender,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 14.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    group.stops.forEachIndexed { stopIndex, stop ->
                        PandalStopRow(
                            stop = stop,
                            number = stopIndex + 1,
                            context = context
                        )
                        if (stopIndex != group.stops.lastIndex) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }

                route.detourNote?.let { note ->
                    Spacer(modifier = Modifier.height(12.dp))
                    RouteNoteCard(note = note)
                }

                route.directionNote?.let { note ->
                    Spacer(modifier = Modifier.height(12.dp))
                    RouteNoteCard(note = note)
                }

                route.facilities?.let { facilities ->
                    Spacer(modifier = Modifier.height(12.dp))
                    RouteFacilitiesCard(
                        routeNumber = displayNumber,
                        facilities = facilities,
                        context = context
                    )
                }
            }
        }
    }
}

@Composable
private fun RouteCategoryPill(route: PandalRoute) {
    val isRose = route.badgeStyle == "rose"
    val shape = CircleShape
    Row(
        modifier = Modifier
            .clip(shape)
            .background(if (isRose) Color(0x994C0519) else SurfaceHigh)
            .border(
                width = 1.dp,
                color = if (isRose) Color(0x669F1239) else Color(0x1AFFFFFF),
                shape = shape
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = route.badgeIcon(),
            contentDescription = null,
            tint = route.badgeIconTint(),
            modifier = Modifier.size(10.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = route.categoryBadge,
            color = if (isRose) Rose300 else TextZinc300,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
private fun PandalStopRow(
    stop: PandalStop,
    number: Int,
    context: Context
) {
    val shape = RoundedCornerShape(12.dp)
    val isAmber = stop.highlighted && stop.highlightAccent != "rose"
    val isRose = stop.highlighted && stop.highlightAccent == "rose"

    val backgroundModifier = when {
        isAmber -> Modifier.background(
            Brush.horizontalGradient(
                colors = listOf(AmberCardStart, SurfaceHigh, SurfaceCard)
            )
        )
        isRose -> Modifier.background(RoseCard)
        else -> Modifier.background(SurfaceCard)
    }
    val borderColor = when {
        isAmber -> AmberBorder
        isRose -> RoseBorder
        else -> CardBorder
    }
    val titleColor = when {
        isAmber -> Amber300
        isRose -> Rose300
        else -> Color.White
    }
    val bengaliColor = when {
        isAmber -> Color(0xCCFDE68A)
        isRose -> TextZinc300
        else -> TextZinc400
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .then(backgroundModifier)
            .border(1.dp, borderColor, shape)
            .padding(12.dp)
            .testTag("home_stop_${stop.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isAmber) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(Amber500)
                            .padding(horizontal = 4.dp, vertical = 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$number.",
                            color = Color.Black,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = "$number.",
                        color = if (isRose) Rose300 else PaleLavender,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.width(14.dp)
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = titleColor)) {
                            append(stop.name)
                        }
                        if (stop.bengaliName.isNotBlank()) {
                            append(" ")
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    color = bengaliColor
                                )
                            ) {
                                append("(${stop.bengaliName})")
                            }
                        }
                    },
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }

            if (stop.description.isNotBlank()) {
                Text(
                    text = stop.description,
                    color = if (stop.highlighted) TextZinc300 else TextZinc400,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(
                        start = if (isAmber) 0.dp else 14.dp,
                        top = 2.dp
                    )
                )
            }

            if (stop.secondaryLabel.isNotBlank()) {
                Text(
                    text = stop.secondaryLabel,
                    color = Amber400,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 13.sp,
                    modifier = Modifier.padding(start = 14.dp, top = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        MapChip(
            label = "Map",
            query = stop.mapQuery,
            context = context,
            emphasized = stop.highlighted,
            compact = true,
            modifier = Modifier.testTag("home_stop_map_${stop.id}")
        )
    }
}

@Composable
private fun RouteNoteCard(note: RouteNote) {
    val shape = RoundedCornerShape(12.dp)
    val accentColor = when (note.accent) {
        "amber" -> Amber400
        "violet" -> RegalViolet
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(shape)
            .background(SurfaceCard)
            .border(1.dp, CardBorder, shape)
    ) {
        if (note.accent != "none") {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )
        }

        Column(
            modifier = Modifier.padding(
                horizontal = if (note.labelOnOwnLine) 12.dp else 10.dp,
                vertical = if (note.labelOnOwnLine) 12.dp else 10.dp
            )
        ) {
            if (note.labelOnOwnLine) {
                Text(
                    text = buildAnnotatedString {
                        if (note.emoji.isNotBlank()) {
                            append(note.emoji)
                            append(" ")
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = PaleLavender)) {
                            append(note.label)
                        }
                    },
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = noteBody(note),
                    color = TextZinc300,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )
            } else {
                Text(
                    text = buildAnnotatedString {
                        if (note.emoji.isNotBlank()) {
                            append(note.emoji)
                            append(" ")
                        }
                        if (note.label.isNotBlank()) {
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = TextZinc300
                                )
                            ) {
                                append(note.label)
                            }
                            append(" ")
                        }
                        appendWithBoldPhrases(note.text, note.boldPhrases)
                    },
                    color = TextZinc300,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RouteFacilitiesCard(
    routeNumber: String,
    facilities: RouteFacilities,
    context: Context
) {
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceCard)
            .border(1.dp, CardBorder, shape)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ROUTE $routeNumber CORRIDOR FACILITIES",
                color = PaleLavender,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp,
                lineHeight = 15.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = facilities.headerSubtitle,
                color = TextZinc400,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x0DFFFFFF))
        )

        Spacer(modifier = Modifier.height(10.dp))

        facilities.facilities.forEachIndexed { index, facility ->
            CorridorFacilityRow(
                facility = facility,
                context = context
            )
            if (index != facilities.facilities.lastIndex) {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        if (facilities.delicacies.isNotEmpty()) {
            if (facilities.facilities.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🍛", fontSize = 12.sp, lineHeight = 15.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = facilities.delicaciesTitle,
                    color = Amber300,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                modifier = Modifier.padding(start = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                facilities.delicacies.forEach { delicacy ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(SurfaceHigh)
                            .border(1.dp, Color(0x0DFFFFFF), RoundedCornerShape(5.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = delicacy,
                            color = TextZinc200,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CorridorFacilityRow(
    facility: CorridorFacility,
    context: Context
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceLow)
            .border(1.dp, CardBorder, shape)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (facility.icon == "stay") "🏨" else "🍽️",
                    fontSize = 12.sp,
                    lineHeight = 15.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = facility.name,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = facility.description,
                color = TextZinc400,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 13.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        MapChip(
            label = "Map",
            query = facility.mapQuery,
            context = context,
            emphasized = true,
            compact = true
        )
    }
}

@Composable
private fun RouteMapButton(
    label: String,
    query: String,
    context: Context,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .clip(shape)
            .background(RegalViolet)
            .border(1.dp, VioletBorderSoft, shape)
            .clickable { openGoogleMaps(context, query) }
            .testTag("home_route_map_button")
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 15.sp
        )
    }
}

@Composable
private fun MapChip(
    label: String,
    query: String,
    context: Context,
    emphasized: Boolean = false,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(if (compact) 8.dp else 10.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(if (emphasized) RegalViolet else SurfaceHigh)
            .border(
                width = 1.dp,
                color = if (emphasized) LocalAppPalette.current.accentSoft else CardBorder,
                shape = shape
            )
            .clickable { openGoogleMaps(context, query) }
            .padding(
                horizontal = if (compact) 10.dp else 12.dp,
                vertical = if (compact) 4.dp else 6.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (emphasized) Color.White else TextZinc200,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 12.sp
        )
    }
}

@Composable
private fun EmptyRoutes(
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
            .padding(24.dp),
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
            text = if (hasQuery) "No routes match your search" else "Routes are being updated",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (hasQuery) {
                "Try another pandal, club or landmark name."
            } else {
                "Pull the latest guide from Settings and try again."
            },
            color = TextZinc400,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        if (hasQuery) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Clear search",
                color = PaleLavender,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClearSearch() }
                    .border(1.dp, VioletBorderSoft, CircleShape)
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            )
        }
    }
}

@Composable
private fun HomeFooter() {
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
                    .background(RegalViolet, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "• Jay Maa Boro Maa •",
                color = TextZinc300,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.4.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(RegalViolet, CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        SectionCreditFooter(
            topPadding = 0.dp,
            bottomPadding = 0.dp
        )
    }
}

private fun noteBody(note: RouteNote): AnnotatedString = buildAnnotatedString {
    appendWithBoldPhrases(note.text, note.boldPhrases)
}

private fun AnnotatedString.Builder.appendWithBoldPhrases(
    text: String,
    phrases: List<String>
) {
    if (phrases.isEmpty()) {
        append(text)
        return
    }

    var cursor = 0
    while (cursor < text.length) {
        val next = phrases
            .mapNotNull { phrase ->
                val index = text.indexOf(phrase, cursor, ignoreCase = true)
                if (index >= 0) index to phrase else null
            }
            .minByOrNull { it.first }

        if (next == null) {
            append(text.substring(cursor))
            break
        }

        append(text.substring(cursor, next.first))
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextZinc100)) {
            append(text.substring(next.first, next.first + next.second.length))
        }
        cursor = next.first + next.second.length
    }
}

private fun PandalRoute.displayNumber(): String =
    routeNumber.toIntOrNull()?.toString() ?: routeNumber

private fun PandalRoute.badgeIcon(): ImageVector = when (badgeIcon) {
    "flame" -> Icons.Default.LocalFireDepartment
    "cloud" -> Icons.Default.Cloud
    "water" -> Icons.Default.Water
    "eye" -> Icons.Default.Visibility
    "temple" -> Icons.Default.TempleHindu
    "rail" -> Icons.Default.Train
    else -> Icons.Default.AltRoute
}

@Composable
private fun PandalRoute.badgeIconTint(): Color = when (badgeIcon) {
    "flame", "temple" -> Rose400
    "cloud", "eye" -> Emerald400
    "water" -> Blue400
    "rail" -> Amber400
    else -> PaleLavender
}

private fun PandalRoute.matchesQuery(rawQuery: String): Boolean {
    val query = rawQuery.trim()
    if (query.isEmpty()) return true

    if (title.contains(query, ignoreCase = true) ||
        subtitle.contains(query, ignoreCase = true) ||
        categoryBadge.contains(query, ignoreCase = true) ||
        routeMapQuery.contains(query, ignoreCase = true) ||
        pandals.any { it.contains(query, ignoreCase = true) }
    ) {
        return true
    }

    if (stopGroups.any { group ->
            group.title.contains(query, ignoreCase = true) ||
                group.stops.any { it.matchesQuery(query) }
        }
    ) {
        return true
    }

    if (detourNote?.matchesQuery(query) == true ||
        directionNote?.matchesQuery(query) == true
    ) {
        return true
    }

    val corridor = facilities ?: return false
    return corridor.facilities.any { facility ->
        facility.name.contains(query, ignoreCase = true) ||
            facility.description.contains(query, ignoreCase = true) ||
            facility.mapQuery.contains(query, ignoreCase = true)
    } || corridor.delicacies.any { it.contains(query, ignoreCase = true) }
}

private fun PandalStop.matchesQuery(query: String): Boolean =
    name.contains(query, ignoreCase = true) ||
        bengaliName.contains(query, ignoreCase = true) ||
        description.contains(query, ignoreCase = true) ||
        secondaryLabel.contains(query, ignoreCase = true) ||
        mapQuery.contains(query, ignoreCase = true)

private fun RouteNote.matchesQuery(query: String): Boolean =
    label.contains(query, ignoreCase = true) ||
        text.contains(query, ignoreCase = true)

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
