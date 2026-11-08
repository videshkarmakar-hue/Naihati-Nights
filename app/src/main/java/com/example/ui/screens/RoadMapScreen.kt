package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.ObsidianNoir
import com.example.ui.theme.PlusJakartaSans

/**
 * Road Map: a high-level map of the Naihati parikrama route and the app's sections.
 * Reached from the sidebar entry under the dedicated "Road Map" slot.
 */
@Composable
fun RoadMapScreen(
    onBack: () -> Unit,
    onOpenSidebar: () -> Unit = { },
    onOpenDeveloperCredits: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    val palette = LocalAppPalette.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianNoir),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 448.dp)
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                .testTag("road_map_screen"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item(key = "road_map_header") {
                AppBrandHeader(
                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                    onOpenSidebar = onOpenSidebar,
                    onOpenDeveloperCredits = onOpenDeveloperCredits,
                    navigation = {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x0AFFFFFF))
                                .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(12.dp))
                                .testTag("road_map_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFFF4F4F5),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            item(key = "road_map_title") {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Road Map",
                        fontFamily = PlusJakartaSans,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF4F4F5)
                    )
                    Text(
                        text = "Parikrama route map and app sections overview.",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }

            item(key = "road_map_image") {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://cdn.phototourl.com/member/2026-11-08-4d519aaa-ddc3-47dc-94a7-b48498331714.jpg")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Road Map",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(16.dp))
                )
            }

            item(key = "road_map_footer") {
                SectionCreditFooter(topPadding = 8.dp, bottomPadding = 4.dp)
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}
