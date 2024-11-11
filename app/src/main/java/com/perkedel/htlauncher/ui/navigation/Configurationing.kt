package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.footerPreference
import me.zhanghai.compose.preference.listPreference
import me.zhanghai.compose.preference.preference
//import com.perkedel.htlauncher.BuildConfig

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Configurationing(
    navController: NavController = rememberNavController(),
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    versionName:String = "XXXX.XX.XX",
    versionNumber:Long = 0,
){
    // https://www.geeksforgeeks.org/how-to-get-the-build-version-number-of-an-android-application-using-jetpack-compose/
//    val versionName:String = BuildConfig.VERSION_NAME
//    val versionName:String

    ProvidePreferenceLocals {
        LazyColumn(
            modifier = Modifier
        ) {
            preference(
                key = "Activation_License",
                title = { Text(text = "Donate" ) },
                icon = { Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null) },
                summary = { Text(text = "You are already FULL VERSION.") },
                onClick = {

                }
            )
            preference(
                key = "quick_start",
                title = { Text(text = "Read Quick Start Guide" ) },
                icon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) },
                onClick = {

                }
            )
            listPreference(
                key = "select_language",
                title = { Text(text = "Select Language" ) },
                defaultValue = "English (US)",
                values = listOf("English (US)", "Indonesian"),
                icon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null) },
                summary = { Text(text = it) },
            )
            preference(
                key = "display",
                title = { Text(text = "Display" ) },
                icon = { Icon(imageVector = Icons.Default.Build, contentDescription = null) },
                onClick = {
                }
            )
            footerPreference(
                key = "about",
//                title= {},
                summary = { Text(text = "${"HT Launcher"} v${versionName} (Itteration No. ${versionNumber})") },
                modifier = Modifier
                    .combinedClickable(
                        onClick = {

                        },
                        onLongClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            Toast.makeText(context,"HELLO A", Toast.LENGTH_SHORT).show()
                        }
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigurationingPreview(){
    HTLauncherTheme {
        Configurationing(
            navController = rememberNavController(),
            context = LocalContext.current
        )
    }
}