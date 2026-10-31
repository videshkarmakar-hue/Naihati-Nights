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
import androidx.compose.material.icons.filled.Info
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
import com.example.model.BormaPujoNoticeData
import com.example.model.PujoNoticeItem
import com.example.ui.components.AppBrandHeader
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.ObsidianNoir
import com.example.ui.theme.PlusJakartaSans

private val NoticeCard = Color(0xFF101018)
private val NoticeInset = Color(0xFF15151D)
private val NoticeText = Color(0xFFF4F4F5)
private val NoticeBody = Color(0xFFD4D4D8)
private val NoticeMuted = Color(0xFFA1A1AA)
private val NoticeFaint = Color(0xFF71717A)
private val NoticeHairline = Color(0x14FFFFFF)

/**
 * Sidebar-only special notice (বিশেষ ঘোষণা) for the annual Borma Pujo observance.
 *
 * Reads the committee's puja closure, darshan and prasad windows in one calm, typographic list;
 * kept off the bottom navigation so the notice never competes with the primary destinations.
 */
@Composable
fun SpecialNoticeScreen(
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
                .testTag("special_notice_screen"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 128.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "special_notice_header") {
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
                                    .border(1.dp, NoticeHairline, RoundedCornerShape(12.dp))
                                    .testTag("special_notice_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = NoticeText,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item(key = "special_notice_hero") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(palette.accentSurfaceStrong, NoticeCard)
                            )
                        )
                        .border(1.dp, palette.accentBorder, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                        .testTag("special_notice_hero"),
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
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = palette.accentBright,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "বিশেষ ঘোষণা",
                            fontFamily = FontFamily.Default,
                            fontSize = 10.sp,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.9.sp,
                            color = palette.accentBright
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "বড়মা পুজো ২০২৬",
                            fontFamily = FontFamily.Default,
                            fontSize = 20.sp,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = NoticeText,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = BormaPujoNoticeData.subtitle,
                            fontFamily = FontFamily.Default,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = NoticeMuted
                        )
                    }
                }
            }

            item(key = "special_notice_body") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(NoticeInset)
                        .border(1.dp, NoticeHairline, RoundedCornerShape(20.dp))
                        .padding(vertical = 4.dp)
                        .testTag("special_notice_card")
                ) {
                    Text(
                        text = "পুজো ও সময়সূচি",
                        fontFamily = FontFamily.Default,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.9.sp,
                        color = NoticeFaint,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp)
                    )

                    BormaPujoNoticeData.notices.forEachIndexed { index, notice ->
                        NoticeRow(
                            notice = notice,
                            number = index + 1,
                            isPrimary = index == 0,
                            showDivider = index != BormaPujoNoticeData.notices.lastIndex
                        )
                    }
                }
            }

            item(key = "special_notice_note") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(NoticeCard)
                        .border(1.dp, NoticeHairline, RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = BormaPujoNoticeData.closingNote,
                        fontFamily = FontFamily.Default,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        color = NoticeMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun NoticeRow(
    notice: PujoNoticeItem,
    number: Int,
    isPrimary: Boolean,
    showDivider: Boolean
) {
    val palette = LocalAppPalette.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .testTag("special_notice_row_${notice.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (isPrimary) palette.accentSoft else Color.Transparent)
                .padding(horizontal = 6.dp, vertical = 11.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (isPrimary) palette.accentDeep else Color(0xFF1E1E28)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPrimary) Color.White else NoticeMuted
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notice.label,
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isPrimary) palette.accentBright else NoticeBody
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = notice.detail,
                    fontFamily = FontFamily.Default,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.Medium,
                    color = NoticeText
                )
            }
        }

        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(NoticeHairline)
            )
        }
    }
}
