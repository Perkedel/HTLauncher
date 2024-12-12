package com.perkedel.htlauncher

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color
import android.os.Build
import androidx.activity.SystemBarStyle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
//import androidx.compose.material.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.perkedel.htlauncher.func.DATA_STORE_FILE_NAME
import com.perkedel.htlauncher.func.createDataStore
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import kotlinx.coroutines.flow.map

class MainActivity : ComponentActivity() {

    private val permissions:Array<String> = if(Build.VERSION.SDK_INT >= 33){
        arrayOf(
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES,
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    val htViewModel:HTViewModel = HTViewModel()

//    lateinit var dataStorePrefs: DataStore<Preferences> = createDataStore(applicationContext)
    companion object{
//        val dataStorePrefs: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_FILE_NAME)
        // https://medium.com/@akarenina25/the-simplest-way-to-use-preferences-datastore-1083d23fade2
        // https://developer.android.com/topic/libraries/architecture/datastore
        private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_FILE_NAME)
//        val keyOnBoardingSession = booleanPreferencesKey(STRING_ONBOARDING_SESSION)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            SystemBarStyle.auto(Color.TRANSPARENT,Color.TRANSPARENT),
            SystemBarStyle.auto(Color.TRANSPARENT,Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)



//        val prefs: DataStore<Preferences> = remember { createDataStore(applicationContext) }
//        val selectedSaveDir by prefs
//            .data
//            .map {
//                val saveDir = stringPreferencesKey("saveDir")
//                it[saveDir] ?: ""
//            }
//            .collectAsState("")
//        val dataStorePrefs: DataStore<Preferences> = createDataStore(applicationContext)
        val dataStorePrefs: DataStore<Preferences> = applicationContext.preferencesDataStore

//        val selectedSaveDir by dataStorePrefs
//            .data
//            .map {
//                val saveDir = stringPreferencesKey("saveDir")
//                it[saveDir] ?: ""
//            }

        if (Build.VERSION.SDK_INT < 26) {
//        val permissionDialogQueue = htui.visiblePermissionDialogQueue
            // Permissioners
            // https://youtu.be/ji6Z32oPUpQ
            // https://github.com/philipplackner/ReadExternalMediaFilesAPI35/blob/master/app/src/main/java/com/plcoding/readexternalmediafilesapi35/MainActivity.kt
            ActivityCompat.requestPermissions(
                this,
                permissions,
                0
            )
        }



        setContent {
            HomeGreeting(
//                prefs = remember { createDataStore(applicationContext) }
                permissionRequests = permissions,
                activityHandOver = this,
                dataStorePrefs = dataStorePrefs,
                prefs = remember { dataStorePrefs },
                anViewModel = htViewModel,
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGreeting(
    context: Context = LocalContext.current,
    permissionRequests: Array<String> = arrayOf(""),
    activityHandOver: ComponentActivity = ComponentActivity(),
    dataStorePrefs: DataStore<Preferences> = createDataStore(context),
    prefs: DataStore<Preferences> = remember { dataStorePrefs },
    anViewModel: HTViewModel = viewModel(),
){
    HTLauncherTheme {
        Navigation(
//            prefs = prefs
            permissionRequests = permissionRequests,
            activityHandOver = activityHandOver,
            dataStorePrefs = dataStorePrefs,
            prefs = prefs,
            anViewModel = anViewModel,
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@HTPreviewAnnotations
@Composable
fun GreetingPreview() {
//    HTLauncherTheme {
////        Greeting("Android")
//
//    }
//    HomeGreeting(
////        prefs = remember { createDataStore(applicationContext) }
//    )
    HTLauncherTheme {
        HomeGreeting()
    }
}