package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
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
    // https://composeexamples.com/components/application-ui/screens/settings
//    val versionName:String = BuildConfig.VERSION_NAME
//    val versionName:String

    ProvidePreferenceLocals {
        LazyColumn(
            modifier = Modifier
        ) {
            // Yoink Big Launcher!!!
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
                icon = { Icon(imageVector = Icons.Default.Translate, contentDescription = null) },
                summary = { Text(text = it) },
            )
            preference(
                key = "display",
                title = { Text(text = "Display" ) },
                icon = { Icon(imageVector = Icons.Default.DisplaySettings, contentDescription = null) },
                onClick = {
                }
            )
            preference(
                key = "accessibility",
                title = { Text(text = "Accessibility" ) },
                icon = { Icon(imageVector = Icons.Default.Accessibility, contentDescription = null) },
                onClick = {
                }
            )
            preference(
                key = "menu_setting",
                title = { Text(text = "Menu Options" ) },
                icon = { Icon(imageVector = Icons.Default.SettingsInputComponent, contentDescription = null) },
                onClick = {
                }
            )

            // Pages
            item() {
//                HorizontalDivider(
//                    modifier = Modifier.padding(vertical = 12.dp)
//                )
                SettingCategoryBar(
                    title = "Pages"
                )
            }
            preference(
                key = "edit",
                title = { Text(text = "Edit Items & Pages" ) },
                icon = { Icon(imageVector = Icons.Default.Edit, contentDescription = null) },
                onClick = {
                }
            )
            preference(
                key = "items_setting",
                title = { Text(text = "Items & Pages Options" ) },
                icon = { Icon(imageVector = Icons.Default.Pages, contentDescription = null) },
                onClick = {
                }
            )

            // Functions
            item() {
//                HorizontalDivider(
//                    modifier = Modifier.padding(vertical = 12.dp)
//                )
                SettingCategoryBar(
                    title = "Functions"
                )
            }
            preference(
                key = "contacts_setting",
                title = { Text(text = "Contacts" ) },
                icon = { Icon(imageVector = Icons.Default.Contacts, contentDescription = null) },
                onClick = {
                }
            )
            preference(
                key = "telephone_setting",
                title = { Text(text = "Telephone" ) },
                icon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
                onClick = {
                }
            )
            preference(
                key = "sms_setting",
                title = { Text(text = "Messages" ) },
                icon = { Icon(imageVector = Icons.Default.Sms, contentDescription = null) },
                onClick = {
                }
            )
            preference(
                key = "sos_setting",
                title = { Text(text = "SOS" ) },
                icon = { Icon(imageVector = Icons.Default.Sos, contentDescription = null) },
                onClick = {
                }
            )
            preference(
                key = "apps_setting",
                title = { Text(text = "Apps" ) },
                icon = { Icon(imageVector = Icons.Default.Apps, contentDescription = null) },
                onClick = {
                }
            )

            // Standards
            item() {
//                HorizontalDivider(
//                    modifier = Modifier.padding(vertical = 12.dp)
//                )
                SettingCategoryBar(
                    title = "Standards"
                )
            }
            preference(
                key = "compatibility_setting",
                title = { Text(text = "Fix Compatibility" ) },
                icon = { Icon(imageVector = Icons.Default.Error, contentDescription = null) },
                onClick = {
                }
            )
            preference(
                key = "terms",
                title = { Text(text = "Terms of Service" ) },
                icon = { Icon(imageVector = Icons.Default.Balance, contentDescription = null) },
                onClick = {
                }
            )
            preference(
                key = "remove_default",
                title = { Text(text = "Remove `HT Launcher` as default launchers" ) },
                icon = { Icon(imageVector = Icons.Default.Restore, contentDescription = null) },
                onClick = {
                }
            )
            preference(
                key = "manual",
                title = { Text(text = "User Manual" ) },
                icon = { Icon(imageVector = Icons.Default.Book, contentDescription = null) },
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

@Composable
fun SettingCategoryBar(
    title:String = "",
    modifier: Modifier = Modifier,
    icon: @Composable() (() -> Unit?)? = null,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
        ,
    ) {
        if(icon != null){
            Box(
                modifier = Modifier.padding(25.dp)
            ){
                icon
            }
        }
        Text(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            text = title
        )
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