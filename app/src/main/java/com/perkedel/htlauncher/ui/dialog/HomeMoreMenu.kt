package com.perkedel.htlauncher.ui.dialog

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.xr.compose.spatial.SpatialDialog
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.switchPreference
import me.zhanghai.compose.preference.preference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMoreMenu (
    modifier: Modifier,
    onChosenMenu: (String) -> Unit,
    onDismissRequest: () -> Unit,
){
    // https://developer.android.com/develop/ui/compose/components/dialog
//    val activity = (LocalContext.current as Activity)

//    BasicAlertDialog(
//        modifier = Modifier,
//        onDismissRequest = {
//            // Dismiss the dialog when the user clicks outside the dialog or on the back
//            // button. If you want to disable that functionality, simply use an empty
//            // onCloseRequest.
//        },
//        content = {
//            Text("HAHO")
//        }
//    )
    SpatialDialog(
        onDismissRequest = onDismissRequest
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
//                .height(250.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
//            Text(
//                text = "This is a minimal dialog",
//                modifier = Modifier
//                    .fillMaxSize()
//                    .wrapContentSize(Alignment.Center),
//                textAlign = TextAlign.Center,
//            )
            // https://github.com/zhanghai/ComposePreference
            // https://stackoverflow.com/a/77611262
            ProvidePreferenceLocals {
                LazyColumn(
                    modifier = Modifier,
                ) {
//                    switchPreference(
//                        key = "switch_preference",
//                        defaultValue = false,
//                        title = { Text(text = "Switch preference") },
//                        icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
//                        summary = { Text(text = if (it) "On" else "Off") }
//                    )
                    preference(
                        key = "edit",
                        title = { Text(text = stringResource(R.string.Home_more_edit) ) },
                        icon = { Icon(imageVector = Icons.Filled.Edit, contentDescription = null) },
//                        summary = { Text(text = "") },
                        onClick = {
                            onChosenMenu("edit")
                        }
                    )
                    preference(
                        key = "configuration",
                        title = { Text(text = stringResource(R.string.Home_more_configuration)) },
                        icon = { Icon(imageVector = Icons.Filled.Build, contentDescription = null) },
//                        summary = { Text(text = "") },
                        onClick = {
                            onChosenMenu("configuration")
                        }
                    )
                    preference(
                        key = "system_setting",
                        title = { Text(text = stringResource(R.string.Home_more_sys_setting)) },
                        icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = null) },
//                        summary = { Text(text = "") },
                        onClick = {
                            onChosenMenu("system_setting")
                        }
                    )
                    preference(
                        key = "fullscreen",
                        title = { Text(text = stringResource(R.string.Home_more_fullscreen)) },
                        icon = { Icon(imageVector = Icons.Default.Fullscreen, contentDescription = null) },
//                        summary = { Text(text = "") },
                        onClick = {
                            onChosenMenu("fullscreen")
                        }
                    )
                    preference(
                        key = "all_apps",
                        title = { Text(text = stringResource(R.string.Home_more_all_apps)) },
                        icon = { Icon(imageVector = Icons.Filled.Apps, contentDescription = null) },
//                        summary = { Text(text = "") },
                        onClick = {
                            onChosenMenu("all_apps")
                        }
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@HTPreviewAnnotations
@Composable
fun HomeMoreMenuPreview(){
    HTLauncherTheme {
        Surface (
            modifier = Modifier.fillMaxSize()
        ){
            HomeMoreMenu(
                onDismissRequest = {},
                onChosenMenu = { val a = it},
                modifier = Modifier
            )
        }

    }
}