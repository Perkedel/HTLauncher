@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.perkedel.htlauncher.ui.navigation

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.footerPreference
import me.zhanghai.compose.preference.listPreference
import me.zhanghai.compose.preference.preference
import java.net.URI

//import com.perkedel.htlauncher.BuildConfig

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Configurationing(
    navController: NavController = rememberNavController(),
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    onSelectedSaveDir: (Uri) -> Unit = {  },
    onChooseSaveDir: () -> Unit = {},
    onChooseTextFile: () -> Unit = {},
    onCheckPermission: () -> Unit = {},
    onClickVersion:() -> Unit = {/* GO TO ABOUT SCREEN*/},
    saveDirResult: Uri? = null,
    testTextResult:String = "",
    onOpenTextFile: ((uri:Uri,contentResolver:ContentResolver)->Unit)? = {uri,contentResolver -> {}},
    versionName:String = "XXXX.XX.XX",
    versionNumber:Long = 0,
    systemUiController: SystemUiController = rememberSystemUiController(),
){
    // https://www.geeksforgeeks.org/how-to-get-the-build-version-number-of-an-android-application-using-jetpack-compose/
    // https://composeexamples.com/components/application-ui/screens/settings
//    val versionName:String = BuildConfig.VERSION_NAME
//    val versionName:String
    // https://medium.com/@yogesh_shinde/implementing-image-video-documents-picker-in-jetpack-compose-73ef846cfffb
    // https://composables.com/jetpack-compose-tutorials/activityresultcontract
    // https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts.OpenDocumentTree
//    val saveDirResult = remember { mutableStateOf<Uri?>(null) }
//    val saveDirLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { dirUri ->
//        if (dirUri != null){
//            println("Selected Save Dir `${dirUri}`")
////            saveDirResult.value = dirUri
//
//            onSelectedSaveDir(dirUri)
//        } else {
//            println("No Save Dir Selected")
//        }
//    }

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
//            preference(
//                key = "terms",
//                title = { Text(text = "Terms of Service" ) },
//                icon = { Icon(imageVector = Icons.Default.Balance, contentDescription = null) },
//                onClick = {
            // combined to About screen
//                }
//            )
            preference(
                key = "remove_default",
                title = { Text(text = "Remove `HT Launcher` as default launcher" ) },
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
            preference(
                key = "check_permission",
                title = { Text(text = "Check Permission" ) },
                summary = {
//                    Text(text = "Selected: ${saveDirResult.value}" )
                    Text(text = "Check & grant required permissions" )
                },
                icon = { Icon(imageVector = Icons.Default.Security, contentDescription = null) },
                onClick = onCheckPermission
            )
            preference(
                key = "save_location",
                title = { Text(text = "Change Configuration Folder Location" ) },
                summary = {
//                    Text(text = "Selected: ${saveDirResult.value}" )
                    Text(text = "Selected: ${saveDirResult}" )
                          },
                icon = { Icon(imageVector = Icons.Default.Folder, contentDescription = null) },
                onClick = onChooseSaveDir
            )
            preference(
                key = "debug_testJson",
                title = { Text(text = "[DEBUG] Test Json" ) },
                summary = {
//                    Text(text = "Selected: ${saveDirResult.value}" )
                    Text(text = "Selected file content:\n${testTextResult}" )
                },
                icon = { Icon(imageVector = Icons.Default.Star, contentDescription = null) },
                onClick = onChooseTextFile
            )

            footerPreference(
                key = "about",
//                title= {},
                summary = { Text(text = "${"HT Launcher"} v${versionName} (Itteration No. ${versionNumber})") },
                modifier = Modifier
                    .combinedClickable(
                        onClick = onClickVersion,
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
    haptic: HapticFeedback = LocalHapticFeedback.current,
    icon: @Composable() (() -> Unit?)? = null,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {

                },
                onLongClick = {
                    // TODO: Talkback read this title!
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                onClickLabel = "Category `${title}`"
            )
        ,
    ) {
        if(icon != null){
            // ikr? just eval `(icon)` does not work unlike in most game engines. What a
            Box(
                modifier = Modifier.padding(25.dp)
            ){
                icon
            }
        }
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            text = title
        )
    }
}

@PreviewFontScale
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@Preview(showBackground = true)
@Composable
fun ConfigurationingPreview(){
    HTLauncherTheme {
        Surface(
            color = rememberColorScheme().background
        ) {
            Configurationing(
                navController = rememberNavController(),
                context = LocalContext.current
            )
        }

    }
}

// https://developer.android.com/training/data-storage/shared/documents-files#grant-access-directory
//fun openSaveDir(pickerInitialUri: Uri){
//// Choose a directory using the system's file picker.
//    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
//        // Optionally, specify a URI for the directory that should be opened in
//        // the system file picker when it loads.
//        putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
//    }
//
//    startActivityForResult(intent, 23748923)
//}