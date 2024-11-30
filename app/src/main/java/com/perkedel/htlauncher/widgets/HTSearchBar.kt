package com.perkedel.htlauncher.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@Composable
fun HTSearchBar(
    modifier: Modifier = Modifier,
    value:String = "",
    megaTitle:String = "",
    onValueChange:(String)->Unit = {},
    initExpanded:Boolean = false,
){
    var expanded by remember { mutableStateOf(initExpanded) }
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            if(megaTitle.isNotBlank()) {
                Text(megaTitle)
            }
//            if(expanded) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    label = { Text("Search") },
                    onValueChange = onValueChange,
                    leadingIcon = {
                        Icon(Icons.Default.Search, "")
                    },
                    trailingIcon = {
                        if (value.isNotEmpty() || LocalInspectionMode.current) {
                            IconButton(
                                onClick = {
                                    onValueChange("")
                                    expanded = false
                                }
                            ) { Icon(Icons.AutoMirrored.Default.Backspace, "") }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                        showKeyboardOnFocus = false,
                    ),
                    keyboardActions = KeyboardActions(
                        // https://developer.android.com/reference/kotlin/androidx/compose/ui/text/input/ImeAction
                        // https://developer.android.com/reference/kotlin/androidx/compose/foundation/text/KeyboardActions
                        onSearch = {expanded = false},
                        onDone = {expanded = false},
                        onPrevious = {expanded = false},
                        onNext = {expanded = false},
                    ),
                )
//            }
//            else
//            {
//                Row(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    HTButton(
//                        modifier = Modifier.weight(1f),
//                        title = value.ifBlank { "Search" },
//                        leftIcon = Icons.Default.Search,
//                        onClick = {
//                            expanded = true
//                        }
//                    )
//                    if(value.isNotBlank()) {
//                        HTButton(
//                            modifier = Modifier,
//                            leftIcon = Icons.AutoMirrored.Default.Backspace,
//                            onClick = {
//                                onValueChange("")
//                                expanded = false
//                            }
//                        )
//                    }
//                }
//
//            }
        }
    }
}

@HTPreviewAnnotations
@Composable
fun HTSearchBarPreview(){
    HTLauncherTheme {
        Surface(

        ) {
            Column {
                HTSearchBar(
                    value = "HELLO"
                )
                HTSearchBar(
                    value = ""
                )
                HTSearchBar(
                    value = "HELLO",
                    initExpanded = true
                )
                HTSearchBar(
                    value = "",
                    initExpanded = true
                )
            }
        }
    }
}