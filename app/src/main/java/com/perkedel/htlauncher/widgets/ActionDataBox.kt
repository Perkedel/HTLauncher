@file:OptIn(ExperimentalFoundationApi::class)

package com.perkedel.htlauncher.widgets

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.enumerations.ActionDataLaunchType
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@Composable
fun ActionDataBox(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    actionData: ActionData = ActionData(),
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null,
    onLongClickLabel: String? = null,
    id:Int = 0,
){
//    val name by remember { mutableStateOf(actionData.name ?: "anAction") }
//    val icon:Drawable? = when(actionData.type){
//        ActionDataLaunchType.LauncherActivity -> pm.getApplicationIcon(actionData.action)
//        ActionDataLaunchType.Activity -> pm.getApplicationIcon(actionData.action)
//        else -> pm.getApplicationIcon(actionData.action)
//    }
    Card(
        modifier = Modifier.fillMaxWidth().combinedClickable (
            onClick = onClick,
            onLongClick = onLongClick,
            onLongClickLabel = onLongClickLabel
        )
    ) {
        Row {
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
            ){
                AsyncImage(
                    modifier = Modifier,
//                    model = if(!LocalInspectionMode.current) icon else null,
                    model = null,
                    contentDescription = "",
                    error = painterResource(R.drawable.mavrickle),
                    placeholder = painterResource(R.drawable.mavrickle),
                )
            }

            Spacer(
                modifier = Modifier.padding(8.dp),
            )
            Column {
                Text(
                    text = actionData.name,
                    fontSize = 24.sp,
                )
                when(actionData.type){
                    ActionDataLaunchType.LauncherActivity -> {
                        Text("execute ${actionData.action} LauncherActivity")
                    }
                    ActionDataLaunchType.Activity -> {
                        Text("execute ${actionData.action} activity ${actionData.args[0]}")
                    }
                    ActionDataLaunchType.Internal -> {
                        Text("Internal Command ${actionData.action}")
                    }
                    else -> {}
                }
            }
        }
    }
}

@HTPreviewAnnotations
@Composable
fun ActionDataBoxPreview(){
    HTLauncherTheme {
        LazyColumn {
            item{
                ActionDataBox()
            }
        }
    }
}