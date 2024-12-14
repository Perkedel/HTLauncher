package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.DropdownMenuOptions
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.enumerations.PageViewStyle
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.ui.dialog.HTAlertDialog
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.widgets.HTButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import my.nanihadesuka.compose.LazyColumnScrollbar
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState
import sh.calvin.reorderable.rememberReorderableLazyListState
import java.util.UUID

@Composable
fun EditHomePageOrders(
    modifier: Modifier = Modifier,
    data: HomepagesWeHave? = null,
    id:Int = 0,
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    onRebuild: (HomepagesWeHave) -> Unit = { homescreen: HomepagesWeHave -> },
//    onSwap: (Int, Int) -> Unit = { i: Int, i1: Int -> },
    onSwap: (List<String>) -> Unit = {},
    onClose: () -> Unit = {},
    view: View = LocalView.current,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    viewModel: ItemEditorViewModel = viewModel(),
    htViewModel: HTViewModel = viewModel(),
    initViewStyle: PageViewStyle = PageViewStyle.Column,
    windowInfo: WindowInfo = rememberWindowInfo(),
    configuration: Configuration = LocalConfiguration.current,
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
    onTryAdd: ()->Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
){
    var list:List<String> by remember { mutableStateOf(data?.pagesPath ?: HomepagesWeHave().pagesPath) }
    var uniqueList:MutableMap<String,String> by remember { mutableStateOf(linkedMapOf()) }
    LaunchedEffect(
        key1 = data
    ) {
        list = data?.pagesPath ?: PageData().items
        uniqueList.clear()
        for(thing in list){
            uniqueList[UUID.randomUUID().toString()] = thing
        }
    }
    var areYouSureToRemove:Boolean by remember { mutableStateOf(false) }
    var toRemove:Int by remember { mutableStateOf(-1) }
    var theRemovedItem:List<String> by remember { mutableStateOf(emptyList()) }

    val lazyListState = rememberLazyGridState()
    val lazyColumnState = rememberLazyListState()
    val bakeData: ()->Unit = {
        onSwap(list)
        data?.copy(
            pagesPath = list
        )?.let { onRebuild(it) }
    }
    val addItemToHere:(String,Boolean) -> Unit = { name:String, intoTop:Boolean ->
        list = list.toMutableList().apply {
            add(if(intoTop) 0 else list.size-1, name)
        }

        bakeData()
    }
    val addItemToIndex:(String,Int) -> Unit = { name:String, index:Int ->
        list = list.toMutableList().apply {
            add(index, name)
        }

        bakeData()
    }
    val removeItemFromHere: (String)->Unit = { name:String ->
        if(toRemove >= 0) {
            list = list.toMutableList().apply {
                remove(name)
            }

            bakeData()
        }
    }
    val removeItemFromHereIndex: (Int)-> Unit = {
        coroutineScope.launch {
            var theItem:String = ""
            var andIndex:Int = 0
            list = list.toMutableList().apply {
                andIndex = it
                theItem = removeAt(it)
            }
            theRemovedItem = theRemovedItem.toMutableList().apply {
                add(theItem)
            }
            val snackbarActionResult: SnackbarResult = snackbarHostState.showSnackbar(
                message = "Removed ${theItem}",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            when(snackbarActionResult){
                SnackbarResult.Dismissed -> {

                }
                SnackbarResult.ActionPerformed -> {
                    theRemovedItem = theRemovedItem.toMutableList().apply{
                        addItemToIndex(theItem, andIndex)
                        remove(theItem)
                    }
                }
                else -> {}
            }
        }

        bakeData()
    }
    val askRemoveItemFromHereIndex:(Int)->Unit = {
        toRemove = it
        areYouSureToRemove = true
    }
    val moveItemToWhere:(String,Int)->Unit = { whichIs:String, intoRelative:Int ->
        list = list.toMutableList().apply{

            add(indexOf(whichIs)+intoRelative, removeAt(indexOf(whichIs)))
        }

//        view.playSoundEffect(SoundEffectConstants.CLICK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
        } else {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }

        bakeData()
    }
    val moveItemToExtreme:(String, Boolean)->Unit = {whichIs:String, toTop:Boolean ->
        list = list.toMutableList().apply{
            add(if(toTop) 0 else list.size-1, removeAt(indexOf(whichIs)))
        }

//        view.playSoundEffect(SoundEffectConstants.CLICK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
        } else {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }

        bakeData()
    }
    val reorderableLazyListState = rememberReorderableLazyGridState(
        lazyGridState = lazyListState,
        scrollThresholdPadding = WindowInsets.systemBars.asPaddingValues(),
    ) { from, to ->
        list = list.toMutableList().apply {
            // can't use .index because there are other items in the list (headers, footers, etc)
            // https://github.com/Calvin-LL/Reorderable/blob/main/demoApp/composeApp/src/commonMain/kotlin/sh/calvin/reorderable/demo/ui/App.kt
            val fromIndex = indexOfFirst { it == from.key }
            val toIndex = indexOfFirst { it == to.key }
//            add(to.index, removeAt(from.index))
            add(toIndex, removeAt(fromIndex))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
        } else {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
        bakeData()
    }
    val reorderableLazyColumnState = rememberReorderableLazyListState(
        lazyListState =lazyColumnState,
        scrollThresholdPadding = WindowInsets.systemBars.asPaddingValues(),
    ) { from, to ->
        list = list.toMutableList().apply {
            // can't use .index because there are other items in the list (headers, footers, etc)
            // https://github.com/Calvin-LL/Reorderable/blob/main/demoApp/composeApp/src/commonMain/kotlin/sh/calvin/reorderable/demo/ui/App.kt
            val fromIndex = indexOfFirst { it == from.key }
            val toIndex = indexOfFirst { it == to.key }
//            add(to.index, removeAt(from.index))
            add(toIndex, removeAt(fromIndex))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
        } else {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
        bakeData()
    }

    val majorActionCard: @Composable () -> Unit = {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                HTButton(
                    modifier = Modifier,
                    title = stringResource(R.string.action_close),
                    leftIcon = Icons.Default.Check,
                    onClick = onClose
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                HTButton(
                    modifier = Modifier,
                    title = stringResource(R.string.action_add),
                    rightIcon = Icons.Default.Add,
                    onClick = {
                        onTryAdd()
                    }
                )
            }

        }
    }

    val columnViewStyle: @Composable (HomepagesWeHave?) -> Unit ={ homeScreen:HomepagesWeHave?->
        homeScreen?.let {
            LazyColumnScrollbar(
                state = lazyColumnState
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyColumnState
                ) {
                    item(
                        key = "__majorActionCard",
                    ) {
                        majorActionCard()
                    }
                    repeat(times = list.size){
//                        val uid:String = UUID.randomUUID().toString()
//                        val uid:String = uniqueList.keys.toList()[it]
                        val unKey:String = list[it]
                        item(
                            key = unKey,
                        ){
                            ReorderableItem(
                                state = reorderableLazyColumnState,
//                                key = it.hashCode(),
                                key = unKey,
                                modifier = Modifier
                                    .semantics {
                                        customActions = listOf(
                                            CustomAccessibilityAction(
                                                label = context.resources.getString(R.string.action_move_top_detailed,list[it]),
                                                action = {
                                                    moveItemToExtreme(list[it],true)
                                                    true
                                                }
                                            ),
                                            CustomAccessibilityAction(
                                                label = context.resources.getString(R.string.action_move_up_detailed,list[it]),
                                                action = {
                                                    moveItemToWhere(list[it],-1)
                                                    true
                                                }
                                            ),
                                            CustomAccessibilityAction(
                                                label = context.resources.getString(R.string.action_move_down_detailed,list[it]),
                                                action = {
                                                    moveItemToWhere(list[it],1)
                                                    true
                                                }
                                            ),
                                            CustomAccessibilityAction(
                                                label = context.resources.getString(R.string.action_move_bottom_detailed,list[it]),
                                                action = {
                                                    moveItemToExtreme(list[it],false)
                                                    true
                                                }
                                            ),
                                            CustomAccessibilityAction(
                                                label = context.resources.getString(R.string.action_remove_detailed,list[it]),
                                                action = {
//                                                    askRemoveItemFromHereIndex(list.indexOf(it))
                                                    askRemoveItemFromHereIndex(it)
                                                    true
                                                }
                                            ),
                                        )
                                    }
                                ,
                            ) { isDragging ->
                                // appear pop up menu of action move up or down
                                // https://github.com/zhanghai/ComposePreference/blob/master/library/src/main/java/me/zhanghai/compose/preference/ListPreference.kt
                                // https://youtu.be/QCSJfMqQY9A Philipp Lackner
                                // https://youtu.be/HeZ1ftQTBgY
                                // https://github.com/philipplackner/ComposeContextDropDown
                                // https://github.com/philipplackner/ComposeContextDropDown/blob/master/app/src/main/java/com/plcoding/composecontextdropdown/PersonItem.kt
                                var openSelector by remember{ mutableStateOf(false) }
                                val dropdownOptions: List<DropdownMenuOptions> = listOf(
                                    DropdownMenuOptions(
                                        key = "move_top",
                                        label = stringResource(R.string.action_move_top),
                                        onClick = {
                                            moveItemToExtreme(list[it],true)
                                        },
                                        icon = Icons.Default.VerticalAlignTop
                                    ),
                                    DropdownMenuOptions(
                                        key = "move_up",
                                        label = stringResource(R.string.action_move_up),
                                        onClick = {
                                            moveItemToWhere(list[it],-1)
                                        },
                                        icon = Icons.Default.KeyboardArrowUp
                                    ),
                                    DropdownMenuOptions(
                                        key = "move_down",
                                        label = stringResource(R.string.action_move_down),
                                        onClick = {
                                            moveItemToWhere(list[it],1)
                                        },
                                        icon = Icons.Default.KeyboardArrowDown
                                    ),
                                    DropdownMenuOptions(
                                        key = "move_bottom",
                                        label = stringResource(R.string.action_move_bottom),
                                        onClick = {
                                            moveItemToExtreme(list[it],false)
                                        },
                                        icon = Icons.Default.VerticalAlignBottom
                                    ),
                                    DropdownMenuOptions(
                                        key = "remove",
                                        label = stringResource(R.string.action_remove),
                                        onClick = {
//                                            askRemoveItemFromHereIndex(list.indexOf(it))
                                            askRemoveItemFromHereIndex(it)
                                        },
                                        icon = Icons.Default.Delete
                                    ),
                                )
                                val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp,
                                    label = "DraggingItem"
                                )
                                val pageData: PageData = htViewModel.getPageData(
                                    of = list[it],
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
                                            model = htViewModel.getPageIcon(
                                                of = pageData.name,
                                                context = context,
                                                ignoreFile = false,
                                                pm = pm,
                                                forceReload = false,
                                            ),
                                            contentDescription = "",
                                        )
                                        Text(
                                            text = pageData.name,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(horizontal = 8.dp)
                                        )
                                        IconButton(
                                            modifier = Modifier.draggableHandle(
                                                onDragStarted = {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                                        view.performHapticFeedback(
                                                            HapticFeedbackConstants.DRAG_START)
                                                    } else {
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    }
                                                },
                                                onDragStopped = {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                                        view.performHapticFeedback(
                                                            HapticFeedbackConstants.GESTURE_END)
                                                    } else {
                                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    }
                                                },
                                            ),
                                            onClick = {
                                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                                openSelector = true
                                            },
                                        ) {
                                            Icon(Icons.Default.DragHandle, contentDescription = "Reorder")
                                        }
                                        DropdownMenu(
                                            expanded = openSelector,
                                            onDismissRequest = {
                                                openSelector = false
                                            }
                                        ) {
                                            dropdownOptions.forEach(){ dropItem ->
                                                DropdownMenuItem(
                                                    onClick = {
                                                        dropItem.onClick()
                                                        view.playSoundEffect(SoundEffectConstants.CLICK)
                                                        openSelector = false
                                                    },
                                                    text = { Text(dropItem.label) },
                                                    leadingIcon = { Icon(dropItem.icon,"") },
                                                )
                                            }
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
    }

    ProvidePreferenceLocals {
        data?.let {
            columnViewStyle(data)
        }
    }
    if(areYouSureToRemove) {
        if (toRemove >= 0) {
            HTAlertDialog(
                title = stringResource(R.string.remove_dialog_title, list[toRemove]),
                context = context,
                text = "${
                    stringResource(
                        R.string.remove_dialog_content,
                        list[toRemove]
                    )
                }\n${stringResource(R.string.will_not_delete_item_itself)}",
                onDismissRequest = {
                    areYouSureToRemove = false
                    toRemove = -1
                },
                onConfirm = {
                    removeItemFromHereIndex(toRemove)
                    areYouSureToRemove = false
                    toRemove = -1
                }
            )
        } else {
            LaunchedEffect(
                key1 = toRemove,
                key2 = areYouSureToRemove
            ) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.resources.getString(R.string.not_found, list[toRemove]),
                        duration = SnackbarDuration.Short
                    )
                }
            }
            areYouSureToRemove = false
        }
    }
}

@HTPreviewAnnotations
@Composable
fun PreviewEditHomePageOrders() {
    HTLauncherTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ){
            EditHomePageOrders(
                data = HomepagesWeHave(),
            )
        }
    }
}