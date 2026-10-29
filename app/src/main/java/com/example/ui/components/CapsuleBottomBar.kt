package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NavigationScreen
import com.example.ui.theme.LocalAppPalette
import com.example.ui.theme.TextMutedZinc
import com.example.ui.theme.TextSoftZinc

data class NavItemSpec(
    val screen: NavigationScreen,
    val label: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
    val testTag: String
)

@Composable
fun CapsuleBottomBar(
    currentScreen: NavigationScreen,
    onNavigate: (NavigationScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItemSpec(
            screen = NavigationScreen.HOME,
            label = "Home",
            activeIcon = Icons.Filled.Home,
            inactiveIcon = Icons.Outlined.Home,
            testTag = "nav_home"
        ),
        NavItemSpec(
            screen = NavigationScreen.RESTAURANT,
            label = "Restaurant",
            activeIcon = Icons.Filled.Restaurant,
            inactiveIcon = Icons.Outlined.Restaurant,
            testTag = "nav_restaurant"
        ),
        NavItemSpec(
            screen = NavigationScreen.HELPLINE,
            label = "Helpline",
            activeIcon = Icons.Filled.Call,
            inactiveIcon = Icons.Outlined.Call,
            testTag = "nav_helpline"
        ),
        NavItemSpec(
            screen = NavigationScreen.THEME_PANDEL,
            label = "Theme Pandel",
            activeIcon = Icons.Filled.Palette,
            inactiveIcon = Icons.Outlined.Palette,
            testTag = "nav_theme_pandel"
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .shadow(elevation = 24.dp, shape = CircleShape, ambientColor = Color.Black, spotColor = LocalAppPalette.current.accentDeep)
                .clip(CircleShape)
                .background(Color(0xF012111B))
                .border(width = 1.dp, color = Color(0x2EFFFFFF), shape = CircleShape)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentScreen == item.screen
                val interactionSource = remember { MutableInteractionSource() }

                if (isSelected) {
                    // Active Pill Capsule
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .shadow(elevation = 8.dp, shape = CircleShape, ambientColor = LocalAppPalette.current.accentDeep)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(LocalAppPalette.current.accentDeep, LocalAppPalette.current.accentStrong)
                                )
                            )
                            .border(width = 1.dp, color = LocalAppPalette.current.accentBorderStrong, shape = CircleShape)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { onNavigate(item.screen) }
                            .padding(horizontal = 12.dp)
                            .testTag(item.testTag),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.activeIcon,
                            contentDescription = item.label,
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = item.label,
                            color = Color.White,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.02.sp
                        )
                    }
                } else {
                    // Inactive Icon Button
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { onNavigate(item.screen) }
                            .testTag(item.testTag),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.inactiveIcon,
                            contentDescription = item.label,
                            tint = TextSoftZinc,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }
    }
}
