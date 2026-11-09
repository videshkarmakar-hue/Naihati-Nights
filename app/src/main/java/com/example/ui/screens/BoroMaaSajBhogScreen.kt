package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.ObsidianNoir
import com.example.ui.theme.PlusJakartaSans

/**
 * Boro Maa's adornment (সাজ) and bhog (ভোগ) details for the Naihati Kali Puja.
 * Reached from the sidebar entry under the dedicated "বড়মার সাজ ও ভোগ" slot.
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

            item(key = "boro_maa_saj_bhog_title") {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "বড়মার সাজ ও ভোগ",
                        fontFamily = PlusJakartaSans,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF4F4F5)
                    )
                    Text(
                        text = "বড়মার সাজ ও ভোগের তালিকা",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }

            item(key = "boro_maa_saj_bhog_content") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "• বড়মার সাজে হীরের টিপ থেকে ১১০ কেজি সোনা-রুপোর গয়না।",
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFFF4F4F5)
                    )
                    Text(
                        text = "• এ বছর মায়ের সাজে রয়েছে :\nসোনার মুকুট,\nনেকলেস,\nজিভ,\nচোখ,\nহাতের বালা,\nচুড়ি,\nপায়ের মল,\nসোনা-রুপোর চাঁদমালা,\nমায়ের কপালে হীরের টিপ,\n(সোনা ও রুপো মিলিয়ে এ বছর মায়ের অলংকারের মোট ওজন প্রায় ১ কুইন্টাল ১০ কেজি)",
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFFF4F4F5)
                    )
                    Text(
                        text = "• পুজো কমিটির তরফে জানানো হয়েছে \"হেলিকপ্টার থেকে বড়মার উদ্দেশ্যে পুষ্পবৃষ্টির পরিকল্পনা রয়েছে\",\nশেষ পর্যন্ত এই আয়োজন করা সম্ভব হবে কি না, তা নির্ভর করছে প্রযুক্তিগত ও আবহাওয়াগত পরিস্থিতির উপর।",
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFFF4F4F5)
                    )
                    Text(
                        text = "• বড়মা এর ভোগের তালিকা:\nখিচুড়ি,\nপাঁচমিশালি তরকারি,\nপোলাও,\nআলুর দম,\nসাদা ভাত,\nলুচি-সহ নানা পদ,\nরসগোল্লা,\nসরপুরিয়া,\nনাড়ু",
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFFF4F4F5)
                    )
                }
            }

            item(key = "boro_maa_saj_bhog_footer") {
                SectionCreditFooter(topPadding = 8.dp, bottomPadding = 4.dp)
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}
