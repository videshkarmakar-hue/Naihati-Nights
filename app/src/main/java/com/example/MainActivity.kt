package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch
import com.example.data.update.AppUpdateState
import com.example.ui.components.AppUpdateDialog
import com.example.ui.components.CapsuleBottomBar
import com.example.ui.components.SidebarNavItem
import com.example.ui.components.SleekSidebar
import com.example.ui.screens.AppDetailsScreen
import com.example.ui.screens.BormaPujoScheduleScreen
import com.example.ui.screens.CheckUpdatesScreen
import com.example.ui.screens.DeveloperCreditsScreen
import com.example.ui.screens.DeveloperProfileScreen
import com.example.ui.screens.FeedbackScreen
import com.example.ui.screens.HelplineScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IntroScreen
import com.example.ui.screens.RestaurantScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SidebarShowcaseScreen
import com.example.ui.screens.SpecialNoticeScreen
import com.example.ui.screens.ThemePandelScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianNoir
import com.example.ui.viewmodel.AppViewModel
import com.example.ui.NavigationScreen

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by viewModel.settings.collectAsState()
            MyApplicationTheme(themeStyle = settings.themeStyle) {
                NaihatiNightsApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NaihatiNightsApp(viewModel: AppViewModel) {
    val appSettings by viewModel.settings.collectAsState()
    val content by viewModel.content.collectAsState()
    val updateCheck by viewModel.updateCheck.collectAsState()
    val appUpdate by viewModel.appUpdate.collectAsState()
    val updatePromptVisible by viewModel.updatePromptVisible.collectAsState()
    val updateNotice by viewModel.updateNotice.collectAsState()

    // GitHub release channel: check on every resume (throttled inside the view model), so a
    // release published while the app was open still raises the popup when the user returns.
    val lifecycleOwner = LocalLifecycleOwner.current
    val watchdogScope = rememberCoroutineScope()
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Re-assert the global update popup on resume so it covers whichever screen the
                // user is on while a newer build is pending, and never leaves them without it.
                viewModel.reassertUpdatePrompt()
                viewModel.autoCheckForAppUpdate()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // Foreground watchdog: while the app is open, keep checking the release feed so a build
        // published mid-session raises the un-closable update popup over whatever screen is shown.
        val watchdog = watchdogScope.launch { viewModel.startUpdateWatchdog() }

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            watchdog.cancel()
            viewModel.stopUpdateWatchdog()
        }
    }

    // The branded Continue gate is the entry point for every fresh launch.
    // The branded Continue gate is the entry point for every fresh launch.
    // It is saveable only so a configuration change doesn't replay the splash.
    var currentScreen by rememberSaveable { mutableStateOf(NavigationScreen.INTRO) }

    // Quick overlay drawer state
    var isDrawerOpen by remember { mutableStateOf(false) }
    var isDrawerExpanded by remember { mutableStateOf(true) }
    var isDrawerDarkMode by remember { mutableStateOf(true) }
    var activeSidebarItem by remember { mutableStateOf<SidebarNavItem?>(null) }
    // App Details can be opened from the sidebar or from the developer profile.
    var appDetailsBackTarget by remember { mutableStateOf(NavigationScreen.HOME) }

    // System Back Navigation Handling
    when {
        isDrawerOpen -> {
            BackHandler { isDrawerOpen = false }
        }
        currentScreen == NavigationScreen.APP_DETAILS -> {
            BackHandler { currentScreen = appDetailsBackTarget }
        }
        currentScreen == NavigationScreen.SIDEBAR_SHOWCASE -> {
            BackHandler { currentScreen = NavigationScreen.HOME }
        }
        currentScreen in listOf(
            NavigationScreen.PROFILE,
            NavigationScreen.SETTINGS,
            NavigationScreen.CHECK_UPDATES,
            NavigationScreen.BORMA_PUJO_SCHEDULE,
            NavigationScreen.SPECIAL_NOTICE,
            NavigationScreen.FEEDBACK,
            NavigationScreen.HELPLINE,
            NavigationScreen.THEME_PANDEL,
            NavigationScreen.RESTAURANT,
            NavigationScreen.DEVELOPER_CREDITS
        ) -> {
            BackHandler { currentScreen = NavigationScreen.HOME }
        }
    }

    // One shared entry point for the Sidebar button rendered in every section header.
    val openSidebar: () -> Unit = {
        activeSidebarItem = when (currentScreen) {
            NavigationScreen.HOME -> SidebarNavItem.ROUTE
            NavigationScreen.RESTAURANT -> SidebarNavItem.RESTAURANT
            NavigationScreen.HELPLINE -> SidebarNavItem.HELPLINE
            NavigationScreen.THEME_PANDEL -> SidebarNavItem.THEME_PANDEL
            NavigationScreen.APP_DETAILS -> SidebarNavItem.APP_INFO_CREDIT
            NavigationScreen.PROFILE -> SidebarNavItem.APP_INFO_CREDIT
            NavigationScreen.DEVELOPER_CREDITS -> SidebarNavItem.DEVELOPER_CREDITS
            NavigationScreen.SETTINGS -> SidebarNavItem.SETTINGS
            NavigationScreen.CHECK_UPDATES -> SidebarNavItem.CHECK_UPDATES
            NavigationScreen.BORMA_PUJO_SCHEDULE -> SidebarNavItem.BORMA_PUJO_SCHEDULE
            NavigationScreen.SPECIAL_NOTICE -> SidebarNavItem.SPECIAL_NOTICE
            NavigationScreen.FEEDBACK -> SidebarNavItem.REPORT_FEEDBACK
            else -> null
        }
        isDrawerOpen = true
    }

    val baseDensity = LocalDensity.current
    val legibilityScale = when (appSettings.textLegibilityScale) {
        1 -> 1f
        2 -> 1.15f
        else -> 1.3f
    }

    CompositionLocalProvider(
        LocalDensity provides Density(
            density = baseDensity.density,
            fontScale = baseDensity.fontScale * legibilityScale
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianNoir)
        ) {
        // Screen Content
        Crossfade(targetState = currentScreen, label = "screenTransition") { screen ->
            when (screen) {
                NavigationScreen.INTRO -> {
                    IntroScreen(
                        onBeginExperience = {
                            viewModel.completeOnboarding()
                            currentScreen = NavigationScreen.HOME
                        },
                        motionReduction = appSettings.motionReduction
                    )
                }

                NavigationScreen.HOME -> {
                    HomeScreen(
                        content = content,
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS },
                        onOpenDining = { currentScreen = NavigationScreen.RESTAURANT }
                    )
                }

                NavigationScreen.RESTAURANT -> {
                    RestaurantScreen(
                        content = content,
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS }
                    )
                }

                NavigationScreen.HELPLINE -> {
                    HelplineScreen(
                        content = content,
                        onBack = { currentScreen = NavigationScreen.HOME },
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS }
                    )
                }

                NavigationScreen.THEME_PANDEL -> {
                    ThemePandelScreen(
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS }
                    )
                }

                NavigationScreen.FEEDBACK -> {
                    FeedbackScreen(
                        content = content,
                        onBack = { currentScreen = NavigationScreen.HOME },
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS },
                        onSubmitFeedback = { inquiryType, zone, relatedLandmark, reporterName, contactNumber, incidentDetails, urgencyLevel, hasAttachedPhoto ->
                            try {
                                viewModel.submitFeedback(
                                    inquiryType = inquiryType.name,
                                    zone = zone,
                                    relatedLandmark = relatedLandmark,
                                    reporterName = reporterName,
                                    contactNumber = contactNumber,
                                    incidentDetails = incidentDetails,
                                    urgencyLevel = urgencyLevel.name,
                                    hasAttachedPhoto = hasAttachedPhoto
                                )
                            } catch (e: Exception) {
                            }
                        }
                    )
                }

                NavigationScreen.SETTINGS -> {
                    SettingsScreen(
                        appSettings = appSettings,
                        content = content,
                        onUpdateSettings = { viewModel.updateSettings(it) },
                        onResetSettings = { viewModel.resetSettings() },
                        onCheckForUpdates = { viewModel.checkForUpdates() },
                        updateCheck = updateCheck,
                        appUpdate = appUpdate,
                        onCheckAppUpdate = { viewModel.checkForAppUpdate() },
                        onDownloadAppUpdate = { viewModel.downloadAndInstallUpdate() },
                        onOpenReleases = { viewModel.openReleasesPage() },
                        onBack = { currentScreen = NavigationScreen.HOME },
                        onOpenDeveloperProfile = { currentScreen = NavigationScreen.PROFILE },
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS }
                    )
                }

                NavigationScreen.CHECK_UPDATES -> {
                    CheckUpdatesScreen(
                        installedVersion = viewModel.installedVersionName,
                        installedBuild = BuildConfig.VERSION_CODE,
                        state = appUpdate,
                        notice = updateNotice,
                        autoCheckEnabled = appSettings.autoUpdateCheck,
                        onScreenOpen = { viewModel.refreshAppUpdate() },
                        onCheck = { viewModel.checkForAppUpdate() },
                        onUpdate = { viewModel.downloadAndInstallUpdate() },
                        onOpenRelease = { viewModel.openReleasesPage() },
                        onAutoCheckChange = {
                            viewModel.updateSettings(appSettings.copy(autoUpdateCheck = it))
                        },
                        onBack = { currentScreen = NavigationScreen.HOME },
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS }
                    )
                }

                NavigationScreen.BORMA_PUJO_SCHEDULE -> {
                    BormaPujoScheduleScreen(
                        onBack = { currentScreen = NavigationScreen.HOME },
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS }
                    )
                }

                NavigationScreen.SPECIAL_NOTICE -> {
                    SpecialNoticeScreen(
                        onBack = { currentScreen = NavigationScreen.HOME },
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS }
                    )
                }

                NavigationScreen.PROFILE -> {
                    DeveloperProfileScreen(
                        onBack = { currentScreen = NavigationScreen.HOME },
                        onOpenFeedback = { currentScreen = NavigationScreen.FEEDBACK },
                        onOpenAppDetails = {
                            appDetailsBackTarget = NavigationScreen.PROFILE
                            currentScreen = NavigationScreen.APP_DETAILS
                        },
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS }
                    )
                }

                NavigationScreen.APP_DETAILS -> {
                    AppDetailsScreen(
                        onBack = { currentScreen = appDetailsBackTarget },
                        onOpenFeedback = { currentScreen = NavigationScreen.FEEDBACK },
                        onOpenHome = { currentScreen = NavigationScreen.HOME },
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS }
                    )
                }

                NavigationScreen.DEVELOPER_CREDITS -> {
                    DeveloperCreditsScreen(
                        onBack = { currentScreen = NavigationScreen.HOME },
                        onOpenFeedback = { currentScreen = NavigationScreen.FEEDBACK },
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS }
                    )
                }

                NavigationScreen.SIDEBAR_SHOWCASE -> {
                    SidebarShowcaseScreen(
                        onBack = { currentScreen = NavigationScreen.HOME }
                    )
                }
            }
        }

        // Floating Capsule Bottom Dock (visible on main experience screens except Intro & Sidebar Showcase)
        if (currentScreen != NavigationScreen.INTRO && currentScreen != NavigationScreen.SIDEBAR_SHOWCASE) {
            CapsuleBottomBar(
                currentScreen = currentScreen,
                onNavigate = { screen -> currentScreen = screen },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
            )
        }

        // Animated Sidebar Drawer Overlay (when drawer is opened)
        AnimatedVisibility(
            visible = isDrawerOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.65f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { isDrawerOpen = false }
            )
        }

        AnimatedVisibility(
            visible = isDrawerOpen,
            enter = slideInHorizontally(initialOffsetX = { -it }),
            exit = slideOutHorizontally(targetOffsetX = { -it }),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(vertical = 12.dp, horizontal = 14.dp)
            ) {
                SleekSidebar(
                    isExpanded = isDrawerExpanded,
                    isDarkMode = isDrawerDarkMode,
                    selectedItem = activeSidebarItem,
                    hasUpdate = appUpdate is AppUpdateState.Available ||
                        appUpdate is AppUpdateState.Ready,
                    onSelectItem = { item ->
                        activeSidebarItem = item
                        when (item) {
                            SidebarNavItem.ROUTE -> {
                                currentScreen = NavigationScreen.HOME
                                isDrawerOpen = false
                            }
                            SidebarNavItem.RESTAURANT -> {
                                currentScreen = NavigationScreen.RESTAURANT
                                isDrawerOpen = false
                            }
                            SidebarNavItem.HELPLINE -> {
                                currentScreen = NavigationScreen.HELPLINE
                                isDrawerOpen = false
                            }
                            SidebarNavItem.APP_INFO_CREDIT -> {
                                appDetailsBackTarget = NavigationScreen.HOME
                                currentScreen = NavigationScreen.APP_DETAILS
                                isDrawerOpen = false
                            }
                            SidebarNavItem.DEVELOPER_CREDITS -> {
                                currentScreen = NavigationScreen.DEVELOPER_CREDITS
                                isDrawerOpen = false
                            }
                            SidebarNavItem.SETTINGS -> {
                                currentScreen = NavigationScreen.SETTINGS
                                isDrawerOpen = false
                            }
                            SidebarNavItem.CHECK_UPDATES -> {
                                currentScreen = NavigationScreen.CHECK_UPDATES
                                isDrawerOpen = false
                            }
                            SidebarNavItem.BORMA_PUJO_SCHEDULE -> {
                                currentScreen = NavigationScreen.BORMA_PUJO_SCHEDULE
                                isDrawerOpen = false
                            }
                            SidebarNavItem.SPECIAL_NOTICE -> {
                                currentScreen = NavigationScreen.SPECIAL_NOTICE
                                isDrawerOpen = false
                            }
                            SidebarNavItem.REPORT_FEEDBACK -> {
                                currentScreen = NavigationScreen.FEEDBACK
                                isDrawerOpen = false

                            }
                            SidebarNavItem.THEME_PANDEL -> {
                                currentScreen = NavigationScreen.THEME_PANDEL
                                isDrawerOpen = false
                            }
                        }
                    }, onToggleExpand = { isDrawerExpanded = !isDrawerExpanded }
                )
            }
        }

        // GitHub release channel: app-binary updates (not the guide content sync above).
        AppUpdateDialog(
            visible = updatePromptVisible,
            state = appUpdate,
            notice = updateNotice,
            onUpdateNow = viewModel::downloadAndInstallUpdate,
            onOpenRelease = viewModel::openReleasesPage
        )
    }
    }
}

