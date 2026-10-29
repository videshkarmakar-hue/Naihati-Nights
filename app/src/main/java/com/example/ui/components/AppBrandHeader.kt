package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TextHighWhite
import com.example.ui.theme.TextSoftZinc

/** Brand shown in the top header of every section. */
const val APP_BRAND_NAME = "Naihati Nights"

/** Remote logo supplied by the product owner. */
const val APP_BRAND_LOGO_URL =
    "https://cdn.phototourl.com/member/2026-10-07-3560ff11-c4ca-4914-ada1-bb03390ea789.png"

private val BrandLogoShape = RoundedCornerShape(11.dp)
private val BrandLogoBackground: Color @Composable get() = LocalAppPalette.current.accentSoft
private val BrandLogoBorder: Color @Composable get() = LocalAppPalette.current.accentBorder
private val HeaderActionShape = RoundedCornerShape(11.dp)

/**
 * Shared top header used by every section: the supplied logo first, then the bold
 * "Naihati Nights" wordmark, then the section's own actions and finally the Sidebar button in the
 * top-right corner.
 *
 * [navigation] is the exit affordance (back) and [trailing] holds section actions such as reset,
 * share or app details. [onOpenDeveloperCredits] and [onOpenSidebar] render the shared Developer
 * Credit and Sidebar buttons as the last items, so both stay visible in the top-right corner of
 * every screen.
 */
@Composable
fun AppBrandHeader(
    modifier: Modifier = Modifier,
    onOpenSidebar: (() -> Unit)? = null,
    onOpenDeveloperCredits: (() -> Unit)? = null,
    navigation: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("app_brand_header")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BrandLogo()

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = APP_BRAND_NAME,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                letterSpacing = (-0.3).sp,
                color = TextHighWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .testTag("app_brand_title")
            )

            if (navigation != null) {
                Spacer(modifier = Modifier.width(4.dp))
                navigation()
            }

            if (trailing != null) {
                Spacer(modifier = Modifier.width(4.dp))
                trailing()
            }

            if (onOpenDeveloperCredits != null) {
                Spacer(modifier = Modifier.width(4.dp))
                HeaderDeveloperCreditButton(onClick = onOpenDeveloperCredits)
            }

            if (onOpenSidebar != null) {
                Spacer(modifier = Modifier.width(4.dp))
                HeaderSidebarButton(onClick = onOpenSidebar)
            }
        }

        DevotionalHeaderNotice()
    }
}

/**
 * Public-interest notice pinned under the header row on every section, so the Boro Maa
 * immersion schedule stays visible from the very first screen.
 */
@Composable
private fun DevotionalHeaderNotice() {
    val accent = LocalAppPalette.current.accentBright

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .testTag("devotional_notice"),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(5.dp)
                .clip(CircleShape)
                .background(accent)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "বড়ো কালী পূজা সমিতির থেকে জনস্বার্থে প্রচারিত:",
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = accent
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "(আগামী ৮ নভেম্বর দীপাবলির দিন নৈহাটিতে পূজিত হবেন বড়মা এবং আগামী ১২ নভেম্বর বিকেলে বড়মা গঙ্গাবক্ষে নিরঞ্জনের উদ্দেশ্যে রওনা হবেন)",
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = TextSoftZinc
            )
        }
    }
}

/**
 * The recurring "About ME" affordance pinned next to the Sidebar button in every header; it opens
 * the developer credit screen. Rendered as a text pill so the destination reads literally instead
 * of as an abstract icon.
 */
@Composable
private fun HeaderDeveloperCreditButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .height(32.dp)
            .clip(HeaderActionShape)
            .background(Color(0x14FFFFFF))
            .border(1.dp, Color(0x26FFFFFF), HeaderActionShape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 12.dp)
            .testTag("header_developer_credit_button"),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "About ME",
            fontFamily = PlusJakartaSans,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.2.sp,
            color = TextHighWhite,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/** The recurring Sidebar affordance pinned to the top-right corner of every section header. */
@Composable
private fun HeaderSidebarButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(HeaderActionShape)
            .background(Color(0x14FFFFFF))
            .border(1.dp, Color(0x26FFFFFF), HeaderActionShape)
            .clickable(role = Role.Button, onClick = onClick)
            .testTag("header_sidebar_button"),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Open sidebar",
            tint = TextHighWhite,
            modifier = Modifier.size(17.dp)
        )
    }
}

@Composable
private fun BrandLogo() {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(APP_BRAND_LOGO_URL)
            .crossfade(true)
            .build(),
        contentDescription = "Naihati Nights logo",
        contentScale = ContentScale.Crop,
        loading = { BrandLogoFallback() },
        error = { BrandLogoFallback() },
        modifier = Modifier
            .size(36.dp)
            .clip(BrandLogoShape)
            .background(BrandLogoBackground)
            .border(1.dp, BrandLogoBorder, BrandLogoShape)
            .testTag("app_brand_logo")
    )
}

@Composable
private fun BrandLogoFallback() {
    Image(
        painter = painterResource(id = R.drawable.app_brand_logo),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}
