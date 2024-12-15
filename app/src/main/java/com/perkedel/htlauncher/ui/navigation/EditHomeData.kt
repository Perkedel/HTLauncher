@file:OptIn(ExperimentalFoundationApi::class)

package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.modules.ttsSpeak
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.preference

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
){
    var rebuild: () -> Unit = {
        data?.copy()?.let { onRebuildItem(it) }
    }

    ProvidePreferenceLocals {
        LazyColumn(

        ) {
            item{
                Text("Homescreen ${data}")
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
                key = "reorder_pages",
                title = { Text(text = stringResource(R.string.editor_page_reorder_item) ) },
                icon = { Icon(imageVector = Icons.Default.Reorder, contentDescription = null) },
                summary = { Text(text = pluralStringResource(R.plurals.editor_home_reorder_pages_count_plural, data?.pagesPath?.size ?: 0, data?.pagesPath?.size ?: 0)) },
//                summary = { Text(text = stringResource(R.string.editor_page_reorder_item_count, data?.pagesPath?.size ?: 0)) },
                onClick = {
                    onSelectedKey("reorder_pages")
                }
            )
        }
    }
}