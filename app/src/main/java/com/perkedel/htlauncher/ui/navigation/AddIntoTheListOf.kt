package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.pdf.PdfDocument.Page
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.enumerations.EditWhich
import com.perkedel.htlauncher.enumerations.PageViewStyle
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.widgets.HTSearchBar
import kotlinx.coroutines.CoroutineScope
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import my.nanihadesuka.compose.LazyColumnScrollbar

@Composable
fun AddIntoTheListOf(
    modifier: Modifier = Modifier,
    data: (List<String>)? = emptyList(),
    pageData: PageData? = null,
    itemData: ItemData? = null,
    homepagesWeHave: HomepagesWeHave? = null,
    addIntoWhich: EditWhich? = EditWhich.Items,
    id:Int = 0,
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
//    onRebuild: (PageData) -> Unit = { pageData: PageData -> },
//    onSwap: (Int, Int) -> Unit = { i: Int, i1: Int -> },
    onSelectThing: (String,EditWhich?,List<String>) -> Unit = {name:String, which:EditWhich?, overwrite:List<String> -> },
    onSwap: (List<String>) -> Unit = {},
    onClose: () -> Unit = {},
    onTryAdd: () -> Unit = {},
    view: View = LocalView.current,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    viewModel: ItemEditorViewModel = viewModel(),
    htViewModel: HTViewModel = viewModel(),
    initViewStyle: PageViewStyle = PageViewStyle.Column,
    windowInfo: WindowInfo = rememberWindowInfo(),
    configuration: Configuration = LocalConfiguration.current,
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
){
    var list:List<String> by remember { mutableStateOf(data ?: emptyList()) }
//    var filterList:List<String> by remember { mutableStateOf(list) }
    val filterList:List<String> by viewModel.wildList.collectAsState()
    var previousCurrentThingList: List<String> by remember { mutableStateOf(
        when(addIntoWhich){
            EditWhich.Pages -> pageData?.items ?: PageData().items
            EditWhich.Home -> homepagesWeHave?.pagesPath ?: HomepagesWeHave().pagesPath
            else -> emptyList()
        }
    ) }
    var areYouSureToRemove:Boolean by remember { mutableStateOf(false) }
    var toRemove:Int by remember { mutableStateOf(-1) }
    var searchSay:String by remember { mutableStateOf("") }
    viewModel.updateSearchInTheWild(searchSay)
    viewModel.updateIsSearchingInTheWild(searchSay.isNotBlank())
    var lazyListState: LazyListState = rememberLazyListState()
//    var isSearching:Boolean by remember { mutableStateOf(false) }
//    when(addIntoWhich){
//        EditWhich.Pages -> {
//            // Add items into this page data from folder
//            list = pageData?.items ?: emptyList()
//        }
//        EditWhich.Home -> {
//            // Add pages into this home data
//            list = itemData?.items ?: emptyList()
//        }
//        else -> list = emptyList()
//    }
//    if(searchSay.isNotBlank()){
//        isSearching = true
//        filterList = list.filter { it.contains(searchSay) }
//    } else{
//        isSearching = false
//        filterList = list
//    }
    LaunchedEffect(
        key1 = data
    ) {
        viewModel.updateWildList(data ?: emptyList())
    }

    ProvidePreferenceLocals {
        LazyColumnScrollbar(
            state = lazyListState
        ) {
            LazyColumn(
                state = lazyListState
            ) {
                item{
                    HTSearchBar(
                        megaTitle = when(addIntoWhich){
                            EditWhich.Pages -> stringResource(R.string.select_an_item)
                            EditWhich.Home -> stringResource(R.string.select_an_page)
                            else -> stringResource(R.string.select_an_idk)
                        },
                        value = searchSay,
                        onValueChange = { searchSay = it },
                    )
                }
                if(filterList.size <= 0){
                    item{
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                            ,
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = stringResource(R.string.item_empty_emphasis)
                            )
                        }
                    }
                } else {
                    items(
                        count = filterList.size,
                        key = { filterList[it] },
                    ) {
                        Preference(
                            title = { Text(text = filterList[it]) },
                            onClick = {
                                previousCurrentThingList = previousCurrentThingList.toMutableList().apply{
                                    add(index = 0, element = filterList[it])
                                }
                                onSelectThing(filterList[it], addIntoWhich, previousCurrentThingList)
                                onClose()
                            },
                            icon = {
                                AsyncImage(
                                    model = when (addIntoWhich) {
                                        EditWhich.Pages -> {
                                            htViewModel.getItemIcon(
                                                of = filterList[it],
                                                context = context,
                                                pm = pm,
                                            )
                                        }
                                        EditWhich.Home -> {
                                            htViewModel.getPageIcon(
                                                of = filterList[it],
                                                context = context,
                                                pm = pm,
                                            )
                                        }
                                        else -> R.drawable.placeholder
                                    },
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(75.dp),
                                    error = painterResource(id = R.drawable.mavrickle),
                                    placeholder = painterResource(id = R.drawable.placeholder),
                                )
                            }
                        )
                    }
                }
            }
        }

    }
}

@HTPreviewAnnotations
@Composable
fun AddIntoTheListOfPreview(){
    HTLauncherTheme {
        Surface() {
            AddIntoTheListOf()
        }
    }
}
