@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

package com.perkedel.htlauncher.ui.navigation

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.enumerations.ActionDataLaunchType
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.widgets.HTButton
import com.perkedel.htlauncher.widgets.OutlinedText
//import me.saket.cascade.CascadeDropdownMenu

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
    onSelectAction: ()->Unit = {},
){
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ){
        // https://foso.github.io/Jetpack-Compose-Playground/material/dropdownmenu/
        // https://medium.com/@nicholausmichael51/creating-a-dropdown-menu-in-jetpack-compose-b86efff8cddf
        // https://composables.com/material3/dropdownmenu
        // https://alexzh.com/jetpack-compose-dropdownmenu/
        // https://github.com/arsildo/CustomDropdownMenu_JetpackCompose
        // https://medium.com/@itsuki.enjoy/android-kotlin-jetpack-compose-dropdown-selectable-list-menu-b7ad86ba6a5a
        // https://github.com/saket/cascade
        // https://saket.github.io/cascade/
        // https://www.geeksforgeeks.org/drop-down-menu-in-android-using-jetpack-compose/
        // https://youtu.be/5h737wNN-qM stoffe dropdown
        // https://developer.android.com/reference/kotlin/androidx/compose/material/ExposedDropdownMenuBoxScope
        Text("Action ${id}\n${data}")
        var name by remember { mutableStateOf(data?.name ?: "Launcher") }
        var action by remember { mutableStateOf(data?.action ?: "") }
        var args by remember { mutableStateOf(data?.args ?: emptyList<String>()) }
        var type by remember { mutableStateOf(data?.type ?: ActionDataLaunchType.LauncherActivity) }

        var typeMenuExpanded by remember { mutableStateOf(false) }

        val rebuildNow: ()->Unit = {
            val newActionData: ActionData = ActionData(
                name = name,
                action = action,
                args = args,
                type = type,
            )
            Log.d("EditActionData", "Rebuild Action ${id}:\n${newActionData}")
            onRebuild(
                newActionData, id
            )
        }

        data?.let {
            // Name
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                label = {
                    Text("Name")
                },
                value = name,
                onValueChange = {
                    name = it
                    rebuildNow()
                }
            )
            // Dropdown type
            ExposedDropdownMenuBox(

                expanded = typeMenuExpanded,
                onExpandedChange = {typeMenuExpanded = !typeMenuExpanded}
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable).fillMaxWidth(),
                    label = {
                        Text("Type")
                    },
                    value = type.name,
                    readOnly = true,
                    onValueChange = {},
                    trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeMenuExpanded)}
                )

                ExposedDropdownMenu(
                    expanded = typeMenuExpanded,
                    onDismissRequest = {
                        typeMenuExpanded = false
                    }
                ) {
                    ActionDataLaunchType.entries.forEachIndexed{ index, actionDataLaunchType ->
                        DropdownMenuItem(
                            text = {
                                Text(actionDataLaunchType.name)
                            },
                            onClick = {
                                type = actionDataLaunchType
                                typeMenuExpanded = false
                                rebuildNow()
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            // action to package name
            when(type){
                ActionDataLaunchType.LauncherActivity -> {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Application Package")
                        },
                        readOnly = true,
                        value = action,
                        onValueChange = {},
                        trailingIcon = {
                            Icon(Icons.Default.ChevronRight, "")
                        }
                    )
                }
                ActionDataLaunchType.Activity -> {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Application Package")
                        },
                        readOnly = true,
                        value = action,
                        onValueChange = {},
                        trailingIcon = {
                            Icon(Icons.Default.ChevronRight, "")
                        }
                    )
                    // TODO: args text
                }
                else -> {}
            }

        }
        HTButton(
            modifier = Modifier.fillMaxWidth(),
            title = "Close",
            leftIcon = Icons.Default.Check,
            onClick = onClose
        )
    }
}

@HTPreviewAnnotations
@Composable
fun EditActionDataPreview(){
    HTLauncherTheme {
        Surface {
            EditActionData(
                data = ActionData(),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}