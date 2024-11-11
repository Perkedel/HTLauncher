package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.perkedel.htlauncher.startIntent
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.ProvidePreferenceLocals

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllAppsScreen(
    navController: NavController?,
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    colorScheme: ColorScheme = rememberColorScheme(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    ) {
    // https://stackoverflow.com/questions/64377518/how-to-initialize-or-access-packagemanager-out-from-coroutinecontext
    // https://www.geeksforgeeks.org/different-ways-to-get-list-of-all-apps-installed-in-your-android-phone/
    // https://youtu.be/1Thp0bB5Ev0
//    val pm:PackageManager = getPackageManager()
//    val pm: PackageManager = context.packageManager
//    val pm:PackageManager = context.getApp
    val packList = pm.getInstalledPackages(0)
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
                items(packList.size){
                    Preference(
                        title = {Text("${packList[it].applicationInfo.loadLabel(pm)}")},
//                    summary = {Text("${packList.get(it).applicationInfo.loadDescription(pm)}")},
                        summary = {Text(packList[it].packageName)},

                        onClick = {
//                            startIntent(context, packList[it].applicationInfo.packageName)

                        }
                    )
                }
            }
        }
    }

}

private fun getAllApps() {

}

@Preview(showBackground = true)
@Composable
fun AllAppsScreenPreview() {
    HTLauncherTheme {
        AllAppsScreen(
            navController = rememberNavController(),
        )
    }
}