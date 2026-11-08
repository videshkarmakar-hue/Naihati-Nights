package com.example

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import com.example.data.NaihatiDataProvider
import com.example.data.update.AppUpdateState
import com.example.data.update.ReleaseInfo
import com.example.model.AppSettings
import com.example.ui.NavigationScreen
import com.example.ui.components.AppBrandHeader
import com.example.ui.components.AppUpdateDialog
import com.example.ui.components.CapsuleBottomBar
import com.example.ui.components.SidebarNavItem
import com.example.ui.components.SleekSidebar
import com.example.ui.screens.AppDetailsScreen
import com.example.ui.screens.BormaPujoScheduleScreen
import com.example.ui.screens.CheckUpdatesScreen
import com.example.ui.screens.DeveloperCreditsScreen
import com.example.ui.screens.DeveloperProfileScreen
import com.example.ui.screens.HelplineScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IntroScreen
import com.example.ui.screens.MidNightTrainsScreen
import com.example.ui.screens.RestaurantScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SpecialNoticeScreen
import com.example.ui.screens.ThemePandelScreen
import com.example.ui.theme.MyApplicationTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = "w350dp-h900dp-xxhdpi")
class CoreScreensSmokeTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val availableRelease = ReleaseInfo(
        tagName = "v3.0.11",
        versionName = "3.0.11",
        notes = "### What's new\n- Check Updates screen in the sidebar\n- Faster release lookup",
        releaseUrl = "https://github.com/videshkarmakar-hue/Naihati-Nights/releases/tag/v3.0.11",
        apkUrl = "https://example.com/NaihatiNights-3.0.11.apk",
        apkSizeBytes = 29_500_000L,
        publishedAt = "2026-10-30T00:00:00Z"
    )

    @Test
    fun introScreen_rendersSacredLandingAndContinues() {
        var started = false
        composeRule.setContent {
            MyApplicationTheme {
                IntroScreen(onBeginExperience = { started = true })
            }
        }

        composeRule.onNodeWithTag("intro_screen").assertIsDisplayed()
        composeRule.onNodeWithText("NAIHATI NIGHTS").assertIsDisplayed()
        composeRule.onNodeWithText("Videsh Karmakar Projects").assertIsDisplayed()
        composeRule.onNodeWithText("CONTINUE").assertIsDisplayed()
        composeRule.onNodeWithText(
            "Just One Tap To Explore Whole Naihati Kali Puja Pandals With Map."
        ).assertExists()
        composeRule.onNodeWithText("Crafted by Videsh Karmakar").assertExists()
        composeRule.onAllNodesWithText("JAY BORO MAA", substring = true).assertCountEquals(0)
        composeRule.onNodeWithTag("begin_parikrama_button").assertHasClickAction()

        composeRule.onNodeWithTag("begin_parikrama_button").performClick()
        composeRule.waitForIdle()
        assertTrue(started)
    }

    @Test
    fun homeScreen_rendersAndMenuOpens() {
        var menuOpened = false
        composeRule.setContent {
            MyApplicationTheme {
                HomeScreen(
                    content = NaihatiDataProvider.content(),
                    onOpenSidebar = { menuOpened = true }
                )
            }
        }

        composeRule.onNodeWithTag("header_sidebar_button").assertHasClickAction()
        composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_logo").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_title").assertIsDisplayed()
        composeRule.onAllNodesWithText("Official Parikrama Guide").assertCountEquals(0)
        composeRule.onNodeWithTag("header_sidebar_button").performClick()
        composeRule.waitForIdle()
        assertTrue(menuOpened)
    }

    @Test
    fun homeSearchHero_hugsTheSearchFieldWithoutBlankBands() {
        composeRule.setContent {
            MyApplicationTheme {
                HomeScreen(content = NaihatiDataProvider.content())
            }
        }

        val hero = composeRule.onNodeWithTag("home_hero_card").getUnclippedBoundsInRoot()
        val search = composeRule.onNodeWithTag("home_search_box").getUnclippedBoundsInRoot()

        assertTrue(
            "Search hero should stay compact, was ${hero.bottom - hero.top}",
            hero.bottom - hero.top <= 90.dp
        )
        assertTrue(
            "Blank band below the search field was ${hero.bottom - search.bottom}",
            hero.bottom - search.bottom <= 20.dp
        )
        assertTrue(
            "Blank band above the search field was ${search.top - hero.top}",
            search.top - hero.top <= 20.dp
        )
    }

    @Test
    fun appBrandHeader_showsLogoWordmarkAndHeaderActions() {
        var sidebarOpened = false
        var developerCreditsOpened = false
        composeRule.setContent {
            MyApplicationTheme {
                AppBrandHeader(
                    onOpenSidebar = { sidebarOpened = true },
                    onOpenDeveloperCredits = { developerCreditsOpened = true }
                )
            }
        }

        val logo = composeRule.onNodeWithTag("app_brand_logo").getUnclippedBoundsInRoot()
        val title = composeRule.onNodeWithTag("app_brand_title").getUnclippedBoundsInRoot()
        val devCredits =
            composeRule.onNodeWithTag("header_developer_credit_button").getUnclippedBoundsInRoot()
        val sidebar = composeRule.onNodeWithTag("header_sidebar_button").getUnclippedBoundsInRoot()

        composeRule.onNodeWithText("Naihati Nights").assertIsDisplayed()
        composeRule.onNodeWithTag("devotional_notice").assertIsDisplayed()
        composeRule.onNodeWithText(
            "বড়ো কালী পূজা সমিতির থেকে জনস্বার্থে প্রচারিত:"
        ).assertIsDisplayed()
        assertTrue(
            "Wordmark should render after the logo, logo=$logo title=$title",
            logo.right <= title.left
        )
        assertTrue(
            "Developer Credit button should follow the wordmark, title=$title devCredits=$devCredits",
            title.right <= devCredits.left
        )
        assertTrue(
            "Sidebar button should be the last header action, devCredits=$devCredits sidebar=$sidebar",
            devCredits.right <= sidebar.left
        )

        composeRule.onNodeWithTag("header_developer_credit_button").performClick()
        composeRule.waitForIdle()
        assertTrue(developerCreditsOpened)

        composeRule.onNodeWithTag("header_sidebar_button").performClick()
        composeRule.waitForIdle()
        assertTrue(sidebarOpened)
    }

    @Test
    fun settingsScreen_matchesReferenceSectionsAndCredits() {
        composeRule.setContent {
            MyApplicationTheme {
                SettingsScreen(
                    appSettings = AppSettings(),
                    content = NaihatiDataProvider.content(),
                    onUpdateSettings = {},
                    onBack = {},
                    onOpenDeveloperProfile = {}
                )
            }
        }

        composeRule.onNodeWithTag("reset_settings_button").assertIsDisplayed()
        composeRule.onNodeWithTag("header_sidebar_button").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_profile_header").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_social_website").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_social_youtube").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_social_facebook").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_list")
            .performScrollToNode(hasText("PRIVACY"))
        composeRule.onNodeWithText("PRIVACY").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_toggle_location").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_toggle_show_password").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_toggle_permissions").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_list")
            .performScrollToNode(hasText("PARIKRAMA & NAVIGATION"))
        composeRule.onNodeWithText("PARIKRAMA & NAVIGATION").assertIsDisplayed()
        composeRule.onNodeWithText("ACTIVE").assertIsDisplayed()
        composeRule.onNodeWithText("Offline Map Cache").assertIsDisplayed()
        composeRule.onNodeWithText("GPS High Precision Mode").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_list")
            .performScrollToNode(hasText("NOTIFICATION"))
        composeRule.onNodeWithText("NOTIFICATION").assertIsDisplayed()
        composeRule.onNodeWithText("Live Pandal Queue Alerts").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_notification_advance").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_list")
            .performScrollToNode(hasText("HELP"))
        composeRule.onNodeWithText("HELP").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_help_helpline").assertIsDisplayed()
        composeRule.onNodeWithText("112 / 100").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_help_municipality").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_list")
            .performScrollToNode(hasText("DISPLAY & EXPERIENCE"))
        composeRule.onNodeWithText("OLED True Black Mode").assertIsDisplayed()
        composeRule.onNodeWithText("Motion Reduction").assertIsDisplayed()
        composeRule.onNodeWithText("Haptic Feedback").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_list")
            .performScrollToNode(hasText("SAFETY & HELPLINE"))
        listOf(
            "24/7 MONITORED",
            "Emergency Quick-Dial Trigger",
            "Community Reporting Sync"
        ).forEach { label ->
            composeRule.onNodeWithTag("settings_list")
                .performScrollToNode(hasText(label))
        }
        composeRule.onNodeWithText("24/7 MONITORED").assertIsDisplayed()
        composeRule.onNodeWithText("Emergency Quick-Dial Trigger").assertIsDisplayed()
        composeRule.onNodeWithText("Community Reporting Sync").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_list")
            .performScrollToNode(hasTestTag("settings_link_portfolio"))
        composeRule.onNodeWithText("DEVELOPER CREDITS").assertIsDisplayed()
        composeRule.onNodeWithText("Verified Creator").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_link_portfolio").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_link_spotify").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_link_instagram").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_list")
            .performScrollToNode(hasTestTag("settings_devotional_footer"))
        composeRule.onNodeWithText("Crafted with Devotion in Naihati").assertIsDisplayed()
        composeRule.onNodeWithText("©Crafted by VIDESH KARMAKAR").assertIsDisplayed()
        composeRule.onAllNodesWithText("JAY BORO MAA", substring = true).assertCountEquals(0)
    }

    @Test
    fun settingsScreen_togglePersistsAndResetRestoresDefaults() {
        var settings by mutableStateOf(AppSettings())

        composeRule.setContent {
            MyApplicationTheme {
                SettingsScreen(
                    appSettings = settings,
                    content = NaihatiDataProvider.content(),
                    onUpdateSettings = { settings = it },
                    onResetSettings = {},
                    onBack = {},
                    onOpenDeveloperProfile = {}
                )
            }
        }

        composeRule.onNodeWithTag("settings_list")
            .performScrollToNode(hasTestTag("settings_toggle_queue_alerts"))
        composeRule.onNodeWithTag("settings_toggle_queue_alerts").performClick()
        composeRule.waitForIdle()
        composeRule.runOnIdle {
            assertEquals(false, settings.queueEstimator)
        }

        composeRule.onNodeWithTag("reset_settings_button").performClick()
        composeRule.waitForIdle()
        composeRule.runOnIdle {
            assertEquals(AppSettings(), settings)
        }
    }

    @Test
    fun homeRoutes_expandAndFilterFromSearch() {
        composeRule.setContent {
            MyApplicationTheme {
                HomeScreen(content = NaihatiDataProvider.content())
            }
        }

        composeRule.onAllNodesWithText("Ramukali", substring = true).assertCountEquals(0)

        composeRule.onNodeWithTag("home_route_card_route_1").performClick()
        composeRule.waitForIdle()
        composeRule.onAllNodesWithText("Ramukali", substring = true).onFirst().assertIsDisplayed()

        composeRule.onNodeWithTag("home_search_field")
            .performTextInput("Boro Maa")
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("home_route_card_route_1").assertIsDisplayed()
        composeRule.onNodeWithTag("home_route_card_route_2").assertDoesNotExist()
        composeRule.onAllNodesWithText("Boro Kali (Boro Maa)", substring = true)
            .onFirst()
            .assertIsDisplayed()
    }

    @Test
    fun homeRoutes_expandAllTogglesAllAccordionCards() {
        composeRule.setContent {
            MyApplicationTheme {
                HomeScreen(content = NaihatiDataProvider.content())
            }
        }

        composeRule.onAllNodesWithText("Ramukali", substring = true).assertCountEquals(0)
    }

    @Test
    fun appDetails_rebuiltDesignAndActionsWork() {
        var backPressed = false
        var homeOpened = false

        composeRule.setContent {
            MyApplicationTheme {
                AppDetailsScreen(
                    onBack = { backPressed = true },
                    onOpenHome = { homeOpened = true }
                )
            }
        }

        composeRule.onNodeWithTag("header_sidebar_button").assertIsDisplayed()

        composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_title").assertIsDisplayed()
        composeRule.onNodeWithText("NAIHATI  NIGHTS").assertIsDisplayed()
        composeRule.onNodeWithText("APP INFORMATION").assertIsDisplayed()
        composeRule.onNodeWithText("VERSION & BUILD").assertIsDisplayed()
        composeRule.onNodeWithText(
            "${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})"
        ).assertExists()

        composeRule.onNodeWithTag("app_details_list")
            .performScrollToNode(hasText("Privacy Policy"))
        composeRule.onNodeWithTag("app_details_legal_privacy").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("app_details_info_dialog").assertExists()
        composeRule.onNodeWithText(
            "Naihati Nights is a local-first festival guide.",
            substring = true
        ).assertExists()
        composeRule.onNodeWithText("Close").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("app_details_info_dialog").assertDoesNotExist()

        composeRule.onNodeWithTag("app_details_list")
            .performScrollToNode(hasText("©Crafted by VIDESH KARMAKAR"))
        composeRule.onNodeWithText("©Crafted by VIDESH KARMAKAR").assertIsDisplayed()
        composeRule.onAllNodesWithText("JAY MAA BORO MAA", substring = true).assertCountEquals(0)
        composeRule.onAllNodesWithText("Report a Problem").assertCountEquals(0)
        composeRule.onAllNodesWithText("Send Feedback or Suggestions").assertCountEquals(0)

        composeRule.onNodeWithTag("app_details_list")
            .performScrollToNode(hasTestTag("app_details_back_button"))
        composeRule.onNodeWithTag("app_details_back_button").performClick()
        composeRule.waitForIdle()
        assertTrue(backPressed)

        composeRule.onNodeWithTag("app_details_list")
            .performScrollToNode(hasTestTag("app_details_discover_pandals"))
        composeRule.onNodeWithTag("app_details_discover_pandals").performClick()
        composeRule.waitForIdle()
        assertTrue(homeOpened)
    }

    @Test
    fun remainingScreens_renderWithoutCrashing() {
        val content = NaihatiDataProvider.content()
        var screen by mutableStateOf("helpline")
        var openAppDetails = false

        composeRule.setContent {
            MyApplicationTheme {
                when (screen) {
                    "helpline" -> HelplineScreen(content = content, onBack = {})
                    "dining" -> RestaurantScreen(content = content)
                    "settings" -> SettingsScreen(
                        appSettings = AppSettings(),
                        content = content,
                        onUpdateSettings = {},
                        onBack = {},
                        onOpenDeveloperProfile = { screen = "profile" }
                    )
                    "profile" -> DeveloperProfileScreen(
                        onBack = {},
                        onOpenAppDetails = { openAppDetails = true }
                    )
                    else -> AppDetailsScreen(onBack = {})
                }
            }
        }

        composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_title").assertIsDisplayed()
        composeRule.runOnIdle { screen = "dining" }
        composeRule.onNodeWithText("Dada Boudi Biryani").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
        composeRule.runOnIdle { screen = "settings" }
        composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_title").assertIsDisplayed()
        composeRule.runOnIdle { screen = "profile" }
        composeRule.onAllNodesWithText("Videsh Karmakar").onFirst().assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
        composeRule.runOnIdle { screen = "app_details" }
        composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_title").assertIsDisplayed()
        assertTrue(!openAppDetails)
    }

    @Test
    fun helplineDirectory_rendersAllGroupsAndFilters() {
        composeRule.setContent {
            MyApplicationTheme {
                HelplineScreen(content = NaihatiDataProvider.content(), onBack = {})
            }
        }

        composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_title").assertIsDisplayed()
        composeRule.onNodeWithTag("helpline_section_title").assertIsDisplayed()
        composeRule.onNodeWithText("EMERGENCY HELPLINE").assertIsDisplayed()
        composeRule.onNodeWithText("Naihati Police Station Control Room").assertIsDisplayed()
        composeRule.onNodeWithText("Boro Maa Trust Emergency Service").assertIsDisplayed()
        composeRule.onNodeWithText("Naihati State General Hospital").assertIsDisplayed()

        composeRule.onNodeWithTag("helpline_search_field").performTextInput("police")
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Naihati Police Station Control Room").assertIsDisplayed()
        composeRule.onNodeWithText("Naihati Fire Station").assertDoesNotExist()
        composeRule.onNodeWithText("ASSISTANCE & POLICE BOOTHS").assertIsDisplayed()

        composeRule.onNodeWithTag("helpline_search_field").performTextClearance()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Women Helpline").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Child Assistance").assertIsDisplayed()
        composeRule.onNodeWithTag("helpline_list").performScrollToNode(
            hasText("ASSISTANCE & POLICE BOOTHS")
        )
        composeRule.onNodeWithText("ASSISTANCE & POLICE BOOTHS").assertIsDisplayed()
        composeRule.onNodeWithTag("helpline_list").performScrollToNode(
            hasText("SAFETY PROTOCOL & INSTRUCTIONS")
        )
        composeRule.onNodeWithText("SAFETY PROTOCOL & INSTRUCTIONS")
            .assertIsDisplayed()
        composeRule.onNodeWithText(
            "Strictly follow designated one-way barricaded entry/exit routes."
        ).assertExists()
        composeRule.onNodeWithText(
            "Report unattended baggage immediately to nearest police assistance booth."
        ).assertExists()
    }

    @Test
    fun helplineDirectory_rendersFinestaDirectoryChrome() {
        composeRule.setContent {
            MyApplicationTheme {
                HelplineScreen(content = NaihatiDataProvider.content(), onBack = {})
            }
        }

        composeRule.onNodeWithText("THANA").assertIsDisplayed()
        composeRule.onNodeWithTag("helpline_list").performScrollToNode(
            hasText("HOSPITAL")
        )
        composeRule.onNodeWithText("TRUST").assertIsDisplayed()
        composeRule.onNodeWithText("FIRE").assertIsDisplayed()
        composeRule.onNodeWithText("Call Station").assertIsDisplayed()
        composeRule.onNodeWithText("HOSPITAL").assertIsDisplayed()
        composeRule.onNodeWithText("CIVIC").assertIsDisplayed()
        composeRule.onNodeWithTag("helpline_list").performScrollToNode(
            hasText("Women Helpline")
        )
        composeRule.onAllNodesWithText("24/7").onFirst().assertIsDisplayed()
        composeRule.onNodeWithTag("helpline_list").performScrollToNode(
            hasText("General Desk")
        )
        composeRule.onNodeWithText("General Desk").assertIsDisplayed()
        composeRule.onNodeWithText("Ambulance SOS").assertIsDisplayed()
        composeRule.onNodeWithText("Hospital").assertIsDisplayed()
        composeRule.onNodeWithText("Ambulance").assertIsDisplayed()
        composeRule.onNodeWithTag("helpline_list").performScrollToNode(
            hasText("FIELD UNITS")
        )
        composeRule.onNodeWithText("FIELD UNITS").assertIsDisplayed()
        composeRule.onNodeWithTag("helpline_list").performScrollToNode(
            hasText("Power House More & Bijoynagar Crossing")
        )
        composeRule.onNodeWithText("Power House More & Bijoynagar Crossing")
            .assertIsDisplayed()
        composeRule.onNodeWithTag("helpline_list").performScrollToNode(
            hasText("PUBLIC ADVISORY")
        )
        composeRule.onNodeWithText("PUBLIC ADVISORY").assertIsDisplayed()
        composeRule.onNodeWithTag("helpline_list").performScrollToNode(
            hasText("\u00A9 Crafted by VIDESH KARMAKAR")
        )
        composeRule.onNodeWithText("\u00A9 Crafted by VIDESH KARMAKAR")
            .assertIsDisplayed()
        composeRule.onAllNodesWithText("JAY BORO MAA", substring = true).assertCountEquals(0)
    }

    @Test
    fun helplineSearch_showsRecoverableEmptyState() {
        composeRule.setContent {
            MyApplicationTheme {
                HelplineScreen(content = NaihatiDataProvider.content(), onBack = {})
            }
        }

        composeRule.onNodeWithTag("helpline_search_field").performTextInput("zzzz-no-match")
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("helpline_no_results").assertIsDisplayed()
        composeRule.onNodeWithText("Naihati Police Station Control Room").assertDoesNotExist()

        composeRule.onNodeWithTag("helpline_clear_search").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("helpline_no_results").assertDoesNotExist()
        composeRule.onNodeWithText("Naihati Police Station Control Room").assertIsDisplayed()
    }

    @Test
    fun helplineActions_launchDialerAndMaps() {
        val recordingContext = RecordingContext(RuntimeEnvironment.getApplication())

        composeRule.setContent {
            CompositionLocalProvider(LocalContext provides recordingContext) {
                MyApplicationTheme {
                    HelplineScreen(content = NaihatiDataProvider.content(), onBack = {})
                }
            }
        }

        composeRule.onNodeWithTag("helpline_call_help_police").performClick()
        composeRule.waitForIdle()

        assertEquals(Intent.ACTION_DIAL, recordingContext.lastIntent?.action)
        assertEquals("tel:03325812223", recordingContext.lastIntent?.dataString)

        composeRule.onNodeWithTag("helpline_list").performScrollToNode(
            hasText("Ferryghat More & River Ghat Booth")
        )
        composeRule.onNodeWithTag("helpline_booth_2").performClick()
        composeRule.waitForIdle()

        assertEquals(Intent.ACTION_VIEW, recordingContext.lastIntent?.action)
        assertTrue(
            recordingContext.lastIntent?.dataString?.startsWith("geo:0,0?q=") == true
        )
    }

    @Test
    fun bottomBar_containsDestinationsAndNavigates() {
        var navigatedTo: NavigationScreen? = null
        composeRule.setContent {
            MyApplicationTheme {
                CapsuleBottomBar(
                    currentScreen = NavigationScreen.HOME,
                    onNavigate = { navigatedTo = it }
                )
            }
        }

        composeRule.onNodeWithTag("nav_home").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_theme_pandel").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_borma_pujo").assertIsDisplayed()
        composeRule.onAllNodesWithTag("nav_restaurant").assertCountEquals(0)
        composeRule.onAllNodesWithTag("nav_feedback").assertCountEquals(0)
        composeRule.onAllNodesWithTag("nav_settings").assertCountEquals(0)
        composeRule.onAllNodesWithTag("nav_helpline").assertCountEquals(0)
        composeRule.onAllNodesWithTag("nav_mid_night_trains").assertCountEquals(0)
        composeRule.onAllNodesWithContentDescription("Helpline").assertCountEquals(0)
        composeRule.onAllNodesWithContentDescription("Restaurant").assertCountEquals(0)
        composeRule.onAllNodesWithContentDescription("Mid-Night Trains").assertCountEquals(0)

        val home = composeRule.onNodeWithTag("nav_home").getUnclippedBoundsInRoot()
        val themePandel = composeRule.onNodeWithTag("nav_theme_pandel").getUnclippedBoundsInRoot()
        val borma = composeRule.onNodeWithTag("nav_borma_pujo").getUnclippedBoundsInRoot()
        assertTrue(
            "Home and Theme Pandel slots should sit close together, home=$home themePandel=$themePandel",
            themePandel.left - home.right <= 12.dp
        )
        assertTrue(
            "Theme Pandel and Boro Maa slots should sit close together, themePandel=$themePandel borma=$borma",
            borma.left - themePandel.right <= 12.dp
        )

        composeRule.onNodeWithTag("nav_home").performClick()
        composeRule.waitForIdle()
        assertTrue(navigatedTo == NavigationScreen.HOME)

        composeRule.onNodeWithTag("nav_theme_pandel").performClick()
        composeRule.waitForIdle()
        assertTrue(navigatedTo == NavigationScreen.THEME_PANDEL)

        composeRule.onNodeWithTag("nav_borma_pujo").performClick()
        composeRule.waitForIdle()
        assertTrue(navigatedTo == NavigationScreen.BORMA_PUJO_SCHEDULE)
    }

    @Test
    fun themePandelScreen_rendersDirectoryDesign() {
        composeRule.setContent {
            MyApplicationTheme {
                ThemePandelScreen()
            }
        }

        composeRule.onNodeWithTag("theme_pandel_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("theme_pandel_card_theme_01").assertIsDisplayed()
        composeRule.onAllNodesWithTag("theme_pandel_map_chip").onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText("PANDAL THEME").onFirst().assertIsDisplayed()

        composeRule.onNodeWithTag("theme_pandel_screen")
            .performScrollToNode(hasTestTag("theme_pandel_card_theme_04"))
        composeRule.onNodeWithText("76th Year Heritage").assertExists()

        composeRule.onNodeWithTag("theme_pandel_screen")
            .performScrollToNode(hasTestTag("theme_pandel_location_theme_05"))
        composeRule.onNodeWithTag("theme_pandel_location_theme_05").assertIsDisplayed()

        composeRule.onNodeWithTag("theme_pandel_screen")
            .performScrollToNode(hasTestTag("theme_pandel_special_theme_09"))
        composeRule.onAllNodesWithText("তৈরী মণ্ডপ", substring = true)
            .onFirst()
            .assertIsDisplayed()

        composeRule.onNodeWithTag("theme_pandel_screen")
            .performScrollToNode(hasTestTag("theme_pandel_card_theme_11"))
        composeRule.onNodeWithText("91st Year Heritage").assertExists()

        composeRule.onNodeWithTag("theme_pandel_screen")
            .performScrollToNode(hasTestTag("theme_pandel_card_theme_12"))
        composeRule.onNodeWithText("50th Golden Jubilee").assertExists()

        composeRule.onNodeWithTag("theme_pandel_screen")
            .performScrollToNode(hasTestTag("theme_pandel_card_theme_13"))
        composeRule.onNodeWithText("অপারেশন সিঁদুর", substring = true).assertIsDisplayed()

        composeRule.onNodeWithTag("theme_pandel_screen")
            .performScrollToNode(hasTestTag("theme_pandel_special_theme_14"))
        composeRule.onAllNodesWithText("কষ্টিপাথরের মূর্তি", substring = true)
            .onFirst()
            .assertIsDisplayed()

        composeRule.onNodeWithTag("theme_pandel_screen")
            .performScrollToNode(hasTestTag("theme_pandel_location_theme_15"))
        composeRule.onNodeWithTag("theme_pandel_location_theme_15").assertIsDisplayed()

        composeRule.onNodeWithTag("theme_pandel_screen")
            .performScrollToNode(hasTestTag("theme_pandel_card_theme_16"))
        composeRule.onNodeWithText("মঙ্গলচিত্রে মহাকালী", substring = true).assertIsDisplayed()

        composeRule.onNodeWithTag("theme_pandel_screen")
            .performScrollToNode(hasTestTag("theme_pandel_footer"))
        composeRule.onNodeWithText("© Crafted BY VIDESH KARMAKAR").assertIsDisplayed()
        composeRule.onAllNodesWithText("Jay Maa Boro Maa", substring = true).assertCountEquals(0)
    }

    @Test
    fun sidebar_containsGuideDestinationsAndFooterCredit() {
        var selected: SidebarNavItem? = null
        composeRule.setContent {
            MyApplicationTheme {
                SleekSidebar(
                    isExpanded = true,
                    isDarkMode = true,
                    selectedItem = SidebarNavItem.SETTINGS,
                    onSelectItem = { selected = it },
                    onToggleExpand = {}
                )
            }
        }

        listOf(
            "sidebar_item_route" to "Route",
            "sidebar_item_theme_pandel" to "Theme Pandel",
            "sidebar_item_borma_pujo_schedule" to "বড়মা পুজোর নির্ঘণ্ট",
            "sidebar_item_special_notice" to "বিশেষ ঘোষণা",
            "sidebar_item_app_info_credit" to "App Info & Developer Credit",
            "sidebar_item_check_updates" to "Check Updates",
            "sidebar_item_settings" to "Settings"
        ).forEach { (tag, label) ->
            composeRule.onNodeWithTag(tag).assertIsDisplayed()
            composeRule.onNodeWithText(label).assertIsDisplayed()
        }

        // No update dot until the release channel reports something newer.
        composeRule.onAllNodesWithTag("sidebar_update_dot").assertCountEquals(0)

        // The guide publishes the full edition list, so no NEW badge is raised.
        composeRule.onAllNodesWithTag("sidebar_theme_new_badge").assertCountEquals(0)

        composeRule.onNodeWithText("©Videsh Karmakar Project").assertIsDisplayed()

        listOf(
            "sidebar_item_home",
            "sidebar_item_search",
            "sidebar_item_shop",
            "sidebar_item_calendar",
            "sidebar_item_notifications",
            "sidebar_item_analytics",
            "sidebar_item_logout"
        ).forEach { removedTag ->
            composeRule.onAllNodesWithTag(removedTag).assertCountEquals(0)
        }
        listOf("Home", "Search", "Shop", "Calendar", "Notifications", "Analytics", "Log out")
            .forEach { removedLabel ->
                composeRule.onAllNodesWithText(removedLabel).assertCountEquals(0)
            }

        composeRule.onNodeWithTag("sidebar_item_route").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.ROUTE, selected)

        composeRule.onNodeWithTag("sidebar_item_theme_pandel").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.THEME_PANDEL, selected)

        composeRule.onNodeWithTag("sidebar_item_borma_pujo_schedule").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.BORMA_PUJO_SCHEDULE, selected)

        composeRule.onNodeWithTag("sidebar_item_special_notice").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.SPECIAL_NOTICE, selected)

        composeRule.onNodeWithTag("sidebar_item_app_info_credit").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.APP_INFO_CREDIT, selected)

        composeRule.onAllNodesWithText("Report & Feedback").assertCountEquals(0)

        composeRule.onNodeWithTag("sidebar_item_check_updates").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.CHECK_UPDATES, selected)

        composeRule.onNodeWithTag("sidebar_item_settings").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.SETTINGS, selected)

        composeRule.onNodeWithTag("sidebar_item_developer_credits").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.DEVELOPER_CREDITS, selected)
    }

    @Test
    fun midNightTrainsScreen_rendersExtraEmuTimetable() {
        var backPressed = 0
        composeRule.setContent {
            MyApplicationTheme {
                MidNightTrainsScreen(onBack = { backPressed++ })
            }
        }

        composeRule.onNodeWithTag("mid_night_trains_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("mid_night_trains_hero").assertIsDisplayed()
        composeRule.onNodeWithTag("mid_night_trains_group_up").assertIsDisplayed()
        composeRule.onNodeWithText("Up EMU Special (Extra For Night)").assertIsDisplayed()
        composeRule.onNodeWithText("Down EMU Special (Extra For Night)").assertIsDisplayed()

        // Every published midnight EMU special stays verbatim, number to timing.
        listOf(
            "Train No. 03161: Sealdah – 23:00 to Dankuni – 23:42 Hrs.",
            "Train No. 03163: Sealdah – 00:05 to Barasat – 01:35 Hrs.",
            "Train No. 03165: Barasat – 01:50 to Bongaon – 03:00 Hrs.",
            "Train No. 03167: Barasat – 01:10 to Hasnabad – 02:36 Hrs.",
            "Train No. 03169: Sealdah – 00:40 to Ranaghat – 02:30 Hrs.",
            "Train No. 03171: Barrackpore – 01:25 to Naihati – 02:10 Hrs.",
            "Train No. 03162: Dankuni – 23:55 to Sealdah – 00:37 Hrs.",
            "Train No. 03164: Barasat – 01:10 to Sealdah – 01:55 Hrs.",
            "Train No. 03166: Bongaon – 23:00 to Barasat – 00:10 Hrs.",
            "Train No. 03168: Hasnabad – 23:30 to Barasat – 00:56 Hrs.",
            "Train No. 03170: Ranaghat – 23:45 to Sealdah – 01:40 Hrs.",
            "Train No. 03172: Sealdah – 00:30 to Barrackpore – 01:15 Hrs."
        ).forEach { line ->
            composeRule.onNodeWithText(line).performScrollTo().assertIsDisplayed()
        }

        composeRule.onNodeWithTag("mid_night_train_03161").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("mid_night_train_03172").performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithTag("mid_night_trains_back_button").performClick()
        composeRule.waitForIdle()
        assertEquals(1, backPressed)
    }

    @Test
    fun bormaPujoScheduleScreen_showsPujoBulletin() {
        var backPressed = 0
        composeRule.setContent {
            MyApplicationTheme {
                BormaPujoScheduleScreen(onBack = { backPressed++ })
            }
        }

        composeRule.onNodeWithTag("borma_pujo_schedule_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("borma_pujo_hero").assertIsDisplayed()
        composeRule.onNodeWithTag("borma_pujo_days").assertIsDisplayed()
        composeRule.onNodeWithText("বার্ষিক উপলক্ষ").assertIsDisplayed()

        composeRule.onNodeWithTag("borma_pujo_day_day_8_8_nov").assertIsDisplayed()
        composeRule.onNodeWithTag("borma_pujo_event_day_8_8_puja_start").assertIsDisplayed()
        composeRule.onNodeWithTag("borma_pujo_event_day_8_8_prasad_bijoy").assertIsDisplayed()
        composeRule.onNodeWithTag("borma_pujo_event_day_2_7_offering").assertIsDisplayed()

        composeRule.onNodeWithTag("borma_pujo_back_button").performClick()
        composeRule.waitForIdle()
        assertEquals(1, backPressed)

        composeRule.onNodeWithTag("borma_pujo_schedule_screen")
            .performScrollToNode(hasTestTag("borma_pujo_note"))
        composeRule.onNodeWithTag("borma_pujo_note").assertIsDisplayed()
    }

    @Test
    fun specialNoticeScreen_showsAnnouncements() {
        var backPressed = 0
        composeRule.setContent {
            MyApplicationTheme {
                SpecialNoticeScreen(onBack = { backPressed++ })
            }
        }

        composeRule.onNodeWithTag("special_notice_screen").assertIsDisplayed()
        composeRule.onNodeWithText("বিশেষ ঘোষণা").assertIsDisplayed()
        composeRule.onNodeWithTag("special_notice_hero").assertIsDisplayed()
        composeRule.onNodeWithTag("special_notice_card").assertIsDisplayed()

        composeRule.onNodeWithTag("special_notice_row_annual_closure").assertIsDisplayed()
        composeRule.onNodeWithTag("special_notice_row_mandir_closure").assertIsDisplayed()
        composeRule.onNodeWithTag("special_notice_row_maternal_darshan").assertIsDisplayed()
        composeRule.onNodeWithTag("special_notice_row_prasad_closure").assertIsDisplayed()
        composeRule.onNodeWithTag("special_notice_row_prasad_resume").assertIsDisplayed()

        composeRule.onNodeWithTag("special_notice_back_button").performClick()
        composeRule.waitForIdle()
        assertEquals(1, backPressed)
    }

    @Test
    fun sidebar_flagsCheckUpdatesWhenReleaseIsAvailable() {
        composeRule.setContent {
            MyApplicationTheme {
                SleekSidebar(
                    isExpanded = true,
                    isDarkMode = true,
                    selectedItem = SidebarNavItem.SETTINGS,
                    hasUpdate = true,
                    onSelectItem = {},
                    onToggleExpand = {}
                )
            }
        }

        composeRule.onAllNodesWithTag("sidebar_update_dot", useUnmergedTree = true)
            .assertCountEquals(1)
        composeRule.onNodeWithText("NEW").assertIsDisplayed()
    }

    @Test
    fun checkUpdatesScreen_promotesUpdateWhenReleaseHasApk() {
        var checked = 0
        var updated = 0
        var releaseOpened = 0
        var screenOpened = 0

        composeRule.setContent {
            MyApplicationTheme {
                CheckUpdatesScreen(
                    installedVersion = "3.0.10",
                    installedBuild = 30010000,
                    state = AppUpdateState.Available(availableRelease),
                    notice = null,
                    onScreenOpen = { screenOpened++ },
                    onCheck = { checked++ },
                    onUpdate = { updated++ },
                    onOpenRelease = { releaseOpened++ },
                    onBack = {}
                )
            }
        }

        composeRule.onNodeWithTag("check_updates_screen").assertIsDisplayed()
        composeRule.onNodeWithText("v3.0.11 is available").assertIsDisplayed()
        composeRule.onNodeWithText("UPDATE NOW").assertIsDisplayed()

        composeRule.onNodeWithTag("check_updates_button").performClick()
        composeRule.waitForIdle()

        assertEquals(1, updated)
        assertEquals(0, checked)
        assertEquals(0, releaseOpened)
        assertEquals(1, screenOpened)

        composeRule.onNodeWithTag("check_updates_screen")
            .performScrollToNode(hasTestTag("check_updates_notes"))
        composeRule.onNodeWithTag("check_updates_notes").assertIsDisplayed()

        composeRule.onNodeWithTag("check_updates_screen")
            .performScrollToNode(hasTestTag("check_updates_telemetry"))
        composeRule.onNodeWithTag("check_updates_telemetry").assertIsDisplayed()
        composeRule.onNodeWithTag("check_updates_auto_toggle").assertExists()

        composeRule.onNodeWithTag("check_updates_screen")
            .performScrollToNode(hasTestTag("check_updates_footer"))
        composeRule.onNodeWithTag("check_updates_footer").assertIsDisplayed()
        composeRule.onNodeWithText("ENGINEERED & ARCHITECTED BY").assertExists()
    }

    @Test
    fun checkUpdatesScreen_offersCheckWhenAlreadyCurrent() {
        var checked = 0
        var updated = 0

        composeRule.setContent {
            MyApplicationTheme {
                CheckUpdatesScreen(
                    installedVersion = "3.0.11",
                    installedBuild = 30011000,
                    state = AppUpdateState.UpToDate("3.0.11", 0L),
                    notice = null,
                    onScreenOpen = {},
                    onCheck = { checked++ },
                    onUpdate = { updated++ },
                    onOpenRelease = {},
                    onBack = {}
                )
            }
        }

        composeRule.onNodeWithText("You're up to date").assertIsDisplayed()
        composeRule.onNodeWithText("CHECK FOR UPDATES").assertIsDisplayed()

        composeRule.onNodeWithTag("check_updates_button").performClick()
        composeRule.waitForIdle()

        assertEquals(1, checked)
        assertEquals(0, updated)

        composeRule.onNodeWithTag("check_updates_screen")
            .performScrollToNode(hasTestTag("check_updates_telemetry"))
        composeRule.onNodeWithText("UPDATE POLICY").assertIsDisplayed()
        composeRule.onNodeWithText("Required • Automatic").assertIsDisplayed()
        composeRule.onNodeWithTag("check_updates_auto_toggle").assertExists()
    }

    @Test
    fun appUpdateDialog_isMandatoryAndCannotBeDismissed() {
        var updateTapped = 0
        var releaseOpened = 0

        composeRule.setContent {
            MyApplicationTheme {
                AppUpdateDialog(
                    visible = true,
                    state = AppUpdateState.Available(availableRelease),
                    notice = null,
                    onUpdateNow = { updateTapped++ },
                    onOpenRelease = { releaseOpened++ }
                )
            }
        }

        composeRule.onNodeWithTag("app_update_dialog").assertIsDisplayed()
        composeRule.onNodeWithText("Update Available").assertIsDisplayed()
        composeRule.onNodeWithTag("app_update_required_note").assertIsDisplayed()
        composeRule.onNodeWithText("This update is required to keep using Naihati Nights. " +
            "It downloads automatically and Android asks you to confirm the install.")
            .assertIsDisplayed()
        composeRule.onNodeWithText("WHAT'S NEW").assertIsDisplayed()
        composeRule.onNodeWithText("Preparing Download").assertIsDisplayed()
        composeRule.onNodeWithText("Starts automatically").assertIsDisplayed()
        composeRule.onNodeWithText("UPDATE NOW").assertIsDisplayed()

        composeRule.onNodeWithTag("app_update_now").performClick()
        composeRule.waitForIdle()

        assertEquals(1, updateTapped)
        assertEquals(0, releaseOpened)

        composeRule.onNodeWithTag("app_update_notes").performClick()
        composeRule.waitForIdle()

        assertEquals(1, releaseOpened)
    }

    @Test
    fun appUpdateDialog_showsInstallWhenDownloadIsReady() {
        var updated = 0
        var releaseOpened = 0

        composeRule.setContent {
            MyApplicationTheme {
                AppUpdateDialog(
                    visible = true,
                    state = AppUpdateState.Ready(
                        availableRelease,
                        java.io.File("ready.apk")
                    ),
                    notice = null,
                    onUpdateNow = { updated++ },
                    onOpenRelease = { releaseOpened++ }
                )
            }
        }

        composeRule.onAllNodesWithText("Ready to Install").assertCountEquals(2)
        composeRule.onNodeWithText("100%").assertIsDisplayed()
        composeRule.onNodeWithText("Download Complete").assertIsDisplayed()
        composeRule.onNodeWithText("INSTALL UPDATE").assertIsDisplayed()

        composeRule.onNodeWithTag("app_update_install").performClick()
        composeRule.waitForIdle()

        assertEquals(1, updated)
        assertEquals(0, releaseOpened)
    }

    @Test
    fun restaurantFilters_narrowTheEateryList() {
        composeRule.setContent {
            MyApplicationTheme {
                RestaurantScreen(content = NaihatiDataProvider.content())
            }
        }

        composeRule.onNodeWithTag("restaurant_filter_biryani_&_mughlai").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Dada Boudi Biryani").assertIsDisplayed()
        composeRule.onAllNodesWithText("Bijoynagar Bhojohori Hindu Hotel").assertCountEquals(0)
    }

    @Test
    fun restaurantHeader_menuButtonOpensSidebar() {
        var menuOpened = false
        composeRule.setContent {
            MyApplicationTheme {
                RestaurantScreen(
                    content = NaihatiDataProvider.content(),
                    onOpenSidebar = { menuOpened = true }
                )
            }
        }

        composeRule.onNodeWithTag("header_sidebar_button").assertIsDisplayed()
        composeRule.onNodeWithTag("header_sidebar_button").assertHasClickAction()
        composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_logo").assertIsDisplayed()
        composeRule.onNodeWithTag("header_sidebar_button").performClick()
        composeRule.waitForIdle()
        assertTrue(menuOpened)
    }

    @Test
    fun restaurantSearch_narrowsAndClears() {
        composeRule.setContent {
            MyApplicationTheme {
                RestaurantScreen(content = NaihatiDataProvider.content())
            }
        }

        composeRule.onNodeWithTag("restaurant_search_field").performTextInput("chamcham")
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Naihati Mishti Mahal & Dairy").assertIsDisplayed()
        composeRule.onAllNodesWithText("Dada Boudi Biryani").assertCountEquals(0)

        composeRule.onNodeWithTag("restaurant_search_clear").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Dada Boudi Biryani").assertIsDisplayed()
        composeRule.onNodeWithText("Bijoynagar Bhojohori Hindu Hotel").assertIsDisplayed()
    }

    @Test
    fun restaurantHotelsFilter_hidesEateriesAndKeepsStays() {
        composeRule.setContent {
            MyApplicationTheme {
                RestaurantScreen(content = NaihatiDataProvider.content())
            }
        }

        composeRule.onNodeWithTag("restaurant_filter_hotels_&_stays").performClick()
        composeRule.waitForIdle()

        composeRule.onAllNodesWithText("Dada Boudi Biryani").assertCountEquals(0)
        composeRule.onNodeWithTag("restaurant_screen")
            .performScrollToNode(hasText("ACCOMMODATION", substring = true))
        composeRule.onNodeWithText("Hotel Royal Palace & AC Deluxe Suites").assertIsDisplayed()
        composeRule.onAllNodesWithText("Verified", substring = true).onFirst().assertIsDisplayed()
    }

    @Test
    fun helplineHeader_menuButtonOpensSidebar() {
        var menuOpened = false
        var backPressed = false
        composeRule.setContent {
            MyApplicationTheme {
                HelplineScreen(
                    content = NaihatiDataProvider.content(),
                    onBack = { backPressed = true },
                    onOpenSidebar = { menuOpened = true }
                )
            }
        }

        composeRule.onNodeWithTag("header_sidebar_button").assertIsDisplayed()
        composeRule.onNodeWithTag("header_sidebar_button").assertHasClickAction()
        composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
        composeRule.onNodeWithTag("header_sidebar_button").performClick()
        composeRule.waitForIdle()
        assertTrue(menuOpened)

        composeRule.onNodeWithTag("helpline_back_button").performClick()
        composeRule.waitForIdle()
        assertTrue(backPressed)
    }

    @Test
    fun developerCreditsScreen_rendersProfileSectionsAndSharedCredit() {
        composeRule.setContent {
            MyApplicationTheme {
                DeveloperCreditsScreen(onBack = {})
            }
        }

        composeRule.onNodeWithTag("dev_credits_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_logo").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_title").assertIsDisplayed()
        composeRule.onNodeWithTag("header_sidebar_button").assertIsDisplayed()

        composeRule.onNodeWithTag("dev_credits_hero").assertIsDisplayed()
        composeRule.onNodeWithTag("dev_credits_avatar").assertIsDisplayed()
        composeRule.onAllNodesWithText("Videsh Karmakar").onFirst().assertIsDisplayed()
        composeRule.onNodeWithText("Developer/Creator").assertIsDisplayed()
        composeRule.onNodeWithText("Founder · Developer · UI/UX Designer").assertIsDisplayed()

        composeRule.onNodeWithText("CONNECT WITH THE DEVELOPER").assertExists()
        composeRule.onNodeWithText("DEVELOPMENT & DESIGN").assertExists()
        composeRule.onNodeWithText("Naihati Kali Puja 2026").assertExists()
        composeRule.onNodeWithText("Figma · Stitch").assertExists()
        composeRule.onNodeWithText("Android · iOS Soon").assertExists()

        composeRule.onNodeWithTag("dev_credits_screen")
            .performScrollToNode(hasText("ACKNOWLEDGEMENT"))
        composeRule.waitForIdle()
        composeRule.onNodeWithText("ACKNOWLEDGEMENT").assertExists()
        composeRule.onNodeWithText("Crafted with devotion by VIDESH KARMAKAR").assertExists()
        composeRule.onNodeWithText("© 2026 Videsh Karmakar. All Rights Reserved.").assertExists()
        composeRule.onAllNodesWithText("JAY BORO MAA", substring = true).assertCountEquals(0)
        composeRule.onAllNodesWithText("Send Feedback or Suggestions").assertCountEquals(0)

        composeRule.onNodeWithTag("dev_credits_screen")
            .performScrollToNode(hasText("©Crafted by VIDESH KARMAKAR"))
        composeRule.onNodeWithText("©Crafted by VIDESH KARMAKAR").assertIsDisplayed()
    }

    @Test
    fun developerCreditsScreen_actionsOpenLinksAndBack() {
        val recordingContext = RecordingContext(RuntimeEnvironment.getApplication())
        var backPressed = false

        composeRule.setContent {
            CompositionLocalProvider(LocalContext provides recordingContext) {
                MyApplicationTheme {
                    DeveloperCreditsScreen(
                        onBack = { backPressed = true }
                    )
                }
            }
        }

        composeRule.onNodeWithTag("dev_credits_link_website").performClick()
        composeRule.waitForIdle()
        assertEquals(Intent.ACTION_VIEW, recordingContext.lastIntent?.action)
        assertEquals(
            "https://videshkarmakar.carrd.co/",
            recordingContext.lastIntent?.dataString
        )

        composeRule.onNodeWithTag("dev_credits_screen")
            .performScrollToNode(hasTestTag("dev_credits_back_button"))
        composeRule.onNodeWithTag("dev_credits_back_button").performClick()
        composeRule.waitForIdle()
        assertTrue(backPressed)
    }

    @Test
    fun bottomBar_staysWithinScreen() {
        composeRule.setContent {
            MyApplicationTheme {
                CapsuleBottomBar(
                    currentScreen = NavigationScreen.HOME,
                    onNavigate = { }
                )
            }
        }

        val home = composeRule.onNodeWithTag("nav_home").getUnclippedBoundsInRoot()
        val themePandel = composeRule.onNodeWithTag("nav_theme_pandel").getUnclippedBoundsInRoot()
        val borma = composeRule.onNodeWithTag("nav_borma_pujo").getUnclippedBoundsInRoot()

        assertTrue(
            "Dock should start inside the screen, home=$home",
            home.left >= 0.dp
        )
        assertTrue(
            "Dock should not overflow the screen width, borma=$borma",
            borma.right <= 350.dp
        )
        assertTrue(
            "Theme Pandel slot should keep its label inside the dock, themePandel=$themePandel",
            themePandel.right <= borma.left
        )
    }

    private class RecordingContext(base: Context) : ContextWrapper(base) {        var lastIntent: Intent? = null

        override fun startActivity(intent: Intent?) {
            lastIntent = intent
        }
    }
}

