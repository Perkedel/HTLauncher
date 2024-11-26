package com.perkedel.htlauncher.ui.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.widgets.HTButton

@Composable
fun EditActionData(
    modifier: Modifier = Modifier,
    data: ActionData? = null,
    id:Int = 0,
    viewModel: ItemEditorViewModel = ItemEditorViewModel(),
    windowInfo: WindowInfo = rememberWindowInfo(),
    configuration: Configuration = LocalConfiguration.current,
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
    onRebuild: (ActionData,Int) -> Unit = { actionData: ActionData, i: Int -> },
    onClose: ()->Unit = {},
){
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ){
        Text("Action ${id}\n${data}")
        HTButton(
            title = "Close",
            onClick = onClose
        )
    }
}