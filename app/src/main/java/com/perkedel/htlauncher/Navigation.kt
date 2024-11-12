package com.perkedel.htlauncher

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import android.content.Context;
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.core.content.pm.PackageInfoCompat

import androidx.lifecycle.ViewModel
import com.perkedel.htlauncher.ui.dialog.HomeMoreMenu
import com.perkedel.htlauncher.ui.navigation.HomeScreen
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.navigation.AllAppsScreen
import com.perkedel.htlauncher.ui.navigation.Configurationing
import com.perkedel.htlauncher.ui.theme.rememberColorScheme

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    anViewModel: HTViewModel = viewModel(),
    homePagerState: PagerState = rememberPagerState(pageCount = {10}),
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    colorScheme: ColorScheme = rememberColorScheme()
){
    // https://youtu.be/4gUeyNkGE3g
    // https://github.com/philipplackner/NavigationMultiModule
    // https://youtube.com/shorts/SAD8flVdILY
    // https://www.tutorialspoint.com/how-to-get-the-build-version-number-of-an-android-application
    // https://medium.com/make-apps-simple/get-the-android-app-version-programmatically-5ba27d6a37fe
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.HomeScreen.name
    )
    val hideTopBar:Boolean = false
    val htuiState : HTUIState by anViewModel.uiState.collectAsState()
//    val context: Context = LocalContext.current

    // https://www.tutorialspoint.com/how-to-get-the-build-version-number-of-an-android-application
    // https://medium.com/make-apps-simple/get-the-android-app-version-programmatically-5ba27d6a37fe
    // https://stackoverflow.com/questions/6589797/how-to-get-package-name-from-anywhere
//    try {
        val packageName: String = context.packageName
        val packageInfo:PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            pm.getPackageInfo(packageName, 0)
        }
        val versionName: String = packageInfo.versionName
        val versionNumber: Long = PackageInfoCompat.getLongVersionCode(packageInfo)
//    } catch (e : Exception) {
//        val packageName: String = ""
//        val packageInfo: PackageInfo
//        val versionName: String = ""
//        val versionNumber: Long = 0
//    }
//    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    Surface(
        modifier = Modifier,
        color = when (currentScreen){
            Screen.HomeScreen -> Color.Transparent
            else -> colorScheme.background
        }
    ) {
        Scaffold(
            topBar = {
                HTAppBar(
                    currentScreen = currentScreen,
                    title = {
                        when (currentScreen) {
                            Screen.HomeScreen -> {
                                Text(text = "HT Launcher")
                            }

                            Screen.AllAppsScreen -> {
//                                Text(text = "All Apps | (${pm.getInstalledPackages(0).size})")
                                Text(text = "All Apps | (${pm.getInstalledApplications(0).size})")
                            }

                            Screen.ConfigurationScreen -> {
                                Text(text = "Config | v${versionName}")
                            }

                            else -> {
                                Text(text = "")
                            }
                        }
                    },
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    //                hideIt = hideTopBar
                    hideIt = navController.previousBackStackEntry == null
                )
            },
            containerColor = Color.Transparent,

            ) { innerPadding ->
            //        val uiState by viewModel.uiState.collectAsState()
            NavHost(
                navController = navController,
                startDestination = Screen.HomeScreen.name,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = Screen.HomeScreen.name) {
                    HomeScreen(
                        //                    navController = navController,
                        onAllAppButtonClicked = {
                            navController.navigate(Screen.AllAppsScreen.name)
                        },
                        onMoreMenuButtonClicked = {
                            anViewModel.openTheMoreMenu(true)
                        },
                        handoverPagerState = homePagerState,
                        context = context,
                        colorScheme = colorScheme,
                        haptic = haptic,
                    )
                    if (htuiState.openMoreMenu) {
                        HomeMoreMenu(
                            modifier = Modifier,
                            onChosenMenu = {
                                // https://stackoverflow.com/a/53138234
                                when (it) {
                                    "edit" -> {
                                        // Edit
                                    }

                                    "configuration" -> {
                                        // Configurations
                                        navController.navigate(Screen.ConfigurationScreen.name)
                                    }

                                    "system_setting" -> {
                                        // System Setting
                                        // https://www.geeksforgeeks.org/android-jetpack-compose-open-specific-settings-screen/
                                        //                                    val i = Intent(ACTION_WIRELESS_SETTINGS)
                                        startIntent(
                                            context = context,
                                            what = Settings.ACTION_SETTINGS
                                        )
                                    }

                                    "all_apps" -> {
                                        // All Apps
                                        navController.navigate(Screen.AllAppsScreen.name)
                                    }

                                    else -> {

                                    }
                                }
                                anViewModel.openTheMoreMenu(false)
                            },
                            onDismissRequest = { anViewModel.openTheMoreMenu(false) }
                        )
                    }
                }
                composable(
                    route = Screen.AllAppsScreen.name,

                    ) {
                    AllAppsScreen(
                        navController = navController,
                        pm = pm,
                        context = context,
                        colorScheme = colorScheme,
                        haptic = haptic,
                    )
                }
                composable(
                    route = Screen.ConfigurationScreen.name,
                ) {
                    Configurationing(
                        navController = navController,
                        context = context,
                        pm = pm,
                        haptic = haptic,
                        versionName = versionName,
                        versionNumber = versionNumber,
                    )
                }
            }
        }
    }

}

private fun goBackHome(
    viewModel: ViewModel,
    navController: NavHostController
){
    navController.popBackStack(Screen.HomeScreen.name, inclusive = false)
}


public fun startIntent(context: Context, what: String){
    // https://www.geeksforgeeks.org/android-jetpack-compose-open-specific-settings-screen/
    val i : Intent = Intent(what)
    context.startActivity(i)
}

public fun startIntent(context: Context, what: Intent){
    // https://www.geeksforgeeks.org/android-jetpack-compose-open-specific-settings-screen/
    val i : Intent = what
    context.startActivity(i)
}

public fun startApplication(context: Context, what: String, pm: PackageManager = context.packageManager){
    // https://developer.android.com/reference/android/content/pm/PackageManager.html#getLaunchIntentForPackage(java.lang.String)
    // https://stackoverflow.com/questions/3422758/start-application-knowing-package-name
    val launchIntent : Intent = pm.getLaunchIntentForPackage(what)!!
    startIntent(context,launchIntent)
}