package com.perkedel.htlauncher.widgets

import android.net.Uri
import android.speech.tts.TextToSpeech
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.ui.dialog.HTAlertDialog

@Composable
fun ChangeSaveDirDialog(
    viewModel: HTViewModel,
    attemptChangeSaveDir: MutableState<Boolean>? = null,
    areYouSureChangeSaveDir: MutableState<Boolean>? = null,
    saveDirLauncher: ManagedActivityResultLauncher<Uri?, Uri?>,
    htuiState: HTUIState,
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
)
{
    // currently designed to be implemented for internal use
    HTAlertDialog(
        onDismissRequest = {
            areYouSureChangeSaveDir?.value = false
            attemptChangeSaveDir?.value = false
            viewModel.openChangeSaveDirConfirm(false)
            viewModel.openChangeSaveDir(false)
        },
        swapButton = true,
        icon = {
            Icon(
                Icons.Default.Folder,
                contentDescription = "Folder Icon"
            )
        },
        title = stringResource(R.string.change_savedir_dialog),
        text = "${stringResource(R.string.change_savedir_description)}:"
//                            "Your Config Folder is currently at:\n${""
//                                if (htuiState.selectedSaveDir != null && htuiState.selectedSaveDir.toString()
//                                        .isNotEmpty()
//                                ) htuiState.selectedSaveDir else stringResource(R.string.value_unselected)
//                            +""}"
        ,
        confirmText = stringResource(R.string.change_savedir_change),
        dismissText = stringResource(R.string.dismiss_button),
        onConfirm = {
            saveDirLauncher.launch(null)
            areYouSureChangeSaveDir?.value = false
            attemptChangeSaveDir?.value = false
            viewModel.openChangeSaveDir(false)
            viewModel.openChangeSaveDirConfirm(false)
        },
        tts = tts,
    ){
        val decompose = stringResource(R.string.value_unselected)
        val say = remember {  if (htuiState.selectedSaveDir != null && htuiState.selectedSaveDir.toString()
                .isNotEmpty()
        ) htuiState.selectedSaveDir.toString() else decompose}
        OutlinedTextField(
            value = say,
            trailingIcon = {
                Icon(Icons.Default.Folder, "")
            },
            label = { Text(stringResource(R.string.directory_option)) },
            onValueChange = {
//                                    say = it
            },
            enabled = false,
        )
    }
}