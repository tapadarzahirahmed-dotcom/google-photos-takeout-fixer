package com.example.takeoutfixer.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Scan : Screen("scan")
    object Analysis : Screen("analysis")
    object Preview : Screen("preview")
    object Processing : Screen("processing")
    object Results : Screen("results")
}
