package com.perkedel.htlauncher

import androidx.navigation.NavArgs

sealed class Screen (val route:String) {
    object HomeScreen : Screen("home")
    object AllAppsScreen : Screen("all_apps")
    object ConfigurationScreen : Screen("configuration")

    fun withArgs(vararg args: String):String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}