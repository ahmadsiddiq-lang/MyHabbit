package com.myhabbit.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.myhabbit.app.presentation.activity.ActivityFormScreen
import com.myhabbit.app.presentation.activity.ActivityListScreen
import com.myhabbit.app.presentation.dashboard.DashboardScreen
import com.myhabbit.app.presentation.history.HistoryScreen
import com.myhabbit.app.presentation.profile.ProfileScreen
import com.myhabbit.app.presentation.summary.SummaryScreen

private const val ICON_PLACEHOLDER = android.R.drawable.ic_dialog_info

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.Dashboard to "Beranda",
        Screen.Summary to "Ringkasan",
        Screen.Profile to "Profil"
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { (screen, label) ->
                    NavigationBarItem(
                        icon = {
                            Icon(painterResource(ICON_PLACEHOLDER), contentDescription = label)
                        },
                        label = { Text(label) },
                        selected =
                            currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
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
                    onOpenHistory = { navController.navigate(Screen.History.route) }
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
