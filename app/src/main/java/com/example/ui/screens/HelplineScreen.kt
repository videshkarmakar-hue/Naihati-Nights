package com.example.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppContent
import com.example.model.HelplineItem
import com.example.model.PoliceBooth
import com.example.ui.components.AppBrandHeader
import com.example.ui.theme.PlusJakartaSans

private val PageBlack = Color(0xFF0B0C0E)
private val CardBg = Color(0xFF12141B)
private val CardBorder = Color(0xFF22242F)
private val InnerBg = Color(0xFF171922)
private val InnerBorder = Color(0xFF232634)
private val TagBg = Color(0xFF1B1E27)
private val TagBorder = Color(0xFF2C303F)
private val SearchBg = Color(0xFF12141A)
private val SearchBorder = Color(0xFF242733)
private val FocusBorder = Color(0xFFA1A1AA)
private val BoothBg = Color(0xFF12141A)
private val BoothBorder = Color(0xFF22242E)
private val BadgeBg = Color(0xFF1A1C24)
private val BadgeBorder = Color(0xFF2B2E3C)
private val ButtonBg = Color(0xFF1D202B)
private val ButtonBorder = Color(0xFF2E3243)
private val NavyButtonBg = Color(0xFF222532)
private val NavyButtonBorder = Color(0xFF2C3040)
private val SoftButtonBg = Color(0xFF1E212C)
private val SoftButtonBorder = Color(0xFF2B2E3E)
private val SosButtonBg = Color(0xFFE4E4E7)
private val DividerHairline = Color(0xFF1A1C24)
private val AdvisoryHairline = Color(0xFF1C1E28)
private val White = Color(0xFFFFFFFF)
private val InkBlack = Color(0xFF0B0C0E)
private val Zinc100 = Color(0xFFF4F4F5)
private val Zinc200 = Color(0xFFE4E4E7)
private val Zinc300 = Color(0xFFD4D4D8)
private val Zinc400 = Color(0xFFA1A1AA)
private val Zinc500 = Color(0xFF71717A)

private val CardShape16 = RoundedCornerShape(16.dp)
private val CardShape12 = RoundedCornerShape(12.dp)
private val CardShape8 = RoundedCornerShape(8.dp)
private val TagShape = RoundedCornerShape(6.dp)

@Composable
fun HelplineScreen(
    content: AppContent,
    onBack: () -> Unit,
    onOpenSidebar: () -> Unit = {},
    onOpenDeveloperCredits: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    val query = searchQuery.trim()

    val byId = remember(content.helplines) { content.helplines.associateBy { it.id } }
    val police = byId["help_police"]
    val boromaa = byId["help_boromaa"]
    val hospital = byId["help_hospital"]
    val ambulance = byId["help_ambulance"]
    val fire = byId["help_fire"]
    val municipality = byId["help_municipality"]
    val women = byId["help_women"]
    val child = byId["help_child"]
    val traffic = byId["help_traffic"]

    val standardIds = remember {
        setOf(
            "help_police", "help_boromaa", "help_hospital", "help_ambulance",
            "help_fire", "help_municipality", "help_women", "help_child", "help_traffic"
        )
    }
    val extraHelplines = remember(content.helplines, query) {
        content.helplines
            .filterNot { it.id in standardIds }
            .filter { it.matchesQuery(query) }
    }

    val visiblePolice = police?.takeIf { it.matchesQuery(query) }
    val visibleBoromaa = boromaa?.takeIf { it.matchesQuery(query) }
    val showMedical = hospital?.matchesQuery(query) == true ||
        ambulance?.matchesQuery(query) == true
    val visibleHospital = if (showMedical) hospital else null
    val visibleAmbulance = if (showMedical) ambulance else null
    val visibleFire = fire?.takeIf { it.matchesQuery(query) }
    val visibleMunicipality = municipality?.takeIf { it.matchesQuery(query) }
    val showWomenChild = women?.matchesQuery(query) == true ||
        child?.matchesQuery(query) == true
    val visibleWomen = if (showWomenChild) women else null
    val visibleChild = if (showWomenChild) child else null
    val visibleTraffic = traffic?.takeIf { it.matchesQuery(query) }

    val hasVisibleContacts = listOf(
        visiblePolice, visibleBoromaa, visibleHospital, visibleAmbulance,
        visibleFire, visibleMunicipality, visibleWomen, visibleChild, visibleTraffic
    ).any { it != null } || extraHelplines.isNotEmpty()

    fun dial(number: String) {
        val cleanNumber = number.replace(Regex("[^0-9+]"), "")
        if (cleanNumber.isBlank()) return
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanNumber"))
        try {
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "No dialer found for $cleanNumber", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PageBlack),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .statusBarsPadding()
        ) {
            HelplineTopBar(
                onBack = onBack,
                onOpenSidebar = onOpenSidebar,
                onOpenDeveloperCredits = onOpenDeveloperCredits
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("helpline_list"),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 104.dp
                )
            ) {
                item(key = "helpline_search") {
                    HelplineSearchBar(
                        value = searchQuery,
                        onValueChange = { searchQuery = it }
                    )
                }

                item(key = "helpline_contacts_gap") {
                    Spacer(modifier = Modifier.height(14.dp))
                }

                if (hasVisibleContacts) {
                    item(key = "helpline_contacts") {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            if (visiblePolice != null) {
                                StandardHelplineCard(
                                    item = visiblePolice,
                                    tagLabel = "THANA",
                                    solidAction = true,
                                    onCall = { dial(visiblePolice.dialNumber) }
                                )
                            }

                            if (visibleBoromaa != null) {
                                StandardHelplineCard(
                                    item = visibleBoromaa,
                                    tagLabel = "TRUST",
                                    solidAction = false,
                                    onCall = { dial(visibleBoromaa.dialNumber) }
                                )
                            }

                            if (visibleHospital != null || visibleAmbulance != null) {
                                MedicalHelplineCard(
                                    hospital = visibleHospital,
                                    ambulance = visibleAmbulance,
                                    onCall = { dial(it.dialNumber) }
                                )
                            }

                            if (visibleFire != null) {
                                StandardHelplineCard(
                                    item = visibleFire,
                                    tagLabel = "FIRE",
                                    solidAction = false,
                                    onCall = { dial(visibleFire.dialNumber) }
                                )
                            }

                            if (visibleMunicipality != null) {
                                StandardHelplineCard(
                                    item = visibleMunicipality,
                                    tagLabel = "CIVIC",
                                    solidAction = false,
                                    onCall = { dial(visibleMunicipality.dialNumber) }
                                )
                            }

                            if (visibleWomen != null || visibleChild != null) {
                                WomenChildHelplineCard(
                                    women = visibleWomen,
                                    child = visibleChild,
                                    onCall = { dial(it.dialNumber) }
                                )
                            }

                            if (visibleTraffic != null) {
                                TrafficHelplineCard(
                                    item = visibleTraffic,
                                    onCall = { dial(visibleTraffic.dialNumber) }
                                )
                            }

                            extraHelplines.forEach { extra ->
                                StandardHelplineCard(
                                    item = extra,
                                    tagLabel = "SERVICE",
                                    solidAction = false,
                                    onCall = { dial(extra.dialNumber) }
                                )
                            }
                        }
                    }
                } else if (query.isNotBlank()) {
                    item(key = "helpline_no_results") {
                        NoHelplineResults(
                            query = query,
                            onClear = { searchQuery = "" }
                        )
                    }
                }

                item(key = "helpline_booths_gap") {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item(key = "helpline_booths_heading") {
                    PoliceBoothsHeading()
                }

                item(key = "helpline_booths_heading_gap") {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item(key = "helpline_booths") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        content.policeBooths.forEach { booth ->
                            PoliceBoothRow(
                                booth = booth,
                                onClick = { openBoothInMaps(context, booth) }
                            )
                        }
                    }
                }

                item(key = "helpline_guidelines_gap") {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item(key = "helpline_guidelines") {
                    SafetyGuidelinesCard(guidelines = content.safetyGuidelines)
                }

                item(key = "helpline_footer_gap") {
                    Spacer(modifier = Modifier.height(32.dp))
                }

                item(key = "helpline_footer") {
                    HelplineFooter()
                }
            }
        }
    }
}

private fun HelplineItem.matchesQuery(query: String): Boolean {
    if (query.isBlank()) return true
    val searchable = listOf(title, subtitleLabel, numbers, actionLabel)
        .joinToString(separator = " ")
    return searchable.contains(query, ignoreCase = true)
}

@Composable
private fun HelplineTopBar(
    onBack: () -> Unit,
    onOpenSidebar: () -> Unit,
    onOpenDeveloperCredits: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().background(PageBlack)) {
        AppBrandHeader(
            modifier = Modifier.padding(horizontal = 16.dp),
            onOpenSidebar = onOpenSidebar,
            onOpenDeveloperCredits = onOpenDeveloperCredits,
            navigation = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CardShape12)
                        .background(CardBg)
                        .border(1.dp, CardBorder, CardShape12)
                        .clickable(role = Role.Button, onClick = onBack)
                        .testTag("helpline_back_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_help_arrow_left),
                        contentDescription = "Go back",
                        tint = Zinc300,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            HelpText(
                text = "EMERGENCY HELPLINE",
                size = 12.sp,
                weight = FontWeight.SemiBold,
                color = Zinc200,
                letterSpacing = 0.6.sp,
                lineHeight = 16.sp,
                maxLines = 1,
                modifier = Modifier.testTag("helpline_section_title")
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BoothBorder)
        )
    }
}

@Composable
private fun HelplineSearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape12)
            .background(SearchBg)
            .border(
                width = 1.dp,
                color = if (focused) FocusBorder else SearchBorder,
                shape = CardShape12
            )
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("helpline_search_field"),
            textStyle = TextStyle(
                fontFamily = PlusJakartaSans,
                color = Zinc200,
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            cursorBrush = SolidColor(Zinc400),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { focusManager.clearFocus() }
            ),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_help_search),
                        contentDescription = null,
                        tint = Zinc400,
                        modifier = Modifier.size(16.dp)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            HelpText(
                                text = "Search helpline, thana, medical booth...",
                                size = 12.sp,
                                weight = FontWeight.Normal,
                                color = Zinc500,
                                lineHeight = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
private fun CategoryTag(label: String) {
    Box(
        modifier = Modifier
            .clip(TagShape)
            .background(TagBg)
            .border(1.dp, TagBorder, TagShape)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        HelpText(
            text = label,
            size = 10.sp,
            weight = FontWeight.SemiBold,
            color = Zinc300,
            letterSpacing = 0.5.sp,
            lineHeight = 13.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun StandardHelplineCard(
    item: HelplineItem,
    tagLabel: String,
    solidAction: Boolean,
    onCall: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape16)
            .background(CardBg)
            .border(1.dp, CardBorder, CardShape16)
            .padding(16.dp)
            .testTag("helpline_card_${item.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            HelpText(
                text = item.title,
                size = 14.sp,
                weight = FontWeight.SemiBold,
                color = White,
                letterSpacing = (-0.35).sp,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            CategoryTag(label = tagLabel)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DividerHairline)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            MonoNumber(
                text = item.numbers,
                size = 14.sp,
                weight = FontWeight.Medium,
                color = Zinc100,
                letterSpacing = (-0.3).sp,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            CallButton(
                label = item.actionLabel,
                solid = solidAction,
                onClick = onCall,
                testTag = "helpline_call_${item.id}"
            )
        }
    }
}

@Composable
private fun CallButton(
    label: String,
    solid: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(CardShape12)
            .background(if (solid) White else ButtonBg)
            .border(1.dp, if (solid) White else ButtonBorder, CardShape12)
            .clickable(role = Role.Button, onClick = onClick)
            .testTag(testTag)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        HelpText(
            text = label,
            size = 12.sp,
            weight = if (solid) FontWeight.SemiBold else FontWeight.Medium,
            color = if (solid) InkBlack else Zinc200,
            lineHeight = 16.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun MedicalHelplineCard(
    hospital: HelplineItem?,
    ambulance: HelplineItem?,
    onCall: (HelplineItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape16)
            .background(CardBg)
            .border(1.dp, CardBorder, CardShape16)
            .padding(16.dp)
            .testTag("helpline_card_medical")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            HelpText(
                text = hospital?.title ?: "Naihati State General Hospital",
                size = 14.sp,
                weight = FontWeight.SemiBold,
                color = White,
                letterSpacing = (-0.35).sp,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            CategoryTag(label = "HOSPITAL")
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DividerHairline)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (hospital != null) {
                CompactContactCard(
                    item = hospital,
                    subtitle = "General Desk",
                    sosStyle = false,
                    onClick = { onCall(hospital) },
                    modifier = Modifier.weight(1f)
                )
            }
            if (ambulance != null) {
                CompactContactCard(
                    item = ambulance,
                    subtitle = "Ambulance SOS",
                    sosStyle = true,
                    onClick = { onCall(ambulance) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CompactContactCard(
    item: HelplineItem,
    subtitle: String,
    sosStyle: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(CardShape12)
            .background(InnerBg)
            .border(1.dp, InnerBorder, CardShape12)
            .padding(10.dp)
    ) {
        HelpText(
            text = subtitle,
            size = 9.sp,
            weight = FontWeight.SemiBold,
            color = Zinc500,
            letterSpacing = 0.5.sp,
            lineHeight = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        HelpText(
            text = item.numbers,
            size = 11.sp,
            weight = FontWeight.Medium,
            color = Zinc200,
            family = FontFamily.Monospace,
            lineHeight = 14.sp,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CardShape8)
                .background(if (sosStyle) SosButtonBg else NavyButtonBg)
                .border(
                    1.dp,
                    if (sosStyle) SosButtonBg else NavyButtonBorder,
                    CardShape8
                )
                .clickable(role = Role.Button, onClick = onClick)
                .testTag("helpline_call_${item.id}")
                .padding(vertical = 7.dp),
            contentAlignment = Alignment.Center
        ) {
            HelpText(
                text = item.actionLabel,
                size = 11.sp,
                weight = if (sosStyle) FontWeight.SemiBold else FontWeight.Medium,
                color = if (sosStyle) InkBlack else Zinc200,
                lineHeight = 14.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun WomenChildHelplineCard(
    women: HelplineItem?,
    child: HelplineItem?,
    onCall: (HelplineItem) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        if (women != null) {
            CompactGuardianCard(
                item = women,
                iconRes = R.drawable.ic_help_woman,
                onClick = { onCall(women) },
                modifier = Modifier.weight(1f)
            )
        }
        if (child != null) {
            CompactGuardianCard(
                item = child,
                iconRes = R.drawable.ic_help_child,
                onClick = { onCall(child) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CompactGuardianCard(
    item: HelplineItem,
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(CardShape16)
            .background(CardBg)
            .border(1.dp, CardBorder, CardShape16)
            .padding(14.dp)
            .testTag("helpline_card_${item.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Zinc400,
                modifier = Modifier.size(16.dp)
            )
            HelpText(
                text = "24/7",
                size = 9.sp,
                weight = FontWeight.SemiBold,
                color = Zinc500,
                letterSpacing = 0.5.sp,
                lineHeight = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        HelpText(
            text = item.title,
            size = 12.sp,
            weight = FontWeight.SemiBold,
            color = White,
            letterSpacing = (-0.3).sp,
            lineHeight = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        HelpText(
            text = item.numbers,
            size = 16.sp,
            weight = FontWeight.SemiBold,
            color = Zinc100,
            family = FontFamily.Monospace,
            letterSpacing = (-0.3).sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CardShape12)
                .background(SoftButtonBg)
                .border(1.dp, SoftButtonBorder, CardShape12)
                .clickable(role = Role.Button, onClick = onClick)
                .testTag("helpline_call_${item.id}")
                .padding(vertical = 7.dp),
            contentAlignment = Alignment.Center
        ) {
            HelpText(
                text = item.actionLabel,
                size = 11.sp,
                weight = FontWeight.Medium,
                color = Zinc200,
                lineHeight = 14.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TrafficHelplineCard(
    item: HelplineItem,
    onCall: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape16)
            .background(CardBg)
            .border(1.dp, CardBorder, CardShape16)
            .padding(14.dp)
            .testTag("helpline_card_${item.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            HelpText(
                text = item.title,
                size = 12.sp,
                weight = FontWeight.SemiBold,
                color = Zinc100,
                letterSpacing = (-0.3).sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            MonoNumber(
                text = item.numbers,
                size = 12.sp,
                weight = FontWeight.Normal,
                color = Zinc300,
                lineHeight = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CardShape12)
                .background(TagBg)
                .border(1.dp, BadgeBorder, CardShape12)
                .clickable(role = Role.Button, onClick = onCall)
                .testTag("helpline_call_${item.id}"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_help_phone),
                contentDescription = "Call ${item.title}",
                tint = Zinc300,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun PoliceBoothsHeading() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(Zinc400)
            )
            HelpText(
                text = "ASSISTANCE & POLICE BOOTHS",
                size = 11.sp,
                weight = FontWeight.Medium,
                color = Zinc400,
                letterSpacing = 0.6.sp,
                lineHeight = 15.sp,
                maxLines = 1
            )
        }
        HelpText(
            text = "FIELD UNITS",
            size = 10.sp,
            weight = FontWeight.Normal,
            color = Zinc500,
            letterSpacing = 0.4.sp,
            lineHeight = 14.sp
        )
    }
}

@Composable
private fun PoliceBoothRow(
    booth: PoliceBooth,
    onClick: () -> Unit
) {
    val boothSubtitle = when (booth.number) {
        1 -> "24h Medical + Missing Person Kiosk"
        2 -> "River Police & First Aid Responder"
        else -> "Crowd Diversion & Rapid Response"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape12)
            .background(BoothBg)
            .border(1.dp, BoothBorder, CardShape12)
            .clickable(
                role = Role.Button,
                onClickLabel = "Open ${booth.name} in Maps",
                onClick = onClick
            )
            .testTag("helpline_booth_${booth.number}")
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CardShape8)
                .background(BadgeBg)
                .border(1.dp, BadgeBorder, CardShape8),
            contentAlignment = Alignment.Center
        ) {
            HelpText(
                text = booth.number.toString().padStart(2, '0'),
                size = 10.sp,
                weight = FontWeight.Medium,
                color = Zinc300,
                family = FontFamily.Monospace,
                lineHeight = 13.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            HelpText(
                text = booth.name,
                size = 12.sp,
                weight = FontWeight.SemiBold,
                color = Zinc200,
                letterSpacing = (-0.3).sp,
                lineHeight = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(1.dp))
            HelpText(
                text = boothSubtitle,
                size = 10.sp,
                weight = FontWeight.Normal,
                color = Zinc400,
                lineHeight = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(R.drawable.ic_help_chevron_right),
            contentDescription = null,
            tint = Zinc500,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun SafetyGuidelinesCard(guidelines: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape16)
            .background(BoothBg)
            .border(1.dp, BoothBorder, CardShape16)
            .padding(16.dp)
            .testTag("helpline_guidelines")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_help_shield_alert),
                contentDescription = null,
                tint = Zinc400,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            HelpText(
                text = "SAFETY PROTOCOL & INSTRUCTIONS",
                size = 10.sp,
                weight = FontWeight.SemiBold,
                color = Zinc300,
                letterSpacing = 0.4.sp,
                lineHeight = 14.sp,
                modifier = Modifier.weight(1f)
            )
            HelpText(
                text = "PUBLIC ADVISORY",
                size = 9.sp,
                weight = FontWeight.Medium,
                color = Zinc500,
                letterSpacing = 0.4.sp,
                lineHeight = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(AdvisoryHairline)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            guidelines.forEach { guideline ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    HelpText(
                        text = "\u2022",
                        size = 10.sp,
                        weight = FontWeight.Normal,
                        color = Zinc400,
                        lineHeight = 14.sp,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    HelpText(
                        text = guideline,
                        size = 11.sp,
                        weight = FontWeight.Normal,
                        color = Zinc300,
                        lineHeight = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun NoHelplineResults(
    query: String,
    onClear: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape16)
            .background(BoothBg)
            .border(1.dp, BoothBorder, CardShape16)
            .padding(20.dp)
            .testTag("helpline_no_results"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_help_search),
            contentDescription = null,
            tint = Zinc400,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        HelpText(
            text = "No emergency contact found",
            size = 13.sp,
            weight = FontWeight.SemiBold,
            color = White,
            lineHeight = 17.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        HelpText(
            text = "Nothing matches \u201C$query\u201D. Try Police, Fire, Hospital or another keyword.",
            size = 11.sp,
            weight = FontWeight.Normal,
            color = Zinc400,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .clip(CardShape12)
                .background(BadgeBg)
                .border(1.dp, BadgeBorder, CardShape12)
                .clickable(role = Role.Button, onClick = onClear)
                .testTag("helpline_clear_search")
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            HelpText(
                text = "Clear search",
                size = 12.sp,
                weight = FontWeight.Medium,
                color = Zinc200,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun HelplineFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HelpText(
            text = "\u00A9 Crafted by VIDESH KARMAKAR",
            size = 10.sp,
            weight = FontWeight.Normal,
            color = Zinc500,
            lineHeight = 14.sp
        )
    }
}

@Composable
private fun MonoNumber(
    text: String,
    size: TextUnit,
    weight: FontWeight,
    color: Color,
    modifier: Modifier = Modifier,
    letterSpacing: TextUnit = 0.sp,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = buildAnnotatedString {
            text.split("/").forEachIndexed { index, part ->
                if (index > 0) {
                    withStyle(SpanStyle(color = Zinc500)) { append("/") }
                }
                append(part)
            }
        },
        modifier = modifier,
        color = color,
        fontSize = size,
        fontWeight = weight,
        fontFamily = FontFamily.Monospace,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
private fun HelpText(
    text: String,
    size: TextUnit,
    weight: FontWeight,
    color: Color,
    modifier: Modifier = Modifier,
    family: FontFamily = PlusJakartaSans,
    letterSpacing: TextUnit = 0.sp,
    lineHeight: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = size,
        fontWeight = weight,
        fontFamily = family,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

private fun openBoothInMaps(context: Context, booth: PoliceBooth) {
    val query = "${booth.name}, Naihati, West Bengal, India"
    val encoded = Uri.encode(query)
    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$encoded"))
    try {
        context.startActivity(mapIntent)
    } catch (_: ActivityNotFoundException) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=$encoded")
                )
            )
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "No maps app available", Toast.LENGTH_SHORT).show()
        }
    }
}
