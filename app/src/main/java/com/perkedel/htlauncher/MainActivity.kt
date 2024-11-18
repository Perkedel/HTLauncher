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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.perkedel.htlauncher.func.createDataStore
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

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

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            SystemBarStyle.auto(Color.TRANSPARENT,Color.TRANSPARENT),
            SystemBarStyle.auto(Color.TRANSPARENT,Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)
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
                activityHandOver = this
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGreeting(
//    prefs:DataStore<Preferences>
    permissionRequests:Array<String> = arrayOf(""),
    activityHandOver:ComponentActivity = ComponentActivity()
){
    HTLauncherTheme {
        Navigation(
//            prefs = prefs
            permissionRequests = permissionRequests,
            activityHandOver = activityHandOver
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

@PreviewFontScale
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    HTLauncherTheme {
////        Greeting("Android")
//
//    }
    HomeGreeting(
//        prefs = remember { createDataStore(applicationContext) }
    )
}