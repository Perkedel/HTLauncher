package com.perkedel.htlauncher.ui.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.enumerations.ShowWhichIcon
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.widgets.ActionDataBox
import com.perkedel.htlauncher.widgets.HTButton
import com.perkedel.htlauncher.widgets.ItemCell

@Composable
fun EditItemData(
    modifier:Modifier = Modifier,
    data:ItemData? = null,
    viewModel: ItemEditorViewModel = ItemEditorViewModel(),
    windowInfo: WindowInfo = rememberWindowInfo(),
    configuration: Configuration = LocalConfiguration.current,
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
    onRebuildItem:(ItemData)->Unit = {},
    onEditActionData: (ActionData,Int)->Unit = { actionData: ActionData, i: Int -> {}},
    ){
    val toChange by remember { mutableStateOf(data) }
    var name by remember { mutableStateOf(data?.name ?: "anItem") }
    var label by remember { mutableStateOf(data?.label ?: "") }
    var aria by remember { mutableStateOf(data?.aria ?: "") }
    var imagePath by remember { mutableStateOf(data?.imagePath ?: "") }
    var action by remember { mutableStateOf(data?.action ?: emptyList<ActionData>()) }
    var showLabel by remember { mutableStateOf(data?.showLabel ?: false) }
    var showWhichIcon by remember { mutableStateOf(data?.showWhichIcon ?: ShowWhichIcon.Default) }
    var useAria by remember { mutableStateOf(data?.useAria ?: false) }

    val rebuildNow: ()->Unit = {
        rebuildItemData(
            with = ItemData(
                name = name,
                label = label,
                aria = aria,
                imagePath = imagePath,
                action = action,
                showLabel = showLabel,
                showWhichIcon = showWhichIcon,
                useAria = useAria,
            ),
            viewModel = viewModel
        )
        onRebuildItem(
            ItemData(
                name = name,
                label = label,
                aria = aria,
                imagePath = imagePath,
                action = action,
                showLabel = showLabel,
                showWhichIcon = showWhichIcon,
                useAria = useAria,
            )
        )
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        // https://youtu.be/ZERIxmBYP-U Philip Lackner TextField Material 3
        // https://developer.android.com/develop/ui/compose/text/user-input
        Text("ITEM \n" +
                "${data}")
        data?.let {
//            Text("AAAAAAAAAAAAA \n${data}")
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                label = {
                    Text("label")
                },
//                value = toChange?.label ?: "...",
                value = label,
                onValueChange = {
                    label = it
                    toChange?.label = it
                    viewModel.itemData?.label = it
                    rebuildNow()
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                label = {
                    Text("aria")
                },
//                value = toChange?.aria ?: "...",
                value = aria,
                onValueChange = {
                    aria = it
//                    toChange?.aria = it
                    viewModel.itemData?.aria = it
                    rebuildNow()
                }
            )

            // Actions

            OutlinedCard(

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Actions"
                    )
                    Row(
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        HTButton(
                            title = "Add",
                            leftIcon = Icons.Default.Add
                        )
                    }
//                    ActionDataBox(
//                        id = 4,
//                        actionData = ActionData()
//                    )
                    repeat(
                        times = viewModel.itemData?.action?.size ?: 0,
                        action = {id ->
                            ActionDataBox(
                                id = id,
                                actionData = viewModel.itemData?.action?.get(id) ?: ActionData(),
                                onClick = {
                                    onEditActionData(viewModel.itemData?.action?.get(id) ?: ActionData(), id)
                                }
                            )
                        }
                    )
                }
            }

            if(isCompact){
                Card(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ItemCell(
//                    readTheItemFile = toChange?.name ?: "",
                        readTheItemData = data ?: ItemData()
                    )
                }
            }

        }
    }
}

fun rebuildItemData(with:ItemData, viewModel: ItemEditorViewModel){
//    if(viewModel.itemData != null){
        viewModel.updateItemData(with)
//    }
}

@HTPreviewAnnotations
@Composable
fun EditItemDataPreview(){
    HTLauncherTheme {
        EditItemData(
            data = ItemData(
                name = "idk maaaan"
            ),
        )
    }
}