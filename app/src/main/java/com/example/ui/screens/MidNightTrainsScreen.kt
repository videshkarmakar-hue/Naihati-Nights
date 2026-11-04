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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Train
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.EmergencyRose
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans

private val TrainCard = Color(0xFF101018)
private val TrainInset = Color(0xFF15151D)
private val TrainText = Color(0xFFF4F4F5)
private val TrainBody = Color(0xFFD4D4D8)
private val TrainMuted = Color(0xFFA1A1AA)
private val TrainFaint = Color(0xFF71717A)
private val TrainHairline = Color(0x14FFFFFF)

/** One midnight EMU special: train number plus the exact origin and destination timing. */
private data class MidNightEmuSpecial(
    val number: String,
    val origin: String,
    val originTime: String,
    val destination: String,
    val destinationTime: String
)

private val UpEmuSpecials = listOf(
    MidNightEmuSpecial("03161", "Sealdah", "23:00", "Dankuni", "23:42"),
    MidNightEmuSpecial("03163", "Sealdah", "00:05", "Barasat", "01:35"),
    MidNightEmuSpecial("03165", "Barasat", "01:50", "Bongaon", "03:00"),
    MidNightEmuSpecial("03167", "Barasat", "01:10", "Hasnabad", "02:36"),
    MidNightEmuSpecial("03169", "Sealdah", "00:40", "Ranaghat", "02:30"),
    MidNightEmuSpecial("03171", "Barrackpore", "01:25", "Naihati", "02:10")
)

private val DownEmuSpecials = listOf(
    MidNightEmuSpecial("03162", "Dankuni", "23:55", "Sealdah", "00:37"),
    MidNightEmuSpecial("03164", "Barasat", "01:10", "Sealdah", "01:55"),
    MidNightEmuSpecial("03166", "Bongaon", "23:00", "Barasat", "00:10"),
    MidNightEmuSpecial("03168", "Hasnabad", "23:30", "Barasat", "00:56"),
    MidNightEmuSpecial("03170", "Ranaghat", "23:45", "Sealdah", "01:40"),
    MidNightEmuSpecial("03172", "Sealdah", "00:30", "Barrackpore", "01:15")
)

/**
 * Mid-Night Trains: the extra EMU specials that run after dark on the Naihati parikrama route,
 * split into the Up and Down groups exactly as the committee publishes them.
 *
 * Reached from the bottom dock slot and from the sidebar entry under Theme Pandel.
 */
@Composable
fun MidNightTrainsScreen(
    onBack: () -> Unit,
    onOpenSidebar: () -> Unit = { },
    onOpenDeveloperCredits: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    val palette = LocalAppPalette.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colorStops = arrayOf(
                        0.00f to Color(0xFF0B0C0E),
                        0.45f to Color(0xFF101216),
                        0.80f to Color(0xFF15181E),
                        1.00f to Color(0xFF0D0E11)
                    )
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 448.dp)
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .testTag("mid_night_trains_screen"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
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
                                .border(1.dp, TrainHairline, RoundedCornerShape(12.dp))
                                .testTag("mid_night_trains_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TrainText,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        palette.accentSurfaceStrong
                    )
                    .border(1.dp, palette.accentBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
                    .testTag("mid_night_trains_hero"),
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
                        imageVector = Icons.Filled.Train,
                        contentDescription = null,
                        tint = palette.accentBright,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "EXTRA TRAIN TIMETABLE",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.9.sp,
                        color = palette.accentBright
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Mid-Night Trains",
                        fontFamily = PlusJakartaSans,
                        fontSize = 20.sp,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TrainText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Extra Train Timetable in Mid-Night: six Up and six Down EMU specials for the night hours.",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        color = TrainMuted
                    )
                }
            }

            EmuSpecialGroup(
                header = "Up EMU Special (Extra For Night)",
                trains = UpEmuSpecials,
                testTag = "mid_night_trains_group_up"
            )

            EmuSpecialGroup(
                header = "Down EMU Special (Extra For Night)",
                trains = DownEmuSpecials,
                testTag = "mid_night_trains_group_down"
            )

            SectionCreditFooter(topPadding = 4.dp, bottomPadding = 4.dp)

            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}

@Composable
private fun EmuSpecialGroup(
    header: String,
    trains: List<MidNightEmuSpecial>,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(TrainInset)
            .border(1.dp, TrainHairline, RoundedCornerShape(16.dp))
            .padding(vertical = 4.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Accent bullet stands in for the published red marker.
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(EmergencyRose)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = header,
                fontFamily = PlusJakartaSans,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.9.sp,
                color = TrainFaint
            )
        }

        trains.forEachIndexed { index, train ->
            EmuSpecialRow(
                train = train,
                position = index + 1,
                isPrimary = index == 0,
                showDivider = index != trains.lastIndex
            )
        }
    }
}

@Composable
private fun EmuSpecialRow(
    train: MidNightEmuSpecial,
    position: Int,
    isPrimary: Boolean,
    showDivider: Boolean
) {
    val palette = LocalAppPalette.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .testTag("mid_night_train_${train.number}")
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
                    text = position.toString(),
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPrimary) Color.White else TrainMuted
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = buildAnnotatedString {
                    append("Train No. ")
                    withStyle(
                        SpanStyle(
                            color = if (isPrimary) palette.accentBright else TrainText,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(train.number)
                    }
                    append(": ${train.origin} – ${train.originTime} to ${train.destination} – ${train.destinationTime} Hrs.")
                },
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = TrainBody,
                modifier = Modifier.weight(1f)
            )
        }

        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
                    .height(1.dp)
                    .background(TrainHairline)
            )
        }
    }
}
