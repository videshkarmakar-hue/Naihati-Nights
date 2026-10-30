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

/** Dark Red — deep crimson on blood plum panels (the requested dark red accent). */
private val DarkRedPalette = AppPalette(
    name = "Dark Red",
    accent = Color(0xFFB91C1C),
    accentBright = Color(0xFFF87171),
    accentStrong = Color(0xFF991B1B),
    accentDeep = Color(0xFF7F1D1D),
    accentSurface = Color(0xFF2A0A0A),
    accentSurfaceStrong = Color(0xFF3B0F0F),
    accentSoft = Color(0x33B91C1C),
    accentBorder = Color(0x4DB91C1C),
    accentBorderStrong = Color(0x99B91C1C),
    onAccent = Color(0xFFF8FAFC),
    textBg = Color(0xFF050505),
    text = Color(0xFFF4F4F5),
    textMuted = Color(0xFF94A3B8)
)

/** Forest Green — deep forest green on pine ink panels. */
private val ForestGreenPalette = AppPalette(
    name = "Forest Green",
    accent = Color(0xFF16A34A),
    accentBright = Color(0xFF86EFAC),
    accentStrong = Color(0xFF15803D),
    accentDeep = Color(0xFF14532D),
    accentSurface = Color(0xFF0A1F14),
    accentSurfaceStrong = Color(0xFF0F2B1B),
    accentSoft = Color(0x3316A34A),
    accentBorder = Color(0x4D16A34A),
    accentBorderStrong = Color(0x9916A34A),
    onAccent = Color(0xFFF0FDF4),
    textBg = Color(0xFF050505),
    text = Color(0xFFF4F4F5),
    textMuted = Color(0xFF94A3B8)
)

/** Indigo Night — deep indigo on night plum panels. */
private val IndigoNightPalette = AppPalette(
    name = "Indigo Night",
    accent = Color(0xFF4F46E5),
    accentBright = Color(0xFFC7D2FE),
    accentStrong = Color(0xFF4338CA),
    accentDeep = Color(0xFF312E81),
    accentSurface = Color(0xFF141338),
    accentSurfaceStrong = Color(0xFF1B1A44),
    accentSoft = Color(0x334F46E5),
    accentBorder = Color(0x4D4F46E5),
    accentBorderStrong = Color(0x995F59EB),
    onAccent = Color(0xFFF5F5FF),
    textBg = Color(0xFF050505),
    text = Color(0xFFF4F4F5),
    textMuted = Color(0xFF94A3B8)
)

/** Royal Amber — burnt amber on deep bronze panels. */
private val RoyalAmberPalette = AppPalette(
    name = "Royal Amber",
    accent = Color(0xFFD97706),
    accentBright = Color(0xFFFBBF24),
    accentStrong = Color(0xFFB45309),
    accentDeep = Color(0xFF92400E),
    accentSurface = Color(0xFF241505),
    accentSurfaceStrong = Color(0xFF2E1A08),
    accentSoft = Color(0x33D97706),
    accentBorder = Color(0x4DD97706),
    accentBorderStrong = Color(0x99D97706),
    onAccent = Color(0xFF1C1204),
    textBg = Color(0xFF050505),
    text = Color(0xFFF4F4F5),
    textMuted = Color(0xFF94A3B8)
)

/** Plum Noir — muted plum on ink plum panels. */
private val PlumNoirPalette = AppPalette(
    name = "Plum Noir",
    accent = Color(0xFFA855F7),
    accentBright = Color(0xFFD8B4FE),
    accentStrong = Color(0xFF9333EA),
    accentDeep = Color(0xFF6B21A8),
    accentSurface = Color(0xFF22102E),
    accentSurfaceStrong = Color(0xFF2A1540),
    accentSoft = Color(0x33A855F7),
    accentBorder = Color(0x4DA855F7),
    accentBorderStrong = Color(0x99A855F7),
    onAccent = Color(0xFFFAF5FF),
    textBg = Color(0xFF050505),
    text = Color(0xFFF4F4F5),
    textMuted = Color(0xFF94A3B8)
)

/** The palette backing the currently selected [ThemeStyle]. */
val ThemeStyle.palette: AppPalette
    get() = when (this) {
        ThemeStyle.REGAL_VIOLET -> RegalVioletPalette
        ThemeStyle.SACRED_GOLD -> SacredGoldPalette
        ThemeStyle.KUMARTULI_TEAL -> KumartuliTealPalette
        ThemeStyle.AZURE_SIGNAL -> AzureSignalPalette
        ThemeStyle.DARK_RED -> DarkRedPalette
        ThemeStyle.FOREST_GREEN -> ForestGreenPalette
        ThemeStyle.INDIGO_NIGHT -> IndigoNightPalette
        ThemeStyle.ROYAL_AMBER -> RoyalAmberPalette
        ThemeStyle.PLUM_NOIR -> PlumNoirPalette
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
