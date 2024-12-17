package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings
import java.util.Collections

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
    onlyLauncherActivity:Boolean = false,
){

//    val appList = pm.getInstalledApplications(0)
    val mainIntent = Intent(Intent.ACTION_MAIN, null)
    val mainIntent2 = Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
    mainIntent2.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
//    mainIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)

    var readyToShow by remember { mutableStateOf(false) }


//    val recombination:List<String> = lis
    var searchT:String by searchTerm
    LaunchedEffect(
        key1 = searchT
    ) {
        viewModel.updateAppSearchText(searchT)
        viewModel.updateAppSearchActive(searchT.isNotBlank())
    }
//    var appFilter:List<PackageInfo> = emptyList()
//    appFilter = if(packList != null && searchT.isNotEmpty()) packList.filter { it.applicationInfo?.loadLabel(pm).toString().contains(searchT,true) || it.packageName.contains(searchT,true) || searchT.isEmpty() } else packList
    val appFilter by viewModel.appAll.collectAsState()
//    val appFilterSort = appFilter.sortedBy { it.label }
    LaunchedEffect(
        key1 = pm.getInstalledPackages(0),
        key2 = pm.queryIntentActivities(mainIntent,0)
    ) {
        coroutineScope.launch {
            readyToShow = false
            if(onlyLauncherActivity){
                val launcherActivities = pm.queryIntentActivities(mainIntent,0)
                val tvActivities = pm.queryIntentActivities(mainIntent2,0)
                val theAlreadyExist: MutableList<ResolveInfo> = mutableListOf()
                for(i in tvActivities){
                    var alreadyExist:Boolean = false
                    if (theAlreadyExist.contains(i) || launcherActivities.contains(i)) continue
                    // PLEASE PREVENT DUPLICATE WHEN MERGING!
//                if(launcherActivities.contains(i)){
////                    continue
//                } else {
//                    launcherActivities.add(i)
//                }
                    for(j in launcherActivities){
                        if (theAlreadyExist.contains(j)) continue
                        if(i.activityInfo.packageName == j.activityInfo.packageName){
                            theAlreadyExist.add(j)
                            alreadyExist = true
                            break
                        } else {

                        }
                    }
                    if(!alreadyExist) launcherActivities.add(i)
                }
                Collections.sort(launcherActivities, ResolveInfo.DisplayNameComparator(pm))
                viewModel.initializeAllAppsResolve(launcherActivities,pm)
            } else {
                val packList = pm.getInstalledPackages(0)
                viewModel.initializeAllApps(packList,pm)
            }
            readyToShow = true
        }
    }
    val lazyListState = rememberLazyListState()

    ProvidePreferenceLocals {
        // https://github.com/nanihadesuka/LazyColumnScrollbar
        if(readyToShow || LocalInspectionMode.current) {
            LazyColumnScrollbar(
                state = lazyListState,
                settings = ScrollbarSettings(
                    thumbSelectedColor = colorScheme.primary,
                    thumbUnselectedColor = colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                )
            ) {
                LazyColumn(
                    state = lazyListState,
                ) {
                    item {
                        HTSearchBar(
                            value = searchT,
                            onValueChange = {
                                searchT = it
                            },
                            megaTitle = stringResource(R.string.select_an_app)
                        )

                    }
                    if(appFilter.isNotEmpty()) {
                        items(
//                count = appList.size
//                appList.filter { it.loadLabel(pm).contains(searchT, true) || it.packageName.contains(searchT, true) || searchT.isEmpty()}
                            items = appFilter
                        ) {
//                val ddawe = pm.getApplicationIcon(appList[it].packageName)
                            val ddawe = pm.getApplicationIcon(it.packageName)
//                val ddlabel:String = it.loadLabel(pm).toString()
//                val ddlabel:String = it.applicationInfo?.loadLabel(pm).toString()
                            val ddlabel: String = it.label
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
                                    onSelectedApp(pm.getApplicationInfo(it.packageName, 0))
                                }
                            )
                        }
                    } else {
                        item{
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.item_empty_emphasis)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                HTLoading()
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