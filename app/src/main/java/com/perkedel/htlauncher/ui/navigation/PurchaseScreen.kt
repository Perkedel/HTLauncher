@file:OptIn(ExperimentalFoundationApi::class)

package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.modules.ttsSpeak
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import com.perkedel.htlauncher.widgets.SettingCategoryBar
import kotlinx.coroutines.CoroutineScope
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.preference

@Composable
fun PurchaseScreen(
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
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    windowInfo: WindowInfo = rememberWindowInfo(),
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
    onAttemptBuy: ()->Unit = {}
){
    val cost:String = stringResource(R.string.purchase_cost_changeable)
    val bought:Boolean = true
    ProvidePreferenceLocals {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                SettingCategoryBar(
                    title = stringResource(R.string.purchase_offer_title)
                )
            }
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.donation_option)}. ${context.resources.getString(
                                    R.string.donation_option_desc)}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.quick_start_option),
                    )
                ,
                key = "buy_now",
                title = { Text(text = "($cost) ${stringResource(R.string.purchase_pro_title)}" ) },
                icon = { Icon(imageVector = if(bought) Icons.Default.CheckCircle else Icons.Default.ShoppingCart, contentDescription = null) },
                summary = { Text(text = "(${stringResource(if(bought) R.string.purchase_done_now_say else R.string.purchase_not_yet_say)}) ${stringResource(R.string.purchase_unlocks_all_say)}") },
                onClick = {
                    onAttemptBuy()
                }
            )
        }
    }
}

@HTPreviewAnnotations
@Composable
fun PurchaseScreenPreview(){
    HTLauncherTheme {
        Surface(

        ) {
            PurchaseScreen()
        }
    }
}