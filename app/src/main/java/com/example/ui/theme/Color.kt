package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Obsidian & Noir Canvas
val ObsidianNoir = Color(0xFF050505)
val ObsidianBase = Color(0xFF0E0E12)
val ObsidianRaised = Color(0xFF16161D)
val ObsidianHighlight = Color(0xFF1E1E28)
// Neutral zinc hairlines (pure white at low alpha) — deliberately untinted so every theme
// shares the same structural outline instead of picking up a stray brand hue.
val ObsidianBorder = Color(0x1FFFFFFF) // rgba(255, 255, 255, 0.12)
val ObsidianBorderSubtle = Color(0x14FFFFFF) // rgba(255, 255, 255, 0.08)

// Regal Electric Violet & Accents
val RegalElectricViolet = Color(0xFF3A0CA3)
val RegalVioletHover = Color(0xFF4A15BE)
val VibrantIris = Color(0xFF8B5CF6)
val PaleLavender = Color(0xFFC4B5FD)
val RegalLavenderLight = Color(0xFFCBBEFF)
val DeepVioletMuted = Color(0xFF241442)

// Sacred Divine & Thematic Accents
val SacredGold = Color(0xFFE5A93B)
val SacredGoldLight = Color(0xFFE6B76C)
val KumartuliTeal = Color(0xFF2DD4BF)
val CyanGlow = Color(0xFF00F0FF)

// Alerts & States
val EmergencyRose = Color(0xFFF43F5E)
val EmergencyRoseBg = Color(0x33F43F5E)
val StatusEmerald = Color(0xFF10B981)

// Typography
val TextHighWhite = Color(0xFFF8FAFC)
val TextSoftZinc = Color(0xFF94A3B8)
// 5.4:1 against the Obsidian canvas — AA for small text, so genuinely muted copy stays readable.
val TextMutedZinc = Color(0xFF7C8493)
