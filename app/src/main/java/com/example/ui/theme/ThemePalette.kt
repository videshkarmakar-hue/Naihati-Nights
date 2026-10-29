package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.model.ThemeStyle

/**
 * The themable token set for the app.
 *
 * Only the brand/accent family is themed: the neutral Obsidian canvas, surfaces, hairlines and the
 * three text tones stay fixed so every theme keeps the exact same verified legibility. Each palette
 * is defined as a full ramp — [accentDeep] for solid fills and gradient starts, [accentStrong] for
 * hover/gradient ends, [accent] for vivid interactive elements, [accentBright] for accent text and
 * icons on dark surfaces, plus brand-tinted panel fills and three hairline opacities.
 *
 * Contrast measured against the Obsidian surfaces (canvas `#050505`, base `#0E0E12`,
 * raised `#16161D`, highlight `#1E1E28`):
 *  - [accentBright]  8.9:1 – 16.1:1  (WCAG AA for body text)
 *  - [accent]        3.2:1 – 11.0:1  (WCAG AA for large text / UI graphics)
 *  - white on [accentDeep] 9.0:1 – 11.4:1
 */
data class AppPalette(
    /** Theme name, mirrored from [ThemeStyle.label]. */
    val name: String,
    /** Surface the theme's text tokens sit on (title / body / placeholder). */
    val textBg: Color,
    /** Primary text on [textBg]. */
    val text: Color,
    /** Muted text on [textBg]. */
    val textMuted: Color,
    /** Vivid interactive accent: switches, focused fields, active indicators. */
    val accent: Color,
    /** Light accent used for accent text and icons on the dark Obsidian surfaces. */
    val accentBright: Color,
    /** Saturated mid tone: gradient end and hover states. */
    val accentStrong: Color,
    /** Darkest brand tone: solid fills and gradient starts. */
    val accentDeep: Color,
    /** Brand-tinted panel fill. */
    val accentSurface: Color,
    /** Stronger brand-tinted panel fill for emphasis blocks. */
    val accentSurfaceStrong: Color,
    /** ~20% brand wash behind icons and chips. */
    val accentSoft: Color,
    /** ~30% brand hairline. */
    val accentBorder: Color,
    /** ~60% brand hairline for focused/active outlines. */
    val accentBorderStrong: Color,
    /** Content color for text placed on top of [accent] fills. */
    val onAccent: Color
)

/**
 * Regal Violet — the signature Obsidian & Regal Violet identity. No longer the launch default:
 * [ThemeStyle.KUMARTULI_TEAL] is, but this remains the palette for users who saved violet.
 */
private val RegalVioletPalette = AppPalette(
    name = "Regal Violet",
    accent = Color(0xFF8257E5),
    accentBright = Color(0xFFC4B5FD),
    accentStrong = Color(0xFF4A15BE),
    accentDeep = Color(0xFF3A0CA3),
    accentSurface = Color(0xFF241442),
    accentSurfaceStrong = Color(0xFF2D1B54),
    accentSoft = Color(0x333A0CA3),
    accentBorder = Color(0x4D8257E5),
    accentBorderStrong = Color(0x998257E5),
    onAccent = Color(0xFFF8FAFC),
    textBg = Color(0xFF050505),
    text = Color(0xFFF4F4F5),
    textMuted = Color(0xFF94A3B8)
)

/** Sacred Gold — warm gold ramp over deep amber panels. */
private val SacredGoldPalette = AppPalette(
    name = "Sacred Gold",
    accent = Color(0xFFE5A93B),
    accentBright = Color(0xFFF6D9A0),
    accentStrong = Color(0xFF8A6112),
    accentDeep = Color(0xFF5A3E08),
    accentSurface = Color(0xFF2A1F0B),
    accentSurfaceStrong = Color(0xFF3D2C10),
    accentSoft = Color(0x33E5A93B),
    accentBorder = Color(0x4DE5A93B),
    accentBorderStrong = Color(0x99E5A93B),
    onAccent = Color(0xFF1C1204),
    textBg = Color(0xFF050505),
    text = Color(0xFFF4F4F5),
    textMuted = Color(0xFF94A3B8)
)

/** Kumartuli Teal — sea-glass teal over deep pine panels. */
private val KumartuliTealPalette = AppPalette(
    name = "Kumartuli Teal",
    accent = Color(0xFF2DD4BF),
    accentBright = Color(0xFFA7F3E7),
    accentStrong = Color(0xFF0F766E),
    accentDeep = Color(0xFF0B4F49),
    accentSurface = Color(0xFF0C2A28),
    accentSurfaceStrong = Color(0xFF123A37),
    accentSoft = Color(0x332DD4BF),
    accentBorder = Color(0x4D2DD4BF),
    accentBorderStrong = Color(0x992DD4BF),
    onAccent = Color(0xFF04211F),
    textBg = Color(0xFF04211F),
    text = Color(0xFFCCFBE6),
    textMuted = Color(0xFF94A3B8)
)

/** Azure Signal — crisp sky blue over deep ink panels. */
private val AzureSignalPalette = AppPalette(
    name = "Azure Signal",
    accent = Color(0xFF38BDF8),
    accentBright = Color(0xFFB8E4FD),
    accentStrong = Color(0xFF0369A1),
    accentDeep = Color(0xFF0C4A6E),
    accentSurface = Color(0xFF0B2739),
    accentSurfaceStrong = Color(0xFF0F354D),
    accentSoft = Color(0x3338BDF8),
    accentBorder = Color(0x4D38BDF8),
    accentBorderStrong = Color(0x9938BDF8),
    onAccent = Color(0xFF04202E),
    textBg = Color(0xFF04202E),
    text = Color(0xFFB8E4FD),
    textMuted = Color(0xFF94A3B8)
)

/** The palette backing the currently selected [ThemeStyle]. */
val ThemeStyle.palette: AppPalette
    get() = when (this) {
        ThemeStyle.REGAL_VIOLET -> RegalVioletPalette
        ThemeStyle.SACRED_GOLD -> SacredGoldPalette
        ThemeStyle.KUMARTULI_TEAL -> KumartuliTealPalette
        ThemeStyle.AZURE_SIGNAL -> AzureSignalPalette
    }

/** Every selectable theme, in the order shown by the Settings picker. */
val AvailablePalettes: List<ThemeStyle> = ThemeStyle.entries.toList()

/**
 * Active palette, provided by [MyApplicationTheme]. Screens read accent tokens from here so a
 * theme change re-colours the whole app without any per-screen state.
 */
val LocalAppPalette = staticCompositionLocalOf { KumartuliTealPalette }

/**
 * Material color scheme derived from a palette. The neutral Obsidian surfaces and the three text
 * tones stay constant across themes; only the brand slots follow the selection.
 */
fun AppPalette.toColorScheme(): ColorScheme = darkColorScheme(
    primary = accentBright,
    onPrimary = ObsidianNoir,
    primaryContainer = accentDeep,
    onPrimaryContainer = accentBright,
    secondary = accentBright,
    onSecondary = ObsidianNoir,
    secondaryContainer = accentStrong,
    onSecondaryContainer = accentBright,
    tertiary = SacredGold,
    onTertiary = ObsidianNoir,
    background = ObsidianNoir,
    onBackground = TextHighWhite,
    surface = ObsidianBase,
    onSurface = TextHighWhite,
    surfaceVariant = ObsidianRaised,
    onSurfaceVariant = TextSoftZinc,
    outline = ObsidianBorder,
    outlineVariant = ObsidianBorderSubtle,
    error = EmergencyRose,
    onError = TextHighWhite
)
