package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppContent
import com.example.model.RestaurantPlace
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter

private val RestaurantPage = Color(0xFF0A0B0E)
private val RestaurantSurface = Color(0xFF111217)
private val RestaurantChip = Color(0x0DFFFFFF)
private val RestaurantDivider = Color(0x0DFFFFFF)
private val RestaurantHairline = Color(0x12FFFFFF)
private val RestaurantBrightBorder = Color(0x2EFFFFFF)
private val RestaurantZinc = Color(0xFFD4D4D8)
private val RestaurantZincBody = Color(0xFFA1A1AA)
private val RestaurantZincMuted = Color(0xFF7E8391)
private val RestaurantInk = Color(0xFF0A0B0E)

private const val FILTER_ALL = "All Eateries"
private const val FILTER_HOTELS = "Hotels & Stays"

private val restaurantFilters = listOf(
    FILTER_ALL,
    "Biryani & Mughlai",
    "Bengali Thali",
    "Street Food",
    "Sweets & Desserts",
    FILTER_HOTELS
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
    var searchQuery by remember { mutableStateOf("") }

    val restaurants = content.restaurants
    val query = searchQuery.trim()
    val filteredRestaurants = remember(selectedFilter, restaurants, query) {
        val base = when (selectedFilter) {
            FILTER_ALL -> restaurants
            FILTER_HOTELS -> emptyList()
            else -> restaurants.filter { place -> place.categories.any { it == selectedFilter } }
        }
        if (query.isBlank()) {
            base
        } else {
            base.filter { it.matchesQuery(query) }
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

        // ============ SEARCH ============
        item(key = "restaurant_search") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(RestaurantSurface)
                    .border(1.dp, RestaurantHairline, RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = RestaurantZincMuted,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search restaurant, biryani, sweets, stay...",
                            fontSize = 12.sp,
                            color = RestaurantZincMuted,
                            maxLines = 1
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 12.sp, color = Color.White),
                        cursorBrush = SolidColor(Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("restaurant_search_field")
                    )
                }
                if (searchQuery.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        tint = RestaurantZincMuted,
                        modifier = Modifier
                            .size(15.dp)
                            .clickable { searchQuery = "" }
                            .testTag("restaurant_search_clear")
                    )
                }
            }
        }

        // ============ QUICK CATEGORY FILTERS ============
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                restaurantFilters.forEach { filter ->
                    val selected = selectedFilter == filter
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (selected) Color.White else Color.Transparent)
                            .border(
                                1.dp,
                                if (selected) Color.White else RestaurantHairline,
                                CircleShape
                            )
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("restaurant_filter_${filter.lowercase().replace(" ", "_")}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = filter,
                            fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (selected) RestaurantInk else RestaurantZinc,
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
                    message = when {
                        selectedFilter == FILTER_HOTELS ->
                            "Hotels & stays are listed in the accommodation section below."
                        query.isNotBlank() -> "No eateries match \"$query\"."
                        else -> "No eateries match \"$selectedFilter\"."
                    },
                    onShowAll = {
                        searchQuery = ""
                        selectedFilter = FILTER_ALL
                    },
                    onShowAllVisible = selectedFilter != FILTER_HOTELS
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
        item(key = "restaurant_stays_header") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACCOMMODATION & LODGING • NAIHATI ZONE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.6.sp,
                    color = RestaurantZinc,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${content.accommodationPlaces.size} Verified",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = RestaurantZincMuted
                )
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
                    .padding(top = 20.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Naihati Kali Puja Food Guide • Curated by Videsh Karmakar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = RestaurantZincBody,
                    textAlign = TextAlign.Center
                )
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
    val shape = RoundedCornerShape(16.dp)
    val sacred = place.name.startsWith("Boro Maa")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(shape)
            .background(RestaurantSurface)
            .border(1.dp, if (sacred) RestaurantBrightBorder else RestaurantHairline, shape)
            .testTag("restaurant_card_${place.id}")
    ) {
        if (sacred) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0x33FFFFFF))
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = place.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = place.locationDescription,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = RestaurantZincBody,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                place.dishes.forEach { dish ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(RestaurantChip)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = dish,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = RestaurantZinc,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(RestaurantDivider)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RestaurantAction(
                    label = "Open in Maps",
                    onClick = onMap,
                    style = RestaurantActionStyle.Solid,
                    modifier = Modifier.weight(1f),
                    testTag = "restaurant_map_${place.id}"
                )
                RestaurantAction(
                    label = "Call / Menu",
                    onClick = onCall,
                    style = RestaurantActionStyle.Outline,
                    modifier = Modifier.weight(1f),
                    testTag = "restaurant_call_${place.id}"
                )
            }
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
    val priceMain = priceRange
        .removeSuffix("/night")
        .removeSuffix("/ night")
        .trimEnd()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(RestaurantSurface)
            .border(1.dp, RestaurantHairline, RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("restaurant_stay_${name.lowercase().replace(" ", "_")}")
    ) {
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            lineHeight = 18.sp
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = location,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            color = RestaurantZincBody,
            lineHeight = 14.sp
        )
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = priceMain,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = RestaurantZinc
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "/ night",
                    fontSize = 10.sp,
                    color = RestaurantZincMuted
                )
            }
            RestaurantAction(
                label = "Location & Booking",
                onClick = onMap,
                style = RestaurantActionStyle.Soft,
                testTag = "restaurant_stay_map_$name"
            )
        }
    }
}

private enum class RestaurantActionStyle { Solid, Outline, Soft }

@Composable
private fun RestaurantAction(
    label: String,
    onClick: () -> Unit,
    style: RestaurantActionStyle,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    val shape = RoundedCornerShape(12.dp)
    val container = when (style) {
        RestaurantActionStyle.Solid -> Color.White
        RestaurantActionStyle.Soft -> Color(0x1AFFFFFF)
        RestaurantActionStyle.Outline -> Color.Transparent
    }
    val contentColor = if (style == RestaurantActionStyle.Solid) RestaurantInk else RestaurantZinc

    Box(
        modifier = modifier
            .clip(if (style == RestaurantActionStyle.Soft) RoundedCornerShape(8.dp) else shape)
            .background(container)
            .border(
                1.dp,
                if (style == RestaurantActionStyle.Outline) RestaurantHairline else Color.Transparent,
                if (style == RestaurantActionStyle.Soft) RoundedCornerShape(8.dp) else shape
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (style == RestaurantActionStyle.Solid) FontWeight.SemiBold else FontWeight.Medium,
            color = contentColor,
            maxLines = 1
        )
    }
}

@Composable
private fun RestaurantEmptyState(
    message: String,
    onShowAll: () -> Unit,
    onShowAllVisible: Boolean = true
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
        if (onShowAllVisible) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable { onShowAll() }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Show all eateries",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RestaurantInk
                )
            }
        }
    }
}

private fun RestaurantPlace.matchesQuery(query: String): Boolean =
    name.contains(query, ignoreCase = true) ||
        cuisine.contains(query, ignoreCase = true) ||
        locationDescription.contains(query, ignoreCase = true) ||
        dishes.any { it.contains(query, ignoreCase = true) }

