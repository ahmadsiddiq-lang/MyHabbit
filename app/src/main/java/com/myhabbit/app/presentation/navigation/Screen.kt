package com.myhabbit.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Summary : Screen("summary")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    data object Backup : Screen("backup")
    data object About : Screen("about")
    data object ActivityList : Screen("activity_list")
    data object History : Screen("history")

    data object ActivityEdit : Screen("activity_edit/{habitId}") {
        fun createRoute(habitId: String) = "activity_edit/$habitId"
    }
}
