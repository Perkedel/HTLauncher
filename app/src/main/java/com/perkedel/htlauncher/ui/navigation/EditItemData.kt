package com.perkedel.htlauncher.ui.navigation

import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel

@Composable
fun EditItemData(
    modifier:Modifier = Modifier,
    data:ItemData? = null,
    viewModel: ItemEditorViewModel = ItemEditorViewModel(),

    ){
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        // https://youtu.be/ZERIxmBYP-U Philip Lackner TextField Material 3
        data?.let {
            Text("AAAAAAAAAAAAA \n${data}")
//            TextField(
//
//            )
        }
    }
}