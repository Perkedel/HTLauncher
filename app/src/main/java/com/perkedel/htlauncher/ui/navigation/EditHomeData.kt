@file:OptIn(ExperimentalFoundationApi::class)

package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.modules.ttsSpeak
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.widgets.OptionItemCard
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.preference
import my.nanihadesuka.compose.LazyVerticalGridScrollbar

@Composable
fun EditHomeData(
    modifier: Modifier = Modifier,
    data: HomepagesWeHave? = null,
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    viewModel: ItemEditorViewModel = ItemEditorViewModel(),
    windowInfo: WindowInfo = rememberWindowInfo(),
    configuration: Configuration = LocalConfiguration.current,
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
    onRebuildItem:(HomepagesWeHave)->Unit = {},
    onEditActionData: (ActionData, Int)->Unit = { actionData: ActionData, i: Int -> {}},
    inspectionMode: Boolean = LocalInspectionMode.current,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    onSelectedKey: (String)->Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    grided:Boolean = true,
){
    var lazyGridState = rememberLazyGridState()
    LaunchedEffect(
        key1 = data
    ) {

    }

    var rebuild: () -> Unit = {
        data?.copy()?.let { onRebuildItem(it) }
    }

    ProvidePreferenceLocals {
        if(grided) {
            LazyVerticalGridScrollbar(
                state = lazyGridState
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = lazyGridState,
                ) {
                    item(
                        span = { GridItemSpan(this.maxLineSpan) }
                    ) {
                        OutlinedCard(
                            modifier = Modifier.padding(4.dp),
                        ) {
                            Text(
                                text = "Homescreen ${data}",
                                modifier = Modifier.padding(8.dp),
                            )
                        }

                    }
                    item {
                        OptionItemCard(
                            fullSpanDesign = true,
                            compactDesign = true,
                            title = stringResource(R.string.editor_page_reorder_item),
                            icon = Icons.Default.Reorder,
                            summary = pluralStringResource(
                                R.plurals.editor_page_reorder_item_count_plural,
                                data?.pagesPath?.size ?: 0,
                                data?.pagesPath?.size ?: 0
                            ),
                            readAriaOnLongClick = true,
                            onClick = {
                                onSelectedKey("reorder_pages")
                            },
                        )
                    }
                }
            }
        }
        else {
            LazyColumn(

            ) {
                item {
                    Text("Homescreen ${data}")
                }
                preference(
                    modifier = Modifier
                        .combinedClickable(
                            onClick = {},
                            onLongClick = {
                                ttsSpeak(
                                    handover = tts,
                                    message = ""
                                )
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            onLongClickLabel = context.resources.getString(R.string.quick_start_option),
                        ),
                    key = "reorder_pages",
                    title = { Text(text = stringResource(R.string.editor_page_reorder_item)) },
                    icon = { Icon(imageVector = Icons.Default.Reorder, contentDescription = null) },
                    summary = {
                        Text(
                            text = pluralStringResource(
                                R.plurals.editor_page_reorder_item_count_plural,
                                data?.pagesPath?.size ?: 0,
                                data?.pagesPath?.size ?: 0
                            )
                        )
                    },
//                summary = { Text(text = stringResource(R.string.editor_page_reorder_item_count, data?.pagesPath?.size ?: 0)) },
                    onClick = {
                        onSelectedKey("reorder_pages")
                    }
                )
            }
        }
    }
}

@HTPreviewAnnotations
@Composable
fun EditHomeDataPreview() {
    HTLauncherTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .statusBarsPadding()
        ) {
            EditHomeData()
        }
    }
}