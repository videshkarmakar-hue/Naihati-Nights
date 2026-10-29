package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AppSettings
import com.example.model.NavigationMode
import com.example.model.ThemeStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "naihati_settings")

class SettingsDataStore(private val context: Context) {

    private object Keys {
        val QUEUE_ESTIMATOR = booleanPreferencesKey("queue_estimator")
        val AVOID_CONGESTED = booleanPreferencesKey("avoid_congested")
        val OFFLINE_CORRIDORS = booleanPreferencesKey("offline_corridors")
        val GPS_HIGH_PRECISION = booleanPreferencesKey("gps_high_precision")
        val GRAND_BUDGET = booleanPreferencesKey("grand_budget")
        val TRADITIONAL_SABEKI = booleanPreferencesKey("traditional_sabeki")
        val CHANDANNAGAR = booleanPreferencesKey("chandannagar")
        val SHOW_AMENITIES = booleanPreferencesKey("show_amenities")
        val NAV_MODE = stringPreferencesKey("nav_mode")
        val OLED_BLACK = booleanPreferencesKey("oled_black")
        val NIGHT_GLARE = booleanPreferencesKey("night_glare")
        val MOTION_REDUCTION = booleanPreferencesKey("motion_reduction")
        val HAPTIC = booleanPreferencesKey("haptic")
        val ACCENT_HUE = stringPreferencesKey("accent_hue")
        val TEXT_SCALE = intPreferencesKey("text_scale")
        val THANA_BROADCASTS = booleanPreferencesKey("thana_broadcasts")
        val EMERGENCY_SOS = booleanPreferencesKey("emergency_sos")
        val BHOG_WATER = booleanPreferencesKey("bhog_water")
        val ANONYMOUS_TRAFFIC = booleanPreferencesKey("anonymous_traffic")
        val COMMUNITY_REPORTING = booleanPreferencesKey("community_reporting")
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    }

    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            queueEstimator = prefs[Keys.QUEUE_ESTIMATOR] ?: true,
            avoidCongestedAlleys = prefs[Keys.AVOID_CONGESTED] ?: true,
            offlineCorridors = prefs[Keys.OFFLINE_CORRIDORS] ?: true,
            gpsHighPrecision = prefs[Keys.GPS_HIGH_PRECISION] ?: true,
            grandBudgetPandals = prefs[Keys.GRAND_BUDGET] ?: true,
            traditionalSabeki = prefs[Keys.TRADITIONAL_SABEKI] ?: true,
            chandannagarIllumination = prefs[Keys.CHANDANNAGAR] ?: true,
            showAmenitiesOverlay = prefs[Keys.SHOW_AMENITIES] ?: true,
            navigationMode = prefs[Keys.NAV_MODE]?.let { name ->
                runCatching { NavigationMode.valueOf(name) }.getOrNull()
            } ?: NavigationMode.IN_APP,
            oledTrueBlack = prefs[Keys.OLED_BLACK] ?: true,
            nightGlareDimmer = prefs[Keys.NIGHT_GLARE] ?: true,
            motionReduction = prefs[Keys.MOTION_REDUCTION] ?: false,
            hapticFeedback = prefs[Keys.HAPTIC] ?: true,
            themeStyle = prefs[Keys.ACCENT_HUE]?.let { name ->
                runCatching { ThemeStyle.valueOf(name) }.getOrNull()
            } ?: ThemeStyle.KUMARTULI_TEAL,
            textLegibilityScale = prefs[Keys.TEXT_SCALE] ?: 1,
            thanaFlashBroadcasts = prefs[Keys.THANA_BROADCASTS] ?: true,
            emergencySosBeacon = prefs[Keys.EMERGENCY_SOS] ?: true,
            bhogAndWaterHubs = prefs[Keys.BHOG_WATER] ?: true,
            anonymousFootTraffic = prefs[Keys.ANONYMOUS_TRAFFIC] ?: true,
            communityReportingSync = prefs[Keys.COMMUNITY_REPORTING] ?: true
        )
    }

    val onboardingCompleteFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.ONBOARDING_COMPLETE] ?: false
    }

    suspend fun updateSettings(settings: AppSettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.QUEUE_ESTIMATOR] = settings.queueEstimator
            prefs[Keys.AVOID_CONGESTED] = settings.avoidCongestedAlleys
            prefs[Keys.OFFLINE_CORRIDORS] = settings.offlineCorridors
            prefs[Keys.GPS_HIGH_PRECISION] = settings.gpsHighPrecision
            prefs[Keys.GRAND_BUDGET] = settings.grandBudgetPandals
            prefs[Keys.TRADITIONAL_SABEKI] = settings.traditionalSabeki
            prefs[Keys.CHANDANNAGAR] = settings.chandannagarIllumination
            prefs[Keys.SHOW_AMENITIES] = settings.showAmenitiesOverlay
            prefs[Keys.NAV_MODE] = settings.navigationMode.name
            prefs[Keys.OLED_BLACK] = settings.oledTrueBlack
            prefs[Keys.NIGHT_GLARE] = settings.nightGlareDimmer
            prefs[Keys.MOTION_REDUCTION] = settings.motionReduction
            prefs[Keys.HAPTIC] = settings.hapticFeedback
            prefs[Keys.ACCENT_HUE] = settings.themeStyle.name
            prefs[Keys.TEXT_SCALE] = settings.textLegibilityScale
            prefs[Keys.THANA_BROADCASTS] = settings.thanaFlashBroadcasts
            prefs[Keys.EMERGENCY_SOS] = settings.emergencySosBeacon
            prefs[Keys.BHOG_WATER] = settings.bhogAndWaterHubs
            prefs[Keys.ANONYMOUS_TRAFFIC] = settings.anonymousFootTraffic
            prefs[Keys.COMMUNITY_REPORTING] = settings.communityReportingSync
        }
    }

    suspend fun resetSettings() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    suspend fun setOnboardingComplete(complete: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_COMPLETE] = complete
        }
    }
}
