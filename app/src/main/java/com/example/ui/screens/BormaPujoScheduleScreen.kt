package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BormaPujoScheduleData
import com.example.model.PujoDay
import com.example.model.PujoEvent
import com.example.ui.components.AppBrandHeader
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.ObsidianNoir
import com.example.ui.theme.PlusJakartaSans

private val ScheduleCard = Color(0xFF101018)
private val ScheduleInset = Color(0xFF15151D)
private val ScheduleText = Color(0xFFF4F4F5)
private val ScheduleBody = Color(0xFFD4D4D8)
private val ScheduleMuted = Color(0xFFA1A1AA)
private val ScheduleFaint = Color(0xFF71717A)
private val ScheduleHairline = Color(0x14FFFFFF)

/**
 * Borma Pujo bulletin for Naihati, reached from the bottom navigation bar and the sidebar panel.
 *
 * The committee copy stays verbatim in [BormaPujoScheduleData] so the night can be read out without
 * translation; the primary puja moment is flagged inside its day list for instant reading.
 */
@Composable
fun BormaPujoScheduleScreen(
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
                .testTag("borma_pujo_schedule_screen"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 128.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "borma_header") {
                Column {
                    AppBrandHeader(
                        onOpenSidebar = onOpenSidebar,
                        onOpenDeveloperCredits = onOpenDeveloperCredits,
                        navigation = {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x0AFFFFFF))
                                    .border(1.dp, ScheduleHairline, RoundedCornerShape(12.dp))
                                    .testTag("borma_pujo_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = ScheduleText,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item(key = "borma_hero") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(palette.accentSurfaceStrong, ScheduleCard)
                            )
                        )
                        .border(1.dp, palette.accentBorder, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                        .testTag("borma_pujo_hero"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(palette.accentSoft)
                            .border(1.dp, palette.accentBorderStrong, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = null,
                            tint = palette.accentBright,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = BormaPujoScheduleData.kicker,
                            fontFamily = FontFamily.Default,
                            fontSize = 10.sp,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.9.sp,
                            color = palette.accentBright
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = BormaPujoScheduleData.title,
                            fontFamily = FontFamily.Default,
                            fontSize = 20.sp,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = ScheduleText,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = BormaPujoScheduleData.subtitle,
                            fontFamily = FontFamily.Default,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = ScheduleMuted
                        )
                    }
                }
            }

            item(key = "borma_days") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(ScheduleInset)
                        .border(1.dp, ScheduleHairline, RoundedCornerShape(20.dp))
                        .padding(vertical = 4.dp)
                        .testTag("borma_pujo_days")
                ) {
                    BormaPujoScheduleData.days.forEachIndexed { index, day ->
                        BormaPujoDay(
                            day = day,
                            isFirst = index == 0,
                            isLast = index == BormaPujoScheduleData.days.lastIndex
                        )
                    }
                }
            }

            item(key = "borma_note") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ScheduleCard)
                        .border(1.dp, ScheduleHairline, RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                        .testTag("borma_pujo_note")
                ) {
                    Text(
                        text = BormaPujoScheduleData.timeZoneNote,
                        fontFamily = FontFamily.Default,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        color = ScheduleMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = BormaPujoScheduleData.closingNote,
                        fontFamily = FontFamily.Default,
                        fontSize = 10.sp,
                        lineHeight = 15.sp,
                        color = ScheduleFaint
                    )
                }
            }
        }
    }
}

@Composable
private fun BormaPujoDay(
    day: PujoDay,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("borma_pujo_day_${day.id}")
    ) {
        if (!isFirst) {
            BormaPujoDivider()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp)
        ) {
            Text(
                text = day.dayLabel,
                fontFamily = FontFamily.Default,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = ScheduleText
            )
        }

        day.events.forEachIndexed { eventIndex, event ->
            BormaPujoEvent(
                event = event,
                number = eventIndex + 1,
                showDivider = eventIndex < day.events.lastIndex
            )
        }

        day.note?.let { note ->
            Text(
                text = note,
                fontFamily = FontFamily.Default,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                color = ScheduleFaint,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
        }

        if (!isLast) {
            BormaPujoDivider()
        }
    }
}

@Composable
private fun BormaPujoEvent(
    event: PujoEvent,
    number: Int,
    showDivider: Boolean,
    modifier: Modifier = Modifier
) {
    val palette = LocalAppPalette.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .testTag("borma_pujo_event_${event.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (event.highlight) palette.accentSoft else Color.Transparent)
                .padding(horizontal = 6.dp, vertical = 11.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (event.highlight) palette.accentDeep else Color(0xFF1E1E28)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (event.highlight) Color.White else ScheduleMuted
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.time,
                    fontFamily = FontFamily.Default,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = if (event.highlight) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (event.highlight) palette.accentBright else ScheduleText
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = event.detail ?: "",
                    fontFamily = FontFamily.Default,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    fontWeight = if (event.highlight) FontWeight.Bold else FontWeight.Medium,
                    color = ScheduleText
                )
            }
        }

        if (showDivider) {
            BormaPujoDivider()
        }
    }
}

@Composable
private fun BormaPujoDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(ScheduleHairline)
    )
}
