@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class
)

package com.perkedel.htlauncher.ui.navigation

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.view.SoundEffectConstants
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.enumerations.ConfigSelected
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.modules.ttsSpeak
import com.perkedel.htlauncher.modules.ttsSpeakOrStop
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import com.perkedel.htlauncher.widgets.SettingCategoryBar
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
    view: View = LocalView.current,
    pm: PackageManager = context.packageManager,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    onSelectedConfigMenu: (ConfigSelected) -> Unit = {
        view.playSoundEffect(SoundEffectConstants.CLICK)
    },
    onSelectedSaveDir: (Uri) -> Unit = {  },
    onChooseSaveDir: () -> Unit = {},
    onChooseTextFile: () -> Unit = {},
    onCheckPermission: () -> Unit = {},
    onClickVersion:() -> Unit = {/* GO TO ABOUT SCREEN*/},
    saveDirResult: Uri? = null,
    testTextResult:String = "",
    onOpenTextFile: ((uri:Uri, contentResolver:ContentResolver)->Unit)? = { uri, contentResolver -> {}},
    versionName:String = "XXXX.XX.XX",
    versionNumber:Long = 0,
    systemUiController: SystemUiController = rememberSystemUiController(),
    viewModel:HTViewModel = HTViewModel(),
    uiState: HTUIState = HTUIState(),
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
){
    // https://www.geeksforgeeks.org/how-to-get-the-build-version-number-of-an-android-application-using-jetpack-compose/
    // https://composeexamples.com/components/application-ui/screens/settings
    // https://youtu.be/AIC_OFQ1r3k
    // https://youtu.be/EqCvUETekjk
    // https://youtu.be/W3R_ETKMj0E List detail screen Pilipp Lackner
//    val versionName:String = BuildConfig.VERSION_NAME
//    val versionName:String
    // https://medium.com/@yogesh_shinde/implementing-image-video-documents-picker-in-jetpack-compose-73ef846cfffb
    // https://composables.com/jetpack-compose-tutorials/activityresultcontract
    // https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts.OpenDocumentTree
    // https://serge-hulne.medium.com/how-to-do-text-to-speech-the-easy-way-with-android-kotlin-compose-2024-628015d4c5c2
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
//    rememberLazyListState()
//    PivotOffsets()
//    LazyColumn(
//        modifier = Modifier,
//        contentPadding = PaddingValues(0.dp),
//        reverseLayout = false,
//        verticalArrangement = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
//        horizontalAlignment = Alignment.Start,
//        userScrollEnabled = true
//    ) { }


    ProvidePreferenceLocals {
        val lazyListState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            reverseLayout = false,
//            horizontalAlignment = Alignment.Start,
            userScrollEnabled = true,
        ) {
            // Yoink Big Launcher!!!
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.donation_option)}. ${context.resources.getString(R.string.donation_option_desc)}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.quick_start_option),
                    )
                ,
                key = "activation_license",
                title = { Text(text = stringResource(R.string.donation_option) ) },
                icon = { Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null) },
                summary = { Text(text = stringResource(R.string.donation_option_desc)) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.Donation)
                }
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.quick_start_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.quick_start_option),
                    )
                ,
                key = "quick_start",
                title = { Text(text = stringResource(R.string.quick_start_option) ) },
                icon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.GetStarted)
                }
            )
            listPreference(
                key = "select_language",
                title = { Text(text = stringResource(R.string.language_option) ) },
                defaultValue = "Automatic",
                values = listOf("Automatic","English (US)", "Indonesian"),
                icon = { Icon(imageVector = Icons.Default.Translate, contentDescription = null) },
                summary = { Text(text = it) },
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.display_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.display_option),
                    )
                ,
                key = "display",
                title = { Text(text = stringResource(R.string.display_option) ) },
                icon = { Icon(imageVector = Icons.Default.DisplaySettings, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.Display)
                }
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.a11y_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.a11y_option),
                    )
                ,
                key = "accessibility",
                title = { Text(text = stringResource(R.string.a11y_option) ) },
                icon = { Icon(imageVector = Icons.Default.Accessibility, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.Accessibility)
                }
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.menus_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.menus_option),
                    )
                ,
                key = "menu_setting",
                title = { Text(text = stringResource(R.string.menus_option) ) },
                icon = { Icon(imageVector = Icons.Default.SettingsInputComponent, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.MenusSettings)
                }
            )

            // Pages
            item() {
//                HorizontalDivider(
//                    modifier = Modifier.padding(vertical = 12.dp)
//                )
                SettingCategoryBar(
                    title = stringResource(R.string.category_type_pages),
                    icon = { Icon(Icons.Default.Pages,"") },
                )
            }
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.level_editor_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.level_editor_option),
                    )
                ,
                key = "edit",
                title = { Text(text = stringResource(R.string.level_editor_option) ) },
                icon = { Icon(imageVector = Icons.Default.Edit, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.LevelEditor)
                }
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.items_pages_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.items_pages_option),
                    )
                ,
                key = "items_setting",
                title = { Text(text = stringResource(R.string.items_pages_option) ) },
                icon = { Icon(imageVector = Icons.Default.Pages, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.ItemsSettings)
                }
            )

            // Functions
            item() {
//                HorizontalDivider(
//                    modifier = Modifier.padding(vertical = 12.dp)
//                )
                SettingCategoryBar(
                    title = stringResource(R.string.category_type_functions),
                    icon = { Icon(Icons.Default.Functions,"") },
                )
            }
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.contacts_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.contacts_option),
                    )
                ,
                key = "contacts_setting",
                title = { Text(text = stringResource(R.string.contacts_option) ) },
                icon = { Icon(imageVector = Icons.Default.Contacts, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.ContactsSettings)
                }
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.telephone_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.telephone_option),
                    )
                ,
                key = "telephone_setting",
                title = { Text(text = stringResource(R.string.telephone_option) ) },
                icon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.TelephoneSettings)
                }
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.messages_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.messages_option),
                    )
                ,
                key = "sms_setting",
                title = { Text(text = stringResource(R.string.messages_option) ) },
                icon = { Icon(imageVector = Icons.Default.Sms, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.MessagesSettings)
                }
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.emergency_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.emergency_option),
                    )
                ,
                key = "sos_setting",
                title = { Text(text = stringResource(R.string.emergency_option) ) },
                icon = { Icon(imageVector = Icons.Default.Sos, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.EmergencySettings)
                }
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.apps_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.apps_option),
                    )
                ,
                key = "apps_setting",
                title = { Text(text = stringResource(R.string.apps_option) ) },
                icon = { Icon(imageVector = Icons.Default.Apps, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.AppsSettings)
                }
            )

            // Standards
            item() {
//                HorizontalDivider(
//                    modifier = Modifier.padding(vertical = 12.dp)
//                )
                SettingCategoryBar(
                    title = stringResource(R.string.category_type_standards),
                    icon = { Icon(Icons.Default.SettingsApplications,"") },
                )
            }
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.compatibility_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.compatibility_option),
                    )
                ,
                key = "compatibility_setting",
                title = { Text(text = stringResource(R.string.compatibility_option) ) },
                icon = { Icon(imageVector = Icons.Default.Error, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.Compatibility)
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
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.remove_default_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.remove_default_option),
                    )
                ,
                key = "remove_default",
                title = { Text(text = stringResource(R.string.remove_default_option) ) },
                icon = { Icon(imageVector = Icons.Default.Restore, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.RemoveDefault)
                }
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.manual_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.manual_option),
                    )
                ,
                key = "manual",
                title = { Text(text = stringResource(R.string.manual_option) ) },
                icon = { Icon(imageVector = Icons.Default.Book, contentDescription = null) },
                onClick = {
                    onSelectedConfigMenu(ConfigSelected.UserManual)
                }
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.permissions_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.permissions_option),
                    )
                ,
                key = "check_permission",
                title = { Text(text = stringResource(R.string.permissions_option) ) },
                summary = {
//                    Text(text = "Selected: ${saveDirResult.value}" )
                    Text(text = "Check & grant required permissions" )
                },
                icon = { Icon(imageVector = Icons.Default.Security, contentDescription = null) },
                onClick = onCheckPermission
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.save_dir_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.save_dir_option),
                    )
                ,
                key = "save_location",
                title = { Text(text = stringResource(R.string.save_dir_option) ) },
                summary = {
//                    Text(text = "Selected: ${saveDirResult.value}" )
                    Text(text = "Selected: ${saveDirResult}" )
                          },
                icon = { Icon(imageVector = Icons.Default.Folder, contentDescription = null) },
                onClick = onChooseSaveDir
            )
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.debug_test_option)}. ${""}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.debug_test_option),
                    )
                ,
                key = "debug_testJson",
                title = { Text(text = stringResource(R.string.debug_test_option) ) },
                summary = {
//                    Text(text = "Selected: ${saveDirResult.value}" )
                    Text(text = "Selected file content:\n${testTextResult}" )
                },
                icon = { Icon(imageVector = Icons.Default.Star, contentDescription = null) },
                onClick = onChooseTextFile,
            )

            footerPreference(
                key = "about",
//                title= {},
                summary = { Text(text = "${"HT Launcher"} v${versionName} (${stringResource(R.string.iteration_option)} ${stringResource(R.string.number_prefix_short_alt)} ${versionNumber})") },
                modifier = Modifier
                    .combinedClickable(
                        onClick = onClickVersion,
                        onLongClick = {
                            val readout = context.resources.getString(R.string.About_version_readout, versionName, versionNumber)
                            ttsSpeakOrStop(
                                handover = tts,
                                message = readout
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                            Toast.makeText(context,"HELLO A", Toast.LENGTH_SHORT).show()
                            Toast.makeText(context,readout, Toast.LENGTH_SHORT).show()
                        },
                        onLongClickLabel = context.resources.getString(R.string.About_version_readout, versionName, versionNumber)
                    )
            )
        }
    }
}

//fun HTpreference(
//    key: String,
//    title: @Composable () -> Unit,
//    modifier: Modifier = Modifier.fillMaxWidth(),
//    enabled: Boolean = true,
//    icon: @Composable (() -> Unit)? = null,
//    summary: @Composable (() -> Unit)? = null,
//    widgetContainer: @Composable (() -> Unit)? = null,
//    onClick: (() -> Unit)? = null,
//    onLongClick: (()-> Unit)? = null,
//){
//    // filter the zhanghai preference item
//
//}

@HTPreviewAnnotations
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