package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.listPreference
import me.zhanghai.compose.preference.preference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllAppsScreen(
    navController: NavController?,
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager
) {
    // https://stackoverflow.com/questions/64377518/how-to-initialize-or-access-packagemanager-out-from-coroutinecontext
    // https://www.geeksforgeeks.org/different-ways-to-get-list-of-all-apps-installed-in-your-android-phone/
//    val pm:PackageManager = getPackageManager()
//    val pm: PackageManager = context.packageManager
//    val pm:PackageManager = context.getApp
    val packList = pm.getInstalledPackages(0)
    ProvidePreferenceLocals {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
//            items(packList.size){
////
//                Box(
//
//                ){
//
////                    preference(
////                        key = "Activation_License",
////                        title = { Text(text = "Donate" ) },
////                        icon = { Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null) },
////                        summary = { Text(text = "You are already FULL VERSION.") },
////                        onClick = {
////
////                        }
////                    )
//                }

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
            navController = rememberNavController()
        )
    }
}