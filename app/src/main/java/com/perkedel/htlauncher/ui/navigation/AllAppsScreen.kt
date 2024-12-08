package com.perkedel.htlauncher.ui.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
//import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.SearchableApps
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.startApplication
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import com.perkedel.htlauncher.widgets.HTSearchBar
import com.perkedel.htlauncher.widgets.SettingCategoryBar
import com.perkedel.htlauncher.widgets.simpleVerticalScrollbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings

//import com.gigamole.scrollbars

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllAppsScreen(
    navController: NavController? = rememberNavController(),
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    colorScheme: ColorScheme = rememberColorScheme(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember {SnackbarHostState()},
    onSnackbarResult:(SnackbarResult) -> Unit = {  },
    systemUiController: SystemUiController = rememberSystemUiController(),
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    onLaunchApp: (ApplicationInfo)->Unit = {},
    searchTerm: MutableState<String> = remember{mutableStateOf("")},
    anViewModel: HTViewModel = viewModel(),
    ) {
    // https://stackoverflow.com/questions/64377518/how-to-initialize-or-access-packagemanager-out-from-coroutinecontext
    // https://www.geeksforgeeks.org/different-ways-to-get-list-of-all-apps-installed-in-your-android-phone/
    // https://youtu.be/1Thp0bB5Ev0
    // https://stackoverflow.com/questions/56767624/how-to-load-image-from-drawable-in-jetpack-compose
    // https://stackoverflow.com/questions/10696121/get-icons-of-all-installed-apps-in-android
    // https://www.youtube.com/watch?v=OsNplCy4BKE
    // https://github.com/coil-kt/coil
    // https://github.com/rumboalla/apkupdater/blob/3.x/app/src/main/kotlin/com/apkupdater/ui/component/Image.kt#L53
    // https://github.com/rumboalla/apkupdater/blob/3.x/app/src/main/kotlin/com/apkupdater/ui/component/UiComponents.kt#L57
    // https://developer.android.com/develop/ui/compose/graphics/images/customize
    // https://github.com/rumboalla/apkupdater/blob/3.x/app/src/main/kotlin/com/apkupdater/ui/component/Text.kt#L113
    // https://stackoverflow.com/questions/53607151/how-to-get-installed-packages-using-packagemanager
    // https://developer.android.com/reference/android/content/pm/PackageManager#getInstalledPackages(int)
    // https://developer.android.com/reference/android/content/pm/PackageManager.PackageInfoFlags
    // https://developer.android.com/reference/android/content/pm/PackageManager#GET_ACTIVITIES
    // https://stackoverflow.com/questions/53607151/how-to-get-installed-packages-using-packagemanager
    // https://github.com/rumboalla/apkupdater/blob/3.x/app/src/main/kotlin/com/apkupdater/repository/AppsRepository.kt#L15
    // https://stackoverflow.com/questions/8167343/how-to-get-application-labels-of-installed-applications
    // https://stackoverflow.com/questions/23900697/how-to-get-the-list-of-all-apps-on-android-device-using-terminal
    // https://github.com/philipplackner/SearchFieldCompose/blob/master/app/src/main/java/com/plcoding/searchfieldcompose/MainViewModel.kt
    // https://medium.com/@begunova/android-app-package-name-5-ways-of-retrieval-57089a3cf33a
    // https://gist.github.com/akexorcist/b058ed1fea821614bcd4
    // https://stackoverflow.com/questions/8167343/how-to-get-application-labels-of-installed-applications
    // https://youtu.be/P_z1yveCm64?si=J7aQz4JslC7EgDRt
    // https://youtu.be/jKCgdraXCs0?si=RJxHNcnabDyTOxX2
    // https://github.com/the-android-factory/SimpleRick
    // https://gist.github.com/akexorcist/b058ed1fea821614bcd4
    // https://blog.savvas.cloud/2023/09/07/the-dos-and-donts-of-jetpack-compose/
    // https://meetpatadia9.medium.com/efficient-search-with-lazy-layouts-in-jetpack-compose-0a6c3f219ff5
//    val pm:PackageManager = getPackageManager()
//    val pm: PackageManager = context.packageManager
//    val pm:PackageManager = context.getApp
    val packList = pm.getInstalledPackages(0)
//    val packList = pm.getInstalledApplications(0)
    val appList = pm.getInstalledApplications(0)
    LaunchedEffect(true) {
        coroutineScope.launch {
            anViewModel.initializeAllApps(packList,pm)
        }
    }

//    anViewModel.installAllApps(packList,pm)
//    val searchableApps: MutableList<SearchableApps> = emptyList<SearchableApps>().toMutableList()
//    for (i in appList){
//        searchableApps.add(
//            SearchableApps(
////                itself = i,
//                packageName = i.packageName,
//                label = i.loadLabel(pm).toString(),
////                icon = pm.getApplicationIcon(i.packageName),
//            )
//        )
//    }
//    appList.forEach {
//        searchableApps.add(
//            SearchableApps(
//                packageName = it.packageName,
//                label = it.
//            )
//        )
//    }
//    packList.forEach{
//        searchableApps.add(
//            SearchableApps(
//                packageName = it.packageName,
//                label = it.applicationInfo?.loadLabel(pm).toString()
//            )
//        )
//    }
//    searchableApps.addAll(
//        S
//    )

//    var searchT:String by searchTerm
    var searchT:String by remember{mutableStateOf("")}
    anViewModel.updateAppSearchText(searchT)
    anViewModel.updateAppSearchActive(searchT.isNotBlank())
//    val appFilter = if(appList != null && searchT.isNotEmpty()) appList.filter {
////        it.loadLabel(pm).contains(searchT, true)
//                it.packageName.contains(searchT, true)
//                        || it.loadLabel(pm).toString().contains(searchT,true)
////                        || pm.getApplicationLabel(it).contains(searchT,true)
////                || searchT.isEmpty()
//    } else appList
//    val appFilter = if(searchableApps.isNotEmpty() && searchT.isNotEmpty()) searchableApps.filter {
////        it.packageName.contains(searchT)
////        it.label.contains(searchT)
//        it.doesMatchSearchQuery(searchT)
//    } else searchableApps
//    anViewModel._appAll.value = packList.
//    anViewModel.updateAppAll(packList)
//    anViewModel.updateAppAll()
//    anViewModel.appSearchActive.value = searchT.isNotBlank()
//    var appFilter:List<PackageInfo> = emptyList()
//    LaunchedEffect(
//        true
//    ) {}
//
//    appFilter = if(packList != null && searchT.isNotEmpty()) packList.filter { it.applicationInfo?.loadLabel(pm).toString().contains(searchT,true) || it.packageName.contains(searchT,true) || searchT.isEmpty() } else packList
    val appFilter by anViewModel.appAll.collectAsState()
    val lazyListState = rememberLazyListState()

    Box(
        modifier = Modifier
    ){
        ProvidePreferenceLocals {
            // https://blog.stackademic.com/jetpack-compose-multiplatform-scrollbar-scrolling-7c231a002ee1
            // https://github.com/nanihadesuka/LazyColumnScrollbar
            LazyColumnScrollbar(
                state = lazyListState,
                settings = ScrollbarSettings(
                    thumbSelectedColor = colorScheme.primary,
                    thumbUnselectedColor = colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                )
            ) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item{
                        HTSearchBar(
                            value = searchT,
                            onValueChange = {
                                searchT = it
                            }
                        )
                    }
                    item{
                        SettingCategoryBar(
                            title = stringResource(R.string.recent_apps),
                            icon = {
                                Icon(Icons.Default.Restore,"")
                            },
                        )
                    }
                    item{
                        SettingCategoryBar(
                            title = stringResource(R.string.whole_apps),
                            icon = {
                                Icon(Icons.Default.Apps,"")
                            },
                        )
                    }
                    if(appFilter.isNotEmpty()){
                        items(
//                    count = appList.size
//                    items = appList.filter { it.loadLabel(pm).contains(searchT, true) || it.packageName.contains(searchT, true) || searchT.isEmpty() }
//                    items = appList
                            items = appFilter
//                    items = packList.filter { it.applicationInfo?.loadLabel(pm).toString().contains(searchT,true) || it.packageName.contains(searchT,true) || searchT.isEmpty() }
                        ) {
//                    val ddawe = pm.getApplicationIcon(packList[it].packageName)
//                    val ddawe = pm.getApplicationIcon(appList[it].packageName)
                            val ddawe = pm.getApplicationIcon(it.packageName)
//                    val ddlabel:String = it.loadLabel(pm).toString()
                            val ddlabel:String = it.label
//                    val ddlabel:String = it.applicationInfo?.loadLabel(pm).toString()
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
//                        title = { Text("${packList[it].applicationInfo.loadLabel(pm)}") },
                                title = { Text(ddlabel) },
//                    summary = {Text("${packList.get(it).applicationInfo.loadDescription(pm)}")},
                                summary = {
                                    Text(
//                                packList[it].packageName,
//                                appList[it].packageName,
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
//                            onLaunchApp(appList[it])
                                    onLaunchApp(pm.getApplicationInfo(it.packageName,0))
//                            if(it.applicationInfo != null)
//                                onLaunchApp(it.applicationInfo!!)


                                }
                            )
                        }
                    } else {
                        item{
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                ,
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    "EMPTY"
                                )
                            }
                        }
                    }

                }
            }

//            Scrollbars(state = rememberScrollState())
            // https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Desktop_Components#scrollbars
            // https://foso.github.io/Jetpack-Compose-Playground/desktop/general/scrollbar/
//            VerticalScrollbar(
//                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
//                adapter = rememberScrollbarAdapter(
//                    scrollState = lazyListState
//                )
//            )
        }
    }

}

private fun getAllApps() {

}

@HTPreviewAnnotations
@Composable
fun AllAppsScreenPreview() {
    HTLauncherTheme {
        AllAppsScreen(
        )
    }
}