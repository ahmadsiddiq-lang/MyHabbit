package com.myhabbit.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Summary : Screen("summary")
    data object Profile : Screen("profile")
    data object ActivityList : Screen("activity_list")
    data object ActivityForm : Screen("activity_form")

    data object ActivityEdit : Screen("activity_edit/{habitId}") {
        fun createRoute(habitId: String) = "activity_edit/$habitId"
    }
}
