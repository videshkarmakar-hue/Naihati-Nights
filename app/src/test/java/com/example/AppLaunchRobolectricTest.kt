package com.example

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.SettingsDataStore
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = "w350dp-h900dp-xxhdpi")
class AppLaunchRobolectricTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun appLaunch_showsContinueGateEvenAfterOnboardingWasCompleted() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        runBlocking {
            SettingsDataStore(context).setOnboardingComplete(true)
        }

        try {
            ActivityScenario.launch(MainActivity::class.java).use {
                composeRule.waitForIdle()

                composeRule.onNodeWithTag("intro_screen").assertIsDisplayed()
                composeRule.onNodeWithText("CONTINUE").assertIsDisplayed()
                composeRule.onNodeWithTag("begin_parikrama_button").performClick()
                composeRule.waitForIdle()

                composeRule.onNodeWithTag("header_sidebar_button").assertIsDisplayed()
                composeRule.onNodeWithTag("header_developer_credit_button").assertIsDisplayed()
                composeRule.onNodeWithTag("app_brand_header").assertIsDisplayed()
                composeRule.onNodeWithTag("app_brand_title").assertIsDisplayed()
            }
        } finally {
            runBlocking {
                SettingsDataStore(context).setOnboardingComplete(false)
            }
        }
    }

    @Test
    fun headerDeveloperCreditButton_opensCreditsAndStaysVisibleInSettings() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        runBlocking {
            SettingsDataStore(context).setOnboardingComplete(false)
        }

        try {
            ActivityScenario.launch(MainActivity::class.java).use {
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("begin_parikrama_button").performClick()
                composeRule.waitForIdle()

                composeRule.onNodeWithTag("header_developer_credit_button").assertIsDisplayed()
                composeRule.onNodeWithTag("header_developer_credit_button").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("dev_credits_screen").assertIsDisplayed()

                composeRule.onNodeWithTag("header_sidebar_button").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("sidebar_item_settings").performClick()
                composeRule.waitForIdle()

                composeRule.onNodeWithTag("header_developer_credit_button").assertIsDisplayed()
                composeRule.onNodeWithTag("header_sidebar_button").assertIsDisplayed()
            }
        } finally {
            runBlocking {
                SettingsDataStore(context).setOnboardingComplete(false)
            }
        }
    }

    @Test
    fun sidebar_navigatesToReportFeedbackAndSettings() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        runBlocking {
            SettingsDataStore(context).setOnboardingComplete(false)
        }

        try {
            ActivityScenario.launch(MainActivity::class.java).use {
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("begin_parikrama_button").performClick()
                composeRule.waitForIdle()

                // Open the sidebar from Home and route to Report & Feedback.
                composeRule.onNodeWithTag("header_sidebar_button").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("sidebar_item_report_feedback").assertIsDisplayed()
                composeRule.onNodeWithTag("sidebar_item_settings").assertIsDisplayed()
                composeRule.onNodeWithTag("sidebar_item_report_feedback").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithText("Select Inquiry Type").assertIsDisplayed()

                // Return Home, then open the sidebar again and route to Settings.
                composeRule.onNodeWithTag("nav_home").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("header_sidebar_button").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("sidebar_item_settings").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("reset_settings_button").assertIsDisplayed()
            }
        } finally {
            runBlocking {
                SettingsDataStore(context).setOnboardingComplete(false)
            }
        }
    }

    @Test
    fun sidebar_opensFromRestaurantAndHelplineSections() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        runBlocking {
            SettingsDataStore(context).setOnboardingComplete(false)
        }

        try {
            ActivityScenario.launch(MainActivity::class.java).use {
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("begin_parikrama_button").performClick()
                composeRule.waitForIdle()

                // Restaurant section exposes the sidebar through its own header menu.
                composeRule.onNodeWithTag("nav_restaurant").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("header_sidebar_button").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("sidebar_item_restaurant").assertIsDisplayed()
                composeRule.onNodeWithTag("sleek_sidebar_container").assertIsDisplayed()

                // Dismiss by navigating back into the same section.
                composeRule.onNodeWithTag("sidebar_item_restaurant").performClick()
                composeRule.waitForIdle()

                // Helpline section exposes the sidebar through its header menu too.
                composeRule.onNodeWithTag("nav_helpline").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("header_sidebar_button").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("sidebar_item_helpline").assertIsDisplayed()
                composeRule.onNodeWithTag("sleek_sidebar_container").assertIsDisplayed()
            }
        } finally {
            runBlocking {
                SettingsDataStore(context).setOnboardingComplete(false)
            }
        }
    }

    @Test
    fun sidebar_showsGuideDestinationsAndCreditFooter() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        runBlocking {
            SettingsDataStore(context).setOnboardingComplete(false)
        }

        try {
            ActivityScenario.launch(MainActivity::class.java).use {
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("begin_parikrama_button").performClick()
                composeRule.waitForIdle()

                composeRule.onAllNodesWithText("Official Parikrama Guide").assertCountEquals(0)

                composeRule.onNodeWithTag("header_sidebar_button").performClick()
                composeRule.waitForIdle()

                listOf(
                    "Route",
                    "Restaurant",
                    "Helpline",
                    "App Info & Developer Credit",
                    "Developer Credits",
                    "Settings",
                    "Report & Feedback",
                    "©Videsh Karmakar Project"
                ).forEach { label ->
                    composeRule.onNodeWithText(label).assertIsDisplayed()
                }

                composeRule.onNodeWithTag("sidebar_item_app_info_credit").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("app_details_list").assertIsDisplayed()
            }
        } finally {
            runBlocking {
                SettingsDataStore(context).setOnboardingComplete(false)
            }
        }
    }

    @Test
    fun sidebar_navigatesToDeveloperCreditsSection() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        runBlocking {
            SettingsDataStore(context).setOnboardingComplete(false)
        }

        try {
            ActivityScenario.launch(MainActivity::class.java).use {
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("begin_parikrama_button").performClick()
                composeRule.waitForIdle()

                composeRule.onNodeWithTag("header_sidebar_button").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("sidebar_item_developer_credits").performClick()
                composeRule.waitForIdle()

                composeRule.onNodeWithTag("dev_credits_screen").assertIsDisplayed()
                composeRule.onNodeWithTag("dev_credits_hero").assertIsDisplayed()
                composeRule.onNodeWithText("CONNECT WITH THE DEVELOPER").assertExists()
                composeRule.onNodeWithTag("dev_credits_screen")
                    .performScrollToNode(hasText("© 2026 Videsh Karmakar. All Rights Reserved."))
                composeRule.onNodeWithText("© 2026 Videsh Karmakar. All Rights Reserved.")
                    .assertExists()

                composeRule.onNodeWithTag("dev_credits_screen")
                    .performScrollToNode(hasTestTag("dev_credits_back_button"))
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("dev_credits_back_button").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("header_sidebar_button").assertExists()
            }
        } finally {
            runBlocking {
                SettingsDataStore(context).setOnboardingComplete(false)
            }
        }
    }
}


