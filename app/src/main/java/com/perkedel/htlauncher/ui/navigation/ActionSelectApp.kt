package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import kotlinx.coroutines.CoroutineScope
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.ProvidePreferenceLocals

@Composable
fun ActionSelectApp(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    colorScheme: ColorScheme = rememberColorScheme(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onSnackbarResult:(SnackbarResult) -> Unit = {  },
    systemUiController: SystemUiController = rememberSystemUiController(),
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    onSelectedApp: (ApplicationInfo) -> Unit = {}
){
    val packList = pm.getInstalledPackages(0)
    val appList = pm.getInstalledApplications(0)
    Text("Select an App")
    ProvidePreferenceLocals {
        LazyColumn {
            items(appList.size){
                val ddawe = pm.getApplicationIcon(appList[it].packageName)
                Preference(
                    icon = {
                        AsyncImage(
                            model = ddawe,
                            contentDescription = "",
                            modifier = Modifier
                                .size(75.dp),
                        )
                    },
                    title = { Text("${appList[it].loadLabel(pm)}") },
                    summary = {
                        Text(
                            appList[it].packageName,
                            modifier = Modifier.basicMarquee(
                                // https://medium.com/@theAndroidDeveloper/jetpack-compose-gets-official-support-for-marquee-heres-how-to-use-it-1f678aecb851
                                // https://composables.com/foundation/basicmarquee
                                spacing = MarqueeSpacing(20.dp),
                                iterations = Int.MAX_VALUE,
                                animationMode = MarqueeAnimationMode.Immediately
                            )
                        )
                    },
                    onClick = {
                        onSelectedApp(appList[it])
                    }
                )
            }
        }
    }
}

@HTPreviewAnnotations
@Composable
fun ActionSelectAppPreview() {
    HTLauncherTheme {
        ActionSelectApp()
    }
}