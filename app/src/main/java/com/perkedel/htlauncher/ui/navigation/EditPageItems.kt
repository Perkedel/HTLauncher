@file:OptIn(ExperimentalComposeUiApi::class)

package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.enumerations.PageGridType
import com.perkedel.htlauncher.enumerations.PageViewStyle
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import com.perkedel.htlauncher.widgets.OutlinedText
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.LazyVerticalGridScrollbar
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun EditPageItems(
    modifier: Modifier = Modifier,
    data: PageData? = null,
    id:Int = 0,
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    onRebuild: (PageData) -> Unit = { pageData: PageData -> },
//    onSwap: (Int, Int) -> Unit = { i: Int, i1: Int -> },
    onSwap: (List<String>) -> Unit = {},
    view: View = LocalView.current,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    viewModel: HTViewModel = viewModel(),
    initViewStyle: PageViewStyle = PageViewStyle.Column,
    windowInfo: WindowInfo = rememberWindowInfo(),
    configuration: Configuration = LocalConfiguration.current,
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
) {
    // https://medium.com/@mousaieparniyan/building-a-draggable-lazy-column-in-jetpack-compose-372e4783d964
    // https://dev.to/mardsoul/how-to-create-lazycolumn-with-drag-and-drop-elements-in-jetpack-compose-part-1-4bn5
    // https://medium.com/@artemsi93/reordering-list-via-drag-n-drop-in-jetpack-compose-cfb8c63ccf9b
    // https://github.com/Calvin-LL/Reorderable use this!
    // https://github.com/aclassen/ComposeReorderable
    // https://github.com/Calvin-LL/Reorderable?tab=readme-ov-file#complete-example-with-haptic-feedback and with this section.
//    val dragDropState = rememberDragDropState(onSwap)
    var list:List<String> by remember { mutableStateOf(data?.items ?: PageData().items) }
    val lazyListState = rememberLazyGridState()
    val lazyColumnState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyGridState(lazyListState) { from, to ->
        list = list.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
        } else {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
        onSwap(list)
    }
    val reorderableLazyColumnState = rememberReorderableLazyListState(lazyColumnState) { from, to ->
        list = list.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
        } else {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
        onSwap(list)
    }
    val viewStyle by remember { mutableStateOf(initViewStyle) }

    val gridViewStyle: @Composable (PageData?) -> Unit = { pageData:PageData?->
        pageData?.let {
            LazyVerticalGridScrollbar(
                state = lazyListState,
            ) {
                LazyVerticalGrid(
                    state = lazyListState,
                    columns = when(pageData.gridType){
                        PageGridType.Default -> GridCells.Fixed(if(isCompact) pageData.cellCount else pageData.cellCountLandscape)
                        PageGridType.Adaptive -> GridCells.Adaptive(pageData.cellSize.dp)
                        else -> GridCells.Fixed(pageData.cellCount)
                    },
                ) {
                    repeat(times = list.size){
                        val itemData:ItemData = viewModel.getItemData(
                            of = list[it],
                            context = context,
                        )
                        item(
                            key = it,
                            span = { if(itemData.isCategory) GridItemSpan(this.maxLineSpan) else GridItemSpan(1) }
                        ){
                            ReorderableItem(
                                state = reorderableLazyListState,
                                key = it,
                            ) { isDragging ->
                                val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp,
                                    label = "DraggingItem"
                                )
                                val itemCelling: @Composable () -> Unit = {
                                    Surface(
                                        shadowElevation = elevation,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                    ) {
                                        Box(){
                                            AsyncImage(
                                                modifier = Modifier.fillMaxSize(),
                                                placeholder = painterResource(R.drawable.placeholder),
                                                error = painterResource(R.drawable.mavrickle),
                                                model = viewModel.getItemIcon(
                                                    of = itemData.name,
                                                    context = context,
                                                    ignoreFile = false,
                                                    pm = pm,
                                                    forceReload = false,
                                                ),
                                                contentDescription = "",
                                            )
                                            OutlinedText(
                                                modifier = Modifier
                                                    .align(Alignment.BottomCenter)
                                                    .basicMarquee()
                                                ,
                                                text = itemData.label,
                                                textAlign = TextAlign.Center,
                                                fillColor = rememberColorScheme().onSurface,
                                                outlineColor = rememberColorScheme().surfaceBright,
                                                outlineDrawStyle = Stroke(
                                                    width = 10f,
                                                    miter = 5f,
                                                    join = StrokeJoin.Round,
                                                ),
                                            )
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                ,
                                                colors = CardDefaults.cardColors(
                                                    // ask codeium for help: apply half transparency variant of this color
                                                    containerColor = rememberColorScheme().surface.copy(alpha = 0.5f)
                                                )
                                            ) {
                                                Row(

                                                ){
                                                    Spacer(
                                                        modifier = Modifier
                                                            .weight(1f)
                                                        ,
                                                    )
                                                    IconButton(
                                                        modifier = Modifier.draggableHandle(
                                                            onDragStarted = {
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                                                    view.performHapticFeedback(HapticFeedbackConstants.DRAG_START)
                                                                } else {
                                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                }
                                                            },
                                                            onDragStopped = {
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                                                    view.performHapticFeedback(HapticFeedbackConstants.GESTURE_END)
                                                                } else {
                                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                                }
                                                            },
                                                        ),
                                                        onClick = {},
                                                    ) {
                                                        Icon(Icons.Default.DragHandle, contentDescription = "Reorder")
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                                val itemCategorizing: @Composable () -> Unit = {

                                }


                                if(itemData.isCategory){
                                    itemCategorizing()
                                } else {
                                    itemCelling()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    val columnViewStyle: @Composable (PageData?) -> Unit ={ pageData:PageData?->
        pageData?.let {
            LazyColumnScrollbar(
                state = lazyColumnState
            ) {
                LazyColumn(
                    state = lazyColumnState
                ) {
                    items(
                        items = list,
                        key = { it }
                    ){
                        ReorderableItem(
                            state = reorderableLazyColumnState,
                            key = it,
                        ) { isDragging ->
                            val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp,
                                label = "DraggingItem"
                            )
                            val itemData:ItemData = viewModel.getItemData(
                                of = it,
                                context = context,
                            )
                            Surface(shadowElevation = elevation) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    AsyncImage(
                                        modifier = Modifier.size(72.dp),
                                        placeholder = painterResource(R.drawable.placeholder),
                                        error = painterResource(R.drawable.mavrickle),
                                        model = viewModel.getItemIcon(
                                            of = itemData.name,
                                            context = context,
                                            ignoreFile = false,
                                            pm = pm,
                                            forceReload = false,
                                        ),
                                        contentDescription = "",
                                    )
                                    Text(
                                        text = itemData.label,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 8.dp)
                                    )
                                    IconButton(
                                        modifier = Modifier.draggableHandle(
                                            onDragStarted = {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                                    view.performHapticFeedback(HapticFeedbackConstants.DRAG_START)
                                                } else {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                }
                                            },
                                            onDragStopped = {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                                    view.performHapticFeedback(HapticFeedbackConstants.GESTURE_END)
                                                } else {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                }
                                            },
                                        ),
                                        onClick = {},
                                    ) {
                                        Icon(Icons.Default.DragHandle, contentDescription = "Reorder")
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .padding(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    data?.let {
        when(viewStyle){
            PageViewStyle.Grid -> {
                gridViewStyle(data)
            }
            PageViewStyle.Column -> {
                columnViewStyle(data)
            }
            else -> {
                columnViewStyle(data)
            }
        }

    }
}

@HTPreviewAnnotations
@Composable
fun EditPageItemsPreview() {
    HTLauncherTheme {
        Surface(
            modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()
        ) {
            EditPageItems(
                data = PageData(),
                initViewStyle = PageViewStyle.Column
            )
        }

    }
}