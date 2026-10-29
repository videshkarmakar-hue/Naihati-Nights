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
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
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
import com.example.ui.components.CapsuleBottomBar
import com.example.ui.components.SidebarNavItem
import com.example.ui.components.SleekSidebar
import com.example.ui.screens.AppDetailsScreen
import com.example.ui.screens.CheckUpdatesScreen
import com.example.ui.screens.DeveloperCreditsScreen
import com.example.ui.screens.DeveloperProfileScreen
import com.example.ui.screens.FeedbackScreen
import com.example.ui.screens.HelplineScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IntroScreen
import com.example.ui.screens.RestaurantScreen
import com.example.ui.screens.SettingsScreen
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
        composeRule.onNodeWithText("CONTINUE").assertIsDisplayed()
        composeRule.onNodeWithText("Architected by Videsh Karmakar").assertExists()
        composeRule.onNodeWithText("©Crafted by VIDESH KARMAKAR").assertExists()
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
        composeRule.onNodeWithText("PARIKRAMA & NAVIGATION").assertIsDisplayed()
        composeRule.onNodeWithText("ACTIVE").assertIsDisplayed()
        composeRule.onNodeWithText("Live Pandal Queue Alerts").assertIsDisplayed()
        composeRule.onNodeWithText("Offline Map Cache").assertIsDisplayed()
        composeRule.onNodeWithText("GPS High Precision Mode").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_list")
            .performScrollToNode(hasText("DISPLAY & EXPERIENCE"))
        composeRule.onNodeWithText("OLED True Black Mode").assertIsDisplayed()
        composeRule.onNodeWithText("Motion Reduction").assertIsDisplayed()
        composeRule.onNodeWithText("Haptic Feedback").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_list")
            .performScrollToNode(hasText("SAFETY & HELPLINE"))
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
            .performScrollToNode(hasTestTag("check_for_updates_button"))
        composeRule.onNodeWithText("APP ARCHITECTURE").assertIsDisplayed()
        composeRule.onNodeWithText("Parikrama Radar Core").assertIsDisplayed()
        composeRule.onNodeWithText("v${BuildConfig.VERSION_NAME} • STABLE").assertIsDisplayed()
        composeRule.onNodeWithText("v${BuildConfig.VERSION_NAME} Patch Notes").assertIsDisplayed()
        composeRule.onNodeWithText(
            "Optimized low-light contrast & OLED battery efficiency"
        ).assertExists()

        composeRule.onNodeWithTag("settings_list")
            .performScrollToNode(hasText("JAY BORO MAA"))
        composeRule.onNodeWithText("Crafted with Devotion in Naihati").assertIsDisplayed()
        composeRule.onNodeWithText("©Crafted by VIDESH KARMAKAR").assertIsDisplayed()
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
    fun feedbackScreen_validatesAndSubmits() {
        var submitted = false
        composeRule.setContent {
            MyApplicationTheme {
                FeedbackScreen(
                    content = NaihatiDataProvider.content(),
                    onSubmitFeedback = { _, _, _, _, _, _, _, _ -> submitted = true }
                )
            }
        }

        composeRule.onNodeWithTag("header_sidebar_button").assertIsDisplayed()

        composeRule.onNodeWithTag("feedback_details_field")
            .performScrollTo()
            .performTextInput("Temporary barricade blocking the main route near the station.")
        composeRule.onNodeWithTag("feedback_submit_button")
            .performScrollTo()
            .performClick()

        composeRule.waitUntil(timeoutMillis = 3_000) { submitted }
        assertTrue(submitted)
    }

    @Test
    fun feedbackScreen_submitsWithDesignControlsAndShowsTicket() {
        var captured = ""
        composeRule.setContent {
            MyApplicationTheme {
                FeedbackScreen(
                    content = NaihatiDataProvider.content(),
                    onSubmitFeedback = { type, zone, _, _, _, details, urgency, _ ->
                        captured = "${type.name}|$zone|$details|${urgency.name}"
                    }
                )
            }
        }

        composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
        composeRule.onNodeWithTag("app_brand_title").assertIsDisplayed()
        composeRule.onNodeWithText("Select Inquiry Type").assertIsDisplayed()
        composeRule.onNodeWithText("Route 1 (Station PF1)").assertIsDisplayed()
        composeRule.onNodeWithText("General App").assertExists()

        composeRule.onNodeWithTag("feedback_type_pandal_update").performClick()
        composeRule.onNodeWithTag("feedback_details_field")
            .performScrollTo()
            .performTextInput("Queue is spilling onto the main road near Boro Maa.")
        composeRule.onNodeWithTag("feedback_urgency_urgent_diversion")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithTag("feedback_submit_button")
            .performScrollTo()
            .performClick()

        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("feedback_success_notice")
                .fetchSemanticsNodes().isNotEmpty()
        }

        assertEquals(
            "PANDAL_UPDATE|Route 1 (Station PF1)|Queue is spilling onto the main road near Boro Maa.|URGENT_DIVERSION",
            captured
        )
        composeRule.onNodeWithText("Report Logged Successfully").assertIsDisplayed()
        composeRule.onNodeWithTag("feedback_ticket_id").assertIsDisplayed()
        composeRule.onNodeWithText("• JAY BORO MAA •").assertExists()
        composeRule.onNodeWithText("©Crafted by VIDESH KARMAKAR").assertExists()
    }

    @Test
    fun appDetails_rebuiltDesignAndActionsWork() {
        var backPressed = false
        var feedbackOpened = false
        var homeOpened = false

        composeRule.setContent {
            MyApplicationTheme {
                AppDetailsScreen(
                    onBack = { backPressed = true },
                    onOpenFeedback = { feedbackOpened = true },
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
            .performScrollToNode(hasTestTag("app_details_feedback_button"))
        composeRule.onNodeWithTag("app_details_feedback_button").performClick()
        composeRule.waitForIdle()
        assertTrue(feedbackOpened)

        composeRule.onNodeWithTag("app_details_list")
            .performScrollToNode(hasText("©Crafted by VIDESH KARMAKAR"))
        composeRule.onNodeWithText("©Crafted by VIDESH KARMAKAR").assertIsDisplayed()
        composeRule.onNodeWithText("• JAY MAA BORO MAA •").assertExists()

        composeRule.onNodeWithTag("app_details_list")
            .performScrollToNode(hasTestTag("app_details_legal_report"))
        composeRule.onNodeWithTag("app_details_legal_report").performClick()
        composeRule.waitForIdle()
        assertTrue(feedbackOpened)

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
                        onOpenFeedback = {},
                        onOpenAppDetails = { openAppDetails = true }
                    )
                    else -> AppDetailsScreen(onBack = {}, onOpenFeedback = {})
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
        composeRule.onNodeWithText("Naihati Police Station Control Room").assertIsDisplayed()
        composeRule.onNodeWithText("Boro Maa Trust Emergency Service").assertIsDisplayed()
        composeRule.onNodeWithText("Naihati State General Hospital").assertIsDisplayed()

        composeRule.onNodeWithTag("helpline_search_field").performTextInput("police")
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Naihati Police Station Control Room").assertIsDisplayed()
        composeRule.onNodeWithText("Naihati Fire Station").assertDoesNotExist()
        composeRule.onNodeWithText("IMPORTANT POLICE ASSISTANCE BOOTHS").assertIsDisplayed()

        composeRule.onNodeWithTag("helpline_search_field").performTextClearance()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Women Helpline").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Child Assistance").assertIsDisplayed()
        composeRule.onNodeWithTag("helpline_list").performScrollToNode(
            hasText("IMPORTANT POLICE ASSISTANCE BOOTHS")
        )
        composeRule.onNodeWithText("IMPORTANT POLICE ASSISTANCE BOOTHS").assertIsDisplayed()
        composeRule.onNodeWithTag("helpline_list").performScrollToNode(
            hasText("VISITOR GUIDELINES & SAFETY INSTRUCTIONS")
        )
        composeRule.onNodeWithText("VISITOR GUIDELINES & SAFETY INSTRUCTIONS")
            .assertIsDisplayed()
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
    fun bottomBar_containsOnlyPrimaryDestinationsAndNavigates() {
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
        composeRule.onNodeWithTag("nav_restaurant").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_helpline").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_theme_pandel").assertIsDisplayed()
        composeRule.onAllNodesWithTag("nav_feedback").assertCountEquals(0)
        composeRule.onAllNodesWithTag("nav_settings").assertCountEquals(0)

        val home = composeRule.onNodeWithTag("nav_home").getUnclippedBoundsInRoot()
        val restaurant = composeRule.onNodeWithTag("nav_restaurant").getUnclippedBoundsInRoot()
        val helpline = composeRule.onNodeWithTag("nav_helpline").getUnclippedBoundsInRoot()
        val themePandel = composeRule.onNodeWithTag("nav_theme_pandel").getUnclippedBoundsInRoot()
        assertTrue(
            "Home and Restaurant slots should sit close together, home=$home restaurant=$restaurant",
            restaurant.left - home.right <= 12.dp
        )
        assertTrue(
            "Restaurant and Helpline slots should sit close together, restaurant=$restaurant helpline=$helpline",
            helpline.left - restaurant.right <= 12.dp
        )
        assertTrue(
            "Helpline and Theme Pandel slots should sit close together, helpline=$helpline themePandel=$themePandel",
            themePandel.left - helpline.right <= 12.dp
        )

        composeRule.onNodeWithTag("nav_restaurant").performClick()
        composeRule.waitForIdle()
        assertTrue(navigatedTo == NavigationScreen.RESTAURANT)

        composeRule.onNodeWithTag("nav_theme_pandel").performClick()
        composeRule.waitForIdle()
        assertTrue(navigatedTo == NavigationScreen.THEME_PANDEL)
    }

    @Test
    fun themePandelScreen_rendersTwelveThemesAndFilters() {
        composeRule.setContent {
            MyApplicationTheme {
                ThemePandelScreen()
            }
        }

        composeRule.onNodeWithTag("theme_pandel_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("theme_pandel_hero_card").assertIsDisplayed()
        composeRule.onNodeWithTag("theme_pandel_search_field").assertIsDisplayed()
        composeRule.onNodeWithTag("theme_pandel_card_theme_01").assertIsDisplayed()

        composeRule.onNodeWithTag("theme_pandel_screen")
            .performScrollToNode(hasTestTag("theme_pandel_card_theme_12"))
        composeRule.onNodeWithTag("theme_pandel_card_theme_12").assertIsDisplayed()

        composeRule.onNodeWithTag("theme_pandel_screen")
            .performScrollToNode(hasTestTag("theme_pandel_search_field"))
        composeRule.onNodeWithTag("theme_pandel_search_field")
            .performTextInput("Mahakal")
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("theme_pandel_card_theme_10").assertIsDisplayed()
        composeRule.onAllNodesWithTag("theme_pandel_card_theme_01").assertCountEquals(0)

        composeRule.onNodeWithTag("theme_pandel_search_field")
            .performTextClearance()
        composeRule.onNodeWithTag("theme_pandel_search_field")
            .performTextInput("zzzz-no-match")
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("theme_pandel_empty").assertIsDisplayed()

        composeRule.onNodeWithTag("theme_pandel_search_clear").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("theme_pandel_card_theme_01").assertIsDisplayed()
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
            "sidebar_item_restaurant" to "Restaurant",
            "sidebar_item_helpline" to "Helpline",
            "sidebar_item_theme_pandel" to "Theme Pandel",
            "sidebar_item_app_info_credit" to "App Info & Developer Credit",
            "sidebar_item_check_updates" to "Check Updates",
            "sidebar_item_settings" to "Settings",
            "sidebar_item_report_feedback" to "Report & Feedback"
        ).forEach { (tag, label) ->
            composeRule.onNodeWithTag(tag).assertIsDisplayed()
            composeRule.onNodeWithText(label).assertIsDisplayed()
        }

        // No update dot until the release channel reports something newer.
        composeRule.onAllNodesWithTag("sidebar_update_dot").assertCountEquals(0)

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

        composeRule.onNodeWithTag("sidebar_item_restaurant").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.RESTAURANT, selected)

        composeRule.onNodeWithTag("sidebar_item_helpline").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.HELPLINE, selected)

        composeRule.onNodeWithTag("sidebar_item_theme_pandel").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.THEME_PANDEL, selected)

        composeRule.onNodeWithTag("sidebar_item_app_info_credit").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.APP_INFO_CREDIT, selected)

        composeRule.onNodeWithTag("sidebar_item_report_feedback").performClick()
        composeRule.waitForIdle()
        assertEquals(SidebarNavItem.REPORT_FEEDBACK, selected)

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
                    autoCheckEnabled = true,
                    onScreenOpen = { screenOpened++ },
                    onCheck = { checked++ },
                    onUpdate = { updated++ },
                    onOpenRelease = { releaseOpened++ },
                    onAutoCheckChange = {},
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
                    autoCheckEnabled = false,
                    onScreenOpen = {},
                    onCheck = { checked++ },
                    onUpdate = { updated++ },
                    onOpenRelease = {},
                    onAutoCheckChange = {},
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
                DeveloperCreditsScreen(onBack = {}, onOpenFeedback = {})
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
        composeRule.onNodeWithText("PRODUCT LEAD & ARCHITECT").assertIsDisplayed()
        composeRule.onNodeWithText("Founder · Developer · UI/UX Designer").assertIsDisplayed()

        composeRule.onNodeWithText("CONNECT WITH THE DEVELOPER").assertExists()
        composeRule.onNodeWithText("DEVELOPMENT & DESIGN").assertExists()
        composeRule.onNodeWithText("Naihati Kali Puja 2026").assertExists()
        composeRule.onNodeWithText("Figma · Stitch").assertExists()
        composeRule.onNodeWithText("Android · iOS Soon").assertExists()

        composeRule.onNodeWithTag("dev_credits_screen")
            .performScrollToNode(hasTestTag("dev_credits_feedback_button"))
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("dev_credits_feedback_button").assertIsDisplayed()
        composeRule.onNodeWithText("ACKNOWLEDGEMENT").assertExists()
        composeRule.onNodeWithText("Crafted with devotion by VIDESH KARMAKAR").assertExists()
        composeRule.onNodeWithText("© 2026 Videsh Karmakar. All Rights Reserved.").assertExists()
        composeRule.onNodeWithText("• JAY BORO MAA •").assertExists()

        composeRule.onNodeWithTag("dev_credits_screen")
            .performScrollToNode(hasText("©Crafted by VIDESH KARMAKAR"))
        composeRule.onNodeWithText("©Crafted by VIDESH KARMAKAR").assertIsDisplayed()
    }

    @Test
    fun developerCreditsScreen_actionsOpenLinksFeedbackAndBack() {
        val recordingContext = RecordingContext(RuntimeEnvironment.getApplication())
        var backPressed = false
        var feedbackOpened = false

        composeRule.setContent {
            CompositionLocalProvider(LocalContext provides recordingContext) {
                MyApplicationTheme {
                    DeveloperCreditsScreen(
                        onBack = { backPressed = true },
                        onOpenFeedback = { feedbackOpened = true }
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
            .performScrollToNode(hasTestTag("dev_credits_feedback_button"))
        composeRule.onNodeWithTag("dev_credits_feedback_button").performClick()
        composeRule.waitForIdle()
        assertTrue(feedbackOpened)

        composeRule.onNodeWithTag("dev_credits_screen")
            .performScrollToNode(hasTestTag("dev_credits_back_button"))
        composeRule.onNodeWithTag("dev_credits_back_button").performClick()
        composeRule.waitForIdle()
        assertTrue(backPressed)
    }

    private class RecordingContext(base: Context) : ContextWrapper(base) {        var lastIntent: Intent? = null

        override fun startActivity(intent: Intent?) {
            lastIntent = intent
        }
    }
}

