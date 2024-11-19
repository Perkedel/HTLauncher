package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Source
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.startIntent
import com.perkedel.htlauncher.ui.dialog.HTAlertDialog
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import com.perkedel.htlauncher.widgets.AboutTermsOptions
import com.perkedel.htlauncher.widgets.ApplicationEmblemBanner
import kotlinx.coroutines.CoroutineScope
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.preference

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutTerms(
    navController: NavController? = rememberNavController(),
    context: Context = LocalContext.current,
    configuration: Configuration = LocalConfiguration.current,
    pm: PackageManager = context.packageManager,
    colorScheme: ColorScheme = rememberColorScheme(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    systemUiController: SystemUiController = rememberSystemUiController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onSnackbarResult:(SnackbarResult) -> Unit = {  },
    versionName:String = "XXXX.XX.XX",
    versionNumber:Long = 0,
    onReadTerms: () -> Unit = {},
    onReadDisclaimer: () -> Unit = {},
){
    ProvidePreferenceLocals {
        val windowInfo = rememberWindowInfo()
        val isCompact = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact
        // https://stackoverflow.com/a/64755245/9079640
        val isOrientation:Int = configuration.orientation // This doesn't seems to work
        if(isCompact){
            AboutTermsOptions(
                modifier = Modifier,
                navController = navController,
                context = context,
                pm = pm,
                colorScheme = colorScheme,
                haptic = haptic,
                configuration = configuration,
                coroutineScope = coroutineScope,
                snackbarHostState = snackbarHostState,
                onSnackbarResult = onSnackbarResult,
                versionNumber = versionNumber,
                versionName = versionName,
                onReadTerms = onReadTerms,
                onReadDisclaimer = onReadDisclaimer,
                addThese = {
                    ApplicationEmblemBanner(
                        modifier = Modifier.fillMaxWidth(),
                        modifierLogo = Modifier.fillMaxWidth(),
                        modifierText = Modifier.fillMaxWidth(),
                        context = context,
                        pm = pm,
                    )
                }
            )
        } else {
            Row(
                modifier = Modifier
            )
            {
                // DONE: Big logo of this
                ApplicationEmblemBanner(
                    modifier = Modifier.fillMaxHeight(),
                    context = context,
                    pm = pm,
                )
                AboutTermsOptions(
                    modifier = Modifier.weight(1f),
                    navController = navController,
                    context = context,
                    pm = pm,
                    colorScheme = colorScheme,
                    haptic = haptic,
                    configuration = configuration,
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    onSnackbarResult = onSnackbarResult,
                    versionNumber = versionNumber,
                    versionName = versionName,
                    onReadTerms = onReadTerms,
                    onReadDisclaimer = onReadDisclaimer,
                    systemUiController = systemUiController
                )
            }
        }

    }
}


@HTPreviewAnnotations
@Composable
fun AboutTermsPreview(){
    HTLauncherTheme {
        Surface(
            color = rememberColorScheme().background
        ) {
            AboutTerms()
        }

    }
}