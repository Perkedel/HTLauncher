package com.perkedel.htlauncher.enumerations

import androidx.annotation.StringRes
import com.perkedel.htlauncher.R
import kotlinx.serialization.Serializable

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

@Serializable
enum class Screen (@StringRes val title: Int){
    HomeScreen(title = R.string.app_name),
    AllAppsScreen(title = R.string.all_apps),
    ConfigurationScreen(title = R.string.configuration_screen),
    AboutScreen(title = R.string.about_screen),
    LevelEditor(title = R.string.editor_screen),
    ItemsExplorer(title = R.string.items_explorer_screen),
    OpenAPage(title = R.string.open_page_screen),
    WidgetTest(title = R.string.widget_test_screen),
    GetStarted(title = R.string.get_started_screen),
    PurchaseScreen(title = R.string.purchase_screen),
}