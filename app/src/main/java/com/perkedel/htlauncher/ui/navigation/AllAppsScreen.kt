package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.startApplication
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.ProvidePreferenceLocals

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllAppsScreen(
    navController: NavController? = rememberNavController(),
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    colorScheme: ColorScheme = rememberColorScheme(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember {SnackbarHostState()},
    onSnackbarResult:(SnackbarResult) -> Unit = {  },
    systemUiController: SystemUiController = rememberSystemUiController(),
    ) {
    // https://stackoverflow.com/questions/64377518/how-to-initialize-or-access-packagemanager-out-from-coroutinecontext
    // https://www.geeksforgeeks.org/different-ways-to-get-list-of-all-apps-installed-in-your-android-phone/
    // https://youtu.be/1Thp0bB5Ev0
    // https://stackoverflow.com/questions/56767624/how-to-load-image-from-drawable-in-jetpack-compose
    // https://stackoverflow.com/questions/10696121/get-icons-of-all-installed-apps-in-android
    // https://www.youtube.com/watch?v=OsNplCy4BKE
    // https://github.com/coil-kt/coil
    // https://github.com/rumboalla/apkupdater/blob/3.x/app/src/main/kotlin/com/apkupdater/ui/component/Image.kt#L53
    // https://github.com/rumboalla/apkupdater/blob/3.x/app/src/main/kotlin/com/apkupdater/ui/component/UiComponents.kt#L57
    // https://developer.android.com/develop/ui/compose/graphics/images/customize
    // https://github.com/rumboalla/apkupdater/blob/3.x/app/src/main/kotlin/com/apkupdater/ui/component/Text.kt#L113
    // https://stackoverflow.com/questions/53607151/how-to-get-installed-packages-using-packagemanager
    // https://developer.android.com/reference/android/content/pm/PackageManager#getInstalledPackages(int)
    // https://developer.android.com/reference/android/content/pm/PackageManager.PackageInfoFlags
    // https://developer.android.com/reference/android/content/pm/PackageManager#GET_ACTIVITIES
    // https://stackoverflow.com/questions/53607151/how-to-get-installed-packages-using-packagemanager
    // https://github.com/rumboalla/apkupdater/blob/3.x/app/src/main/kotlin/com/apkupdater/repository/AppsRepository.kt#L15
//    val pm:PackageManager = getPackageManager()
//    val pm: PackageManager = context.packageManager
//    val pm:PackageManager = context.getApp
    val packList = pm.getInstalledPackages(0)
//    val packList = pm.getInstalledApplications(0)
    val appList = pm.getInstalledApplications(0)

    Box(
        modifier = Modifier
    ){
        ProvidePreferenceLocals {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
//        Text("HAI ALL")
//        items(5){
//            Text("HAH ${it}")
//        }
//        pm.getInstalledPackages(0)
//            itemsIndexed(
//                packList
//            ) { index, packageInf ->
////                Text("${packList.get(index).applicationInfo.loadLabel(pm)}")
//                Preference(
////                    key = "Activation_License",
//                    title = { Text("${packList.get(index).applicationInfo.loadLabel(pm)}") },
//                    icon = {
//                        Icon(
//                            imageVector = Icons.Default.ShoppingCart,
//                            contentDescription = null
//                        )
//                    },
//                    summary = { Text(text = "You are already FULL VERSION.") },
//                    onClick = {
//
//                    }
//                )
//            }

                items(appList.size) {
//                    val ddawe = pm.getApplicationIcon(packList[it].packageName)
                    val ddawe = pm.getApplicationIcon(appList[it].packageName)
                    Preference(
                        icon = {
//                            Icon(
////                                painter = painterResource(packList[it].applicationInfo.icon), // not working
//                                bitmap = ,
//                                contentDescription = ""
//                            )
                            AsyncImage(
                                model = ddawe,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(75.dp),
                            )
                        },
//                        title = { Text("${packList[it].applicationInfo.loadLabel(pm)}") },
                        title = { Text("${appList[it].loadLabel(pm)}") },
//                    summary = {Text("${packList.get(it).applicationInfo.loadDescription(pm)}")},
                        summary = {
                            Text(
//                                packList[it].packageName,
                                appList[it].packageName,
                                modifier = Modifier.basicMarquee(
                                    // https://medium.com/@theAndroidDeveloper/jetpack-compose-gets-official-support-for-marquee-heres-how-to-use-it-1f678aecb851
                                    // https://composables.com/foundation/basicmarquee
                                    spacing = MarqueeSpacing(20.dp),
                                    iterations = Int.MAX_VALUE,
                                    animationMode = MarqueeAnimationMode.Immediately
                                )
                            )
                        },

                        onClick = {
                            try {
//                            startIntent(context, packList[it].applicationInfo.packageName)
//                                startApplication(context, packList[it].applicationInfo.packageName)
                                startApplication(context, appList[it].packageName)
                            } catch (e: Exception) {
//                                println(e)
                                e.printStackTrace()
                                coroutineScope.launch {
                                    onSnackbarResult(snackbarHostState.showSnackbar("WERROR 404! Launcher Activity undefined"))
                                }
                            }


                        }
                    )
                }
            }
        }
    }

}

private fun getAllApps() {

}

@HTPreviewAnnotations
@Composable
fun AllAppsScreenPreview() {
    HTLauncherTheme {
        AllAppsScreen(
        )
    }
}