package com.perkedel.htlauncher.ui.navigation

import android.content.Context
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.ui.page.BasePage
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
//    navController: NavController?,
//    isFirstPage:Boolean = false,
    handoverPagerState: PagerState = rememberPagerState(pageCount = {10}),
    onAllAppButtonClicked: () -> Unit,
    onMoreMenuButtonClicked: () -> Unit,
    modifier: Modifier = Modifier.fillMaxSize(),
    hideTopBar: Boolean = true,
    context: Context = LocalContext.current,
    colorScheme: ColorScheme = rememberColorScheme(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
){
    // https://developer.android.com/develop/ui/compose/layouts/pager
    Box(
        modifier = Modifier
    ){
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
                    isOnNumberWhat = page,
                    isFirstPage = page == 0,
                    // TODO: read JSON file `anPage.json` in folder `Pages`, if the `isFirstPage` declaration in itself is set to true.
                    // TODO: a JSON file `PageCollection.json` lists the pages included. It includes some JSON files in `Pages` folder. Basically, it's just 1 array for it, making this in order.
                    onMoreMenuButtonClicked = onMoreMenuButtonClicked,
                    howManyItemsHere = 25,
                    modifier = Modifier,
                    context = context,
                    colorScheme = colorScheme,
                    haptic = haptic,
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
                val color = if (handoverPagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                        .combinedClickable (
                            onClick = {
                                // Set to what page
                            },
                            onLongClick = {

                            }
                        )
                    ,

                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HTLauncherTheme {
        HomeScreen(

            onAllAppButtonClicked = {},
            onMoreMenuButtonClicked = {},
        )
    }
}