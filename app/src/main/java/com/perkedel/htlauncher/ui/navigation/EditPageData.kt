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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.enumerations.ConfigSelected
import com.perkedel.htlauncher.enumerations.PageViewStyle
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.modules.ttsSpeak
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import me.zhanghai.compose.preference.ListPreference
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.listPreference
import me.zhanghai.compose.preference.preference

@Composable
fun EditPageData(
    modifier: Modifier = Modifier,
    data: PageData? = null,
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    viewModel: ItemEditorViewModel = ItemEditorViewModel(),
    windowInfo: WindowInfo = rememberWindowInfo(),
    configuration: Configuration = LocalConfiguration.current,
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
    onRebuildItem:(PageData)->Unit = {},
    onEditActionData: (ActionData, Int)->Unit = { actionData: ActionData, i: Int -> {}},
    inspectionMode: Boolean = LocalInspectionMode.current,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    onSelectedKey: (String)->Unit = {}
){
    var pageViewStyle:PageViewStyle by remember { mutableStateOf(data?.viewStyle ?: PageViewStyle.Default) }
    var rebuildPageData: () -> Unit = {
        data?.copy(
            viewStyle = pageViewStyle,

        )?.let { onRebuildItem(it) }
    }

    // https://youtu.be/6dRwaXH2cYA
    // https://youtu.be/E00-PHw90X0
    ProvidePreferenceLocals {
        LazyColumn {
            item{
                Text("Page ${data}")
            }
            preference(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            ttsSpeak(
                                handover = tts,
                                message = "${context.resources.getString(R.string.donation_option)}. ${context.resources.getString(
                                    R.string.donation_option_desc)}"
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onLongClickLabel = context.resources.getString(R.string.quick_start_option),
                    )
                ,
                key = "reorder_items",
                title = { Text(text = stringResource(R.string.editor_page_reorder_item) ) },
                icon = { Icon(imageVector = Icons.Default.Reorder, contentDescription = null) },
                summary = { Text(text = pluralStringResource(R.plurals.editor_page_reorder_item_count_plural, data?.items?.size ?: 0, data?.items?.size ?: 0)) },
//                summary = { Text(text = stringResource(R.string.editor_page_reorder_item_count, data?.items?.size ?: 0)) },
                onClick = {
                    onSelectedKey("reorder_items")
                }
            )
            item {
                ListPreference<PageViewStyle>(
                    value = pageViewStyle,
                    modifier = Modifier,
                    values = PageViewStyle.entries,
//                defaultValue = PageViewStyle.Grid,
                    summary = { Text(text = data?.viewStyle.toString()) },
                    title = { Text(text = stringResource(R.string.editor_page_view_style)) },
                    onValueChange = {
                        pageViewStyle = it
                        rebuildPageData()
                    },
                    icon = {
                        Icon(when(pageViewStyle){
                            PageViewStyle.Default -> Icons.Default.GridView
                            PageViewStyle.Grid -> Icons.Default.GridView
                            PageViewStyle.Column -> Icons.AutoMirrored.Default.ViewList
                            else -> Icons.Default.GridView
                        },"")
                    }
                )
            }
//            listPreference(
//                modifier = Modifier,
//                key = "view_style",
//                defaultValue = PageViewStyle.Grid,
//                values = PageViewStyle.entries,
//                title = { Text(text = stringResource(R.string.editor_page_view_style) ) },
//                summary = { Text(text = data?.viewStyle.toString()) },
////                onValueChange = { onSelectedKey("view_style") },
//            )
//
        }
    }
}

@HTPreviewAnnotations
@Composable
fun EditPageDataPreview(){
    HTLauncherTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .statusBarsPadding()
        ) {
            EditPageData()
        }
    }
}