package com.perkedel.htlauncher

sealed class Screen (val route:String) {
    object HomeScreen : Screen("home")
    object AllAppsScreen : Screen("all_apps")
    object ConfigurationScreen : Screen("configuration")
}