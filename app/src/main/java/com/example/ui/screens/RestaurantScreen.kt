package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppContent
import com.example.model.RestaurantPlace
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.LocalAppPalette

private val RestaurantPage = Color(0xFF050505)
private val RestaurantSurface = Color(0xFF0E0E14)
private val RestaurantChip = Color(0xFF13131B)
private val RestaurantButton = Color(0xFF181824)
private val RestaurantHairline = Color(0x14FFFFFF)
private val RestaurantDivider = Color(0x0DFFFFFF)
private val RestaurantViolet: Color @Composable get() = LocalAppPalette.current.accentDeep
private val RestaurantVioletBorder: Color @Composable get() = LocalAppPalette.current.accentBorder
private val RestaurantActiveBorder: Color @Composable get() = LocalAppPalette.current.accentStrong
private val RestaurantZinc = Color(0xFFD4D4D8)
private val RestaurantZincBody = Color(0xFFA1A1AA)
private val RestaurantZincMuted = Color(0xFF71717A)
private val RestaurantLavender: Color @Composable get() = LocalAppPalette.current.accentBright
private val RestaurantGreen = Color(0xFF34D399)
private val RestaurantAmber = Color(0xFFFBBF24)
private val RestaurantAmberText = Color(0xFFFCD34D)

private const val FILTER_ALL = "All Eateries"
private const val FILTER_LATE_NIGHT = "Late Night (24x7)"

private val restaurantFilters = listOf(
    FILTER_ALL,
    "Biryani & Mughlai",
    "Bengali Thali",
    "Street Food",
    "Cafes & Snacks",
    "Sweets & Desserts",
    FILTER_LATE_NIGHT
)

@Composable
fun RestaurantScreen(
    content: AppContent = AppContent(),
    onOpenSidebar: () -> Unit = {},
    onOpenDeveloperCredits: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf(FILTER_ALL) }

    val restaurants = content.restaurants
    val filteredRestaurants = remember(selectedFilter, restaurants) {
        restaurants.filter { place ->
            when (selectedFilter) {
                FILTER_ALL -> true
                FILTER_LATE_NIGHT -> place.isLateNight
                else -> place.categories.any { it == selectedFilter }
            }
        }
    }

    fun openInGoogleMaps(query: String) {
        val webUri = Uri.parse(
            "https://www.google.com/maps/search/?api=1&query=" + Uri.encode(query)
        )
        val geoUri = Uri.parse("geo:0,0?q=" + Uri.encode(query))
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
        } catch (e: Exception) {
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, geoUri))
            } catch (e2: Exception) {
                Toast.makeText(context, "Maps are unavailable on this device", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun dial(number: String) {
        val clean = number.replace(Regex("[^0-9+]"), "")
        try {
            context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$clean")))
        } catch (e: Exception) {
            Toast.makeText(context, "Calling is unavailable on this device", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(RestaurantPage)
            .statusBarsPadding()
            .testTag("restaurant_screen"),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // ============ TOP BAR ============
        item {
            AppBrandHeader(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onOpenSidebar = onOpenSidebar,
                onOpenDeveloperCredits = onOpenDeveloperCredits
            )
        }

        // ============ QUICK CATEGORY FILTERS ============
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                restaurantFilters.forEach { filter ->
                    val selected = selectedFilter == filter
                    val lateNight = filter == FILTER_LATE_NIGHT
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (selected) RestaurantViolet else RestaurantSurface)
                            .border(
                                1.dp,
                                if (selected) RestaurantActiveBorder else RestaurantHairline,
                                CircleShape
                            )
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .testTag("restaurant_filter_${filter.lowercase().replace(" ", "_")}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(if (lateNight) 4.dp else 6.dp)
                    ) {
                        if (lateNight) {
                            PulsingDot(color = RestaurantAmber, size = 6.dp)
                        }
                        Text(
                            text = filter,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = when {
                                selected -> Color.White
                                lateNight -> RestaurantAmberText
                                else -> RestaurantZinc
                            },
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // ============ EATERIES ============
        if (filteredRestaurants.isEmpty()) {
            item {
                RestaurantEmptyState(
                    message = "No eateries match \"$selectedFilter\".",
                    onShowAll = { selectedFilter = FILTER_ALL }
                )
            }
        } else {
            items(filteredRestaurants, key = { it.id }) { place ->
                RestaurantCard(
                    place = place,
                    onMap = { openInGoogleMaps(place.mapQuery) },
                    onCall = { dial(place.phone) }
                )
            }
        }

        // ============ HOTELS & STAYS ============
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 6.dp)
            ) {
                Text(
                    text = "ACCOMMODATION & STAYS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = RestaurantAmber,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Hotel,
                        contentDescription = null,
                        tint = RestaurantAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Best Hotels & Lodging in Naihati Zone",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        if (content.accommodationPlaces.isEmpty()) {
            item {
                Text(
                    text = "No stays listed yet.",
                    fontSize = 10.sp,
                    color = RestaurantZincMuted,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        } else {
            items(content.accommodationPlaces, key = { it.id }) { stay ->
                RestaurantStayCard(
                    name = stay.name,
                    location = stay.locationDescription,
                    priceRange = stay.priceRange,
                    onMap = { openInGoogleMaps("${stay.name}, Naihati") }
                )
            }
        }

        // ============ FOOTER ============
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(RestaurantViolet)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• Jay Boro Maa •",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RestaurantZinc,
                        letterSpacing = 0.6.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(RestaurantViolet)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                SectionCreditFooter(
                    topPadding = 0.dp,
                    bottomPadding = 0.dp
                )
            }
        }

        // Space for the floating bottom dock.
        item { Spacer(modifier = Modifier.height(104.dp)) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RestaurantCard(
    place: RestaurantPlace,
    onMap: () -> Unit,
    onCall: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(RestaurantSurface)
            .border(1.dp, RestaurantHairline, RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("restaurant_card_${place.id}")
    ) {
        Text(
            text = place.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 15.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = place.cuisine,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = RestaurantLavender
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = place.locationDescription,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = RestaurantZincBody,
            lineHeight = 13.sp
        )

        Spacer(modifier = Modifier.height(10.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            place.dishes.forEach { dish ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(RestaurantChip)
                        .border(1.dp, RestaurantHairline, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = dish,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = RestaurantZinc,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(RestaurantDivider)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RestaurantAction(
                label = "Open in Google Maps",
                icon = Icons.Default.Place,
                onClick = onMap,
                modifier = Modifier.weight(1f),
                primary = true,
                testTag = "restaurant_map_${place.id}"
            )
            RestaurantAction(
                label = "Call / Menu",
                icon = Icons.Default.Phone,
                onClick = onCall,
                primary = false,
                testTag = "restaurant_call_${place.id}"
            )
        }
    }
}

@Composable
private fun RestaurantStayCard(
    name: String,
    location: String,
    priceRange: String,
    onMap: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(RestaurantSurface)
            .border(1.dp, RestaurantHairline, RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("restaurant_stay_${name.lowercase().replace(" ", "_")}"),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = location,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = RestaurantZincBody,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(RestaurantChip)
                    .border(1.dp, RestaurantHairline, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = priceRange,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = RestaurantZinc
                )
            }
        }
        RestaurantAction(
            label = "Location",
            icon = Icons.Default.LocationOn,
            onClick = onMap,
            primary = true,
            compact = true,
            testTag = "restaurant_stay_map_$name"
        )
    }
}

@Composable
private fun RestaurantAction(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    primary: Boolean,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    testTag: String = ""
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (primary) RestaurantViolet else RestaurantButton)
            .border(
                1.dp,
                if (primary) RestaurantVioletBorder else RestaurantHairline,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(
                horizontal = if (compact) 12.dp else 8.dp,
                vertical = 6.dp
            )
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (primary && !compact) Color.White else RestaurantLavender,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(if (compact) 6.dp else 4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (primary) FontWeight.Bold else FontWeight.Medium,
            color = if (primary) Color.White else RestaurantZinc,
            maxLines = 1
        )
    }
}

@Composable
private fun RestaurantEmptyState(
    message: String,
    onShowAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(RestaurantSurface)
            .border(1.dp, RestaurantHairline, RoundedCornerShape(16.dp))
            .padding(vertical = 24.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(RestaurantViolet)
                .border(1.dp, RestaurantVioletBorder, RoundedCornerShape(12.dp))
                .clickable { onShowAll() }
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Show all eateries",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun PulsingDot(color: Color, size: androidx.compose.ui.unit.Dp) {
    val transition = rememberInfiniteTransition(label = "restaurantPulse")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "restaurantPulseAlpha"
    )
    Box(
        modifier = Modifier
            .size(size)
            .alpha(alpha)
            .clip(CircleShape)
            .background(color)
    )
}
