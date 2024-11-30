package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import com.perkedel.htlauncher.widgets.HTSearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
    onSelectedApp: (ApplicationInfo) -> Unit = {},
    searchTerm: MutableState<String> = remember{mutableStateOf("")},
    viewModel: ItemEditorViewModel = ItemEditorViewModel(),
){
    val packList = pm.getInstalledPackages(0)
    val appList = pm.getInstalledApplications(0)
    LaunchedEffect(true) {
        coroutineScope.launch {
            viewModel.initializeAllApps(packList,pm)
        }
    }

//    val recombination:List<String> = lis
    var searchT:String by searchTerm
    viewModel.updateAppSearchText(searchT)
    viewModel.updateAppSearchActive(searchT.isNotBlank())
//    var appFilter:List<PackageInfo> = emptyList()
//    appFilter = if(packList != null && searchT.isNotEmpty()) packList.filter { it.applicationInfo?.loadLabel(pm).toString().contains(searchT,true) || it.packageName.contains(searchT,true) || searchT.isEmpty() } else packList
    val appFilter by viewModel.appAll.collectAsState()

    ProvidePreferenceLocals {
        LazyColumn {
            item{
                HTSearchBar(
                    value = searchT,
                    onValueChange = {
                        searchT = it
                    },
                    megaTitle = "Select an App"
                )
                
            }
            items(
//                count = appList.size
//                appList.filter { it.loadLabel(pm).contains(searchT, true) || it.packageName.contains(searchT, true) || searchT.isEmpty()}
                items = appFilter
            ){
//                val ddawe = pm.getApplicationIcon(appList[it].packageName)
                val ddawe = pm.getApplicationIcon(it.packageName)
//                val ddlabel:String = it.loadLabel(pm).toString()
//                val ddlabel:String = it.applicationInfo?.loadLabel(pm).toString()
                val ddlabel:String = it.label
                Preference(
                    icon = {
                        AsyncImage(
                            model = ddawe,
                            contentDescription = "",
                            modifier = Modifier
                                .size(75.dp),
                            error = painterResource(id = R.drawable.mavrickle),
                            placeholder = painterResource(id = R.drawable.mavrickle),
                        )
                    },
                    title = { Text(ddlabel) },
                    summary = {
                        Text(
//                            appList[it].packageName,
                            text = it.packageName,
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
//                        onSelectedApp(appList[it])
//                        onSelectedApp(it)
//                        if(it.applicationInfo != null)
//                            onSelectedApp(it.applicationInfo!!)
                        onSelectedApp(pm.getApplicationInfo(it.packageName,0))
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
        Surface(
            modifier = Modifier.fillMaxSize().navigationBarsPadding().statusBarsPadding()
        ) {
            ActionSelectApp()
        }
    }
}