package com.example.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.AltRoute
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.TempleHindu
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppContent
import com.example.model.InquiryType
import com.example.model.UrgencyLevel
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.SectionCreditFooter
import com.example.ui.theme.PlusJakartaSans
import kotlinx.coroutines.delay
import kotlin.random.Random

// ---------------------------------------------------------------------------
// Design tokens lifted from the "Report & Feedback" reference screen.
// ---------------------------------------------------------------------------
private val PageBlack = Color(0xFF0B0C0F)
private val SurfaceLow = Color(0xFF12141B)
private val SurfaceCard = Color(0xFF12141A)
private val SurfaceHigh = Color(0xFF20232E)
private val FieldSurface = Color(0xFF181922)
private val Zinc200 = Color(0xFFE4E4E7)
private val Zinc300 = Color(0xFFD4D4D8)
private val Zinc400 = Color(0xFFA1A1AA)
private val Zinc500 = Color(0xFF717586)
private val FocusBorder = Color(0xFFA1A1AA)
private val BlackInk = Color(0xFF0B0C0F)
private val BorderCard = Color(0x14FFFFFF) // white / 8 %
private val BorderField = Color(0x14FFFFFF) // white / 8 %
private val BorderSoft = Color(0x1AFFFFFF) // white / 10 %
private val BorderDashed = Color(0x26FFFFFF) // white / 15 %
private val BorderHeader = Color(0x0DFFFFFF) // white / 5 %
private val ErrorRose = Color(0xFFFB7185)
private const val MaxDetailsLength = 500
private const val MaxAttachmentBytes = 10L * 1024L * 1024L

private val CategoryShape = RoundedCornerShape(12.dp)
private val FieldShape = RoundedCornerShape(12.dp)
private val ChipShape = RoundedCornerShape(8.dp)

/** The zone chips are fixed by the reference design. */
private val FeedbackZones = listOf(
    "Route 1 (Station PF1)",
    "Route 2 (Shyam Rd)",
    "Route 3 (Swapnabithi)",
    "Route 4 (Kanaidas)",
    "Route 5 (Green View)",
    "Route 6 (29 Haat)",
    "Route 7 (Power House)",
    "General App"
)

@Composable
fun FeedbackScreen(
    content: AppContent? = null,
    onBack: () -> Unit = {},
    onOpenSidebar: () -> Unit = {},
    onOpenDeveloperCredits: () -> Unit = {},
    onSubmitFeedback: (
        InquiryType,
        String,
        String,
        String,
        String,
        String,
        UrgencyLevel,
        Boolean
    ) -> Unit = { _, _, _, _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var selectedInquiry by remember { mutableStateOf(InquiryType.SUGGESTION) }
    var selectedZone by remember { mutableStateOf(FeedbackZones.first()) }
    var landmark by remember { mutableStateOf("") }
    var reporterName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var urgency by remember { mutableStateOf(UrgencyLevel.IMPORTANT) }
    var attachedName by remember { mutableStateOf<String?>(null) }
    var attachmentError by remember { mutableStateOf<String?>(null) }
    var detailsError by remember { mutableStateOf<String?>(null) }
    var contactError by remember { mutableStateOf<String?>(null) }
    var formLocked by remember { mutableStateOf(false) }
    var submissionCount by remember { mutableIntStateOf(0) }
    var ticketCode by remember { mutableStateOf<Int?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val size = querySize(context, uri)
            if (size != null && size > MaxAttachmentBytes) {
                attachmentError = "Photo is larger than 10 MB. Please choose a smaller image."
                attachedName = null
            } else {
                attachmentError = null
                attachedName = queryDisplayName(context, uri)
            }
        }
    }

    fun clearErrors() {
        detailsError = null
        contactError = null
        attachmentError = null
    }

    fun validate(): Boolean {
        clearErrors()
        var valid = true
        if (details.trim().length < 10) {
            detailsError = "Please describe the incident in at least 10 characters."
            valid = false
        }
        val digits = contactNumber.count { it.isDigit() }
        if (contactNumber.isNotBlank() && digits !in 7..15) {
            contactError = "Enter a valid contact number (7–15 digits)."
            valid = false
        }
        return valid
    }

    fun submit() {
        if (formLocked) return
        focusManager.clearFocus()
        if (!validate()) return
        onSubmitFeedback(
            selectedInquiry,
            selectedZone,
            landmark.trim(),
            reporterName.trim(),
            contactNumber.trim(),
            details.trim(),
            urgency,
            attachedName != null
        )
        ticketCode = Random.nextInt(1000, 10000)
        formLocked = true
        submissionCount += 1
    }

    LaunchedEffect(submissionCount) {
        if (submissionCount == 0) return@LaunchedEffect
        delay(3500)
        // The reference form resets itself after the success toast appears.
        landmark = ""
        reporterName = ""
        contactNumber = ""
        details = ""
        attachedName = null
        attachmentError = null
        detailsError = null
        contactError = null
        selectedZone = FeedbackZones.first()
        urgency = UrgencyLevel.IMPORTANT
        formLocked = false
    }

    LaunchedEffect(ticketCode) {
        if (ticketCode != null) {
            delay(160)
            scrollState.animateScrollTo(scrollState.maxValue)
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
            FeedbackTopBar(
                onBack = onBack,
                onOpenSidebar = onOpenSidebar,
                onOpenDeveloperCredits = onOpenDeveloperCredits
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 168.dp)
            ) {
                // ---------------- Category hub ----------------
                Text(
                    text = "Select Inquiry Type",
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.2).sp,
                    color = Zinc200,
                    modifier = Modifier.padding(start = 2.dp, end = 2.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                val categories = InquiryType.entries
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    categories.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            row.forEach { type ->
                                CategoryPill(
                                    type = type,
                                    selected = selectedInquiry == type,
                                    onClick = {
                                        selectedInquiry = type
                                        clearErrors()
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (row.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ---------------- Intake form ----------------
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceLow)
                        .border(1.dp, BorderCard, RoundedCornerShape(16.dp))
                        .alpha(if (formLocked) 0.4f else 1f)
                        .padding(16.dp)
                ) {
                    // Affected zone / corridor
                    FormLabelRow(
                        title = "Affected Zone / Corridor",
                        trailing = "7 Core Routes"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(top = 2.dp, bottom = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FeedbackZones.forEachIndexed { index, zone ->
                            val selected = selectedZone == zone
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .clip(ChipShape)
                                    .background(if (selected) Color.White else FieldSurface)
                                    .border(
                                        1.dp,
                                        if (selected) Color.White else BorderCard,
                                        ChipShape
                                    )
                                    .clickable(enabled = !formLocked) { selectedZone = zone }
                                    .padding(horizontal = 14.dp)
                                    .testTag("feedback_zone_$index"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = zone,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                                    color = if (selected) BlackInk else Zinc300,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Related pandal / landmark
                    FormLabelRow(
                        title = "Related Pandal / Landmark",
                        trailing = "Recommended"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FeedbackTextField(
                        value = landmark,
                        onValueChange = { if (it.length <= 90) landmark = it },
                        placeholder = "e.g., Boro Maa, Ramukali, Shyam Road junction",
                        icon = Icons.Outlined.Place,
                        singleLine = true,
                        enabled = !formLocked,
                        testTag = "feedback_landmark_field"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Devotee details
                    FormLabelRow(title = "Your Name", trailing = "Optional")
                    Spacer(modifier = Modifier.height(6.dp))
                    FeedbackTextField(
                        value = reporterName,
                        onValueChange = { if (it.length <= 60) reporterName = it },
                        placeholder = "e.g., Sayan Mukherjee",
                        icon = Icons.Outlined.Badge,
                        singleLine = true,
                        enabled = !formLocked,
                        testTag = "feedback_name_field"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    FormLabelRow(
                        title = "Contact / WhatsApp",
                        trailing = "For quick on-ground sync"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FeedbackTextField(
                        value = contactNumber,
                        onValueChange = {
                            if (it.length <= 18 && it.all { ch -> ch.isDigit() || ch in "+ -()" }) {
                                contactNumber = it
                                if (contactError != null) contactError = null
                            }
                        },
                        placeholder = "+91 98765 XXXXX",
                        icon = Icons.Outlined.PhoneIphone,
                        singleLine = true,
                        enabled = !formLocked,
                        keyboardType = KeyboardType.Phone,
                        isError = contactError != null,
                        testTag = "feedback_contact_field"
                    )
                    InlineError(contactError)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Detailed description
                    FormLabelRow(
                        title = "Detailed Incident / Feedback",
                        trailing = "${details.length} / $MaxDetailsLength",
                        trailingMonospace = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FeedbackTextField(
                        value = details,
                        onValueChange = {
                            if (it.length <= MaxDetailsLength) {
                                details = it
                                if (detailsError != null) detailsError = null
                            }
                        },
                        placeholder = "Describe the road blockage, queue status, incorrect information, or suggestion in detail...",
                        icon = null,
                        singleLine = false,
                        enabled = !formLocked,
                        height = 108.dp,
                        isError = detailsError != null,
                        testTag = "feedback_details_field"
                    )
                    InlineError(detailsError)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Photo evidence
                    FormLabel("Evidence / Attachment Slot")
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FieldShape)
                            .background(FieldSurface)
                            .dashedBorder(BorderDashed, 12.dp)
                            .clickable(
                                enabled = !formLocked,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { photoPicker.launch("image/*") }
                            .padding(16.dp)
                            .testTag("feedback_attach_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceHigh)
                                    .border(1.dp, BorderSoft, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.AddAPhoto,
                                    contentDescription = null,
                                    tint = Zinc200,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = attachedName?.let { "Attached: $it" }
                                    ?: "Tap to attach photo evidence or screenshot",
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (attachedName != null) Color.White else Zinc200,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "JPG, PNG up to 10MB",
                                fontSize = 11.sp,
                                lineHeight = 14.sp,
                                color = Zinc400,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    InlineError(attachmentError)

                    Spacer(modifier = Modifier.height(20.dp))

                    // Urgency
                    FormLabel("Urgency Level")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(androidx.compose.foundation.layout.IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        UrgencyLevel.entries.forEach { level ->
                            val selected = urgency == level
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .heightIn(min = 36.dp)
                                    .clip(FieldShape)
                                    .background(if (selected) Color.White else FieldSurface)
                                    .border(
                                        1.dp,
                                        if (selected) Color.White else BorderCard,
                                        FieldShape
                                    )
                                    .clickable(enabled = !formLocked) { urgency = level }
                                    .padding(horizontal = 8.dp, vertical = 10.dp)
                                    .testTag("feedback_urgency_${level.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = level.label,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                                    color = if (selected) BlackInk else Zinc300,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit + safeguard
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(FieldShape)
                            .background(Color.White)
                            .clickable(enabled = !formLocked) { submit() }
                            .testTag("feedback_submit_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = null,
                                tint = BlackInk,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Submit Report & Feedback",
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BlackInk
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.VerifiedUser,
                            contentDescription = null,
                            tint = Zinc400,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Privacy protected • Information directly verified by the Naihati Kali Puja Volunteer Desk",
                            fontSize = 11.sp,
                            lineHeight = 14.sp,
                            color = Zinc400,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }
                }

                // ---------------- Success notice ----------------
                AnimatedVisibility(
                    visible = ticketCode != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(20.dp))
                        SubmissionSuccessCard(ticketCode = ticketCode ?: 0)
                    }
                }

                // ---------------- Footer ----------------
                Spacer(modifier = Modifier.height(28.dp))
                Text(
                    text = "✦ JAY MAA BORO MAA ✦",
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.6.sp,
                    color = Zinc300,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Naihati Kali Puja Guide • Verified by Videsh Karmakar",
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Zinc400,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                SectionCreditFooter(
                    topPadding = 0.dp,
                    bottomPadding = 0.dp
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Header
// ---------------------------------------------------------------------------
@Composable
private fun FeedbackTopBar(
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
                        .clip(CategoryShape)
                        .background(SurfaceCard)
                        .border(1.dp, BorderSoft, CategoryShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onBack
                        )
                        .testTag("feedback_back_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BorderHeader)
        )
    }
}

// ---------------------------------------------------------------------------
// Category pills
// ---------------------------------------------------------------------------
@Composable
private fun CategoryPill(
    type: InquiryType,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon: ImageVector = when (type) {
        InquiryType.ROUTE_ALERT -> Icons.Outlined.AltRoute
        InquiryType.PANDAL_UPDATE -> Icons.Outlined.TempleHindu
        InquiryType.SUGGESTION -> Icons.Outlined.TipsAndUpdates
        InquiryType.EXPERIENCE -> Icons.Outlined.SentimentSatisfied
    }

    Row(
        modifier = modifier
            .clip(CategoryShape)
            .background(if (selected) Color.White else SurfaceCard)
            .border(
                1.dp,
                if (selected) Color.White else BorderCard,
                CategoryShape
            )
            .clickable { onClick() }
            .padding(12.dp)
            .testTag("feedback_type_${type.name.lowercase()}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) BlackInk else Zinc400,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = type.title,
            fontSize = 12.sp,
            lineHeight = 15.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) BlackInk else Zinc300,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ---------------------------------------------------------------------------
// Form building blocks
// ---------------------------------------------------------------------------
@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Zinc200
    )
}

@Composable
private fun FormLabelRow(
    title: String,
    trailing: String,
    trailingMonospace: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Zinc200,
            modifier = Modifier.weight(1f, fill = false)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = trailing,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = if (trailingMonospace) FontFamily.Monospace else FontFamily.Default,
            color = Zinc400,
            textAlign = TextAlign.End,
            maxLines = 1
        )
    }
}

@Composable
private fun FeedbackTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector?,
    singleLine: Boolean,
    testTag: String,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    height: androidx.compose.ui.unit.Dp = 46.dp,
    isError: Boolean = false
) {
    var focused by remember { mutableStateOf(false) }
    val borderColor = when {
        isError -> ErrorRose.copy(alpha = 0.6f)
        focused -> FocusBorder
        else -> BorderField
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = height)
            .height(height)
            .clip(FieldShape)
            .background(FieldSurface)
            .border(1.dp, borderColor, FieldShape)
            .padding(
                start = if (icon != null) 14.dp else 14.dp,
                end = 14.dp,
                top = if (singleLine) 0.dp else 14.dp,
                bottom = if (singleLine) 0.dp else 14.dp
            ),
        verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Zinc500,
                modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontFamily = PlusJakartaSans
            ),
            singleLine = singleLine,
            maxLines = if (singleLine) 1 else 6,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            cursorBrush = SolidColor(Color.White),
            modifier = Modifier
                .weight(1f)
                .then(if (singleLine) Modifier else Modifier.fillMaxHeight())
                .onFocusChanged { focused = it.isFocused }
                .testTag(testTag),
            decorationBox = { innerTextField ->
                Box(
                    modifier = if (singleLine) {
                        Modifier.fillMaxWidth()
                    } else {
                        Modifier.fillMaxSize()
                    }
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = Zinc500
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
private fun InlineError(message: String?) {
    if (message == null) return
    Spacer(modifier = Modifier.height(5.dp))
    Text(
        text = message,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        fontWeight = FontWeight.Medium,
        color = ErrorRose
    )
}

@Composable
private fun SubmissionSuccessCard(ticketCode: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCard)
            .border(1.dp, BorderSoft, RoundedCornerShape(16.dp))
            .padding(20.dp)
            .testTag("feedback_success_notice"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = BlackInk,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = "Report Dispatched Successfully",
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = buildAnnotatedString {
                append("Ticket ")
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("#NKP-${ticketCode.toString().padStart(4, '0')}")
                }
                append(" transmitted to volunteer control room.")
            },
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Zinc400,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("feedback_ticket_id")
        )
    }
}

private fun Modifier.dashedBorder(
    color: Color,
    radius: androidx.compose.ui.unit.Dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 1.dp
): Modifier = drawBehind {
    val stroke = Stroke(
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(7.dp.toPx(), 5.dp.toPx()),
            0f
        )
    )
    drawRoundRect(
        color = color,
        cornerRadius = CornerRadius(radius.toPx()),
        style = stroke
    )
}

private fun queryDisplayName(context: Context, uri: Uri): String {
    var name = uri.lastPathSegment ?: "photo.jpg"
    runCatching {
        context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index >= 0 && cursor.moveToFirst()) {
                name = cursor.getString(index) ?: name
            }
        }
    }
    return name
}

private fun querySize(context: Context, uri: Uri): Long? {
    var size: Long? = null
    runCatching {
        context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.SIZE),
            null,
            null,
            null
        )?.use { cursor ->
            val index = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (index >= 0 && cursor.moveToFirst() && !cursor.isNull(index)) {
                size = cursor.getLong(index)
            }
        }
    }
    return size
}
