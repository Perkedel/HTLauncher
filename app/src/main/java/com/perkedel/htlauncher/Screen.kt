package com.perkedel.htlauncher

import androidx.annotation.StringRes
import androidx.navigation.NavArgs

//sealed class Screen (val route:String) {
//    data object HomeScreen : Screen("home")
//    data object AllAppsScreen : Screen("all_apps")
//    data object ConfigurationScreen : Screen("configuration")
//
//    fun withArgs(vararg args: String):String {
//        return buildString {
//            append(route)
//            args.forEach { arg ->
//                append("/$arg")
//            }
//        }
//    }
//}

enum class Screen (@StringRes val title: Int){
    HomeScreen(title = R.string.app_name),
    AllAppsScreen(title = R.string.all_apps),
    ConfigurationScreen(title = R.string.configuration_screen),
}