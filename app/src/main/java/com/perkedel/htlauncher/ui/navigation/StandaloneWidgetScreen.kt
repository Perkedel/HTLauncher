package com.perkedel.htlauncher.ui.navigation

import android.R
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Parcelable
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.modules.AppWidgetHostView
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import com.perkedel.htlauncher.widgets.HTButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


@Composable
fun StandaloneWidgetScreen(
    onAllAppButtonClicked: () -> Unit = {},
    onMoreMenuButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxSize(),
    hideTopBar: Boolean = true,
    widgetSelectLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    context: Context = LocalContext.current,
    configuration: Configuration = LocalConfiguration.current,
    pm: PackageManager = context.packageManager,
    viewModel: HTViewModel = HTViewModel(),
    uiState: HTUIState = HTUIState(),
    contentResolver: ContentResolver = context.contentResolver,
    colorScheme: ColorScheme = rememberColorScheme(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    configFile: HomepagesWeHave? = null,
    systemUiController: SystemUiController = rememberSystemUiController(),
    json: Json = Json {
        // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
        prettyPrint = true
        encodeDefaults = true
    },
    appWidgetManager:AppWidgetManager = AppWidgetManager.getInstance(context),
    appWidgetId: Int = 1,
    appWidgetHost:AppWidgetHost = AppWidgetHost(context, appWidgetId),
    appWidgetInfo: AppWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    isReady:Boolean = false,
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    onLaunchOneOfAction: (List<ActionData>)->Unit = {},
    windowInfo: WindowInfo = rememberWindowInfo(),
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
) {
    // https://stackoverflow.com/questions/77532675/how-to-host-and-draw-installed-app-widgets-in-a-compose-app
    // http://coderender.blogspot.com/2012/01/hosting-android-widgets-my.html
//    var eAppWidgetInfo : AppWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId)
    var eAppWidgetInfo : MutableState<AppWidgetProviderInfo?> = remember{mutableStateOf<AppWidgetProviderInfo?>(null)}
    Box(){
        Column {
            HTButton(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                title = "SELECT",
                onClick = {
                    val appWidgetIding: Int = appWidgetHost.allocateAppWidgetId()
                    val pairing = Pair(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetIding.toString())
                    val extraing = mapOf(pairing)
//                    startIntent(context,AppWidgetManager.ACTION_APPWIDGET_PICK, extraing)
                    val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
                    pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIding)
                    pickIntent.putExtra("requestCode","REQUEST_PICK_APPWIDGET")
                    viewModel.setStandaloneWidgetConfigMode("REQUEST_PICK_APPWIDGET")
                    Log.d("Widgeting","Will Intent ${pickIntent.toString()}")
                    Log.d("Widgeting","Will Extra ${pickIntent.extras.toString()}")
                    addEmptyData(pickIntent)
//                    startActivityForResult(context.acti, pickIntent, R.id.REQUEST_PICK_APPWIDGET)
//                    context.startActivity(pickIntent)
                    widgetSelectLauncher.launch(pickIntent)
                }
            )
            LaunchedEffect(
                uiState.standaloneWidgetIdSelected
            ) {
                coroutineScope.launch {
 //                eAppWidgetInfo = appWidgetManager.getAppWidgetInfo(uiState.standaloneWidgetIdSelected)
                    eAppWidgetInfo.value = null
                    eAppWidgetInfo.value = appWidgetManager.getAppWidgetInfo(uiState.standaloneWidgetIdSelected)
                    Log.d("Widgeting","StandaloneWidgetIdSelected ${uiState.standaloneWidgetIdSelected}")
//                Log.d("Widgeting", "Provider ${appWidgetInfo.toString()}")
                    Log.d("Widgeting", "Provider ${eAppWidgetInfo.toString()}")
                }
            }
//            AppWidgetHostView(
//                context = context,
//                modifier = Modifier.fillMaxSize(),
//                appWidgetManager = appWidgetManager,
//                appWidgetHost = appWidgetHost,
//                appWidgetId = appWidgetId,
//                appWidgetInfo = appWidgetInfo,
//                viewModel = viewModel,
//                uiState = uiState,
//            )
            if(eAppWidgetInfo.value != null)
                AppWidgetHostView(
                    context = context,
                    modifier = Modifier.fillMaxSize(),
                    appWidgetManager = appWidgetManager,
                    appWidgetHost = appWidgetHost,
                    appWidgetId = uiState.standaloneWidgetIdSelected,
                    appWidgetInfo = eAppWidgetInfo.value!!,
                    viewModel = viewModel,
                    uiState = uiState,
                )
        }

    }
}

fun addEmptyData(pickIntent: Intent) {
    val customInfo: ArrayList<Parcelable> = ArrayList<Parcelable>()
    pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo)
    val customExtras: ArrayList<Parcelable> = ArrayList<Parcelable>()
    pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras)
};