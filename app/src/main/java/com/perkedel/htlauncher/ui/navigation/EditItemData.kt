package com.perkedel.htlauncher.ui.navigation

import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.widgets.ItemCell

@Composable
fun EditItemData(
    modifier:Modifier = Modifier,
    data:ItemData? = null,
    viewModel: ItemEditorViewModel = ItemEditorViewModel(),

    ){
    val toChange by remember { mutableStateOf(data) }
    var label by remember { mutableStateOf(toChange?.label ?: "") }
    var aria by remember { mutableStateOf(toChange?.aria ?: "") }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        // https://youtu.be/ZERIxmBYP-U Philip Lackner TextField Material 3
        // https://developer.android.com/develop/ui/compose/text/user-input
        data?.let {
            Text("AAAAAAAAAAAAA \n${data}")
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
                    rebuildItemData(
                        with = ItemData(
                            name = toChange?.name ?: "",
                            label = label,
                            aria = aria,
                        ),
                        viewModel = viewModel
                    )
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
                    rebuildItemData(
                        with = ItemData(
                            name = toChange?.name ?: "",
                            label = label,
                            aria = aria,
                        ),
                        viewModel = viewModel
                    )
                }
            )

            Card(
                modifier = Modifier.padding(16.dp)
            ) {
                ItemCell(
//                    readTheItemFile = toChange?.name ?: "",
                    readTheItemData = toChange ?: ItemData()
                )
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