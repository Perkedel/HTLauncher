@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalSharedTransitionApi::class)

package com.perkedel.htlauncher

import android.Manifest
import android.content.ActivityNotFoundException
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
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.SoundEffectConstants
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import java.util.Locale as JavaLocale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.graphics.scaleMatrix
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.documentfile.provider.DocumentFile

import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.platform.LocalSpatialCapabilities
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.TestJsonData
import com.perkedel.htlauncher.constanta.HTLauncherHardcodes
import com.perkedel.htlauncher.enumerations.ActionDataLaunchType
import com.perkedel.htlauncher.enumerations.ActionGoToSystemSetting
import com.perkedel.htlauncher.enumerations.ActionInternalCommand
import com.perkedel.htlauncher.enumerations.ButtonTypes
import com.perkedel.htlauncher.enumerations.ConfigSelected
import com.perkedel.htlauncher.enumerations.EditWhich
import com.perkedel.htlauncher.enumerations.Screen
import com.perkedel.htlauncher.func.createDataStore
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.modules.ttsSpeakInterrupt
import com.perkedel.htlauncher.modules.ttsSpeakOrStop
import com.perkedel.htlauncher.ui.activities.ItemEditorActivity
//import androidx.wear.compose.material3.ScaffoldState
import com.perkedel.htlauncher.ui.dialog.HomeMoreMenu
import com.perkedel.htlauncher.ui.navigation.HomeScreen
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.dialog.CameraPermissionTextProvider
import com.perkedel.htlauncher.ui.dialog.HTAlertDialog
import com.perkedel.htlauncher.ui.dialog.PermissionDialog
import com.perkedel.htlauncher.ui.dialog.PhoneCallPermissionTextProvider
import com.perkedel.htlauncher.ui.dialog.PhoneStatePermissionTextProvider
import com.perkedel.htlauncher.ui.dialog.ReadFilePermissionTextProvider
import com.perkedel.htlauncher.ui.dialog.RecordAudioPermissionTextProvider
import com.perkedel.htlauncher.ui.dialog.TextInputDialog
import com.perkedel.htlauncher.ui.dialog.WriteFilePermissionTextProvider
import com.perkedel.htlauncher.ui.navigation.AboutTerms
import com.perkedel.htlauncher.ui.navigation.AllAppsScreen
import com.perkedel.htlauncher.ui.navigation.Configurationing
import com.perkedel.htlauncher.ui.navigation.ItemsExplorer
import com.perkedel.htlauncher.ui.navigation.LevelEditor
import com.perkedel.htlauncher.ui.navigation.StandalonePageScreen
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import com.perkedel.htlauncher.widgets.ContainsSharedTransition
import com.perkedel.htlauncher.widgets.HTButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
    configuration: Configuration = LocalConfiguration.current,
    pm: PackageManager = context.packageManager,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    dataStorePrefs: DataStore<Preferences> = createDataStore(context),
    prefs: DataStore<Preferences> = remember { dataStorePrefs },
    selectedSaveDir: Flow<String> = remember { dataStorePrefs.data.map { it[stringPreferencesKey("saveDir")] ?: "" } },
    anViewModel: HTViewModel = rememberSaveable { HTViewModel() },
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
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    view: View = LocalView.current,
    inspectionMode:Boolean = LocalInspectionMode.current,
//    xrSession = checkNotNull(LocalSession.current),
//    animatedContentTransitionScope: AnimatedContentTransitionScope =
){
    // https://developer.android.com/codelabs/large-screens/add-keyboard-and-mouse-support-with-compose#0

    // TODO: go back to page 0 if on Homescreen, and press back button.

    var homePagerState: PagerState = rememberPagerState(pageCount = {10})
//    val xrSession = checkNotNull(LocalSession.current)
//    val xrSession = LocalSession.current
    val uiIsSpatialized = LocalSpatialCapabilities.current.isSpatialUiEnabled
    val toggleXrMode = {
//        try{
//            val xrSession = checkNotNull(LocalSession.current)
//
//        } catch (e:Exception){
//
//        }
//        xrSession?.let {
//            if (uiIsSpatialized) {
//                { it.requestHomeSpaceMode() }
//            } else {
//                { it.requestFullSpaceMode() }
//            }
//        }
    }
    val setXrMode:(Boolean) -> Unit = { into ->
//        xrSession?.let{
//            if(into){
//                it.requestFullSpaceMode()
//            } else {
//                it.requestHomeSpaceMode()
//            }
//        }
    }

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
//    val currentScreen = Screen.valueOf(
//        backStackEntry?.destination?.route ?: Screen.HomeScreen.name
//    )
    val currentScreen:String = backStackEntry?.destination?.route ?: Screen.HomeScreen.name
    var hideTopBar: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
    val htuiState : HTUIState by anViewModel.uiState.collectAsState()
//    val context: Context = LocalContext.current

    // https://www.tutorialspoint.com/how-to-get-the-build-version-number-of-an-android-application
    // https://medium.com/make-apps-simple/get-the-android-app-version-programmatically-5ba27d6a37fe
    // https://stackoverflow.com/questions/6589797/how-to-get-package-name-from-anywhere
//    try {
        val packageName: String = context.packageName
        val packageInfo:PackageInfo =
//        if(!inspectionMode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                pm.getPackageInfo(packageName, 0)
            }
//        } else {
//            PackageInfo()
//        }
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
//    val selectedSaveDir by prefs
//        .data
//        .map {
//            val saveDir = stringPreferencesKey("saveDir")
//            it[saveDir] ?: ""
//        }
//        .collectAsState("")
    val selectedSaveDirState by selectedSaveDir.collectAsState(htuiState.selectedSaveDir?.toString() ?: "")
//    val ramSaveDir by rememberSaveable { mutableStateOf(selectedSaveDir) }
    LaunchedEffect(
//        key1 = 0,
//        prefs,
//        selectedSaveDir,
        selectedSaveDirState,
//        prefs.data.map {
//            val saveDir = stringPreferencesKey("saveDir")
//        },
//        htuiState.selectedSaveDir,
//        Uri.parse(selectedSaveDir)
    ) {
        try{
            if(!htuiState.inited && selectedSaveDirState != null && selectedSaveDirState.isNotBlank()){
                anViewModel.selectSaveDirUri(Uri.parse(selectedSaveDirState))
            }
        } catch (e:Exception){
            e.printStackTrace()
        } catch (e:IOException){
            e.printStackTrace()
        }
    }
    LaunchedEffect(
        key1 = hideTopBar.value
    ) {
        setXrMode(hideTopBar.value)
        if(hideTopBar.value){

        } else {

        }
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
    val listOfFolder = listOf(
        context.resources.getString(R.string.pages_folder),
        context.resources.getString(R.string.items_folder),
        context.resources.getString(R.string.themes_folder),
        context.resources.getString(R.string.medias_folder),
        context.resources.getString(R.string.shortcuts_folder),
    )
    val folders: MutableMap<String,Uri> = LinkedHashMap<String,Uri>()
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
            coroutineScope.launch {
                anViewModel.preloadFiles(context,saveDirResolver,htuiState, listOfFolder = listOfFolder, folders = folders, json = json, force = true)
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



    var initing:Boolean by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(htuiState.selectedSaveDir) {
//        val initing = htuiState.inited
//        initing = htuiState.inited
        initing = false
        Log.d("Recompose","Recompose Inited is ${initing}")
//        val preloadThing = async {
//            // https://medium.com/@rajputmukesh748/mastering-async-and-await-in-kotlin-coroutines-833e57fa0e8f
////            delay(5000)
//            anViewModel.preloadFiles(
//                context = context,
//                contentResolver = saveDirResolver,
//                uiStating = htuiState,
//                listOfFolder = listOfFolder,
//                folders = folders,
//                json = json,
//            )
//        }
//        preloadThing.await()
//        val preloadThing = anViewModel.preloadFiles(
//            context = context,
//            contentResolver = saveDirResolver,
//            uiStating = htuiState,
//            listOfFolder = listOfFolder,
//            folders = folders,
//            json = json,
//        )
//        preloadThing

        coroutineScope.launch {
//            if(htuiState.selectedSaveDir != null) {
//                anViewModel.setIsReady(false)
                if (!initing) {
                    anViewModel.preloadFiles(
                        context = context,
                        contentResolver = saveDirResolver,
                        uiStating = htuiState,
                        listOfFolder = listOfFolder,
                        folders = folders,
                        json = json,
//                    force = true,
                    )
                    initing = true
                } else {
                    anViewModel.setIsReady(true)
                }
//                anViewModel.setIsReady(true)
//            } else {
////                anViewModel.setIsReady(true)
//            }
        }

//        coroutineScope.launch {
//
//            // Full screen
//            // https://stackoverflow.com/a/69689196/9079640
////        systemUiController.isStatusBarVisible = false
//
//            // You must Folders!!
//            val homeSafData:HomepagesWeHave = HomepagesWeHave()
//            if(htuiState.selectedSaveDir != null && htuiState.selectedSaveDir.toString().isNotEmpty()){
//                for(a in listOfFolder){
//                    folders[a] = getADirectory(htuiState.selectedSaveDir!!, context, a)
//                    Log.d("FolderQuery", "Folder ${folders[a]} queried")
//                }
//                for(i in folders){
//                    Log.d("InitFileLoader","Folder ${i.key} we have ${i.value}")
//                }
////            getADirectory(htuiState.selectedSaveDir!!, context, "Items")
////            getADirectory(htuiState.selectedSaveDir!!, context, "Themes")
////            getADirectory(htuiState.selectedSaveDir!!, context, "Medias")
//
//
//                val homeSaf:String = json.encodeToString<HomepagesWeHave>(homeSafData)
//                val homeSafFileUri = getATextFile(
//                    dirUri = htuiState.selectedSaveDir!!,
//                    context = context,
//                    fileName = "${context.resources.getString(R.string.home_screen_file)}.json",
//                    initData = homeSaf,
//                    hardOverwrite = true,
//                )
//                Log.d("InitFileLoader", "Pls Homescreen:\n${homeSaf}")
//                anViewModel.setHomeScreenJson(
//                    homeSafFileUri
//                )
//                Log.d("InitFileLoader", "Pls the file Homescreen ${htuiState.coreConfig}")
//
//
//            } else {
//                Log.d("InitFileLoader", "Save Dir Not Selected")
//            }
//
//
//            if(htuiState.selectedSaveDir != null && htuiState.selectedSaveDir.toString().isNotEmpty()) {
//                // https://dev.to/vtsen/how-to-debug-jetpack-compose-recomposition-with-logging-k7g
//                // https://developer.android.com/reference/android/util/Log
//                // https://stackoverflow.com/a/74044617/9079640
//                Log.d("DebugHomescreen", "Will check ${htuiState.selectedSaveDir}")
//                if (htuiState.coreConfig != null && htuiState.coreConfig.toString().isNotEmpty()) {
//                    Log.d("DebugHomescreen", "There is something!")
//                    val fileStream:String = openATextFile(
//                        uri = htuiState.coreConfig!!,
//                        contentResolver = saveDirResolver
//                    )
//                    Log.d("DebugHomescreen", "It contains:\n${fileStream}")
//                    anViewModel.loadHomeScreenJsonElements(
//                        json.decodeFromString<HomepagesWeHave>(
//                            fileStream
//                        )
//                    )
//                } else {
//                    Log.d("DebugHomescreen", "There is nothing!")
//                    anViewModel.loadHomeScreenJsonElements(
//                        homeSafData
//                    )
//                }
//            } else {
//                // DONE: when not select, add dummy demo page
//                Log.d("DebugHomescreen", "Literally nothing!")
//                anViewModel.loadHomeScreenJsonElements(
//                    homeSafData
//                )
//            }
//
//            // Load Pages & Items
//            if(htuiState.testPreloadAll && htuiState.coreConfigJson != null && folders[context.resources.getString(R.string.pages_folder)] != null){
//                for(i in htuiState.coreConfigJson!!.pagesPath){
//                    Log.d("PageLoader","Checking page ${i}")
//                    Log.d("PageLoader","Eval context ${context}")
//                    Log.d("PageLoader","Eval resource name ${context.resources.getString(R.string.pages_folder)}")
//                    Log.d("PageLoader","Eval dirUri ${folders[context.resources.getString(R.string.pages_folder)]}")
//
//                    var aPage:PageData = PageData()
//
//                    if(htuiState.pageList.contains(i) && htuiState.pageList[i] != null &&
//                        isFileExist(
//                            dirUri = folders[context.resources.getString(R.string.pages_folder)]!!,
//                            context = context, fileName = "$i.json")
//                        ){
//                        Log.d("PageLoader", "Already Exist ${htuiState.itemList[i]}")
//                        aPage = htuiState.pageList[i]!!
//                    } else {
//                        val aPageUri: Uri = getATextFile(
//                            dirUri = folders[context.resources.getString(R.string.pages_folder)]!!,
//                            context = context,
//                            initData = json.encodeToString<PageData>(PageData()),
//                            fileName = "$i.json",
//                            hardOverwrite = false,
//                        )
//                        Log.d("PageLoader", "Page URI in total ${aPageUri}")
//                        aPage = json.decodeFromString<PageData>(
//                            openATextFile(
//                                uri = aPageUri,
//                                contentResolver = saveDirResolver,
//                            )
//                        )
//                        if (htuiState.pageList.contains(i)) {
//                            // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/contains-key.html
//                            Log.d("PageLoader", "Key $i Exist!")
//                        } else {
//                            Log.d("PageLoader", "Key $i 404 NOT FOUND!")
//                        }
//                        htuiState.pageList[i] = aPage
//                    }
//
//                    // item
//                    for (j in aPage.items) {
//                        Log.d("ItemLoader", "Checking item ${j}")
//                        var aItem: ItemData = ItemData()
//                        if (htuiState.itemList.contains(j) && htuiState.itemList[j] != null) {
//                            Log.d("ItemLoader", "Already Exist ${htuiState.itemList[j]}")
//                            aItem = htuiState.itemList[j]!!
//                        } else {
//                            val aItemUri: Uri = getATextFile(
//                                dirUri = folders[context.resources.getString(R.string.items_folder)]!!,
//                                context = context,
//                                initData = json.encodeToString<ItemData>(ItemData()),
//                                fileName = "$j.json",
//                                hardOverwrite = false,
//                            )
//                            aItem = json.decodeFromString<ItemData>(
//                                openATextFile(
//                                    uri = aItemUri,
//                                    contentResolver = saveDirResolver,
//                                )
//                            )
//                            if (htuiState.itemList.contains(j)) {
//                                // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/contains-key.html
//                                Log.d("ItemLoader", "Key $j Exist!")
//                            } else {
//                                Log.d("ItemLoader", "Key $j 404 NOT FOUND!")
//                            }
//                            htuiState.itemList[j] = aItem
//                        }
//                    }
//                }
//            }
//            anViewModel.setIsReady(true)
//        }
    }



    Surface(
        modifier = Modifier,
        color = when{
            currentScreen == Screen.HomeScreen.name -> Color.Transparent
            currentScreen == Screen.AllAppsScreen.name -> Color.Transparent
            currentScreen.startsWith("${context.packageName}.data.PageData") -> Color.Transparent
            else -> colorScheme.background
        }
    ) {
        Scaffold(
            topBar = {
                HTAppBar(
                    currentScreen = currentScreen,
                    iconModel = when{
                        currentScreen.startsWith("${context.packageName}.data.PageData") -> {
                            htuiState.currentPageIconModel
                        }
                        else -> null
                    },
                    textTitle= when{
                        // https://www.dhiwise.com/post/the-basics-of-kotlin-string-contains-a-beginners-guide
                        currentScreen == Screen.HomeScreen.name -> stringResource(R.string.app_name)
                        currentScreen == Screen.AllAppsScreen.name -> stringResource(R.string.all_apps)
                        currentScreen == Screen.ConfigurationScreen.name -> stringResource(R.string.configuration_screen)
                        currentScreen == Screen.AboutScreen.name -> stringResource(R.string.about_screen)
                        currentScreen == Screen.LevelEditor.name -> stringResource(R.string.editor_screen)
                        currentScreen == Screen.ItemsExplorer.name -> "${stringResource(R.string.items_explorer_screen)} ${stringResource(htuiState.toEditWhatFile.label)}"
                        currentScreen.startsWith("${context.packageName}.data.PageData") -> {
                            val backStackName:String = backStackEntry?.toRoute<PageData>()?.name ?: PageData().name
                            when{
                                backStackName == stringResource(R.string.internal_pages_settings) -> stringResource(R.string.internal_pages_settings_label)
                                else -> backStackName
                            }
                        }
                        else -> currentScreen
                    },
                    textDescription = when(currentScreen){
                        Screen.HomeScreen.name -> null
//                        Screen.AllAppsScreen.name -> "${pm.getInstalledApplications(0).size} ${stringResource(R.string.unit_packages_installed)}"
//                        Screen.AllAppsScreen.name -> "${htuiState.installedPackageInfo} ${stringResource(R.string.unit_packages_installed)}"
                        Screen.AllAppsScreen.name -> {
                            val allAppThing = anViewModel._appAll.collectAsState()
                            val sayPluralLaunchable: String = pluralStringResource(
                                R.plurals.unit_packages_launchable,
                                allAppThing.value.size,
                                allAppThing.value.size,
                            )
                            val sayPluralAll: String = pluralStringResource(
                                R.plurals.unit_packages_installed,
                                pm.getInstalledApplications(0).size,
                                pm.getInstalledApplications(0).size,
                            )
                            stringResource(
                                R.string.unit_package_bridge,
                                sayPluralLaunchable,
                                sayPluralAll
                            )
                        }

                        Screen.ConfigurationScreen.name -> "${stringResource(R.string.version_option)} ${versionName} (${
                            stringResource(
                                R.string.iteration
                            )
                        } ${versionNumber})"

                        Screen.AboutScreen.name -> "${stringResource(R.string.version_option)} ${versionName} (${stringResource(R.string.iteration)} ${versionNumber})"
                        else -> null
                    },
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = {
//                        if(navController.previousBackStackEntry?. == true){
//
//                        }
                        navController.navigateUp()
                    },
                    //                hideIt = hideTopBar
                    hideIt = navController.previousBackStackEntry == null && htuiState.isReady,
                    hideMenuButton = when{
                        currentScreen == Screen.HomeScreen.name -> false
                        currentScreen.startsWith("${context.packageName}.data.PageData") -> false
                        else -> true
                    },
                    actions = {
                        when(currentScreen){
                            Screen.HomeScreen.name -> {

                            }
                            Screen.ItemsExplorer.name -> {
                                HTButton(
                                    buttonType = ButtonTypes.IconButton,
                                    leftIcon = Icons.Default.Add,
                                    onClick = {
                                        anViewModel.openCreateNewFile(true)
                                    }
                                )
                            }
                            else -> {}
                        }
                    },
                    onMoreMenu = {
                        when{
                            currentScreen == Screen.HomeScreen.name && !htuiState.isReady -> {
                                anViewModel.openTheMoreMenu(true)
                            }
                            currentScreen.startsWith("${context.packageName}.data.PageData") -> {
                                anViewModel.openTheMoreMenu(true)
                            }
                            else -> null
                        }
                    }
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
            // https://youtu.be/MhXa-5Arw3Q Land of Coding Animation Transition
            // https://github.com/mohammednawas8
            // https://github.com/lofcoding/ComposeNavigationTransitions
            //        val uiState by viewModel.uiState.collectAsState()
            SharedTransitionLayout {
                NavHost(
                    navController = navController,
                    startDestination = Screen.HomeScreen.name,
                    modifier = Modifier.padding(innerPadding),
                ) {
                    composable(route = Screen.HomeScreen.name,
                        exitTransition = {
                            when{
                                // https://developer.android.com/develop/ui/compose/animation/composables-modifiers#enter-exit-transition
//                            currentScreen.startsWith("${context.packageName}.data.PageData") ->
                                else ->scaleOut(

                                )
                            }

                        },
                        popEnterTransition = {
                            scaleIn()
                        }
                    ) {
                        LaunchedEffect(true) {

                        }
//                    setStatusBarVisibility(false,systemUiController)

//                        Log.d("DebugHomescreen","pls check jsona ${htuiState.coreConfigJson}")
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

                        ContainsSharedTransition(
                            animatedVisibilityScope = this
                        ){
                            HomeScreen(
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
                                uiState = htuiState,
                                coroutineScope = coroutineScope,
                                isReady = htuiState.isReady,
                                tts = tts,
                                onLaunchOneOfAction = {
                                    onLaunchAction(
                                        data = it,
                                        context = context,
                                        coroutineScope = coroutineScope,
                                        pm = pm,
                                        snackbarHostState = snackbarHostState,
                                        contentResolver = saveDirResolver,
                                        navController = navController,
                                        viewModel = anViewModel,
                                        uiState = htuiState,
                                        inspectionMode = inspectionMode,
                                        handoverPagerState = homePagerState,
                                        tts = tts,
                                    )
                                }
                            )
                        }


                        LaunchedEffect(true) {

                        }

                    }
                    composable<PageData>(
//                composable(
//                    route = "${Screen.OpenAPage.name}/{name}"
                        exitTransition = {
                            scaleOut(

                            )
                        },
                        enterTransition = {
                            scaleIn(

                            )
                        },
                        popEnterTransition = {
                            scaleIn()
                        },
                        popExitTransition = {
                            scaleOut()
                        }
                    ) { pagingData ->
                        // https://youtu.be/AIC_OFQ1r3k
//                        Log.d("OpenAPage","Page Opened $currentScreen")
                        val args = pagingData.toRoute<PageData>()
                        StandalonePageScreen(
                            pageData = args,
                            onAllAppButtonClicked = {
                                navController.navigate(Screen.AllAppsScreen.name)
                            },
                            onMoreMenuButtonClicked = {
                                anViewModel.openTheMoreMenu(true)

                            },
                            context = context,
                            configuration = configuration,
                            colorScheme = colorScheme,
                            haptic = haptic,
                            // DONE: handover the homescreen file json
                            configFile = htuiState.coreConfigJson,
                            viewModel = anViewModel,
                            contentResolver = saveDirResolver,
                            systemUiController = systemUiController,
                            uiState = htuiState,
                            coroutineScope = coroutineScope,
                            isReady = htuiState.isReady,
                            tts = tts,
                            onLaunchOneOfAction = {
                                onLaunchAction(
                                    data = it,
                                    context = context,
                                    coroutineScope = coroutineScope,
                                    pm = pm,
                                    snackbarHostState = snackbarHostState,
                                    contentResolver = saveDirResolver,
                                    navController = navController,
                                    viewModel = anViewModel,
                                    uiState = htuiState,
                                    inspectionMode = inspectionMode,
                                    handoverPagerState = homePagerState,
                                    tts = tts,
                                )
                            }
                        )
                    }
                    composable(route = Screen.AllAppsScreen.name,
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                animationSpec = tween(),
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                animationSpec = tween(),
                            )
                        }
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
                            tts = tts,
                            onLaunchApp = {
                                try {
                                    startApplication(context, it.packageName)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("WERROR 404! Launcher Activity undefined")
                                    }
                                } catch (e: ActivityNotFoundException){
                                    // https://youtu.be/2hIY1xuImuQ
                                    // https://youtu.be/2hIY1xuImuQ
                                    e.printStackTrace()
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("WERROR 404! Launcher Activity undefined")
                                    }
                                }
                            },
                            anViewModel = anViewModel
                        )
                    }
                    composable(route = Screen.ConfigurationScreen.name,
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                animationSpec = tween(),
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(),
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween()
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                animationSpec = tween()
                            )
                        }
                    ) {
                        val attemptChangeSaveDir = remember { mutableStateOf(false) }
                        val attemptPermission = remember { mutableStateOf(false) }
                        val areYouSureChangeSaveDir = remember { mutableStateOf(false) }
                        LaunchedEffect(htuiState.editingLevel) {
                            if(htuiState.editingLevel){
                                coroutineScope.launch {
                                    anViewModel.preloadFiles(
                                        context = context,
                                        contentResolver = saveDirResolver,
                                        uiStating = htuiState,
                                        listOfFolder = listOfFolder,
                                        folders = folders,
                                        json = json,
                                        force = true,
                                    )
                                    anViewModel.setEditingLevel(false)
                                    snackbarHostState.showSnackbar(
                                        message = context.resources.getString(R.string.reloading_save)
                                    )
                                }
                            }
                        }
                        Configurationing(
                            navController = navController,
                            context = context,
                            pm = pm,
                            saveDirResult = htuiState.selectedSaveDir,
                            onSelectedConfigMenu = { configSelect ->
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                when(configSelect){
                                    ConfigSelected.Donation -> {

                                    }
                                    ConfigSelected.LevelEditor -> {
                                        navController.navigate(Screen.LevelEditor.name)
                                    }
                                    ConfigSelected.ItemsExplorer -> {}
                                    else -> {}
                                }
                            },
                            onSelectedSaveDir = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
//                            anViewModel.selectSaveDirUri(it)
                            },
                            onChooseSaveDir = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
//                            areYouSureChangeSaveDir.value = true
                                attemptChangeSaveDir.value = true
                            },
                            onOpenTextFile = { uri, contentResolver ->
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                openATextFile(uri = uri, contentResolver =  contentResolver)
                            },
                            onChooseTextFile = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                testFileLauncher.launch(arrayOf<String>("",""))
                            },
                            onCheckPermission = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                attemptPermission.value = true
                            },
                            onClickVersion = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                navController.navigate(Screen.AboutScreen.name)
                            },
                            testTextResult = htuiState.testResult,
                            haptic = haptic,
                            versionName = versionName,
                            versionNumber = versionNumber,
                            systemUiController = systemUiController,
                            uiState = htuiState,
                            viewModel = anViewModel,
                            tts = tts,
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
                                tts = tts,
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
                                },
                                tts = tts,
                            )
                        } else {
//                        attemptPermission.value = false
                        }

                        anViewModel.visiblePermissionDialogQueue
                            .reversed()
                            .forEach{ permission ->
                                PermissionDialog(
                                    permissionsTextProvider = when(permission){
                                        Manifest.permission.READ_EXTERNAL_STORAGE -> {
                                            ReadFilePermissionTextProvider()
                                        }
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                                            WriteFilePermissionTextProvider()
                                        }
                                        Manifest.permission.CAMERA -> {
                                            CameraPermissionTextProvider()
                                        }
                                        Manifest.permission.RECORD_AUDIO -> {
                                            RecordAudioPermissionTextProvider()
                                        }
                                        Manifest.permission.CALL_PHONE -> {
                                            PhoneCallPermissionTextProvider()
                                        }
                                        Manifest.permission.READ_PHONE_STATE -> {
                                            PhoneStatePermissionTextProvider()
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
                    composable(route = Screen.AboutScreen.name,
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(),
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(),
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween()
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween()
                            )
                        }
                    ) {
                        AboutTerms(
                            navController = navController,
                            context = context,
                            pm = pm,
                            haptic = haptic,
                            versionName = versionName,
                            versionNumber = versionNumber,
                            systemUiController = systemUiController,
                            tts = tts,
                        )
                    }
                    composable(route = Screen.LevelEditor.name,
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(),
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(),
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween()
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween()
                            )
                        }
                    ) {
                        LaunchedEffect(htuiState.editingLevel) {
//                        if(!htuiState.editingLevel && currentScreen == Screen.LevelEditor){
//
//                            anViewModel.setEditingLevel(true)
//                        }
                        }

                        LevelEditor(
                            navController = navController,
                            context = context,
                            pm = pm,
                            saveDirResult = htuiState.selectedSaveDir,
                            onSelectedSaveDir = {
//                            anViewModel.selectSaveDirUri(it)
                            },
                            onChooseSaveDir = {

                            },
                            onOpenTextFile = {
                                    uri, contentResolver -> openATextFile(uri = uri, contentResolver =  contentResolver)
                            },
                            onChooseTextFile = {
                                testFileLauncher.launch(arrayOf<String>("",""))
                            },
                            onCheckPermission = {
                            },
                            onClickVersion = {
                                navController.navigate(Screen.AboutScreen.name)
                            },
                            testTextResult = htuiState.testResult,
                            haptic = haptic,
                            versionName = versionName,
                            versionNumber = versionNumber,
                            systemUiController = systemUiController,
                            uiState = htuiState,
                            viewModel = anViewModel,
                            onEditWhat = { selection ->
                                Log.d("LevelEditor", "Trying to edit ${selection}")
                                anViewModel.setEditWhich(selection)
                                if(selection != EditWhich.Home) {
                                    navController.navigate(Screen.ItemsExplorer.name)
                                } else {
                                    if(htuiState.selectedSaveDir != null) {
                                        if (!htuiState.editingLevel && currentScreen == Screen.LevelEditor.name) {

                                            anViewModel.setEditingLevel(true)
                                        }
                                        var toIntent = Intent(
                                            context, ItemEditorActivity::class.java
                                        )
                                        val theUri = getATextFile(
                                            dirUri = htuiState.selectedSaveDir!!,
                                            context = context,
                                            fileName = "${context.resources.getString(R.string.home_screen_file)}.json",
                                            mimeType = context.resources.getString(R.string.text_plain_type),
                                            initData = json.encodeToString<HomepagesWeHave>(
                                                HomepagesWeHave()
                                            ),
                                            hardOverwrite = false,
                                        )
                                        toIntent.apply {
                                            type = context.resources.getString(R.string.text_plain_type)
                                        }
                                        toIntent.putExtra(
                                            "uri", theUri
                                        )
                                        toIntent.putExtra("saveDirUri", htuiState.selectedSaveDir)
                                        toIntent.putExtra(Intent.EXTRA_STREAM, theUri)
//                                toIntent.putExtra("editType", editType as? Parcelable)
                                        toIntent.putExtra("editType", EditWhich.Home.name)
                                        toIntent.putExtra("editTypeName",EditWhich.Home.name)
                                        toIntent.putExtra("filename",context.resources.getString(R.string.home_screen_file))
                                        toIntent.putExtra("fileName",context.resources.getString(R.string.home_screen_file))
                                        startIntent(
                                            context = context,
                                            what = toIntent
                                        )


                                    }
                                }
                            },
                            tts = tts,
                        )
                    }
                    composable(Screen.ItemsExplorer.name,
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(),
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(),
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween()
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween()
                            )
                        }
                    ) {
                        ItemsExplorer(
                            navController = navController,
                            context = context,
                            pm = pm,
                            saveDirResult = htuiState.selectedSaveDir,
                            onSelectedSaveDir = {
//                            anViewModel.selectSaveDirUri(it)
                            },
                            onChooseSaveDir = {

                            },
                            onOpenTextFile = {
                                    uri, contentResolver -> openATextFile(uri = uri, contentResolver =  contentResolver)
                            },
                            onChooseTextFile = {
                                testFileLauncher.launch(arrayOf<String>("",""))
                            },
                            onCheckPermission = {
                            },
                            onClickVersion = {
                                navController.navigate(Screen.AboutScreen.name)
                            },
                            testTextResult = htuiState.testResult,
                            haptic = haptic,
                            versionName = versionName,
                            versionNumber = versionNumber,
                            systemUiController = systemUiController,
                            uiState = htuiState,
                            viewModel = anViewModel,
                            onEditWhat = { editType, filename ->
                                // https://youtu.be/2hIY1xuImuQ
                                Log.d("ItemExplorer", "To Edit ${editType.name} ${filename}")
                                if(htuiState.selectedSaveDir != null) {
                                    if (!htuiState.editingLevel && currentScreen == Screen.ItemsExplorer.name) {

                                        anViewModel.setEditingLevel(true)
                                    }
                                    var toIntent = Intent(
                                        context, ItemEditorActivity::class.java
                                    )
                                    val theUri = getATextFile(
                                        dirUri = getADirectory(
                                            dirUri = htuiState.selectedSaveDir!!,
                                            context = context,
                                            dirName = context.resources.getString(editType.select)
                                        ),
                                        context = context,
                                        fileName = "${filename}.json",
                                        initData = when(editType){
                                            EditWhich.Pages -> json.encodeToString<PageData>(PageData(
                                                name = filename
                                            ))
                                            EditWhich.Items -> json.encodeToString<ItemData>(ItemData(
                                                name = filename
                                            ))
                                            EditWhich.Home -> json.encodeToString<HomepagesWeHave>(
                                                HomepagesWeHave()
                                            )
//                                        EditWhich.Themes -> json.encodeToString<Then>(HomepagesWeHave())
                                            else -> ""
                                        },
                                        mimeType = context.resources.getString(R.string.text_plain_type),
                                        hardOverwrite = false,
                                    )
                                    toIntent.apply {
                                        type = context.resources.getString(R.string.text_plain_type)
                                    }
                                    toIntent.putExtra(
                                        "uri", theUri
                                    )
//                                    .also {
//                                    it.putExtra(Intent.EXTRA_STREAM, theUri)
//                                    it.putExtra("editType",editType)
//                                    it.putExtra("fileName",filename)
//                                    }
                                    toIntent.putExtra("saveDirUri", htuiState.selectedSaveDir)
                                    toIntent.putExtra(Intent.EXTRA_STREAM, theUri)
//                                toIntent.putExtra("editType", editType as? Parcelable)
                                    toIntent.putExtra("editType", editType.name)
                                    toIntent.putExtra("editTypeName",editType.name)
                                    toIntent.putExtra("filename",filename)
                                    toIntent.putExtra("fileName",filename)
                                    startIntent(
                                        context = context,
                                        what = toIntent
                                    )


                                }
                            },
                            exploreType = htuiState.toEditWhatFile,
                            tts = tts,
                        )

                        if(htuiState.openCreateNewFile){
                            TextInputDialog(
                                title = stringResource(R.string.create_new_file_dialog_override,
                                    stringResource(htuiState.toEditWhatFile.label)
                                ),
                                selectIcon = Icons.Default.Create,
                                onConfirmText = {
                                    anViewModel.createNewFileNow(
                                        context = context,
//                                    name = "$it${if(it.lastIndexOf(".json") < 0) ".json" else ""}",
                                        name = it,
                                        atWhere = htuiState.toEditWhatFile,
                                        uiStating = htuiState,
                                    )
                                    anViewModel.openCreateNewFile(false)
                                },
                                placeholder = "anItem",
                                mustBeFilled = true,
                                onDismiss = {
                                    anViewModel.openCreateNewFile(false)
                                }
                            )
                        }
                    }
                }
            }
        }
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

                    "fullscreen" -> {
                        // Fullscreen

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

private fun goBackHome(
    viewModel: ViewModel,
    navController: NavHostController
){
    navController.popBackStack(Screen.HomeScreen.name, inclusive = false)
}

public fun startIntent(context: Context, what: String, extras:Map<String,String> = mapOf()){
    // https://www.geeksforgeeks.org/android-jetpack-compose-open-specific-settings-screen/
    val i : Intent = Intent(what)
//    i.putExtra()
    for(extra in extras){
        i.putExtra(extra.key,extra.value)
    }
    context.startActivity(i)
}

public fun startIntent(context: Context, what: Intent, extras:Map<String,String> = mapOf()){
    // https://www.geeksforgeeks.org/android-jetpack-compose-open-specific-settings-screen/
    val i : Intent = what
    for(extra in extras){
        i.putExtra(extra.key,extra.value)
    }
    context.startActivity(i)
}

//public fun startIntent(context: Context, what:ManagedActivityResultLauncher<I,O>, args){
//    what.launch(args)
//}

@Throws(Exception::class)
public fun startApplication(context: Context, what: String, pm: PackageManager = context.packageManager, extras:Map<String,String> = mapOf()){
    // https://developer.android.com/reference/android/content/pm/PackageManager.html#getLaunchIntentForPackage(java.lang.String)
    // https://stackoverflow.com/questions/3422758/start-application-knowing-package-name
    val launchIntent : Intent = pm.getLaunchIntentForPackage(what)!!
    startIntent(context,launchIntent,extras)
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

public fun isFileExist(dirUri:Uri, context: Context, fileName:String = "text.txt"): Boolean{
    Log.d("IsFileExist","Starting to check file: ${fileName} from ${dirUri}")
    val thingieTree:DocumentFile = DocumentFile.fromTreeUri(context,dirUri)!!
    Log.d("IsFileExist","File ${fileName} ${if(thingieTree.findFile(fileName) != null) "Exist" else "404 NOT FOUND"}")
    return thingieTree.findFile(fileName) != null
}
public fun getAFile(dirUri:Uri, context: Context, fileName:String): Uri?{
    Log.d("GetFile","Starting to get file: ${fileName} from ${dirUri}")
    val thingieTree:DocumentFile = DocumentFile.fromTreeUri(context,dirUri)!!
    Log.d("GetFile","File ${fileName} ${if(thingieTree.findFile(fileName) != null) "Exist" else "404 NOT FOUND"}")
    return if(thingieTree.findFile(fileName) != null){
        thingieTree.findFile(fileName)!!.uri
    } else {
        null
    }
}
public fun getATextFile(dirUri:Uri, context: Context, fileName:String = "text.txt", mimeType:String = "text/plain", initData:String = "", hardOverwrite:Boolean = false): Uri{
//    println("Starting to get file: ${fileName} (mime: ${mimeType}) from ${dirUri}")
    Log.d("GetTextFile","Starting to get file: ${fileName} (mime: ${mimeType}) from ${dirUri}")
    // https://github.com/abdallahmehiz/mpvKt/blob/74d407106e1fb0bae4b7bc66e3b0f83e77a6cbc2/app/src/main/java/live/mehiz/mpvkt/ui/preferences/AdvancedPreferencesScreen.kt#L189
    val thingieTree:DocumentFile = DocumentFile.fromTreeUri(context,dirUri)!!
    Log.d("GetTextFile","Thingie Tree ${thingieTree.uri}")
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
        if(hardOverwrite && initData.isNotEmpty() && initData != "{}"){
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
            } else {

            }
        }
        thingieTree.findFile(fileName)!!.uri
    }
}

public fun getADirectory(dirUri:Uri, context: Context, dirName:String = "Folder"): Uri{
    // https://github.com/abdallahmehiz/mpvKt/blob/74d407106e1fb0bae4b7bc66e3b0f83e77a6cbc2/app/src/main/java/live/mehiz/mpvkt/ui/preferences/AdvancedPreferencesScreen.kt#L189
    Log.d("GetDirectory", "Attempt Get Directory ${dirUri} of ${dirName}")
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

fun onLaunchAction(
    data:List<ActionData>,
    context: Context,
    coroutineScope: CoroutineScope,
    contentResolver: ContentResolver = context.contentResolver,
    pm: PackageManager = context.packageManager,
    snackbarHostState:SnackbarHostState,
    navController: NavHostController,
    viewModel:HTViewModel,
    uiState:HTUIState,
    inspectionMode: Boolean = false,
    handoverPagerState: PagerState,
    selectAction:Int = 0,
    tts: MutableState<TextToSpeech?>,
){
    coroutineScope.launch {
        when(data[selectAction].type){
            ActionDataLaunchType.LauncherActivity -> {
                try {
                    if (data[selectAction].action.isNotEmpty()) {
                        startApplication(
                            context = context,
                            pm = pm,
                            what = data[selectAction].action,
                            extras = data[selectAction].extras ?: mapOf()
                        )
                    } else {
                        throw IllegalArgumentException(
                            "Action command cannot be empty",
                            IllegalStateException("Action LauncherActivity ${data[selectAction].action}")
                        )
                    }
                } catch (e:IllegalArgumentException) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("WERROR 300! Action LauncherActivity blank")
                    }
                    e.printStackTrace()
                } catch (e:Exception) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("WERROR 404! Launcher Activity undefined")
                    }
                    e.printStackTrace()
                } catch (e: ActivityNotFoundException){
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("WERROR 404! Launcher Activity undefined")
                    }
                    e.printStackTrace()
                }
            }
            ActionDataLaunchType.Internal -> {
                val containAction = data[selectAction].action
                try {
                    when{
                        containAction == context.resources.getString(ActionInternalCommand.AllApps.id)->{
                            navController.navigate(Screen.AllAppsScreen.name)
                        }
                        containAction == context.resources.getString(ActionInternalCommand.Camera.id)->{
                            // https://stackoverflow.com/a/13977619/9079640
                            // https://developer.android.com/guide/components/intents-common#CameraStill
                            startIntent(
                                context = context,
                                what = Intent(
                                    "android.media.action.STILL_IMAGE_CAMERA"
                                ),
                                extras = data[selectAction].extras ?: mapOf()
                            )
                        }
                        containAction == context.resources.getString(ActionInternalCommand.Clock.id) -> {
                            // https://stackoverflow.com/a/4281243/9079640
//                            startIntent(
//                                context = context,
//                                what = Intent(AlarmClock.ACTION_SHOW_ALARMS)
////                                what = Intent("android.intent.action.SHOW_ALARMS")
//                            )
//                            startApplication(
//                                context = context,
//                                what = "com.android.deskclock",
//                                pm = pm,
//                            )
                            var foundApp:Boolean = false
                            var targetApp:PackageInfo = PackageInfo()
                            if(!inspectionMode) {
                                for (i in HTLauncherHardcodes.ALARM_APP_IMPLEMENTATIONS) {
                                    try {
                                        targetApp = pm.getPackageInfo(i.value.packageName, 0)
                                        foundApp = true
                                        break
                                    } catch (e: NameNotFoundException) {
//                                    e.printStackTrace()
                                        foundApp = false
                                    } catch (e: Exception) {
//                                    e.printStackTrace()
                                    }
                                }
                            }
                            if(foundApp){
                                startApplication(
                                    context = context,
                                    pm = pm,
                                    what = targetApp.packageName
                                )
                            } else {
                                snackbarHostState.showSnackbar(message = "WERROR 404! Clock App Not Found")
                            }
                        }
                        containAction == context.resources.getString(ActionInternalCommand.Settings.id) -> {
//                            startIntent(
//                                context = context,
//                                what = Intent(Settings.ACTION_SETTINGS)
//                            )
                            navController.navigate<PageData>(
                                viewModel.getPageData(
                                    of = "Settings",
                                    context = context
                                )
                            )
//                            navController.navigate(Screen.OpenAPage.name, navigatorExtras = )
                        }
                        containAction == context.resources.getString(ActionInternalCommand.SystemSettings.id) -> {
                            startIntent(
                                context = context,
                                what = Intent(Settings.ACTION_SETTINGS)
                            )
                        }
                        // MEGA SETTING
                        containAction.startsWith("Settings") -> {
//                            ActionGoToSystemSetting.valueOf(containAction)
                            val settingGo:String = containAction.replaceFirst("Settings","")
                            Log.d("GoToSettings","${settingGo}")
                            startIntent(
                                context = context,
                                what = Intent(ActionGoToSystemSetting.valueOf(settingGo).route)
                            )
                        }
                        // END MEGA SETTING
                        containAction == context.resources.getString(ActionInternalCommand.Contacts.id) -> {
//                            startIntent(
//                                context = context,
//                                what = Intent(Contacts.)
//                            )
                        }
                        containAction == context.resources.getString(ActionInternalCommand.Preferences.id) -> {
                            navController.navigate(Screen.ConfigurationScreen.name)
                        }
                        containAction == context.resources.getString(ActionInternalCommand.Emergency.id) -> {

                        }
                        containAction == context.resources.getString(ActionInternalCommand.Messages.id) -> {

                        }
                        containAction == context.resources.getString(ActionInternalCommand.Telephone.id) -> {

                        }
                        containAction == context.resources.getString(ActionInternalCommand.Gallery.id) -> {

                        }
                        containAction == context.resources.getString(ActionInternalCommand.GoToPage.id) -> {
                            if(data[selectAction].args[0].isNotBlank()){
                                uiState.coreConfigJson?.let {
                                    if(uiState.coreConfigJson?.pagesPath?.contains(data[0].args[0]) == true){
                                        val indexor = uiState.coreConfigJson?.pagesPath?.indexOf(data[0].args[0])
                                        handoverPagerState.animateScrollToPage(indexor ?: 0)
                                    }
                                }
                            } else {
                                throw IllegalArgumentException("Argument 0 cannot be empty", IllegalStateException("Action ${data[selectAction].action}: Empty Argument 0: ${data[selectAction].args[0]}"))
                            }
                        }
                        containAction == context.resources.getString(ActionInternalCommand.OpenAPage.id) -> {
                            Log.d("OpenAPage","Try open page ${data[selectAction].args[0]}")
                            if(data[selectAction].args[0].isNotBlank()) {
                                navController.navigate<PageData>(
                                    viewModel.getPageData(
                                        of = data[selectAction].args[0],
                                        context = context
                                    )
                                )
                            } else {
                                // https://kotlinlang.org/docs/exceptions.html#throw-exceptions-with-precondition-functions
                                throw IllegalArgumentException("Argument 0 cannot be empty", IllegalStateException("Action ${data[selectAction].action}: Empty Argument 0: ${data[selectAction].args[0]}"))
                            }
                        }
                        containAction == context.resources.getString(ActionInternalCommand.Aria.id) -> {
                            // arg 0 = select language. blank is auto
                            // extras Lang, Read of this language. first is default. Recommended start from EN
                            val listOfLocales:Array<JavaLocale> = JavaLocale.getAvailableLocales()
                            val saySelectLocale:String = data[selectAction].args[0]
                            val selectLocale:JavaLocale = if(saySelectLocale.isNotEmpty()) JavaLocale.forLanguageTag(data[selectAction].args[0]) else JavaLocale.getDefault()
                            val thereforeChosenLocale:String = selectLocale.language
                            val readout:String = (if(data[selectAction].extras?.contains(thereforeChosenLocale) == true)
                                data[selectAction].extras?.get(thereforeChosenLocale) ?: "" else "")
                            Toast
                                .makeText(
                                    context,
                                    readout,
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            ttsSpeakInterrupt(
                                handover = tts,
                                message = readout,
                            )
                        }
                        else -> {}
                    }
                } catch (e:Exception){
                    e.printStackTrace()
                } catch (e:ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            ActionDataLaunchType.PageDialog -> {
                val containAction = data[selectAction].action
                Log.d("OpenAPage","Try open page ${containAction}")
                try {
                    if(containAction.isNotBlank()) {
                        navController.navigate<PageData>(
                            viewModel.getPageData(
                                of = containAction,
                                context = context
                            )
                        )
                    } else {
                        // https://kotlinlang.org/docs/exceptions.html#throw-exceptions-with-precondition-functions
                        throw IllegalArgumentException("Argument Action cannot be empty", IllegalStateException("Action ${containAction}: Empty Action: ${containAction}"))
                    }
                } catch (e:Exception){
                    e.printStackTrace()
                }
            }
            else -> {}
        }
    }
}

@PreviewFontScale
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@Preview(showBackground = true)
@Composable
fun NavigationPreview(){
    HTLauncherTheme {
        Navigation()
    }
}