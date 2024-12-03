package com.perkedel.htlauncher.ui.page

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.CombinedClickableNode
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.enumerations.PageGridType
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.getADirectory
import com.perkedel.htlauncher.getATextFile
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.openATextFile
import com.perkedel.htlauncher.ui.previews.BasePagePreviewParameter
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.previews.PagePreviewParameter
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import com.perkedel.htlauncher.widgets.FirstPageCard
import com.perkedel.htlauncher.widgets.ItemCell
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasePage(
    pageData: PageData? = null,
    fileName:String = "",
    isOnNumberWhat: Int = 0,
    isFirstPage: Boolean = false,
    howManyItemsHere: Int = 0,
    onMoreMenuButtonClicked: () -> Unit,
    modifier: Modifier,
    context: Context = LocalContext.current,
    configuration: Configuration = LocalConfiguration.current,
    pm: PackageManager = context.packageManager,
    viewModel: HTViewModel = HTViewModel(),
    contentResolver: ContentResolver = context.contentResolver,
    uiState: HTUIState = HTUIState(),
    colorScheme: ColorScheme = rememberColorScheme(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    json: Json = Json {
        // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
        prettyPrint = true
        encodeDefaults = true
    },
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    windowInfo: WindowInfo = rememberWindowInfo(),
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
    onLaunchOneOfAction: (List<ActionData>)->Unit = {}
){
    // Load this file!
    var pageUri:Uri = Uri.parse("")
    var pageFolder:Uri = Uri.parse("")
    var pageOfIt:PageData = PageData()
    LaunchedEffect(
        true
    ) {
//        Log.d("BasePage", "Eval filename = ${fileName}")
//        Log.d("BasePage", "Eval selected save = ${uiState.selectedSaveDir}")

        if(pageData != null){
            pageOfIt = pageData
        } else {
            if (uiState.pageList.contains(fileName) && uiState.pageList[fileName] != null) {
                pageOfIt = uiState.pageList[fileName]!!
            } else {
//            if (fileName.isNotEmpty() && uiState.selectedSaveDir != null && uiState.selectedSaveDir.toString()
//                    .isNotEmpty()
//            ) {
//                pageFolder = getADirectory(
//                    dirUri = uiState.selectedSaveDir,
//                    context = context,
//                    dirName = context.resources.getString(R.string.pages_folder)
//                )
//                pageUri = getATextFile(
//                    dirUri = pageFolder,
//                    context = context,
//                    fileName = "${fileName}.json",
//                    initData = json.encodeToString<PageData>(PageData()),
//                    hardOverwrite = false
//                )
//                pageOfIt = json.decodeFromString<PageData>(openATextFile(pageUri, contentResolver))
//                Log.d("BasePage", "a Page ${fileName} has:\n${pageOfIt}")
//            } else {
//                Log.d("BasePage", "(EMPTY) a Page ${fileName} has:\n${pageOfIt}")
//            }
//            uiState.pageList[fileName] = pageOfIt
            }
        }
    }

    if(pageData != null){
        pageOfIt = pageData
    } else {
        if (uiState.pageList.contains(fileName) && uiState.pageList[fileName] != null) {
            pageOfIt = uiState.pageList[fileName]!!
        }
    }

    // https://youtu.be/UhnTTk3cwc4?si=5BoNxc4uZdM6y5nG
    // https://youtu.be/qP-ieASbqMY?si=JFoxgnsQyDf3iJob
    // https://youtu.be/NPmgnGFzopA?si=yOJydgvsQrLfsHKk
    // https://youtu.be/HmXgVBys7BU?si=u6nsssd2LeP48TED
    val lazyListState = rememberLazyGridState()
    Column(
        modifier = modifier
    ) {
//        val windowInfo = rememberWindowInfo()
//        val isCompact = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact
        // https://stackoverflow.com/a/64755245/9079640
//        val isOrientation:Int = configuration.orientation // This doesn't seems to work
        if(isCompact){
            // if screen is compact
            LazyVerticalGrid(
                modifier = Modifier,
                columns = when(pageOfIt.gridType){
                    PageGridType.Default -> GridCells.Fixed(pageOfIt.cellCount)
                    PageGridType.Adaptive -> GridCells.Adaptive(pageOfIt.cellSize.dp)
                    else -> GridCells.Fixed(pageOfIt.cellCount)
                },
                state = lazyListState,
                content = {
                    // Permanent Card on first page
                    if (isFirstPage || pageOfIt.isHome){
                        item(
                            span = { GridItemSpan(this.maxLineSpan) }
                        ){
                            FirstPageCard(
                                handoverText = pageOfIt.name,
                                isCompact = isCompact,
                                isOnNumberWhat = isOnNumberWhat,
                                modifier = Modifier.weight(1f),
                                onMoreMenuButton = onMoreMenuButtonClicked,
                            )
                        }
                    }
                    // Rest of the items
                    items(pageOfIt.items.size){i->
                        ItemCell(
                            readTheItemFile = pageOfIt.items[i],
                            handoverText = "Item ${i}",
                            context = context,
                            pm = pm,
                            uiState = uiState,
                            viewModel = viewModel,
                            tts = tts,
                            onClick = onLaunchOneOfAction,
                        )
                    }

                }
            )
        } else {
            // anything else
            Row(
                modifier = Modifier
                ,
            ) {
                // Permanent Card on first page
                if(isFirstPage || pageOfIt.isHome){
                    Column(){
                        FirstPageCard(
                            handoverText = pageOfIt.name,
                            isCompact = isCompact,
                            isOnNumberWhat = isOnNumberWhat,
                            modifier = Modifier.weight(1f),
                            onMoreMenuButton = onMoreMenuButtonClicked,
                        )
//                        Spacer(
//                            modifier = Modifier
//                                .weight(.05f)
//                        )
                    }
                }
                LazyVerticalGrid(
                    columns = when(pageOfIt.gridType){
                        PageGridType.Default -> GridCells.Fixed(pageOfIt.cellCountLandscape)
                        PageGridType.Adaptive -> GridCells.Adaptive(pageOfIt.cellSize.dp)
                        else -> GridCells.Fixed(pageOfIt.cellCountLandscape)
                    },
                    state = lazyListState,
                    content = {
                        // Rest of the items
                        items(pageOfIt.items.size){i->
                            ItemCell(
                                readTheItemFile = pageOfIt.items[i],
                                handoverText = "Item ${i}",
                                context = context,
                                pm = pm,
                                uiState = uiState,
                                viewModel = viewModel,
                                tts = tts,
                                onClick = onLaunchOneOfAction,
                            )
                        }
                    }
                )
            }
        }

    }
}

@HTPreviewAnnotations
@Composable
fun BasePagePreview(
    @PreviewParameter(BasePagePreviewParameter::class) data:PagePreviewParameter
){
    HTLauncherTheme {
        BasePage(
            pageData = PageData(
                name = when(data.gridType){
                    PageGridType.Default -> "Default ${data.fixedCount} ${data.adaptiveSize}"
                    PageGridType.Fixed -> "Fixed ${data.fixedCount}"
                    PageGridType.Adaptive -> "Adaptive ${data.adaptiveSize}"
                    else -> "Idk ${data.fixedCount} ${data.adaptiveSize}"
                },
                gridType = data.gridType,
                cellCount = data.fixedCount,
                cellSize = data.adaptiveSize,
            ),
            isOnNumberWhat = 0,
            isFirstPage = true,
            howManyItemsHere = 10,
            onMoreMenuButtonClicked = {},
            modifier = Modifier.navigationBarsPadding().statusBarsPadding(),
        )
    }
}