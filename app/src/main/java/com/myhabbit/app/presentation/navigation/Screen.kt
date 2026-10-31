package com.myhabbit.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Summary : Screen("summary")
    data object Profile : Screen("profile")
}
