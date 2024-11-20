package com.perkedel.htlauncher.ui.navigation

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.ui.page.BasePage
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

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
    isReady:Boolean = false,
){


    // https://developer.android.com/develop/ui/compose/layouts/pager
    Box(
        modifier = Modifier
    ){
        if(isReady) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
//        Button(
//            onClick = {
////                    navController?.navigate(Screen.AllAppsScreen)
////                navController?.navigate(Screen.AllAppsScreen.name)
//                onAllAppButtonClicked()
//            },
//            modifier = Modifier.align(Alignment.End)
//        ) {
//            Text(text="All Apps")
//        }

                HorizontalPager(
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
                        modifier = Modifier,
                        context = context,
                        configuration = configuration,
                        colorScheme = colorScheme,
                        haptic = haptic,
                        viewModel = viewModel,
                        contentResolver = contentResolver,
                        uiState = uiState,
                    )
                }

            }
            // Indicator?!
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(handoverPagerState.pageCount) { iteration ->
                    val color =
                        if (handoverPagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(16.dp)
                            .combinedClickable(
                                onClick = {
                                    // Set to what page
                                },
                                onLongClick = {

                                }
                            ),

                        )
                }
            }
        } else {
            Column(
                modifier = modifier,
            ) {
                Text(text = "Loading")
            }
        }
    }

}

@HTPreviewAnnotations
@Composable
fun HomeScreenPreview(){
    HTLauncherTheme {
        HomeScreen(
                isReady = true,
//            onAllAppButtonClicked = {},
//            onMoreMenuButtonClicked = {},
        )
    }
}