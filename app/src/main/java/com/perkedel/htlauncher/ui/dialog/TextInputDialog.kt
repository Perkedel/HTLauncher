package com.perkedel.htlauncher.ui.dialog

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import org.w3c.dom.Text

@Composable
fun TextInputDialog(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    title:String = "Text Input",
    initInput:String = "",
    mustBeFilled:Boolean = false,
    showSupportingTextOnErrorEval:Boolean = true,
    label:String = "Text",
    placeholder:String = "...",
    text:String = "Insert the text here",
    supportingText:String = stringResource(R.string.textfield_cannot_empty),
    onConfirmText: (String) -> Unit = {},
    onDismiss: () -> Unit =  {},
    onOkClick: () -> Unit = {},
    selectIcon:ImageVector = Icons.Default.TextFields,
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
){
    var input:String by remember { mutableStateOf(initInput) }

    HTAlertDialog(
        modifier = modifier,
        title = title,
        text = text,
//        confirmText = "",
//        confirmButtonDismiss = !isPermanentlyDeclined,
        onDismissRequest = onDismiss,
        onConfirm = {
            onConfirmText(input)
            onOkClick()
        },
        tts = tts,
        selectIcon = selectIcon,
        enableConfirmButton = if(mustBeFilled) input.isNotBlank() else true,
    ){
        OutlinedTextField(
            modifier = Modifier,
            value = input,
            onValueChange = {
                input = it
            },
            placeholder = {
                Text(placeholder)
            },
            label = {
                Text(label)
            },
            keyboardOptions = KeyboardOptions(
                showKeyboardOnFocus = false,
            ),
            isError = if(mustBeFilled) input.isBlank() else false,
            supportingText = {
                if(supportingText.isNotBlank()){
                    if(showSupportingTextOnErrorEval){
                        if(input.isBlank()){
                            Text(
                                supportingText
                            )
                        }
                    } else {
                        Text(
                            supportingText
                        )
                    }
                }
            }
        )
    }
}

@HTPreviewAnnotations
@Composable
fun TextInputDialogPreview(){
    HTLauncherTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TextInputDialog(
                mustBeFilled = true,
//                supportingText = "Must not empty"
            )
        }
    }
}