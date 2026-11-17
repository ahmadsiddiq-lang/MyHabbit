package com.myhabbit.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.myhabbit.app.R
import com.myhabbit.app.presentation.activity.ActivityFormScreen
import com.myhabbit.app.presentation.activity.ActivityListScreen
import com.myhabbit.app.presentation.dashboard.DashboardScreen
import com.myhabbit.app.presentation.history.HistoryScreen
import com.myhabbit.app.presentation.login.LoginScreen
import com.myhabbit.app.presentation.login.LoginViewModel
import com.myhabbit.app.presentation.profile.ProfileScreen
import com.myhabbit.app.presentation.splash.SplashScreen
import com.myhabbit.app.presentation.summary.SummaryScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

/** Item bottom nav — icon vector sesuai fungsi tiap tab. */
private data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val iconRes: Int
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Status login (mockup UI.png #2): belum login → layar Login Google
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState by loginViewModel.uiState.collectAsStateWithLifecycle()

    // Splash (mockup UI.png #1) — tampil singkat sebelum konten utama
    var showSplash by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1600)
        showSplash = false
    }
    if (showSplash || !loginState.authChecked) {
        SplashScreen()
        return
    }
    if (loginState.user == null) {
        LoginScreen(viewModel = loginViewModel)
        return
    }

    val items = listOf(
        BottomNavItem(Screen.Dashboard, "Beranda", R.drawable.ic_nav_dashboard),
        BottomNavItem(Screen.Summary, "Ringkasan", R.drawable.ic_nav_summary),
        BottomNavItem(Screen.Profile, "Profil", R.drawable.ic_nav_profile)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(item.iconRes),
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected =
                            currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onOpenActivityList = { navController.navigate(Screen.ActivityList.route) },
                    onCreateActivity = { navController.navigate(Screen.ActivityForm.route) },
                    onOpenHistory = { navController.navigate(Screen.History.route) },
                    onOpenProfile = { navController.navigate(Screen.Profile.route) }
                )
            }
            composable(Screen.Summary.route) { SummaryScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.History.route) {
                HistoryScreen()
            }

            composable(Screen.ActivityList.route) {
                ActivityListScreen(
                    onEditActivity = { habitId ->
                        navController.navigate(Screen.ActivityEdit.createRoute(habitId))
                    },
                    onCreateActivity = { navController.navigate(Screen.ActivityForm.route) }
                )
            }

            composable(Screen.ActivityForm.route) {
                ActivityFormScreen(
                    habitId = null,
                    onSaved = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.ActivityEdit.route,
                arguments = listOf(
                    navArgument("habitId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                ActivityFormScreen(
                    habitId = backStackEntry.arguments?.getString("habitId"),
                    onSaved = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}
