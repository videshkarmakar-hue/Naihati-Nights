package com.example

import android.content.Intent
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
import androidx.compose.foundation.gestures.awaitEachGesture
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlin.math.abs
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
import com.example.ui.screens.HelplineScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IntroScreen
import com.example.ui.screens.MidNightTrainsScreen
import com.example.ui.screens.RoadMapScreen
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
    val context = LocalContext.current
    val content by viewModel.content.collectAsState()
    val updateCheck by viewModel.updateCheck.collectAsState()
    val appUpdate by viewModel.appUpdate.collectAsState()
    val updatePromptVisible by viewModel.updatePromptVisible.collectAsState()
    val updateNotice by viewModel.updateNotice.collectAsState()

    // Mandatory update gate: while an update is pending (or the prompt is on screen) the system back
    // press is consumed here so the mandatory update surface can never be bypassed through screen
    // navigation. It is disabled whenever nothing is pending, so everyday back behaviour is intact.
    val updateGateActive = updatePromptVisible ||
        appUpdate is AppUpdateState.Available ||
        appUpdate is AppUpdateState.Downloading ||
        appUpdate is AppUpdateState.Ready ||
        appUpdate is AppUpdateState.Failed
    BackHandler(enabled = updateGateActive) { }

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
            NavigationScreen.HELPLINE,
            NavigationScreen.ROAD_MAP,
            NavigationScreen.MID_NIGHT_TRAINS,
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
            NavigationScreen.ROAD_MAP -> SidebarNavItem.ROAD_MAP
            NavigationScreen.MID_NIGHT_TRAINS -> SidebarNavItem.MID_NIGHT_TRAINS
            NavigationScreen.THEME_PANDEL -> SidebarNavItem.THEME_PANDEL
            NavigationScreen.APP_DETAILS -> SidebarNavItem.APP_INFO_CREDIT
            NavigationScreen.PROFILE -> SidebarNavItem.APP_INFO_CREDIT
            NavigationScreen.DEVELOPER_CREDITS -> SidebarNavItem.DEVELOPER_CREDITS
            NavigationScreen.SETTINGS -> SidebarNavItem.SETTINGS
            NavigationScreen.CHECK_UPDATES -> SidebarNavItem.CHECK_UPDATES
            NavigationScreen.BORMA_PUJO_SCHEDULE -> SidebarNavItem.BORMA_PUJO_SCHEDULE
            NavigationScreen.SPECIAL_NOTICE -> SidebarNavItem.SPECIAL_NOTICE
            else -> null
        }
        isDrawerOpen = true
    }

    // Left-edge swipe: a rightward drag that starts within 28dp of the left edge opens the Sidebar
    // through the same entry point as the header button (so the current section stays highlighted).
    // A leftward drag while the Sidebar is open closes it. Skipped on the intro gate, the sidebar
    // showcase and while the mandatory update dialog owns the screen.
    val edgeSwipeEnabled = currentScreen != NavigationScreen.INTRO &&
        currentScreen != NavigationScreen.SIDEBAR_SHOWCASE &&
        !updatePromptVisible
    val edgeSwipeState = rememberUpdatedState(edgeSwipeEnabled)
    val openSidebarState = rememberUpdatedState(openSidebar)

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
                .pointerInput(Unit) {
                    val slop = viewConfiguration.touchSlop
                    awaitEachGesture {
                        // Find the touch this gesture starts from. Raw awaitPointerEvent is used so
                        // the swipe still works when the touch begins on a clickable child (cards,
                        // header buttons), which may consume the down event first.
                        var start: PointerInputChange? = null
                        var firstEvent = true
                        while (start == null) {
                            val event = awaitPointerEvent()
                            start = event.changes.firstOrNull {
                                it.pressed && (firstEvent || !it.previousPressed)
                            }
                            firstEvent = false
                            if (start == null && event.changes.none { it.pressed }) break
                        }
                        val gesture = start ?: return@awaitEachGesture

                        var totalDx = 0f
                        var totalDy = 0f
                        var handled = false
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == gesture.id } ?: break
                            if (!change.pressed) break
                            totalDx += change.position.x - change.previousPosition.x
                            totalDy += change.position.y - change.previousPosition.y
                            // Only a horizontally dominant drag past the touch slop counts, so
                            // vertical scrolling never triggers a navigation gesture.
                            if (!handled && abs(totalDx) > abs(totalDy) && abs(totalDx) > slop) {
                                when {
                                    // Left-to-right anywhere: open the Sidebar.
                                    edgeSwipeState.value && !isDrawerOpen && totalDx > 0f -> {
                                        openSidebarState.value()
                                        handled = true
                                    }
                                    // Right-to-left while the Sidebar is open: close it.
                                    edgeSwipeState.value && isDrawerOpen && totalDx < 0f -> {
                                        isDrawerOpen = false
                                        handled = true
                                    }
                                }
                            }
                            if (handled) change.consume()
                        }
                    }
                }
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

                NavigationScreen.MID_NIGHT_TRAINS -> {
                    MidNightTrainsScreen(
                        onBack = { currentScreen = NavigationScreen.HOME },
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS }
                    )
                }

                NavigationScreen.ROAD_MAP -> {
                    RoadMapScreen(
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
                        onScreenOpen = { viewModel.refreshAppUpdate() },
                        onCheck = { viewModel.checkForAppUpdate() },
                        onUpdate = { viewModel.downloadAndInstallUpdate() },
                        onOpenRelease = { viewModel.openReleasesPage() },
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
                        onOpenHome = { currentScreen = NavigationScreen.HOME },
                        onOpenSidebar = openSidebar,
                        onOpenDeveloperCredits = { currentScreen = NavigationScreen.DEVELOPER_CREDITS }
                    )
                }

                NavigationScreen.DEVELOPER_CREDITS -> {
                    DeveloperCreditsScreen(
                        onBack = { currentScreen = NavigationScreen.HOME },
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
                            SidebarNavItem.APP_INFO_CREDIT -> {
                                appDetailsBackTarget = NavigationScreen.HOME
                                currentScreen = NavigationScreen.APP_DETAILS
                                isDrawerOpen = false
                            }
                            SidebarNavItem.ROAD_MAP -> {
                                currentScreen = NavigationScreen.ROAD_MAP
                                isDrawerOpen = false
                            }
                            SidebarNavItem.MID_NIGHT_TRAINS -> {
                                currentScreen = NavigationScreen.MID_NIGHT_TRAINS
                                isDrawerOpen = false
                            }
                            SidebarNavItem.DEVELOPER_CREDITS -> {
                                currentScreen = NavigationScreen.DEVELOPER_CREDITS
                                isDrawerOpen = false
                            }
                            SidebarNavItem.SHARE_APP -> {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "https://naihatinights.pages.dev/")
                                    putExtra(Intent.EXTRA_SUBJECT, "Naihati Nights")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Naihati Nights"))
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

