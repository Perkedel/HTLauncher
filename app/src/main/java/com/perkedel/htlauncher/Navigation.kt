package com.perkedel.htlauncher

import android.Manifest
import android.content.ContentResolver
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
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.pm.PackageInfoCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.documentfile.provider.DocumentFile

import androidx.lifecycle.ViewModel
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.enumerations.Screen
import com.perkedel.htlauncher.func.createDataStore
//import androidx.wear.compose.material3.ScaffoldState
import com.perkedel.htlauncher.ui.dialog.HomeMoreMenu
import com.perkedel.htlauncher.ui.navigation.HomeScreen
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.dialog.CameraPermissionTextProvider
import com.perkedel.htlauncher.ui.dialog.HTAlertDialog
import com.perkedel.htlauncher.ui.dialog.PermissionDialog
import com.perkedel.htlauncher.ui.dialog.PhoneCallPermissionTextProvider
import com.perkedel.htlauncher.ui.dialog.RecordAudioPermissionTextProvider
import com.perkedel.htlauncher.ui.navigation.AboutTerms
import com.perkedel.htlauncher.ui.navigation.AllAppsScreen
import com.perkedel.htlauncher.ui.navigation.Configurationing
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    prefs: DataStore<Preferences> = remember { createDataStore(context) },
    anViewModel: HTViewModel = viewModel(),
//    homePagerState: PagerState = rememberPagerState(pageCount = {10}),
//    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState:SnackbarHostState = remember {SnackbarHostState()},
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    colorScheme: ColorScheme = rememberColorScheme(),
    permissionRequests: Array<String> = arrayOf(""),
//    permissionStates = rememberMultiplePermissionsState(permissionRequests),
    activityHandOver: ComponentActivity = ComponentActivity()
){
    var homePagerState: PagerState = rememberPagerState(pageCount = {10})

    // Dummies
    val dummyHomeScreen:HomepagesWeHave = HomepagesWeHave(
        pagesPath = listOf(
            "Home",
            "Second",
            "Third",
        )
    )
    val dummyItems:ItemData = ItemData()

    //permission
    //
//    val permissions:Array<String> = if(Build.VERSION.SDK_INT >= 33){
//        arrayOf(
//            Manifest.permission.READ_MEDIA_AUDIO,
//            Manifest.permission.READ_MEDIA_VIDEO,
//            Manifest.permission.READ_MEDIA_IMAGES,
//        )
//    } else {
//        arrayOf(
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        )
//    }
    var multiplePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            // https://github.com/philipplackner/PermissionsGuideCompose/blob/master/app/src/main/java/com/plcoding/permissionsguidecompose/MainActivity.kt
            // https://youtu.be/D3JCtaK8LSU
            permissionRequests.forEach { aPermission ->
                anViewModel.onPermissionResult(
                    permission = aPermission,
                    isGranted = perms[aPermission] == true,
                )
            }
        }
    )

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
        val versionName: String = packageInfo.versionName!!
        val versionNumber: Long = PackageInfoCompat.getLongVersionCode(packageInfo)
//    } catch (e : Exception) {
//        val packageName: String = ""
//        val packageInfo: PackageInfo
//        val versionName: String = ""
//        val versionNumber: Long = 0
//    }
//    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    // LOAD SETTING
    val selectedSaveDir by prefs
        .data
        .map {
            val saveDir = stringPreferencesKey("saveDir")
            it[saveDir] ?: ""
        }
        .collectAsState("")
    try{
    anViewModel.selectSaveDirUri(Uri.parse(selectedSaveDir))
    } catch (e:Exception){
        e.printStackTrace()
    } catch (e:IOException){
        e.printStackTrace()
    }
    // https://medium.com/@yogesh_shinde/implementing-image-video-documents-picker-in-jetpack-compose-73ef846cfffb
    // https://composables.com/jetpack-compose-tutorials/activityresultcontract
    // https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts.OpenDocumentTree
    // https://developer.android.com/training/data-storage/shared/documents-files#perform-operations
    // https://medium.com/@bdulahad/file-from-uri-content-scheme-ac51c10c8331
    // https://stackoverflow.com/questions/2992231/slashes-in-url-variables
    // https://www.w3schools.com/tags/ref_urlencode.ASP
    // https://github.com/coil-kt/coil/discussions/833
    // https://www.nutrient.io/blog/open-pdf-in-jetpack-compose-app/
    // https://stackoverflow.com/questions/76699478/open-simple-text-file-by-open-file-dialog-in-jetpack-compose-and-read-line-by-li
    // https://stackoverflow.com/questions/76699478/open-simple-text-file-by-open-file-dialog-in-jetpack-compose-and-read-line-by-li
    // https://commonsware.com/Jetpack/pages/chap-content-001.html
    // https://www.geeksforgeeks.org/android-jetpack-compose-external-storage/
    // https://github.com/android/storage-samples
    // https://developer.android.com/reference/kotlin/androidx/activity/result/contract/ActivityResultContracts.OpenDocumentTree
    // https://youtu.be/_k4tUuG47TU
    // https://www.linkedin.com/pulse/displaying-files-from-local-storage-jetpack-compose-wajahat-jawaid
    // https://www.linkedin.com/pulse/displaying-files-from-local-storage-jetpack-compose-wajahat-jawaid
            // https://fvilarino.medium.com/using-activity-result-contracts-in-jetpack-compose-14b179fb87de
    // https://github.com/DrVipinKumar/Android-Jetpack-Compose/tree/main/FileRWInternalStorageJetpackCompose
    // https://github.com/philipplackner/PermissionsGuideCompose/blob/master/app/src/main/java/com/plcoding/permissionsguidecompose/MainActivity.kt
    // https://developer.android.com/training/data-storage/shared/documents-files#grant-access-directory
    // https://stackoverflow.com/questions/68730711/how-to-set-text-size-in-android-jetpack-compose-text
    // https://github.com/abdallahmehiz/mpvKt/blob/main/app%2Fsrc%2Fmain%2Fjava%2Flive%2Fmehiz%2Fmpvkt%2Fui%2Fpreferences%2FAdvancedPreferencesScreen.kt WORK
    // https://github.com/abdallahmehiz/mpvKt
    //
//    val saveDirResult = remember { mutableStateOf<Uri?>(null) }
    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    val saveDirResolver = context.contentResolver
    val saveDirLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { dirUri ->
        if (dirUri != null){
            println("Selected Save Dir `${dirUri}`")
//            saveDirResult.value = dirUri
            val totalUriTest = "${htuiState.selectedSaveDir}%2Ftest.json"
//            onSelectedSaveDir(dirUri)
            val jsonTestRaw:MutableState<String> = mutableStateOf<String>("")
            anViewModel.selectSaveDirUri(dirUri)
            saveDirResolver.takePersistableUriPermission(dirUri,takeFlags)
            coroutineScope.launch {
                prefs.edit { dataStore ->
                    val saveDir = stringPreferencesKey("saveDir")
                    dataStore[saveDir] = dirUri.toString()
                }
            }
            println("Let's try test.json!")
            println("Parse URI! ${Uri.parse(totalUriTest)}")
            try {
                val tempFile = kotlin.io.path.createTempFile()
//                println(runCatching {
                    val urei = DocumentFile.fromTreeUri(context,Uri.parse("${htuiState.selectedSaveDir}")!!)!!.findFile("test.json")!!.uri
                println("Urei! ${urei}")
                    jsonTestRaw.value = openATextFile(
//                    Uri.withAppendedPath(htuiState.selectedSaveDir, "test.json"),
//                    htuiState.selectedSaveDir!!.path,
//                    fromTreeUri(context, Uri.withAppendedPath(htuiState.selectedSaveDir, "test.json"))!!.uri,
//                    Uri.parse(totalUriTest), // FUCK YOU WHY CAN'T YOU GIVE ME FUCKING EXAMPLE!??!?!
                        urei, // FRUCKING FRNALLY!!! THANCC MPV KT
                        saveDirResolver
                    )
                    println("JSON Test:\n${jsonTestRaw.value}")
                    println("Find Parser!\n\n${Json.parseToJsonElement(jsonTestRaw.value)}")
                    anViewModel.changeTestResult(jsonTestRaw.value)
//                })
            } catch (e:Exception){
                println("WERROR EXCEPTION")
                e.printStackTrace()
            } catch (e:IOException){
                println("WERROR IOEXCEPTION")
                e.printStackTrace()
            }

            println("Okay Parser Found, let's interpret!")
            try{
                anViewModel.injectTestJsonResult(Json.parseToJsonElement(jsonTestRaw.value))

                println("READ THE INJECT JSON ${htuiState.testJsonElement}")
                println("& TEST VALUE IS `${htuiState.testJsonElement.jsonObject.getValue("test")}`")
            } catch (e:Exception){
                println("WERROR EXCEPTION JSON")
                e.printStackTrace()
            }
        } else {
            println("No Save Dir Selected")
        }
    }
    val testFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        uri ->
        if (uri != null){
            println("File Test:")
            try {
                val jsonTestRaw = openATextFile(uri, saveDirResolver)
                println("JSON Test:\n${jsonTestRaw}")
            } catch (e:Exception){
                println("WERROR EXCEPTION")
                e.printStackTrace()
            } catch (e:IOException){
                println("WERROR IOEXCEPTION")
                e.printStackTrace()
            }
        } else {
            println("No Test File Selected")
        }
    }




    LaunchedEffect(true, htuiState.selectedSaveDir, context) {
        // You must Folders!!
        if(htuiState.selectedSaveDir != null && htuiState.selectedSaveDir.toString().isNotEmpty()){
            getADirectory(htuiState.selectedSaveDir!!, context, "Pages")
            getADirectory(htuiState.selectedSaveDir!!, context, "Items")
            getADirectory(htuiState.selectedSaveDir!!, context, "Themes")
            getADirectory(htuiState.selectedSaveDir!!, context, "Medias")
        } else {

        }
    }

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
            snackbarHost = {
                // https://developer.android.com/develop/ui/compose/components/snackbar
                // https://youtu.be/_yON9d9if6g?si=l5HVadsldckAuErk
                SnackbarHost(
                    hostState = snackbarHostState
                )
            }
            ) { innerPadding ->
            //        val uiState by viewModel.uiState.collectAsState()
            NavHost(
                navController = navController,
                startDestination = Screen.HomeScreen.name,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = Screen.HomeScreen.name) {
                    LaunchedEffect(true) {

                    }
                    if(htuiState.selectedSaveDir != null && htuiState.selectedSaveDir.toString().isNotEmpty()) {
                        // https://dev.to/vtsen/how-to-debug-jetpack-compose-recomposition-with-logging-k7g
                        // https://developer.android.com/reference/android/util/Log
                        Log.d("DebugHomescreen", "Will check ${htuiState.selectedSaveDir}")
                        val urei = getATextFile(htuiState.selectedSaveDir!!,context,"${stringResource(R.string.home_screen_file)}.json")
                        Log.d("DebugHomescreen", "So, there is ${urei}")
                        anViewModel.setHomeScreenJson(urei)
//                        Log.d("DebugHomescreen", "Which contains ${openATextFile(htuiState.coreConfig!!, contentResolver = saveDirResolver)}")
//                        anViewModel.loadHomeScreenJsonElements(Json.decodeFromString<HomepagesWeHave>(openATextFile(htuiState.coreConfig!!, contentResolver = saveDirResolver)))
                        if(htuiState.coreConfigJson != null && htuiState.coreConfigJson.toString().isNotEmpty()){
                            if(htuiState.coreConfigJson!!.pagesPath.isNotEmpty()){
                                Log.d("DebugHomescreen", "There is something!")
                            } else {
                                Log.d("DebugHomescreen", "Still nothing!")
                                anViewModel.loadHomeScreenJsonElements(
                                    dummyHomeScreen
                                )
                            }

                        } else {
                            Log.d("DebugHomescreen", "There is nothing!")
                            anViewModel.loadHomeScreenJsonElements(
                                dummyHomeScreen
                            )
                        }
                    } else {
                        // TODO: when not select, add dummy demo page
                        Log.d("DebugHomescreen", "Literally nothing!")

//                        homePagerState
                        anViewModel.loadHomeScreenJsonElements(
                            dummyHomeScreen
                        )
                    }
                    Log.d("DebugHomescreen","pls check jsona ${htuiState.coreConfigJson}")
//                    try {
                        homePagerState =
//                            rememberPagerState(pageCount = { htuiState.coreConfigJson!!.pagesPath.size })
                            rememberPagerState(pageCount = {
                                var counte:Int = 2
                                counte = if(htuiState.coreConfigJson != null) htuiState.coreConfigJson!!.pagesPath.size else 2
                                counte
                            })
//                    } catch (e:Exception){
//                        e.printStackTrace()
//                    }

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
                        // DONE: handover the homescreen file json
                        configFile = htuiState.coreConfigJson,
                        viewModel = anViewModel,
                        contentResolver = saveDirResolver,
                    )

                    LaunchedEffect(true) {

                    }



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
                        coroutineScope = coroutineScope,
                        snackbarHostState = snackbarHostState,
                        onSnackbarResult = { snackbarResult ->
                            {

                            }
                        }
                    )
                }
                composable(
                    route = Screen.ConfigurationScreen.name,
                ) {
                    val attemptChangeSaveDir = remember { mutableStateOf(false) }
                    val attemptPermission = remember { mutableStateOf(false) }
                    val areYouSureChangeSaveDir = remember { mutableStateOf(false) }
                    Configurationing(
                        navController = navController,
                        context = context,
                        pm = pm,
                        saveDirResult = htuiState.selectedSaveDir,
                        onSelectedSaveDir = {
//                            anViewModel.selectSaveDirUri(it)
                        },
                        onChooseSaveDir = {
//                            areYouSureChangeSaveDir.value = true
                            attemptChangeSaveDir.value = true
                        },
                        onOpenTextFile = {
                            uri, contentResolver -> openATextFile(uri = uri, contentResolver =  contentResolver)
                        },
                        onChooseTextFile = {
                            testFileLauncher.launch(arrayOf<String>("",""))
                        },
                        onCheckPermission = {
                            attemptPermission.value = true
                        },
                        onClickVersion = {
                            navController.navigate(Screen.AboutScreen.name)
                        },
                        testTextResult = htuiState.testResult,
                        haptic = haptic,
                        versionName = versionName,
                        versionNumber = versionNumber,
                    )
                    if(attemptChangeSaveDir.value) {
                        if (htuiState.selectedSaveDir != null) {
                            areYouSureChangeSaveDir.value = true
                        } else {
                            saveDirLauncher.launch(null)
                            attemptChangeSaveDir.value = false
                        }
                    }
                    if (areYouSureChangeSaveDir.value) {
                        HTAlertDialog(
                            onDismissRequest = {
                                areYouSureChangeSaveDir.value = false
                                attemptChangeSaveDir.value = false
                            },
                            swapButton = true,
                            icon = { Icon(Icons.Default.Folder, contentDescription = "Folder Icon")},
                            title = "Configuration Directory",
                            text = "Your Config Folder is currently at:\n${htuiState.selectedSaveDir}",
                            confirmText = "Change",
                            dismissText = "Dismiss",
                            onConfirm = {
                                saveDirLauncher.launch(null)
                                areYouSureChangeSaveDir.value = false
                                attemptChangeSaveDir.value = false
                            },
                        ){

                        }
                    } else {
                        // Close dialog cancel
                        attemptChangeSaveDir.value = false
                    }

                    if(attemptPermission.value){
                        HTAlertDialog(
                            title = "Permissions",
                            text = "Make sure you have granted permission for this app to ensure working experience\nClick `Grant` to proceed, or `Details` to manually grant permissions.",
                            thirdButton = true,
                            confirmText = "Grant",
                            thirdText = "Details",
                            onConfirm = {
                                multiplePermissionLauncher.launch(permissionRequests)
                                attemptPermission.value = false
                            },
                            onThirdButton = {
                                startIntent(
                                    context, Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", packageName, null)
                                    )
                                )
                                attemptPermission.value = false
                            },
                            onDismissRequest = {
                                attemptPermission.value = false
                            }
                        )
                    } else {
//                        attemptPermission.value = false
                    }

                    anViewModel.visiblePermissionDialogQueue
                        .reversed()
                        .forEach{ permission ->
                            PermissionDialog(
                                permissionsTextProvider = when(permission){
                                    Manifest.permission.CAMERA -> {
                                        CameraPermissionTextProvider()
                                    }
                                    Manifest.permission.RECORD_AUDIO -> {
                                        RecordAudioPermissionTextProvider()
                                    }
                                    Manifest.permission.CALL_PHONE -> {
                                        PhoneCallPermissionTextProvider()
                                    }
                                    else -> return@forEach
                                },
                                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                    activityHandOver,
                                    permission,
                                ),
                                onDismiss = anViewModel::dissmissPermissionDialog,
                                onOkClick = {
                                    anViewModel.dissmissPermissionDialog()
                                    multiplePermissionLauncher.launch(
                                        arrayOf(permission)
                                    )
                                },
                                onGoToAppSettingClick = {

                                    startIntent(
                                        context, Intent(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", packageName, null)
                                        )
                                    )
                                }
                            )
                        }
                }
                composable(Screen.AboutScreen.name) {
                    AboutTerms(
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

//public fun startIntent(context: Context, what:ManagedActivityResultLauncher<I,O>, args){
//    what.launch(args)
//}

@Throws(Exception::class)
public fun startApplication(context: Context, what: String, pm: PackageManager = context.packageManager){
    // https://developer.android.com/reference/android/content/pm/PackageManager.html#getLaunchIntentForPackage(java.lang.String)
    // https://stackoverflow.com/questions/3422758/start-application-knowing-package-name
    val launchIntent : Intent = pm.getLaunchIntentForPackage(what)!!
    startIntent(context,launchIntent)
}

@Throws(IOException::class)
public fun openATextFile(uri:Uri, contentResolver: ContentResolver,newLine:Boolean = true):String{
    // https://developer.android.com/training/data-storage/shared/documents-files#input_stream
    val stringBuilder = StringBuilder()
    contentResolver.openInputStream(uri)?.use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line+(if (newLine) "\n" else ""))
                line = reader.readLine()
            }
        }
    }
    return stringBuilder.toString()
}

public fun writeATextFile(uri:Uri, contentResolver: ContentResolver){

}

public fun getATextFile(dirUri:Uri, context: Context, fileName:String = "text.txt", mimeType:String = "text/plain", initData:String = ""): Uri{
    // https://github.com/abdallahmehiz/mpvKt/blob/74d407106e1fb0bae4b7bc66e3b0f83e77a6cbc2/app/src/main/java/live/mehiz/mpvkt/ui/preferences/AdvancedPreferencesScreen.kt#L189
    val thingieTree:DocumentFile = DocumentFile.fromTreeUri(context,dirUri)!!
    // check exist
    return if (thingieTree.findFile(fileName) == null){
        val thingieFile = thingieTree.createFile(mimeType,fileName)!!
        thingieFile.renameTo(fileName)
        Log.d("GetTextFile","Create Text File of URI: ${thingieFile.uri}")
        //TODO: write dummy file
        thingieFile.uri
    } else{
        Log.d("GetTextFile","Found Text File of URI: ${thingieTree.findFile(fileName)!!.uri}")
        thingieTree.findFile(fileName)!!.uri
    }
}

public fun getADirectory(dirUri:Uri, context: Context, dirName:String = "Folder"): Uri{
    // https://github.com/abdallahmehiz/mpvKt/blob/74d407106e1fb0bae4b7bc66e3b0f83e77a6cbc2/app/src/main/java/live/mehiz/mpvkt/ui/preferences/AdvancedPreferencesScreen.kt#L189
    val thingieTree:DocumentFile = DocumentFile.fromTreeUri(context,dirUri)!!
//    var thingieDir = thingieTree.createDirectory(dirName)!!
    return if(thingieTree.findFile(dirName) == null || !thingieTree.findFile(dirName)!!.isDirectory){
        val thingieDir = thingieTree.createDirectory(dirName)!!
//        thingieDir.renameTo(dirName)
        Log.d("GetDirectory","Create Directory of URI: ${thingieDir.uri}")
        thingieDir.uri
    } else {
        Log.d("GetDirectory","Found Directory of URI: ${thingieTree.findFile(dirName)!!.uri}")
        thingieTree.findFile(dirName)!!.uri
    }
}

//private fun setNewSaveDir(intoUri: Uri){
//
//}

//@Composable
//public fun changeSaveDir(): Uri? {
//    var nowUri: Uri? = null
//    val saveDirLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { dirUri ->
//        if (dirUri != null){
//            println("Selected Save Dir `${dirUri}`")
////            saveDirResult.value = dirUri
//
//            nowUri = dirUri
//        } else {
//            println("No Save Dir Selected")
//        }
//
//    }
//    return nowUri
//}