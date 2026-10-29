package com.example.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
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
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans

private val PageBlack = Color(0xFF050505)
private val SurfaceLow = Color(0xFF0E0E14)
private val SurfaceCard = Color(0xFF13131B)
private val SurfaceHigh = Color(0xFF181824)
private val RegalViolet: Color @Composable get() = LocalAppPalette.current.accentDeep
private val Rose400 = Color(0xFFFB7185)
private val Rose500 = Color(0xFFF43F5E)
private val Rose600 = Color(0xFFE11D48)
private val Amber400 = Color(0xFFFBBF24)
private val Amber500 = Color(0xFFF59E0B)
private val Violet300: Color @Composable get() = LocalAppPalette.current.accentBright
private val Violet400: Color @Composable get() = LocalAppPalette.current.accentBright
private val Pink300 = Color(0xFFF9A8D4)
private val Pink400 = Color(0xFFF472B6)
private val Emerald400 = Color(0xFF34D399)
private val Orange400 = Color(0xFFFB923C)
private val Blue400 = Color(0xFF60A5FA)
private val Yellow400 = Color(0xFFFACC15)
private val Zinc300 = Color(0xFFD4D4D8)
private val Zinc400 = Color(0xFFA1A1AA)
private val Zinc500 = Color(0xFF71717A)
private val BorderSubtle = Color(0x14FFFFFF)
private val BorderFaint = Color(0x0FFFFFFF)
private val BorderRose = Color(0x4DF43F5E)
private val BorderRoseButton = Color(0x66FB7185)
private val BorderAmber = Color(0x4DF59E0B)
private val BorderViolet: Color @Composable get() = LocalAppPalette.current.accentBorder
private val BorderVioletButton: Color @Composable get() = LocalAppPalette.current.accentBorder
private val BorderVioletSoft: Color @Composable get() = LocalAppPalette.current.accentBorder
private val BorderPink = Color(0x4DEC4899)
private val BorderAmbulance = Color(0x4DFB7185)

private val CardShape16 = RoundedCornerShape(16.dp)
private val CardShape12 = RoundedCornerShape(12.dp)
private val CardShape8 = RoundedCornerShape(8.dp)

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
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (hasVisibleContacts) {
                    item(key = "helpline_contacts") {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            if (visiblePolice != null) {
                                StandardHelplineCard(
                                    item = visiblePolice,
                                    borderColor = BorderRose,
                                    buttonBackground = Rose600,
                                    buttonBorderColor = BorderRoseButton,
                                    buttonIcon = R.drawable.ic_help_phone,
                                    buttonIconTint = Color.White,
                                    numberLetterSpacing = 0.4.sp,
                                    onCall = { dial(visiblePolice.dialNumber) }
                                )
                            }

                            if (visibleBoromaa != null) {
                                StandardHelplineCard(
                                    item = visibleBoromaa,
                                    borderColor = BorderAmber,
                                    labelColor = Amber400.copy(alpha = 0.9f),
                                    buttonBackground = RegalViolet,
                                    buttonBorderColor = BorderVioletButton,
                                    buttonIcon = R.drawable.ic_help_phone_badge,
                                    buttonIconTint = Color(0xFFFCD34D),
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
                                    buttonBackground = SurfaceHigh,
                                    buttonBorderColor = BorderSubtle,
                                    buttonIcon = R.drawable.ic_help_fire,
                                    buttonIconTint = Orange400,
                                    onCall = { dial(visibleFire.dialNumber) }
                                )
                            }

                            if (visibleMunicipality != null) {
                                StandardHelplineCard(
                                    item = visibleMunicipality,
                                    buttonBackground = SurfaceHigh,
                                    buttonBorderColor = BorderSubtle,
                                    buttonIcon = R.drawable.ic_help_phone,
                                    buttonIconTint = Blue400,
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
                                    buttonBackground = SurfaceHigh,
                                    buttonBorderColor = BorderSubtle,
                                    buttonIcon = R.drawable.ic_help_phone,
                                    buttonIconTint = Violet300,
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
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item(key = "helpline_booths") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                        .background(SurfaceCard)
                        .border(1.dp, BorderSubtle, CardShape12)
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
                .height(1.dp)
                .background(BorderFaint)
        )
    }
}

@Composable
private fun EmergencyPulseDot() {
    val transition = rememberInfiniteTransition(label = "helpline_emergency_pulse")
    val pingScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 2.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearOutSlowInEasing)
        ),
        label = "ping_scale"
    )
    val pingAlpha by transition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearOutSlowInEasing)
        ),
        label = "ping_alpha"
    )

    Box(modifier = Modifier.size(8.dp), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .graphicsLayer {
                    scaleX = pingScale
                    scaleY = pingScale
                    alpha = pingAlpha
                }
                .clip(CircleShape)
                .background(Rose400)
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    ambientColor = Rose500,
                    spotColor = Rose500
                )
                .clip(CircleShape)
                .background(Rose500)
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
            .background(SurfaceLow)
            .border(
                width = 1.dp,
                color = if (focused) RegalViolet else BorderSubtle,
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
                color = Color.White,
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            cursorBrush = SolidColor(Violet300),
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
                        tint = Violet300,
                        modifier = Modifier.size(16.dp)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            HelpText(
                                text = "Search helpline or area (Police, Fire, Hospital, Booth)...",
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
private fun StandardHelplineCard(
    item: HelplineItem,
    borderColor: Color = BorderSubtle,
    labelColor: Color = Zinc400,
    numberColor: Color = Color.White,
    buttonBackground: Color,
    buttonBorderColor: Color,
    buttonIcon: Int,
    buttonIconTint: Color,
    numberLetterSpacing: TextUnit = 0.sp,
    onCall: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape16)
            .background(SurfaceLow)
            .border(1.dp, borderColor, CardShape16)
            .padding(16.dp)
            .testTag("helpline_card_${item.id}")
    ) {
        HelpText(
            text = item.title,
            size = 14.sp,
            weight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = (-0.35).sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CardShape12)
                .background(SurfaceCard)
                .border(1.dp, BorderFaint, CardShape12)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                HelpText(
                    text = item.subtitleLabel,
                    size = 10.sp,
                    weight = FontWeight.SemiBold,
                    color = labelColor,
                    letterSpacing = 0.5.sp,
                    lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                HelpText(
                    text = item.numbers,
                    size = 16.sp,
                    weight = FontWeight.ExtraBold,
                    color = numberColor,
                    family = FontFamily.Monospace,
                    letterSpacing = numberLetterSpacing,
                    lineHeight = 21.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            CallPill(
                label = item.actionLabel,
                background = buttonBackground,
                borderColor = buttonBorderColor,
                iconRes = buttonIcon,
                iconTint = buttonIconTint,
                onClick = onCall,
                testTag = "helpline_call_${item.id}"
            )
        }
    }
}

@Composable
private fun CallPill(
    label: String,
    background: Color,
    borderColor: Color,
    iconRes: Int,
    iconTint: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .clip(CardShape12)
            .background(background)
            .border(1.dp, borderColor, CardShape12)
            .clickable(role = Role.Button, onClick = onClick)
            .testTag(testTag)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(14.dp)
        )
        HelpText(
            text = label,
            size = 12.sp,
            weight = FontWeight.Bold,
            color = Color.White,
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
            .background(SurfaceLow)
            .border(1.dp, BorderSubtle, CardShape16)
            .padding(16.dp)
            .testTag("helpline_card_medical")
    ) {
        HelpText(
            text = hospital?.title ?: "Naihati State General Hospital",
            size = 14.sp,
            weight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = (-0.35).sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (hospital != null) {
                CompactContactCard(
                    item = hospital,
                    numberColor = Color.White,
                    iconRes = R.drawable.ic_help_hospital,
                    buttonIconTint = Emerald400,
                    buttonBackground = SurfaceHigh,
                    buttonBorderColor = BorderSubtle,
                    onClick = { onCall(hospital) },
                    modifier = Modifier.weight(1f)
                )
            }
            if (ambulance != null) {
                CompactContactCard(
                    item = ambulance,
                    numberColor = Rose400,
                    iconRes = R.drawable.ic_help_ambulance,
                    buttonIconTint = Color.White,
                    buttonBackground = Rose600,
                    buttonBorderColor = BorderAmbulance,
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
    numberColor: Color,
    iconRes: Int,
    buttonIconTint: Color,
    buttonBackground: Color,
    buttonBorderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(CardShape12)
            .background(SurfaceCard)
            .border(1.dp, BorderFaint, CardShape12)
            .padding(10.dp)
    ) {
        HelpText(
            text = item.subtitleLabel.uppercase(),
            size = 9.sp,
            weight = FontWeight.SemiBold,
            color = Zinc400,
            letterSpacing = 0.45.sp,
            lineHeight = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        HelpText(
            text = item.numbers,
            size = 12.sp,
            weight = FontWeight.Bold,
            color = numberColor,
            family = FontFamily.Monospace,
            lineHeight = 16.sp,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CardShape8)
                .background(buttonBackground)
                .border(1.dp, buttonBorderColor, CardShape8)
                .clickable(role = Role.Button, onClick = onClick)
                .testTag("helpline_call_${item.id}")
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = buttonIconTint,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            HelpText(
                text = item.actionLabel,
                size = 11.sp,
                weight = FontWeight.SemiBold,
                color = Color.White,
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
            CompactIconHelplineCard(
                item = women,
                iconRes = R.drawable.ic_help_woman,
                iconTint = Pink400,
                iconBorder = BorderPink,
                callIconTint = Pink300,
                onClick = { onCall(women) },
                modifier = Modifier.weight(1f)
            )
        }
        if (child != null) {
            CompactIconHelplineCard(
                item = child,
                iconRes = R.drawable.ic_help_child,
                iconTint = Violet400,
                iconBorder = BorderViolet,
                callIconTint = Violet300,
                onClick = { onCall(child) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CompactIconHelplineCard(
    item: HelplineItem,
    iconRes: Int,
    iconTint: Color,
    iconBorder: Color,
    callIconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(CardShape16)
            .background(SurfaceLow)
            .border(1.dp, BorderSubtle, CardShape16)
            .padding(14.dp)
            .testTag("helpline_card_${item.id}")
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CardShape8)
                .background(SurfaceHigh)
                .border(1.dp, iconBorder, CardShape8),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        HelpText(
            text = item.title,
            size = 12.sp,
            weight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        HelpText(
            text = item.numbers,
            size = 14.sp,
            weight = FontWeight.ExtraBold,
            color = Color.White,
            family = FontFamily.Monospace,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CardShape12)
                .background(RegalViolet)
                .border(1.dp, BorderVioletSoft, CardShape12)
                .clickable(role = Role.Button, onClick = onClick)
                .testTag("helpline_call_${item.id}")
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_help_phone),
                contentDescription = null,
                tint = callIconTint,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            HelpText(
                text = item.actionLabel,
                size = 12.sp,
                weight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 16.sp,
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
            .background(SurfaceLow)
            .border(1.dp, BorderSubtle, CardShape16)
            .padding(16.dp)
            .testTag("helpline_card_${item.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            HelpText(
                text = item.title,
                size = 14.sp,
                weight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = (-0.35).sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            HelpText(
                text = item.numbers,
                size = 12.sp,
                weight = FontWeight.Bold,
                color = Color.White,
                family = FontFamily.Monospace,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CardShape12)
                .background(SurfaceHigh)
                .border(1.dp, BorderSubtle, CardShape12)
                .clickable(role = Role.Button, onClick = onCall)
                .testTag("helpline_call_${item.id}"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_help_phone),
                contentDescription = "Call ${item.title}",
                tint = Yellow400,
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
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(RegalViolet)
        )
        Spacer(modifier = Modifier.width(6.dp))
        HelpText(
            text = "IMPORTANT POLICE ASSISTANCE BOOTHS",
            size = 12.sp,
            weight = FontWeight.Bold,
            color = Zinc400,
            letterSpacing = 0.6.sp,
            lineHeight = 16.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun PoliceBoothRow(
    booth: PoliceBooth,
    onClick: () -> Unit
) {
    val boothIcon = when (booth.number) {
        1 -> R.drawable.ic_help_booth_clock
        2 -> R.drawable.ic_help_booth_route
        else -> R.drawable.ic_help_booth_star
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape12)
            .background(SurfaceLow)
            .border(1.dp, BorderSubtle, CardShape12)
            .clickable(
                role = Role.Button,
                onClickLabel = "Open ${booth.name} in Maps",
                onClick = onClick
            )
            .testTag("helpline_booth_${booth.number}")
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CardShape8)
                .background(SurfaceHigh)
                .border(1.dp, BorderViolet, CardShape8),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    painter = painterResource(boothIcon),
                    contentDescription = null,
                    tint = Violet400,
                    modifier = Modifier.size(12.dp)
                )
                HelpText(
                    text = booth.number.toString(),
                    size = 12.sp,
                    weight = FontWeight.Bold,
                    color = Violet300,
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        HelpText(
            text = booth.name,
            size = 12.sp,
            weight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 16.sp,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(R.drawable.ic_help_chevron_right),
            contentDescription = null,
            tint = Zinc500,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun SafetyGuidelinesCard(guidelines: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape16)
            .background(SurfaceLow)
            .border(1.dp, BorderSubtle, CardShape16)
            .padding(16.dp)
            .testTag("helpline_guidelines")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_help_shield_alert),
                contentDescription = null,
                tint = Amber400,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            HelpText(
                text = "VISITOR GUIDELINES & SAFETY INSTRUCTIONS",
                size = 12.sp,
                weight = FontWeight.Bold,
                color = Amber400,
                letterSpacing = 0.6.sp,
                lineHeight = 16.sp,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            guidelines.forEach { guideline ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_help_check_circle),
                        contentDescription = null,
                        tint = Amber500.copy(alpha = 0.9f),
                        modifier = Modifier
                            .padding(top = 1.dp)
                            .size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    HelpText(
                        text = guideline,
                        size = 11.sp,
                        weight = FontWeight.Normal,
                        color = Zinc300,
                        lineHeight = 16.5.sp,
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
            .background(SurfaceLow)
            .border(1.dp, BorderSubtle, CardShape16)
            .padding(20.dp)
            .testTag("helpline_no_results"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_help_search),
            contentDescription = null,
            tint = Violet300,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        HelpText(
            text = "No emergency contact found",
            size = 13.sp,
            weight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 17.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        HelpText(
            text = "Nothing matches \u201C$query\u201D. Try Police, Fire, Hospital or another keyword.",
            size = 11.sp,
            weight = FontWeight.Normal,
            color = Zinc400,
            lineHeight = 16.5.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(SurfaceHigh)
                .border(1.dp, BorderSubtle, CircleShape)
                .clickable(role = Role.Button, onClick = onClear)
                .testTag("helpline_clear_search")
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            HelpText(
                text = "Clear search",
                size = 12.sp,
                weight = FontWeight.SemiBold,
                color = Color.White,
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
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(RegalViolet)
            )
            Spacer(modifier = Modifier.width(8.dp))
            HelpText(
                text = "\u2022 Jay Boro Maa \u2022",
                size = 11.sp,
                weight = FontWeight.Medium,
                color = Zinc400,
                letterSpacing = 0.55.sp,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(RegalViolet)
            )
        }
        SectionCreditFooter(
            topPadding = 10.dp,
            bottomPadding = 0.dp
        )
    }
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
