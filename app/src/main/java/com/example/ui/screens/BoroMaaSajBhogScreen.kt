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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans

/**
 * Boro Maa's adornment (সাজ) and bhog (ভোগ) details for the Naihati Kali Puja.
 * Reached from the sidebar entry under the dedicated "বড়মার সাজ ও ভোগ" slot.
 * Visual treatment follows the Finesta KWGT Reev Dark mockup: misty bottom gradient,
 * monochromatic adornment badges, and a refined bhog capsule grid.
 */
@Composable
fun BoroMaaSajBhogScreen(
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
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.00f to Color(0xFF050505),
                        0.55f to Color(0xFF0B0C0E),
                        0.72f to Color(0xFF27292D),
                        0.86f to Color(0xFF6E737B),
                        0.95f to Color(0xFFD1D5DB),
                        1.00f to Color(0xFFE2E5E9)
                    )
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 448.dp)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .testTag("boro_maa_saj_bhog_screen"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item(key = "boro_maa_saj_bhog_header") {
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
                                .testTag("boro_maa_saj_bhog_back_button")
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

            // Hairline divider
            item(key = "boro_maa_saj_bhog_divider") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x14FFFFFF),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            // Adornments section
            item(key = "boro_maa_adornments") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(6.dp)
                                .height(14.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFFE5E7EB))
                        )
                        Text(
                            text = "মায়ের অলঙ্কার ও রাজকীয় সাজ",
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE5E7EB),
                            letterSpacing = 0.5.sp
                        )
                    }

                    // Summary weight banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xCC121215))
                            .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x1AFFFFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "★",
                                color = Color(0xFFE5E7EB),
                                fontSize = 14.sp
                            )
                        }
                        Column {
                            Text(
                                text = "সার্বিক স্বর্ণ ও রৌপ্য পরিমাপ",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.sp,
                                letterSpacing = 1.sp,
                                color = Color(0xFF9CA3AF)
                            )
                            Text(
                                text = "1 কুইন্টাল 10 কেজি (110 কেজি স্বর্ণালঙ্কার)",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    // Adornment badges grid
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        val adornments = listOf(
                            "01" to "সোনার মুকুট",
                            "02" to "হীরের টিপ",
                            "03" to "সোনার জিভ ও চোখ",
                            "04" to "সোনার নেকলেস",
                            "05" to "হাতের বালা ও চুড়ি",
                            "06" to "পায়ের মল ও চাঁদমালা"
                        )
                        adornments.chunked(2).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { (num, name) ->
                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x99121215))
                                            .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(10.dp))
                                            .padding(horizontal = 10.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = num,
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 10.sp,
                                            color = Color(0xFF6B7280),
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = name,
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFFE5E7EB)
                                        )
                                    }
                                }
                                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // Special initiative card
            item(key = "boro_maa_initiative") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x80121215))
                        .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE5E7EB))
                        )
                        Text(
                            text = "পুজো কমিটির বিশেষ পরিকল্পনা",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp,
                            color = Color(0xFFD1D5DB)
                        )
                    }
                    Text(
                        text = "\"হেলিকপ্টার থেকে বড়মার উদ্দেশ্যে পুষ্পবৃষ্টির পরিকল্পনা রয়েছে\", আবহাওয়া ও ছাড়পত্রের ওপর ভিত্তি করে যা বাস্তবায়িত হবে।",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.sp,
                        lineHeight = 17.sp,
                        color = Color(0xFFD1D5DB)
                    )
                }
            }

            // Bhog section
            item(key = "boro_maa_bhog") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(6.dp)
                                .height(14.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFFD1D5DB))
                        )
                        Text(
                            text = "বড়মার ভোগের তালিকা",
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE5E7EB),
                            letterSpacing = 0.5.sp
                        )
                    }

                    // Main dishes grid (3 columns)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        val dishes = listOf(
                            "খিচুড়ি" to "Khichuri",
                            "পোলাও" to "Pulao",
                            "পাঁচমিশালি" to "Mix Curry",
                            "আলুর দম" to "Aloor Dum",
                            "সাদা ভাত" to "Govindabhog",
                            "লুচি পদ" to "Luchi & Sides"
                        )
                        dishes.chunked(3).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                row.forEach { (bn, en) ->
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x99121215))
                                            .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(10.dp))
                                            .padding(vertical = 10.dp, horizontal = 4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Text(
                                            text = bn,
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFFE5E7EB),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = en,
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 8.5.sp,
                                            color = Color(0xFF6B7280),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                                repeat(3 - row.size) { Spacer(modifier = Modifier.weight(1f)) }
                            }
                        }
                    }

                    // Sweets sub-row
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x80121215))
                            .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(Triple("01", "রসগোল্লা", "Rosogolla"),
                                    Triple("02", "সরপুরিয়া", "Sarpuria"),
                                    Triple("03", "নাড়ু", "Naru")).forEach { (num, bn, en) ->
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xCC121215))
                                        .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(10.dp))
                                        .padding(vertical = 10.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = num,
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 9.sp,
                                        color = Color(0xFF6B7280)
                                    )
                                    Text(
                                        text = bn,
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFE5E7EB),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = en,
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 8.5.sp,
                                        color = Color(0xFF6B7280),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item(key = "boro_maa_saj_bhog_footer") {
                SectionCreditFooter(topPadding = 8.dp, bottomPadding = 4.dp)
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}
