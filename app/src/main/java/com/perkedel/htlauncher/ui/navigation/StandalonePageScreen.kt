package com.perkedel.htlauncher.ui.navigation

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.pdf.PdfDocument.Page
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.ui.page.BasePage
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

@Composable
fun StandalonePageScreen(
    pageData: PageData = PageData(),
    onAllAppButtonClicked: () -> Unit = {},
    onMoreMenuButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxSize(),
    hideTopBar: Boolean = true,
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
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    isReady:Boolean = false,
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    onLaunchOneOfAction: (List<ActionData>)->Unit = {},
    windowInfo: WindowInfo = rememberWindowInfo(),
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
) {
    LaunchedEffect(
        key1 = pageData
    ) {
        viewModel.updateCurrentPageIconModel(
            viewModel.getPageIcon(
                context = context,
                of = pageData.name,
                json = json,
                ignoreFile = false,
                forceReload = false,
                pm = pm,
            )
        )
    }

    Box(){
        BasePage(
            pageData = pageData,
            fileName = "",
            isOnNumberWhat = 0,
            isFirstPage = false,
            howManyItemsHere = 0,
            onMoreMenuButtonClicked = onMoreMenuButtonClicked,
            modifier = modifier,
            context = context,
            configuration = configuration,
            pm = pm,
            viewModel = viewModel,
            contentResolver = contentResolver,
            uiState = uiState,
            colorScheme = colorScheme,
            haptic = haptic,
            json = json,
            tts = tts,
            windowInfo = windowInfo,
            isCompact = isCompact,
            isOrientation = isOrientation,
            onLaunchOneOfAction = onLaunchOneOfAction,
        )
    }
}

@HTPreviewAnnotations
@Composable
fun StandalonePageScreenPreview(){
    HTLauncherTheme {
        Surface(
            modifier = Modifier.statusBarsPadding().navigationBarsPadding(),
            color = Color.Transparent,
        ) {
            StandalonePageScreen()
        }
    }
}
