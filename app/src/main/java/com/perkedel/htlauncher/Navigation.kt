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
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.pm.PackageInfoCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.documentfile.provider.DocumentFile

import androidx.lifecycle.ViewModel
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.TestJsonData
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
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
    configuration: Configuration = LocalConfiguration.current,
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
    activityHandOver: ComponentActivity = ComponentActivity(),
    systemUiController: SystemUiController = rememberSystemUiController(),
    json: Json = Json {
        // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
        prettyPrint = true
        encodeDefaults = true
                      },
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
            println("IEAYIE")
            saveDirResolver.takePersistableUriPermission(dirUri,takeFlags)

            println("Saving Setting")
            coroutineScope.launch {
                prefs.edit { dataStore ->
                    val saveDir = stringPreferencesKey("saveDir")
                    dataStore[saveDir] = dirUri.toString()
                }
            }

            println("About to launch a test!")
            coroutineScope.launch {
                println("Let's try test.json!")
                println("Parse URI! ${Uri.parse(totalUriTest)}")
                println("Oh yeah!")
                try {
                    // https://stackoverflow.com/a/24869904/9079640
                    // Java.nio is only available since API 26. Min was 21, crash!

//                val tempFile = kotlin.io.path.createTempFile()
//                println(runCatching {
//                val urei = DocumentFile.fromTreeUri(context,Uri.parse("${htuiState.selectedSaveDir}")!!)!!.findFile("test.json")!!.uri
//                val compatPath =
                    var urei:Uri
//                if(Build.VERSION.SDK_INT >= 26) {
                    urei = getATextFile(
                        dirUri = dirUri,
                        context = context,
                        fileName = "test.json",
                        initData = json.encodeToString<TestJsonData>(TestJsonData(
                            test = "This is a file"
                        )),
                        hardOverwrite = true,
                    )
                    println("Urei! ${urei}")
                    jsonTestRaw.value = openATextFile(
//                    Uri.withAppendedPath(htuiState.selectedSaveDir, "test.json"),
//                    htuiState.selectedSaveDir!!.path,
//                    fromTreeUri(context, Uri.withAppendedPath(htuiState.selectedSaveDir, "test.json"))!!.uri,
//                    Uri.parse(totalUriTest), // FUCK YOU WHY CAN'T YOU GIVE ME FUCKING EXAMPLE!??!?!
                        uri = urei, // FRUCKING FRNALLY!!! THANK YOU MPV KT
                        contentResolver = saveDirResolver,
                        newLine = true,
                    )
                    // https://stackoverflow.com/a/75573771/9079640
                    // https://stackoverflow.com/questions/77073202/getting-unexpected-json-token-at-offset-0-with-kotlin-serialization
//                    Gson().fromJson(jsonTestRaw.value, TypeToken<TestJsonData>(){}.type)
//                    println("JSON Test:\n${jsonTestRaw.value}")
//                    println("Find Parser!\n\n${Json.parseToJsonElement(jsonTestRaw.value)}")
                    println("JSON ABRUR \n${jsonTestRaw.value}")
                    anViewModel.changeTestResult(jsonTestRaw.value)
//                })
//                }
                } catch (e:Exception){
                    println("WERROR EXCEPTION")
                    e.printStackTrace()
                } catch (e:IOException){
                    println("WERROR IOEXCEPTION")
                    e.printStackTrace()
                }

                println("Okay Parser Found, let's interpret!")
                try{
//                anViewModel.injectTestJsonResult(Json.parseToJsonElement(jsonTestRaw.value))
                    anViewModel.injectTestJsonResult(json.decodeFromString<TestJsonData>(jsonTestRaw.value))

                    println("READ THE INJECT JSON ${htuiState.testJsonElement}")
//                println("& TEST VALUE IS `${htuiState.testJsonElement.jsonObject.getValue("test")}`")
                } catch (e:Exception){
                    println("WERROR EXCEPTION JSON")
                    e.printStackTrace()
                }
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
        // Full screen
        // https://stackoverflow.com/a/69689196/9079640
//        systemUiController.isStatusBarVisible = false

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
                                Text(text = stringResource(R.string.app_name))
                            }

                            Screen.AllAppsScreen -> {
                                Column {
                                    //                                Text(text = "All Apps | (${pm.getInstalledPackages(0).size})")
                                    Text(text = "${stringResource(R.string.all_apps)}")
                                    Text(
                                        text = "${pm.getInstalledApplications(0).size} ${stringResource(R.string.unit_packages_installed)}",
                                        fontSize = 8.sp,
                                    )
                                }
                            }

                            Screen.ConfigurationScreen -> {
                                Column {
                                    Text(text = "${stringResource(R.string.configuration_screen)}")
                                    Text(
                                        text = "${stringResource(R.string.version_option)} ${stringResource(R.string.iteration)} (${versionNumber})",
                                        fontSize = 8.sp,
                                    )
                                }

                            }

                            Screen.AboutScreen -> {
                                Column {
                                    Text(text = "${stringResource(R.string.about_screen)}")
                                    Text(
                                        text = "${stringResource(R.string.version_option)} ${versionName}",
                                        fontSize = 8.sp,
                                    )
                                }

                            }

                            else -> {
                                Text(text = currentScreen.name)
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
//                    setStatusBarVisibility(false,systemUiController)
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
                        configuration = configuration,
                        colorScheme = colorScheme,
                        haptic = haptic,
                        // DONE: handover the homescreen file json
                        configFile = htuiState.coreConfigJson,
                        viewModel = anViewModel,
                        contentResolver = saveDirResolver,
                        systemUiController = systemUiController,
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
                            onDismissRequest = {
                                anViewModel.openTheMoreMenu(false)
//                                setStatusBarVisibility(false,systemUiController)
                            }
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
                        },
                        systemUiController = systemUiController,
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
                        systemUiController = systemUiController,
                    )
                    if(attemptChangeSaveDir.value) {
                        if (htuiState.selectedSaveDir != null && htuiState.selectedSaveDir.toString().isNotEmpty()) {
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
                            icon = {
                                Icon(
                                    Icons.Default.Folder,
                                    contentDescription = "Folder Icon"
                                )
                            },
                            title = stringResource(R.string.change_savedir_dialog),
                            text = "${stringResource(R.string.change_savedir_description)}:"
//                            "Your Config Folder is currently at:\n${""
//                                if (htuiState.selectedSaveDir != null && htuiState.selectedSaveDir.toString()
//                                        .isNotEmpty()
//                                ) htuiState.selectedSaveDir else stringResource(R.string.value_unselected)
//                            +""}"
                            ,
                            confirmText = stringResource(R.string.change_savedir_change),
                            dismissText = stringResource(R.string.dismiss_button),
                            onConfirm = {
                                saveDirLauncher.launch(null)
                                areYouSureChangeSaveDir.value = false
                                attemptChangeSaveDir.value = false
                            },
                        ){
                            val decompose = stringResource(R.string.value_unselected)
                            val say = remember {  if (htuiState.selectedSaveDir != null && htuiState.selectedSaveDir.toString()
                                    .isNotEmpty()
                            ) htuiState.selectedSaveDir.toString() else decompose}
                            OutlinedTextField(
                                value = say,
                                trailingIcon = {
                                    Icon(Icons.Default.Folder, "")
                                },
                                label = { Text(stringResource(R.string.directory_option)) },
                                onValueChange = {
//                                    say = it
                                },
                                enabled = false,
                            )
                        }
                    } else {
                        // Close dialog cancel
                        attemptChangeSaveDir.value = false
                    }

                    if (attemptPermission.value) {
                        HTAlertDialog(
                            title = stringResource(R.string.permission_dialog),
                            text = stringResource(R.string.permission_description),
                            thirdButton = true,
                            confirmText = stringResource(R.string.permission_grant),
                            thirdText = stringResource(R.string.permission_details),
                            icon = {
                                Icon(Icons.Default.Security,"")
                            },
                            onConfirm = {
                                Log.d("PermissionDialog","Attempt to run permission grant!")
                                multiplePermissionLauncher.launch(permissionRequests)
                                Log.d("PermissionDialog","Is it done?")
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
                        systemUiController = systemUiController,
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

@Throws(IOException::class)
public fun writeATextFile(uri:Uri, contentResolver: ContentResolver, with:String = ""){
    // https://blog.stackademic.com/android-kotlin-jetpack-compose-save-file-documents-to-files-app-and-solve-item-cant-be-saved-3ed63c422b85
    // https://stackoverflow.com/a/42043137/9079640
    contentResolver.openOutputStream(uri)?.let{ outputStream ->
        outputStream.write(with.toByteArray())

        outputStream.flush()
        outputStream.close()
    }
}


public fun getATextFile(dirUri:Uri, context: Context, fileName:String = "text.txt", mimeType:String = "text/plain", initData:String = "", hardOverwrite:Boolean = false): Uri{
    println("Starting to get file: ${fileName} (mime: ${mimeType}) from ${dirUri}")
    Log.d("GetTextFile","Starting to get file: ${fileName} (mime: ${mimeType}) from ${dirUri}")
    // https://github.com/abdallahmehiz/mpvKt/blob/74d407106e1fb0bae4b7bc66e3b0f83e77a6cbc2/app/src/main/java/live/mehiz/mpvkt/ui/preferences/AdvancedPreferencesScreen.kt#L189
    val thingieTree:DocumentFile = DocumentFile.fromTreeUri(context,dirUri)!!
    // check exist
    return if (thingieTree.findFile(fileName) == null){
        Log.d("GetTextFile","404 NOT FOUND, let's make the file now! at: ${dirUri}")
        val thingieFile = thingieTree.createFile(mimeType,fileName)!!
        if(initData.isNotEmpty() && thingieFile.canWrite()){
            Log.d("GetTextFile","Initialize empty file: ${thingieFile.uri} with these data:\n${initData}")
            try{
                writeATextFile(thingieFile.uri, context.contentResolver, initData)
            } catch (e:Exception){
                // I've heard a young girl somewhere in a realm or init and planet somewhere uttered a swear word
                // `peck neck`. What is that? Is that `fucking hell` / `anjing ngentot` for init realm & init planet?
                // upd: her name was Puella Prescott from Chronicle planet, whose featured in a Jason's TV show. Thanks your Excellency.
                Log.d("GetTextFile", "Peck Neck: EXCEPTION when initializing!")
                e.printStackTrace()
            } catch (e:IOException){
                Log.d("GetTextFile", "Peck Neck: IO-EXCEPTION when initializing!")
                e.printStackTrace()
            }
        }
        thingieFile.renameTo(fileName)
        Log.d("GetTextFile","Create Text File of URI: ${thingieFile.uri}")
        //TODO: write dummy file
        thingieFile.uri
    } else{
        Log.d("GetTextFile","Found Text File of URI: ${thingieTree.findFile(fileName)!!.uri}")
        if(hardOverwrite){
            Log.d("GetTextFile","Hard Overwrite for empty file: ${thingieTree.findFile(fileName)!!.uri}, contains:\n${initData}")
            val thingieFile = thingieTree.findFile(fileName)!!
            if(thingieFile.exists()){
                if(openATextFile(thingieFile.uri, contentResolver = context.contentResolver, newLine = false).isEmpty()){
                    Log.d("GetTextFile","The File is indeed empty! Let's initialize!")
                    if(thingieFile.isFile && thingieFile.canWrite()){
                        writeATextFile(uri = thingieFile.uri, contentResolver = context.contentResolver, with = initData)
                    } else {
                        if(thingieFile.isDirectory) Log.d("GetTextFile", "Wait a second, this is a Folder!")
                        if(!thingieFile.canWrite()) Log.d("GetTextFile", "I somehow cannot write to it!")
                    }
                } else {
                    Log.d("GetTextFile","The File is not empty! leave it alone!")
                }
            }
        }
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

public fun setStatusBarVisibility(into:Boolean, systemUiController: SystemUiController){
    systemUiController.isStatusBarVisible = into
}

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