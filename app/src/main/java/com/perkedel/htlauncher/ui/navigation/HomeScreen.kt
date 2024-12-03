package com.perkedel.htlauncher.ui.navigation

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.ui.page.BasePage
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import com.perkedel.htlauncher.widgets.HTHorizontalPageButtonShim
import com.perkedel.htlauncher.widgets.HTHorizontalPageIndicators
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
//    navController: NavController?,
//    isFirstPage:Boolean = false,
    handoverPagerState: PagerState = rememberPagerState(pageCount = {10}),
    onAllAppButtonClicked: () -> Unit = {},
    onMoreMenuButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxSize(),
    hideTopBar: Boolean = true,
    context: Context = LocalContext.current,
    configuration: Configuration = LocalConfiguration.current,
    pm: PackageManager = context.packageManager,
    viewModel:HTViewModel = HTViewModel(),
    uiState: HTUIState = HTUIState(),
    contentResolver: ContentResolver = context.contentResolver,
    colorScheme: ColorScheme = rememberColorScheme(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    configFile:HomepagesWeHave? = null,
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
){


    // https://developer.android.com/develop/ui/compose/layouts/pager
    Box(
        modifier = Modifier
            .fillMaxSize()
            // https://youtu.be/pLMw9Vlbfgw Stevdza-San system bar paddings
//            .statusBarsPadding()
//            .navigationBarsPadding()
        ,
    ){
        if(isReady) {
            Column(
                modifier = modifier
//                    .statusBarsPadding()
//                    .navigationBarsPadding()
                ,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HorizontalPager(
                    modifier = Modifier.weight(1f),
                    state = handoverPagerState,
                    ) { page ->
                    BasePage(
                        fileName = if (configFile != null) configFile.pagesPath[page] else "",
                        isOnNumberWhat = page,
                        isFirstPage = page == 0,
                        // TODO: read JSON file `anPage.json` in folder `Pages`, if the `isFirstPage` declaration in itself is set to true.
                        // TODO: a JSON file `PageCollection.json` lists the pages included. It includes some JSON files in `Pages` folder. Basically, it's just 1 array for it, making this in order.
                        onMoreMenuButtonClicked = onMoreMenuButtonClicked,
                        howManyItemsHere = 25,
                        modifier = Modifier.fillMaxSize(),
                        context = context,
                        configuration = configuration,
                        colorScheme = colorScheme,
                        haptic = haptic,
                        viewModel = viewModel,
                        contentResolver = contentResolver,
                        uiState = uiState,
                        tts = tts,
                        onLaunchOneOfAction = onLaunchOneOfAction,
                    )
                }
            }
            // Indicator?!
            HTHorizontalPageButtonShim(
                modifier = Modifier
                        .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .statusBarsPadding()
                ,
                size = 30.dp,
                hasFirstLastButton = !isCompact,
                onLastPage = {
                    coroutineScope.launch {
                        handoverPagerState.animateScrollToPage(0)
                    }
                },
                onFirstPage = {
                    coroutineScope.launch {
                        handoverPagerState.animateScrollToPage(page = handoverPagerState.pageCount-1)
                    }
                },
                onLeftPage = {
                    coroutineScope.launch {
                        handoverPagerState.animateScrollToPage(page = handoverPagerState.currentPage-1)
                    }
                },
                onRightPage = {
                    coroutineScope.launch {
                        handoverPagerState.animateScrollToPage(page = handoverPagerState.currentPage+1)
                    }
                },
            ){
                HTHorizontalPageIndicators(
                    pageCount = handoverPagerState.pageCount,
                    currentPage = handoverPagerState.currentPage,
                    targetPage = handoverPagerState.targetPage,
                    currentPageOffsetFraction = handoverPagerState.currentPageOffsetFraction,
                    modifier = Modifier
                            .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                    ,
                    indicatorColor = rememberColorScheme().primary,
                    selectedIndicatorSize = 32.dp,
                    unselectedIndicatorSize = 24.dp,
                    indicatorCornerRadius = 8.dp, // you asked for it, Srf. Yarn! Same, I'm obsessed with capsule shape!
                    onSetPage = { page ->
                        // Set to what page
                        // https://developer.android.com/develop/ui/compose/layouts/pager HOW?
                        // https://medium.com/@domen.lanisnik/exploring-the-official-pager-in-compose-8c2698c49a98
//                                    handoverPagerState.targetPage
                        coroutineScope.launch {
                            handoverPagerState.animateScrollToPage(page)
                        }
                    }
                )
            }
        } else {
//            Column(
//                modifier = modifier
////                    .statusBarsPadding()
////                    .navigationBarsPadding()
//                ,
//            ) {
//                Text(text = "Loading")
//            }
            HTLoading()
        }
    }

}

@HTPreviewAnnotations
@Composable
fun HomeScreenPreview(){
    HTLauncherTheme {
        HomeScreen(
            modifier = Modifier
                .navigationBarsPadding()
                .statusBarsPadding(),
            isReady = true,
//            onAllAppButtonClicked = {},
//            onMoreMenuButtonClicked = {},
        )
    }
}